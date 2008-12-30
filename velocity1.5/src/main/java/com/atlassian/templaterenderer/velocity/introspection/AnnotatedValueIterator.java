package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

/**
 * An iterator that will box each value in the iteration with a collection of annotations.
 */
final class AnnotatedValueIterator implements Iterator, BoxedValue
{
    private final Iterator boxedIterator;
    private final Collection<Annotation> annotations;

    public AnnotatedValueIterator(Iterator iterator, Collection<Annotation> annotations)
    {
        Validate.notNull(iterator, "iterator must not be null");
        Validate.notNull(annotations, "annotations must not be null");
        AnnotationValidationUtils.assertContainsNoNulls(annotations, "annotations must not contain nulls");
        this.boxedIterator = iterator;
        this.annotations = annotations;
    }

    public boolean hasNext()
    {
        return boxedIterator.hasNext();
    }

    public Object next()
    {
        return new AnnotatedValue(boxedIterator.next(), annotations);
    }

    public void remove()
    {
        boxedIterator.remove();
    }

    public Object unbox()
    {
        return boxedIterator;
    }
}
