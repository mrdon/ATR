package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.reflect.AnnotatedElement;

/**
 * This is a union of the {@link BoxedValue} and {@link AnnotatedElement} interfaces to be implemented by implementations
 * that are responsible for associating annotations with an object.
 */
public interface AnnotationBoxedElement extends BoxedValue, AnnotatedElement, BoxingStrategy
{
}
