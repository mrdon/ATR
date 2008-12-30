package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * Holder for a subset of methods belonging to a particular interface.
 *
 * All methods returned by this class are guaranteed to belong to the associated interface.
*/
final class InterfaceMethods
{
    private final Set<Method> methods;
    private final Class declaringInterface;

    /**
     * @param methods These methods must all belong to the same interface
     * @throws IllegalArgumentException if the provided methods do not belong to the same declaring interface
     */
    public InterfaceMethods(Method... methods)
    {
        Validate.notEmpty(methods, "At least one method must be provided");
        Set<Class> declaringClasses = new HashSet<Class>();
        for (Method method : methods)
        {
            Validate.isTrue(method.getDeclaringClass().isInterface(), "Provided methods must be from an interface");
            declaringClasses.add(method.getDeclaringClass());
            Validate.isTrue(declaringClasses.size() == 1, "Provided methods must be from the same interface");
        }

        this.declaringInterface = declaringClasses.iterator().next();
        this.methods = Collections.unmodifiableSet(new HashSet<Method>(Arrays.asList(methods)));
    }

    public boolean isImplementation(Class clazz)
    {
        return getDeclaringInterface().isAssignableFrom(clazz);
    }

    /**
     * @return A set of interface methods
     */
    public Set<Method> getMethods()
    {
        return methods;
    }

    /**
     *
     * @return The interface that declares this method set
     */
    public Class getDeclaringInterface()
    {
        return declaringInterface;
    }
}
