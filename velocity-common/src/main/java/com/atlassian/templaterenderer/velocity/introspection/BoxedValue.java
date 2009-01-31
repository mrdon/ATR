package com.atlassian.templaterenderer.velocity.introspection;

/**
 * The value represents a boxed value. (i.e. any value that has been wrapped but should still be accessible)
 */
public interface BoxedValue<T>
{
    T unbox();
}
