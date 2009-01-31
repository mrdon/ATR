package com.atlassian.templaterenderer.velocity.htmlsafe;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

import com.atlassian.templaterenderer.velocity.introspection.AnnotatedReferenceHandler;

/**
 * A {@link org.apache.velocity.app.event.ReferenceInsertionEventHandler} that HTML encodes any value not annotated
 * as being HtmlSafe.
 *
 * It also excludes some references from escaping based on name:
 * <ul>
 * <li>Those ending in "html"
 * <li>xHtmlContent
 * <li>body
 * </ul>
*/
public class HtmlAnnotationEscaper extends AnnotatedReferenceHandler
{
    private static final Set SAFE_REFERENCE_NAMES = Collections.unmodifiableSet(new HashSet(Arrays.asList("xHtmlContent", "body")));

    protected Object annotatedValueInsert(String referenceName, Object value, Collection<Annotation> annotations)
    {
        if (value == null)
            return null;

        if (shouldEscape(referenceName, value, annotations))
            return StringEscapeUtils.escapeHtml(value.toString());

        return value;
    }

    private boolean shouldEscape(String referenceName, Object value, Collection<Annotation> annotations)
    {
        final RawVelocityReference reference = new RawVelocityReference(referenceName);

        if (reference.isScalar())
        {
            String baseReference = reference.getBaseReferenceName();
            if (baseReference.toLowerCase().endsWith("html") || SAFE_REFERENCE_NAMES.contains(baseReference))
                 return false;
        }

        return (!(HtmlSafeAnnotationUtils.hasHtmlSafeToStringMethod(value) || HtmlSafeAnnotationUtils.containsAnnotationOfType(annotations, HtmlSafe.class)));
    }

}
