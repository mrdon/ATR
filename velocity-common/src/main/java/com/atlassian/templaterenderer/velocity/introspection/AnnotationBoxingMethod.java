package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.apache.velocity.util.introspection.VelMethod;

/**
 * Delegating method strategy that will box the returned value with a collection of annotations.
 */
final class AnnotationBoxingMethod implements VelMethod
{
    private final VelMethod delegate;
    private final Collection<Annotation> returnValAnnotations;

    public AnnotationBoxingMethod(VelMethod delegateMethod, Collection<Annotation> annotations)
    {
        Validate.notNull(delegateMethod, "degateMethod must not be null");
        Validate.notNull(annotations, "annotations must not be null");
        AnnotationValidationUtils.assertContainsNoNulls(annotations, "annotations must not contain nulls");
        this.delegate = delegateMethod;
        this.returnValAnnotations = annotations;
    }

    public Object invoke(Object o, Object[] params) throws Exception
    {
        final Object obj = delegate.invoke(o, params);

        if (obj == null)
            return null;

        return new AnnotatedValue(obj, returnValAnnotations);
    }

    public boolean isCacheable()
    {
        return delegate.isCacheable();
    }

    public String getMethodName()
    {
        return delegate.getMethodName();
    }

    public Class getReturnType()
    {
        return delegate.getReturnType();
    }
}
