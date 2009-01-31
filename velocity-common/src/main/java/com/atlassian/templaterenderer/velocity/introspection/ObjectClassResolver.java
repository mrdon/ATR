package com.atlassian.templaterenderer.velocity.introspection;

/**
 * Strategy for resolving the class of an object.
 */
interface ObjectClassResolver
{
    /**
     * Resolve the class of the provided object
     * @param object Object to resolve
     * @return Resolved class
     */
    Class resolveClass(Object object);
}
