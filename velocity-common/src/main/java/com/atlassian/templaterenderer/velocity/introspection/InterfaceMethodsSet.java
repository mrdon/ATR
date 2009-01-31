package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * Helper class for working with a set of {@link InterfaceMethods}
*/
final class InterfaceMethodsSet
{
    private final Set<InterfaceMethods> interfaceMethodsSet;

    InterfaceMethodsSet()
    {
        interfaceMethodsSet = Collections.emptySet();
    }

    InterfaceMethodsSet(Set<InterfaceMethods> interfaceMethodsSet)
    {
        Validate.notNull(interfaceMethodsSet);
        this.interfaceMethodsSet = Collections.unmodifiableSet(
                new HashSet<InterfaceMethods>(interfaceMethodsSet));
    }

    /**
     * @return The set of interfaces contained
     */
    public Set<Class> getInterfaces()
    {
        Set<Class> interfaceSet = new HashSet<Class>();
        for (InterfaceMethods interfaceMethods : interfaceMethodsSet)
            interfaceSet.add(interfaceMethods.getDeclaringInterface());
        return interfaceSet;
    }

    /**
     * @return The union of all methods contained by the methods set
     */
    public Set<Method> getMethods()
    {
        Set<Method> methodSet = new HashSet<Method>();
        for (InterfaceMethods interfaceMethods : interfaceMethodsSet)
            methodSet.addAll(interfaceMethods.getMethods());
        return methodSet;
    }

    public boolean isEmpty()
    {
        return interfaceMethodsSet.isEmpty();
    }

    /**
     * Return all methods contained in this method set that are implemented by a class
     * @param clazz Class to match
     * @return InterfaceMethodsSet of all matching implemented methods
     */
    public InterfaceMethodsSet getImplementedMethods(Class clazz)
    {
        Set<InterfaceMethods> implementedMethods = new HashSet<InterfaceMethods>(interfaceMethodsSet);

        for (Iterator<InterfaceMethods> iterator = implementedMethods.iterator(); iterator.hasNext(); )
        {
            if (!iterator.next().isImplementation(clazz))
                iterator.remove();
        }

        return new InterfaceMethodsSet(implementedMethods);
    }
}
