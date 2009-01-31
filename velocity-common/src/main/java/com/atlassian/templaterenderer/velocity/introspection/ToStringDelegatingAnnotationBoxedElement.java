package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;

import org.apache.commons.lang.Validate;

/**
 * {@link AnnotationBoxedElement} that delegates all operations to
 * the wrapped element except for toString() which is delegated to the boxed value itself.
 */
public final class ToStringDelegatingAnnotationBoxedElement implements AnnotationBoxedElement
{
    private final AnnotationBoxedElement delegate;

    public ToStringDelegatingAnnotationBoxedElement(AnnotationBoxedElement delegate)
    {
        Validate.notNull(delegate, "delegate must not be null");
        this.delegate = delegate;
    }

    public Object unbox()
    {
        return delegate.unbox();
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType)
    {
        return delegate.isAnnotationPresent(annotationType);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType)
    {
        return delegate.getAnnotation(annotationType);
    }

    public Annotation[] getAnnotations()
    {
        return delegate.getAnnotations();
    }

    public Annotation[] getDeclaredAnnotations()
    {
        return delegate.getDeclaredAnnotations();
    }

    public Object box(Object value)
    {
        final Object boxedValue = delegate.box(value);
        if (boxedValue instanceof AnnotationBoxedElement)
            return new ToStringDelegatingAnnotationBoxedElement((AnnotationBoxedElement) boxedValue);

        return boxedValue;
    }

    public String toString()
    {
        return delegate.unbox().toString();
    }
}
