package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import com.atlassian.templaterenderer.velocity.htmlsafe.HtmlSafeAnnotationUtils;
import com.atlassian.templaterenderer.velocity.htmlsafe.HtmlSafeClassAnnotator;
import com.atlassian.templaterenderer.velocity.htmlsafe.HtmlSafeMethodNameAnnotator;

/**
 * <p>Specialisation of the AnnotatioBoxingUberspect to be used in a Confluence velocity environment.
 *
 * <p>It adds a custom method annotation policy when make a determination on whether a method is HTML safe or not.
 */
public class TemplateRendererAnnotationBoxingUberspect extends AnnotationBoxingUberspect
{
    /**
     * A chain of annotators to mark methods safe based on naming conventions and known safe library methods.
     */
    private static final MethodAnnotator HTML_METHOD_ANNOTATOR = new MethodAnnotatorChain(
        Arrays.asList(new HtmlSafeMethodNameAnnotator(), new HtmlSafeClassAnnotator())
    );

    protected Collection<Annotation> getMethodAnnotations(Method method)
    {
        Collection<Annotation> returnValueAnnotations = super.getMethodAnnotations(method);
        if (returnValueAnnotations.contains(HtmlSafeAnnotationUtils.HTML_SAFE_ANNOTATION))
            return returnValueAnnotations;

        // If we haven't determined whether this method call returns a HTML safe value yet, run it past our
        // other annotation policies.

        Collection<Annotation> htmlAnnotations = new LinkedList<Annotation>(returnValueAnnotations);
        htmlAnnotations.addAll(HTML_METHOD_ANNOTATOR.getAnnotationsForMethod(method));
        return htmlAnnotations;
    }
}
