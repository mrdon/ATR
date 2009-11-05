package com.atlassian.templaterenderer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atlassian.templaterenderer.annotations.CollectionInheritable;
import com.atlassian.templaterenderer.annotations.ReturnValueAnnotation;

/**
 * Declares that this method returns an object that does not require encoding if it is printed to a HTML document via
 * its {@link Object#toString()} method
 * 
 * @deprecated use {@link com.atlassian.velocity.htmlsafe.HtmlSafe} instead
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ReturnValueAnnotation
@CollectionInheritable
@Deprecated
public @interface HtmlSafe
{
}
