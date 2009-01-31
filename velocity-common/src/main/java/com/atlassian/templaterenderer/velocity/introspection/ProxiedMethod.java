package com.atlassian.templaterenderer.velocity.introspection;

import org.apache.commons.lang.Validate;
import org.apache.velocity.util.introspection.VelMethod;

/**
 * A velocity method that proxies method calls to another object
 */
final class ProxiedMethod implements VelMethod
{
    private final VelMethod delegateMethod;
    private final Object proxyObject;

    /**
     * @param delegateMethod method to delegate calls to
     * @param proxyObject object to receive the method call
     */
    public ProxiedMethod(VelMethod delegateMethod, Object proxyObject)
    {
        Validate.notNull(delegateMethod, "delegateMethod must not be null");
        Validate.notNull(proxyObject, "proxyObject must not be null");
        this.delegateMethod = delegateMethod;
        this.proxyObject = proxyObject;
    }

    public Object invoke(Object o, Object[] params)
            throws Exception
    {
        return delegateMethod.invoke(proxyObject, params);
    }

    public boolean isCacheable()
    {
        return delegateMethod.isCacheable();
    }

    public String getMethodName()
    {
        return delegateMethod.getMethodName();
    }

    public Class getReturnType()
    {
        return delegateMethod.getReturnType();
    }
}
