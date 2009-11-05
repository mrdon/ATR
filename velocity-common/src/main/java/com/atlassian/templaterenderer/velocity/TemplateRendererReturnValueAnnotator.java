package com.atlassian.templaterenderer.velocity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.atlassian.velocity.htmlsafe.annotations.ReturnValueAnnotation;
import com.atlassian.velocity.htmlsafe.introspection.MethodAnnotator;

public class TemplateRendererReturnValueAnnotator implements MethodAnnotator
{
    public Collection<Annotation> getAnnotationsForMethod(Method method)
    {
        Collection<Annotation> returnValueAnnotations = new HashSet<Annotation>();

        for (Annotation annotation : method.getAnnotations())
        {
            if (annotation.annotationType().isAnnotationPresent(ReturnValueAnnotation.class)
             || annotation.annotationType().isAnnotationPresent(com.atlassian.templaterenderer.annotations.ReturnValueAnnotation.class))
            {
                returnValueAnnotations.add(annotation);
            }
        }
        return Collections.unmodifiableCollection(returnValueAnnotations);
    }
}
