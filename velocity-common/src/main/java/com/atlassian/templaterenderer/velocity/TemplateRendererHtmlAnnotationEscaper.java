package com.atlassian.templaterenderer.velocity;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.atlassian.templaterenderer.annotations.HtmlSafe;
import com.atlassian.velocity.htmlsafe.HtmlAnnotationEscaper;

public class TemplateRendererHtmlAnnotationEscaper extends HtmlAnnotationEscaper
{
    @Override
    protected boolean shouldEscape(String referenceName, Object value, Collection<Annotation> annotations)
    {
        return !TemplateRendererHtmlSafeAnnotationUtils.hasHtmlSafeToStringMethod(value)
            && !TemplateRendererHtmlSafeAnnotationUtils.containsAnnotationOfType(annotations, HtmlSafe.class)
            && super.shouldEscape(referenceName, value, annotations);
    }
}
