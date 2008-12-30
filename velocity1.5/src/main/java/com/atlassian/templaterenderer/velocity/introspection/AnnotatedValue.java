package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang.Validate;

import com.atlassian.templaterenderer.velocity.annotations.CollectionInheritable;

/**
 * An annotated value associates a collection of annotations with a value.
 */
public final class AnnotatedValue implements AnnotationBoxedElement
{
    private final Object value;
    private final Collection<Annotation> annotations;

    /**
     * Construct a new annotated value. THe iteration order of annotations may not be preserved.
     * @param value The value to annotate
     * @param annotations This values annotations.
     */
    public AnnotatedValue(Object value, Collection<Annotation> annotations)
    {
        Validate.isTrue(!(value instanceof BoxedValue), "Attempting to box an already boxed value");
        Validate.notNull(annotations, "annotations must not be null");
        AnnotationValidationUtils.assertContainsNoNulls(annotations, "Null annotations are not permitted");
        this.value = value;
        this.annotations = new HashSet<Annotation>(annotations);
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> aClass)
    {
        for (Annotation annotation : annotations)
        {
            if (annotation.annotationType().equals(aClass))
                return true;
        }
        return false;
    }

    @SuppressWarnings({"unchecked"})
    public <T extends Annotation> T getAnnotation(Class<T> tClass)
    {
        for (Annotation annotation : annotations)
        {
            if (annotation.annotationType().equals(tClass))
                return (T) annotation;
        }

        return null;
    }

    public Annotation[] getAnnotations()
    {
        return annotations.toArray(new Annotation[annotations.size()]);
    }

    public Annotation[] getDeclaredAnnotations()
    {
        return getAnnotations();
    }

    public Object unbox()
    {
        return value;
    }

    /**
     * This will box another object with the same annotations as this value.
     * @param value Value to box
     * @return Value boxed with the annotations
     */
    public Object box(Object value)
    {
        return new AnnotatedValue(value, annotations);
    }

    public Collection<Annotation> getCollectionInheritableAnnotations()
    {
        Collection<Annotation> inheritableAnnotations = new HashSet<Annotation>();
        for (Annotation annotation : getAnnotations())
        {
            if (annotation.annotationType().isAnnotationPresent(CollectionInheritable.class))
                inheritableAnnotations.add(annotation);
        }
        return Collections.unmodifiableCollection(inheritableAnnotations);
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotatedValue that = (AnnotatedValue) o;

        if (!annotations.equals(that.annotations)) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    public int hashCode()
    {
        int result;
        result = (value != null ? value.hashCode() : 0);
        result = 31 * result + annotations.hashCode();
        return result;
    }

    public final String getDescription()
    {
        return "Annotated value: " + value.toString() + "; Annotations: " + annotations;
    }

    /**
     * Delegates and returns the result of calling toString on the boxed value. This is unpleasant but necessary as
     * Velocity uses the toString() result when context values are used as part of directive arguments.
     *
     * @return String representation of wrapped value
     */
    public String toString()
    {
        return value.toString();
    }
}
