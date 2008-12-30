package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.RuntimeServicesAware;
import org.apache.velocity.util.introspection.Info;
import org.apache.velocity.util.introspection.UberspectImpl;
import org.apache.velocity.util.introspection.VelMethod;
import org.apache.velocity.util.introspection.VelPropertyGet;

import com.atlassian.templaterenderer.velocity.annotations.ReturnValueAnnotation;

/**
 * A Velocity uberspect that boxes return values in an annotated form and unboxes them again when used as arguments
 * to or targets of method calls
 *
 * More specifically this uberspect will inspect any target method call or property for annotations that are marked as
 * {@link ReturnValueAnnotation}s and box the result of calling or accessing the target with these annotations.
 *
 */
public class AnnotationBoxingUberspect extends UberspectImpl implements RuntimeServicesAware
{
    private static final Log log = LogFactory.getLog(AnnotationBoxingUberspect.class);

    private static final InterfaceMethodsSet ANNOTATION_PRESERVING_METHODS = getAnnotationPreservingCollectionMethods();

    private static final MethodAnnotator RETURN_VALUE_ANNOTATOR = new ReturnValueAnnotator();

    private RuntimeServices runtimeServices;

    private final ObjectClassResolver CLASS_RESOLVER = new ObjectClassResolver()
    {
        public Class resolveClass(Object object)
        {
            return getClassForTargetObject(object);
        }
    };

    public void init()
    {
        super.init();

        // Add the special annotation value handler so that final rendering of annotated values in the context is made
        // with the correct value string.
        runtimeServices.getApplicationEventCartridge().addReferenceInsertionEventHandler(new AnnotatedValueStringHandler());
    }

    /**
     * @return InterfaceMethodsSet representing collection API methods that should inherit the target collection's
     * return value annotations
     */
    private static InterfaceMethodsSet getAnnotationPreservingCollectionMethods()
    {
        try
        {
            InterfaceMethods preservingMapMethods = new InterfaceMethods(Map.class.getMethod("get", Object.class));
            InterfaceMethods preservingListMethods = new InterfaceMethods(List.class.getMethod("get", Integer.TYPE));

            Set<InterfaceMethods> preservingMethods = new HashSet<InterfaceMethods>();
            preservingMethods.add(preservingListMethods);
            preservingMethods.add(preservingMapMethods);

            return new InterfaceMethodsSet(preservingMethods);
        }
        catch (NoSuchMethodException ex)
        {
            log.error("Could not find collection method via reflection. Collection inheritance will not function.", ex);
            return new InterfaceMethodsSet();
        }
    }

    /**
     * Return a method that knows how to unbox method call targets and parameters and to box return values
     * according to the return value boxing policy.
     *
     * @param obj Object to locate the method on
     * @param methodName Name of the method to locate
     * @param args Method call arguments
     * @param info Current template info
     * @return Method calling strategy that will transparently handle boxed arguments and targets while automatically
     * boxing method return values with return value annotations.
     * @throws Exception
     */
    public final VelMethod getMethod(Object obj, String methodName, Object[] args, Info info) throws Exception
    {
        final AnnotatedValueHelper valueHelper = AnnotatedValueHelperFactory.getValueHelper(obj, CLASS_RESOLVER);

        final Object[] unboxedArgs = BoxingUtils.unboxArrayElements(args);
        VelMethod method = super.getMethod(valueHelper.unbox(), methodName, unboxedArgs, info);

        if (method == null)
            return null;

        method = checkAndGenerateAnnotationPreservingProxy(valueHelper, method, unboxedArgs, info);

        Method refMethod = introspector.getMethod(valueHelper.getTargetClass(), methodName, unboxedArgs);
        Collection<Annotation> returnValueAnnotations = getMethodAnnotations(refMethod);

        if (!returnValueAnnotations.isEmpty())
            method = new AnnotationBoxingMethod(method, returnValueAnnotations);
        
        return new UnboxingMethod(method);
    }

    private VelMethod checkAndGenerateAnnotationPreservingProxy(AnnotatedValueHelper valueHelper, VelMethod velocityMethod, Object[] unboxedArgs, Info info) throws Exception
    {
        if (!valueHelper.isBoxedValue())
            return velocityMethod;

        // Generate a proxy method if annotations are inherited.
        AnnotationBoxedElement annotatedValue = valueHelper.getBoxedValueWithInheritedAnnotations();
        if (annotatedValue != null)
            return methodProxy(annotatedValue, ANNOTATION_PRESERVING_METHODS, info, unboxedArgs, velocityMethod);

        return velocityMethod;
    }

    /**
     * Get an iterator responsible for preserving annotations while iterating over a collection that has collection
     * inheritable return value annotations.'
     *
     * @param obj object to get an iterator for
     * @param info current template info
     * @return Inheritable annotation preserving iterator
     * @throws Exception
     */
    public final Iterator getIterator(Object obj, Info info) throws Exception
    {
        if (!(obj instanceof AnnotatedValue))
             return super.getIterator(obj, info);

        AnnotatedValue annotatedValue = (AnnotatedValue) obj;
        Iterator iterator = super.getIterator(annotatedValue.unbox(), info);
        if (iterator == null)
            return null;

        Collection<Annotation> inheritedAnnotations = annotatedValue.getCollectionInheritableAnnotations();

        if (inheritedAnnotations.isEmpty())
            return iterator;

        return new AnnotatedValueIterator(iterator, inheritedAnnotations);
    }

    /**
     * Get a property getting strategy that will box the end result with any return value annotations on the property getter
     *
     * @param obj Object on which a property is being retrieved
     * @param identifier Property identifier
     * @param info Current template info
     * @return A return value boxing property getter
     * @throws Exception
     */
    public final VelPropertyGet getPropertyGet(Object obj, String identifier, Info info) throws Exception
    {
        final AnnotatedValueHelper valueHelper = AnnotatedValueHelperFactory.getValueHelper(obj, CLASS_RESOLVER);

        VelPropertyGet getter = super.getPropertyGet(valueHelper.unbox(), identifier, info);

        if (getter == null)
            return null;

        log.debug("Getting introspector method for getter: " + getter.getMethodName());

        Method refMethod = introspector.getMethod(valueHelper.getTargetClass(), getter.getMethodName(), new Object[] { });

        log.debug("got method: " + refMethod);

        if (refMethod == null)
            return getter;

        Collection<Annotation> annotations = getMethodAnnotations(refMethod);

        log.debug("Got return annotations: " + annotations);

        if (annotations.isEmpty())
            return getter;

        return new AnnotationBoxingPropertyGet(getter, annotations);
    }

    /**
     * <p>Return a proxied velocity method if this method call belongs to the set of provided interface methods. The method
     * proxy will be responsible for preserving that annotated value's annotations on any return value from these methods.
     *
     * <p>Invocation proxies are built using the class loader that loaded this uberspect.
     *
     * @param value Object on which the method is being called
     * @param interfaceMethodsSet Method set defining all methods that inherit annotations
     * @param info Current template processing info
     * @param unboxedArgs Unboxed method arguments
     * @param velMethod Velocity method to proxy if annotations are to be inherited
     * @return A velocity method with the corrent annotation preserving strategy
     * @throws Exception if the default uberspect implemention does while scanning for this method
     */
    private VelMethod methodProxy(AnnotationBoxedElement value, InterfaceMethodsSet interfaceMethodsSet, Info info, Object[] unboxedArgs, VelMethod velMethod)
            throws Exception
    {
        InterfaceMethodsSet implementedInterfaceMethods = interfaceMethodsSet.getImplementedMethods(value.unbox().getClass());

        if (implementedInterfaceMethods.isEmpty())
            return velMethod;

        final Set<Class> implementedInterfaces = implementedInterfaceMethods.getInterfaces();

        log.debug("Object implements: " + implementedInterfaces);

        Object proxiedObject = Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                implementedInterfaces.toArray(new Class[implementedInterfaces.size()]),
                new AnnotationPreservingInvocationHandler(value, implementedInterfaceMethods.getMethods()));

        VelMethod proxiedMethod = super.getMethod(proxiedObject, velMethod.getMethodName(), unboxedArgs, info);
        if (proxiedMethod == null)
        {
            // We're not actually calling a method that preserves its annotations. Just return the default method.
            return velMethod;
        }
        log.debug("Proxying method: " + proxiedMethod);
        return new ProxiedMethod(proxiedMethod, proxiedObject);
    }

    public void setRuntimeServices(RuntimeServices runtimeServices)
    {
        this.runtimeServices = runtimeServices;
    }

    /**
     * Retrieve any annotations on the supplied method that are meta-annotated as a {@link ReturnValueAnnotation}
     * @param method Method to search
     * @return Collection of annotations that have been marked as a {@link com.atlassian.confluence.velocity.annotations.ReturnValueAnnotation} on the provided method.
     */
    protected Collection<Annotation> getMethodAnnotations(Method method)
    {
        return Collections.unmodifiableCollection(RETURN_VALUE_ANNOTATOR.getAnnotationsForMethod(method));
    }

    /**
     * Template method for returning the actual concrete class of a the provided object.
     *
     * This method should be overridden in environments where objects may be proxied in such a way that
     * method annotations are not reflected by the proxying object class.
     *
     * This implementation simply returns <tt>targetObject.getClass()</tt>
     *
     * @param targetObject The object for which the class is being queried
     * @return The actual class that should be queried for return value annotations
     */
    protected Class getClassForTargetObject(Object targetObject)
    {
        return targetObject.getClass();
    }
}
