package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * A method annotator that chains calls to a collection of other annotators
*/
public final class MethodAnnotatorChain implements MethodAnnotator
{
    private final List<MethodAnnotator> ANNOTATOR_CHAIN;

    public MethodAnnotatorChain(List<MethodAnnotator> annotators)
    {
        Validate.notNull(annotators, "annotators must not be null");
        ANNOTATOR_CHAIN = Collections.unmodifiableList(new LinkedList<MethodAnnotator>(annotators));
    }

    public Collection<Annotation> getAnnotationsForMethod(Method method)
    {
        Collection<Annotation> annotations = new HashSet<Annotation>();
        for (MethodAnnotator annotator : ANNOTATOR_CHAIN)
            annotations.addAll(annotator.getAnnotationsForMethod(method));

        return annotations;
    }
}
