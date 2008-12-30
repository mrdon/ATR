package com.atlassian.templaterenderer.velocity.introspection;

import org.apache.velocity.app.event.ReferenceInsertionEventHandler;

/**
 * Reference insertion handler that wraps annnoted elements in a {@link ToStringDelegatingAnnotationBoxedElement} so
 * that toString() calls are passed to the underlying value.
 */
final class AnnotatedValueStringHandler implements ReferenceInsertionEventHandler
{
    public Object referenceInsert(String reference, Object value)
    {
        if (value instanceof AnnotationBoxedElement)
            return new ToStringDelegatingAnnotationBoxedElement((AnnotationBoxedElement) value);

        return value;
    }
}
