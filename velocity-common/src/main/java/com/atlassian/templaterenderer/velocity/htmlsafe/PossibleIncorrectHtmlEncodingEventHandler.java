package com.atlassian.templaterenderer.velocity.htmlsafe;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalWrapperContext;
import org.apache.velocity.util.ContextAware;

import com.atlassian.templaterenderer.velocity.introspection.AnnotatedReferenceHandler;

/**
 * <p>Reference insertion handler to be used before the {@link HtmlAnnotationEscaper}.
 *
 * <p>This handler will log warnings whenever a value that hasn't been marked as HtmlSafe contains data that looks like HTML.
 *
 * <p>As this handler is {@link ContextAware} it is not thread safe and should not be reused across multiple threads.
*/
public final class PossibleIncorrectHtmlEncodingEventHandler extends AnnotatedReferenceHandler implements ContextAware
{
    private static final Log log = LogFactory.getLog(PossibleIncorrectHtmlEncodingEventHandler.class);

    static
    {
        log.info("This log records Velocity template references that may have been incorrectly handled by the automatic HTML encoding system");
    }

    private Context context;

    public static boolean isLoggingEnabled()
    {
        return log.isInfoEnabled();
    }

    protected Object annotatedValueInsert(String referenceName, Object value, Collection<Annotation> annotations)
    {
        if (value == null)
            return value;

        boolean isHtmlSafeValue = HtmlSafeAnnotationUtils.containsAnnotationOfType(annotations, HtmlSafe.class) || HtmlSafeAnnotationUtils.hasHtmlSafeToStringMethod(value);

        final String stringValue = value.toString();
        if (!isHtmlSafeValue && (hasHtml(stringValue) || hasEncodedHtml(stringValue)))
        {
            log.info(referenceName + " in " + getCurrentTemplateName());
        }

        return value;
    }

    private boolean hasHtml(String string)
    {
        return HtmlRegExps.HTML_TAG_PATTERN.matcher(string).find();
    }

    private boolean hasEncodedHtml(String string)
    {
        return HtmlRegExps.HTML_ENTITY_PATTERN.matcher(string).find();
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    private String getCurrentTemplateName()
    {
        String templateName;
        if (context instanceof InternalWrapperContext)
        {
            InternalWrapperContext wrapper = (InternalWrapperContext) context;
            templateName = wrapper.getBaseContext().getCurrentTemplateName();
        }
        else
        {
            templateName = "unknown";
        }

        return templateName;
    }
}
