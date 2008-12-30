package com.atlassian.templaterenderer.velocity.introspection;

/**
 * Interface for classes that provide some strategy for boxing an object
 */
public interface BoxingStrategy
{
    /**
     * Box the provided value according to the boxing strategy
     * @param value object to box
     * @return value boxed according to this strategy
     */
    Object box(Object value);
}
