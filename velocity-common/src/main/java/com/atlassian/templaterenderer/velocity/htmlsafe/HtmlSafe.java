package com.atlassian.templaterenderer.velocity.htmlsafe;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atlassian.templaterenderer.velocity.annotations.CollectionInheritable;
import com.atlassian.templaterenderer.velocity.annotations.ReturnValueAnnotation;

/**
 * Declares that this method returns an object that does not require encoding if it is printed to a HTML document via
 * its {@link Object#toString()} method
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ReturnValueAnnotation
@CollectionInheritable
public @interface HtmlSafe
{
}
