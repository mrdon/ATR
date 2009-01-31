package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.atlassian.templaterenderer.velocity.annotations.ReturnValueAnnotation;

/**
 * A method annotator that returns all annotations that have been meta annotated as a {@link ReturnValueAnnotation}
 */
final class ReturnValueAnnotator implements MethodAnnotator
{
    public Collection<Annotation> getAnnotationsForMethod(Method method)
    {
        Collection<Annotation> returnValueAnnotations = new HashSet<Annotation>();

        for (Annotation annotation : method.getAnnotations())
            if (annotation.annotationType().isAnnotationPresent(ReturnValueAnnotation.class))
                returnValueAnnotations.add(annotation);

        return Collections.unmodifiableCollection(returnValueAnnotations);
    }
}
