package com.atlassian.templaterenderer.velocity.introspection;

import org.apache.velocity.app.event.ReferenceInsertionEventHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.Collection;
import java.lang.annotation.Annotation;

/**
 * Support class for {@link org.apache.velocity.app.event.ReferenceInsertionEventHandler}s that need to deal with
 * any {@link AnnotationBoxedElement}s context values.
*/
public abstract class AnnotatedReferenceHandler implements ReferenceInsertionEventHandler
{
    public Object referenceInsert(String referenceName, Object referenceValue)
    {
        if (referenceValue instanceof AnnotationBoxedElement)
        {
            AnnotationBoxedElement returnValue = (AnnotationBoxedElement) referenceValue;
            Object processedValue =  annotatedValueInsert(referenceName, returnValue.unbox(), Arrays.asList(returnValue.getAnnotations()));
            return returnValue.box(processedValue);
        }

        return annotatedValueInsert(referenceName, referenceValue, Collections.EMPTY_LIST);
    }

    /**
     * Process a reference with a collection of annotations
     * @param referenceName name of the reference being inserted
     * @param value unboxed reference value
     * @param annotations any annotations associated with the reference
     * @return Final value to insert
     */
    protected abstract Object annotatedValueInsert(String referenceName, Object value, Collection<Annotation> annotations);
}
