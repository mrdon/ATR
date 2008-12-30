package com.atlassian.templaterenderer.velocity.htmlsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import com.atlassian.templaterenderer.velocity.introspection.MethodAnnotator;

/**
 * <p>Method annotator that marks certain methods as being HtmlSafe based on the method name.
 *
 * <p>This policy will annotate any method whose name ends with "Html" or starts with "render" as having a
 * HTML safe return value.
 */
public class HtmlSafeMethodNameAnnotator implements MethodAnnotator
{
    public Collection<Annotation> getAnnotationsForMethod(Method method)
    {
        final String methodName = method.getName();
        if (methodName.endsWith("Html") || methodName.startsWith("render") || methodName.startsWith("getRender"))
            return Collections.singletonList(HtmlSafeAnnotationUtils.HTML_SAFE_ANNOTATION);

        return Collections.emptyList();
    }
}
