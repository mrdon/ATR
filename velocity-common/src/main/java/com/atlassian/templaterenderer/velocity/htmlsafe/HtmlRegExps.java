package com.atlassian.templaterenderer.velocity.htmlsafe;

import java.util.regex.Pattern;

/**
 * Holder of static regular expression patterns for detecting common HTML structures
 */
public final class HtmlRegExps
{
    public static final Pattern HTML_TAG_PATTERN = Pattern.compile("<([A-Z][A-Z0-9]*)\\b[^>]*>(.*?)</\\1>", Pattern.CASE_INSENSITIVE);
    public static final Pattern HTML_ENTITY_PATTERN =  Pattern.compile("&lt;([A-Z][A-Z0-9]*)\\b(.(?!&gt;))*.?&gt;", Pattern.CASE_INSENSITIVE);

    private HtmlRegExps()
    {
    }
}
