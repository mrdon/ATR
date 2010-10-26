package com.atlassian.templaterenderer;

import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class JavaScriptEscaperTest
{
    @Test
    public void testEscapeQuotes()
    {
        assertEquals("He didn\\'t say, \\\"Stop!\\\"", JavaScriptEscaper.escape("He didn't say, \"Stop!\""));
    }

    @Test
    public void testEscapeTabs()
    {
        assertEquals("Hohoho \\t test", JavaScriptEscaper.escape("Hohoho \t test"));
    }

    @Test
    public void testEscapeTags()
    {
        final String input = "<script type=\"text/javascript\" charset=\"utf-8\"></script>";
        final String expected = "\\u003cscript type=\\\"text\\/javascript\\\" charset=\\\"utf-8\\\">\\u003c\\/script>";
        assertEquals(expected, JavaScriptEscaper.escape(input));
    }
}
