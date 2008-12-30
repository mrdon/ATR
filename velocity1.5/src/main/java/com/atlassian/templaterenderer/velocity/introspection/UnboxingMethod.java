package com.atlassian.templaterenderer.velocity.introspection;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.Validate;
import org.apache.velocity.util.introspection.VelMethod;

/**
 * Velocity method that unboxes the target object and any boxed arguments before calling the delegate method.
 *
 * It will also process any list arguments, unboxing any list members that are {@link BoxedValue}s.
 */
final class UnboxingMethod implements VelMethod
{
    private final VelMethod delegateMethod;

    public UnboxingMethod(VelMethod delegateMethod)
    {
        Validate.notNull(delegateMethod, "delegateMethod must not be null");
        this.delegateMethod = delegateMethod;
    }

    public Object invoke(Object o, Object[] objects) throws Exception
    {
        final Object[] unboxedArgs = BoxingUtils.unboxArrayElements(objects);
        unboxListArgumentElements(unboxedArgs);
        return delegateMethod.invoke(BoxingUtils.unboxObject(o), unboxedArgs);
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

    /**
     * Check an array for any element that implements {@link List) and for each list discovered, replace the list
     * with another list where any boxed members of the list have been unboxed.
     *
     * @param arguments Object array to process
     */
    private void unboxListArgumentElements(Object[] arguments)
    {
        for (int x = 0; x < arguments.length; x++)
        {
            if (arguments[x] instanceof List)
            {
                List unboxedList = new ArrayList((List) arguments[x]);
                ListIterator iterator = unboxedList.listIterator();
                while (iterator.hasNext())
                {
                    iterator.set(BoxingUtils.unboxObject(iterator.next()));
                }
                arguments[x] = unboxedList;
            }
        }
    }
}
