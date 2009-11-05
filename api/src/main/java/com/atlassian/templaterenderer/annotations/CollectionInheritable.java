package com.atlassian.templaterenderer.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * A meta annotation that indicates to the annotation uberspect that the annotation should be retained with every item
 * in a returned collection
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface CollectionInheritable
{
}
