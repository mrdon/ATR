package com.atlassian.templaterenderer.velocity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.atlassian.velocity.htmlsafe.HtmlSafeAnnotationUtils;
import com.atlassian.velocity.htmlsafe.HtmlSafeClassAnnotator;
import com.atlassian.velocity.htmlsafe.HtmlSafeMethodNameAnnotator;
import com.atlassian.velocity.htmlsafe.introspection.AnnotationBoxingUberspect;
import com.atlassian.velocity.htmlsafe.introspection.MethodAnnotator;
import com.atlassian.velocity.htmlsafe.introspection.MethodAnnotatorChain;

public class TemplateRendererAnnotationBoxingUberspect extends AnnotationBoxingUberspect
{
    /**
     * A chain of annotators to mark methods safe based on naming conventions and known safe library methods.
     */
    private static final MethodAnnotator HTML_METHOD_ANNOTATOR = new MethodAnnotatorChain(
            Arrays.asList(new HtmlSafeMethodNameAnnotator(), new HtmlSafeClassAnnotator())
    );

    private static final MethodAnnotator RETURN_VALUE_ANNOTATOR = new TemplateRendererReturnValueAnnotator();

    @Override
    protected Collection<Annotation> getMethodAnnotations(Method method)
    {
        Collection<Annotation> returnValueAnnotations = Collections.unmodifiableCollection(RETURN_VALUE_ANNOTATOR.getAnnotationsForMethod(method));
        if (returnValueAnnotations.contains(TemplateRendererHtmlSafeAnnotationUtils.HTML_SAFE_ANNOTATION)
         || returnValueAnnotations.contains(HtmlSafeAnnotationUtils.HTML_SAFE_ANNOTATION))
            return returnValueAnnotations;

        // If we haven't determined whether this method call returns a HTML safe value yet, run it past our
        // other annotation policies.

        Collection<Annotation> htmlAnnotations = new LinkedList<Annotation>(returnValueAnnotations);
        htmlAnnotations.addAll(HTML_METHOD_ANNOTATOR.getAnnotationsForMethod(method));
        return htmlAnnotations;
    }
}
