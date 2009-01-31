package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.apache.velocity.util.introspection.VelPropertyGet;

/**
 * Delegating property getter that will box the returned value with a collection of annotations.
 */
final class AnnotationBoxingPropertyGet implements VelPropertyGet
{
    private final VelPropertyGet delegate;
    private final Collection<Annotation> annotations;

    public AnnotationBoxingPropertyGet(VelPropertyGet delegate, Collection<Annotation> annotations)
    {
        Validate.notNull(delegate, "delegate must not be null");
        Validate.notNull(annotations, "annotations must not be null");
        AnnotationValidationUtils.assertContainsNoNulls(annotations, "annotations must not contain nulls");
        this.delegate = delegate;
        this.annotations = annotations;
    }

    public Object invoke(Object o) throws Exception
    {
        final Object obj = delegate.invoke(o);

        if (obj == null)
            return obj;

        return new AnnotatedValue(obj, annotations);
    }

    public boolean isCacheable()
    {
        return delegate.isCacheable();
    }

    public String getMethodName()
    {
        return delegate.getMethodName();
    }
}
