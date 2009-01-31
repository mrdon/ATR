package com.atlassian.templaterenderer.velocity.htmlsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.atlassian.templaterenderer.velocity.introspection.MethodAnnotator;

/**
 * This method annotator will annotate various methods that are known to be HTML safe from library classes.
 *
 * At present this annotator will annotate the htmlEncode method from {@link com.opensymphony.util.TextUtils} and
 * {@link com.opensymphony.webwork.util.VelocityWebWorkUtil}, the html method from 
 * {@link org.apache.velocity.tools.generic.EscapeTool}, and the escapeHtml method from 
 * {@link org.apache.commons.lang.StringEscapeUtils} as being HTML safe.
 */
public final class HtmlSafeClassAnnotator implements MethodAnnotator
{
    private static final Map<String, String> HTML_ENCODE_CLASS_METHODS = new HashMap<String, String>() {{
        put("com.opensymphony.util.TextUtils", "htmlEncode"); 
        put("com.opensymphony.webwork.util.VelocityWebWorkUtil.VelocityWebWorkUtil", "htmlEncode");
    }};

    public Collection<Annotation> getAnnotationsForMethod(Method method)
    {
        String className = method.getDeclaringClass().getName();
        if (HTML_ENCODE_CLASS_METHODS.containsKey(className)
         && HTML_ENCODE_CLASS_METHODS.get(className).equals(method.getName()))
        {
            return Collections.singleton(HtmlSafeAnnotationUtils.HTML_SAFE_ANNOTATION);
        }
        return Collections.emptySet();
    }
}
