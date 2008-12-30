package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * Proxy invocation handler that will box the return value of proxied methods with a collection of annotations.
 */
final class AnnotationPreservingInvocationHandler implements InvocationHandler
{
    private final Collection<Annotation> annotations;
    private final Object targetObject;
    private final Set<Method> preservingMethods;

    private AnnotationPreservingInvocationHandler(Collection<Annotation> annotations, Object targetObject, Set<Method> proxiedMethods)
    {
        Validate.notNull(annotations, "annotations must not be null");
        AnnotationValidationUtils.assertContainsNoNulls(annotations, "annotations must not contain nulls");
        Validate.notNull(targetObject, "targetObject must not be null");
        Validate.notEmpty(proxiedMethods, "proxiedMethods must not be empty");
        
        this.annotations = new HashSet<Annotation>(annotations);
        this.targetObject = targetObject;
        this.preservingMethods = proxiedMethods;
    }

    public AnnotationPreservingInvocationHandler(AnnotationBoxedElement value, Set<Method> preservingMethods)
    {
        this(Arrays.asList(value.getAnnotations()), value.unbox(), preservingMethods);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        final Object returnValue = method.invoke(targetObject, args);

        if (preservingMethods.contains(method))
            return new AnnotatedValue(returnValue, annotations);

        return returnValue;
    }
}
