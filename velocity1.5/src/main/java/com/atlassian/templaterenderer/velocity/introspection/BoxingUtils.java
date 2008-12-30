package com.atlassian.templaterenderer.velocity.introspection;

/**
 * Utility methods for working with boxed values
 *
 * @see BoxedValue
 */
final class BoxingUtils
{
    private BoxingUtils() { }

    /**
     * Unbox an object if it is a {@link BoxedValue}
     * @param obj Value to unbox
     * @return The unbox value or obj if it is not a {@link BoxedValue}
     */
    static Object unboxObject(Object obj)
    {
        if (obj instanceof BoxedValue)
            return ((BoxedValue) obj).unbox();

        return obj;
    }

    /**
     * Return an array of unboxed values
     * @param array Array to be unboxed
     * @return A new array where any {@link BoxedValue} elements have been unboxed
     */
    static Object[] unboxArrayElements(Object[] array)
    {
        if (array == null || array.length == 0)
            return array;

        final Object[] unboxedArgs = array.clone();
        for (int idx = 0; idx < array.length; idx++)
        {
            unboxedArgs[idx] = unboxObject(unboxedArgs[idx]);
        }

        return unboxedArgs;
    }
}
