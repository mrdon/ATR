package com.atlassian.templaterenderer.velocity.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * A meta annotation that indicates to the annotation uberspect that the annotation should be retained with the return
 * value in the Velocity context.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ReturnValueAnnotation
{
}
