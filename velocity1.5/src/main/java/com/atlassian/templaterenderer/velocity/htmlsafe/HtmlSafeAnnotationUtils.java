package com.atlassian.templaterenderer.velocity.htmlsafe;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

import com.atlassian.templaterenderer.velocity.introspection.AnnotationBoxedElement;

/**
 * Utilities for working with the {@link HtmlSafe} annotation
 */
public final class HtmlSafeAnnotationUtils
{
    public static final Annotation HTML_SAFE_ANNOTATION = HtmlSafeAnnotationFactory.getHtmlSafeAnnotation();

    /**
     * Provides a mechanism for obtaining an instance of the HtmlSafe marker annotation.
     *
     * This implementation uses a dummy interface with an annotated method declartion and reflectively retrieves an
     * instance from this interface.
     */
    private static class HtmlSafeAnnotationFactory
    {
        static Annotation getHtmlSafeAnnotation()
        {
            try
            {
                return HtmlSafeAnnotationHolder.class.getMethod("holder").getAnnotation(HtmlSafe.class);
            }
            catch (NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            }
        }

        private static interface HtmlSafeAnnotationHolder
        {
            @HtmlSafe
            Object holder();
        }
    }

    private HtmlSafeAnnotationUtils()
    {
    }

    /**
     * Return true if the object has a toString method that has been annotated as HtmlSafe
     *
     * @param value Object to query
     * @return true if HTML safe
     */
    public static boolean hasHtmlSafeToStringMethod(Object value)
    {
        try
        {
            return value.getClass().getMethod("toString").isAnnotationPresent(HtmlSafe.class);
        }
        catch (NoSuchMethodException e)
        {
            // All objects have a toString method
            throw new RuntimeException("Object does not have a toString method");
        }
    }

    /**
     * Determines wheter an annotated value is htmlsafe (i.e should not be encoded during rendering)
     *
     * @param value Object to query
     * @return true if HTML safe
     */
    public static boolean isHtmlSafeValue(AnnotationBoxedElement value)
    {
        return (hasHtmlSafeToStringMethod(value.unbox()) || containsAnnotationOfType(Arrays.asList(value.getAnnotations()), HtmlSafe.class));
    }

    /**
     * Detects whether a collection contains an annotation of a particular type
     *
     * @param annotations Collection to scan
     * @param annotationType Annotation type to detect
     * @return true if annotation type is found
     */
    public static boolean containsAnnotationOfType(Collection<Annotation> annotations, Class<? extends Annotation> annotationType)
    {
        for (Annotation annotation : annotations)
        {
            if (annotation.annotationType().equals(annotationType))
                return true;
        }

        return false;
    }
}
