package com.atlassian.templaterenderer.velocity.one.six.internal;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.atlassian.templaterenderer.annotations.HtmlSafe;
import com.atlassian.velocity.htmlsafe.HtmlAnnotationEscaper;
import com.atlassian.velocity.htmlsafe.HtmlSafeAnnotationUtils;

public class TemplateRendererHtmlAnnotationEscaper extends HtmlAnnotationEscaper
{
    @Override
    protected Object annotatedValueInsert(String referenceName, Object value, Collection<Annotation> annotations)
    {
        // check for the templaterenderer specific HtmlSafe annotation for backwards compatibility
        if (HtmlSafeAnnotationUtils.containsAnnotationOfType(annotations, HtmlSafe.class))
        {
            return value;
        }
        return super.annotatedValueInsert(referenceName, value, annotations);
    }
    
}
