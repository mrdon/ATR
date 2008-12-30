package com.atlassian.templaterenderer.velocity.introspection;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Helper interface for dealing with boxed values.
*/
interface AnnotatedValueHelper extends BoxedValue
{
    /**
     * @return The original value box.
     */
    Object get();

    /**
     * @return Any annotations associated with the boxed value. Must not be null.
     */
    Collection<Annotation> getAnnotations();

    /**
     * @return The currently boxed value with any inherited annotations associated with the original box.
     */
    AnnotationBoxedElement getBoxedValueWithInheritedAnnotations();

    /**
     * @return The class of the boxed value.
     */
    Class getTargetClass();

    /**
     * @return True if this helper was constructed with a boxed value (i.e. the value implements {@link BoxedValue})
     */
    boolean isBoxedValue();
}
