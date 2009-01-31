package com.atlassian.templaterenderer.velocity.introspection;

import org.apache.velocity.app.event.ReferenceInsertionEventHandler;

/**
 * A simple reference insertion handler that unboxes and boxed value references making them transparent during template
 * rendering.
 */
public final class TransparentBoxedValueReferenceHandler implements ReferenceInsertionEventHandler
{
    public Object referenceInsert(String name, Object value)
    {
        return BoxingUtils.unboxObject(value);
    }
}
