package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Strategy for retrieving annotations for a method
 */
public interface MethodAnnotator
{
    /**
     * Return a collection of annotations for a method
     * @param method Method to annotate
     * @return A collection of annotations applicable for the method
     */
    Collection<Annotation> getAnnotationsForMethod(Method method);
}
