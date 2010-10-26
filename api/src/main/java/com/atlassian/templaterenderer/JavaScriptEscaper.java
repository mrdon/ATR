package com.atlassian.templaterenderer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.atlassian.templaterenderer.annotations.HtmlSafe;

/**
 * Escaping that was ripped from Apache Commons-Lang StringUtils class, modified to escape the '&lt;' character as a
 * unicode string as described in AG-1005.
 */
public class JavaScriptEscaper
{
    /**
     * <p>
     * Escapes the characters in a <code>String</code> using JavaScript String
     * rules.
     * </p>
     * <p>
     * Escapes any values it finds into their JavaScript String literal form. Deals
     * correctly with quotes and control-chars (tab, backslash, cr, ff, etc.)
     * </p>
     *
     * <p>
     * So a tab becomes the characters <code>'\\'</code> and <code>'t'</code>.
     * </p>
     *
     * <p>
     * The only difference between Java strings and JavaScript strings is that
     * in JavaScript, a single quote must be escaped.
     * </p>
     *
     * <p>
     * Example:
     *
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn\'t say, \"Stop!\"
     * </pre>
     *
     * </p>
     *
     * @param str
     *            String to escape values in, may be null
     * @return String with escaped values, <code>null</code> if null string
     *         input
     */
    @HtmlSafe
    public static String escape(String str)
    {
        return escapeJavaStyleString(str, true);
    }

    /**
     * <p>
     * Escapes the characters in a <code>String</code> using JavaScript String
     * rules to a <code>Writer</code>.
     * </p>
     *
     * <p>
     * A <code>null</code> string input has no effect.
     * </p>
     *
     * @see #escape(java.lang.String)
     * @param out
     *            Writer to write escaped string into
     * @param str
     *            String to escape values in, may be null
     * @throws IllegalArgumentException
     *             if the Writer is <code>null</code>
     * @throws IOException
     *             if error occurs on underlying Writer
     **/
    @HtmlSafe
    public static void escape(Writer out, String str) throws IOException
    {
        escapeJavaStyleString(out, str, true);
    }

    /**
     * <p>
     * Worker method for the {@link #escape(String)} method.
     * </p>
     *
     * @param str
     *            String to escape values in, may be null
     * @param escapeSingleQuotes
     *            escapes single quotes if <code>true</code>
     * @return the escaped string
     */
    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes);
            return writer.toString();
        }
        catch (IOException ioe)
        {
            // this should never ever happen while writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }

    /**
     * <p>
     * Worker method for the {@link #escape(String)} method.
     * </p>
     *
     * @param out
     *            write to receieve the escaped string
     * @param str
     *            String to escape values in, may be null
     * @param escapeSingleQuote
     *            escapes single quotes if <code>true</code>
     * @throws IOException
     *             if an IOException occurs
     */
    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException
    {
        if (out == null)
        {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null)
        {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++)
        {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff)
            {
                out.write("\\u" + hex(ch));
            }
            else if (ch > 0xff)
            {
                out.write("\\u0" + hex(ch));
            }
            else if (ch > 0x7f)
            {
                out.write("\\u00" + hex(ch));
            }
            else if (ch < 32)
            {
                switch (ch)
                {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    default:
                        if (ch > 0xf)
                        {
                            out.write("\\u00" + hex(ch));
                        }
                        else
                        {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            }
            else
            {
                switch (ch)
                {
                    case '\'':
                        if (escapeSingleQuote)
                        {
                            out.write('\\');
                        }
                        out.write('\'');
                        break;
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    case '/':
                        out.write('\\');
                        out.write('/');
                        break;
                    case '<':
                        out.write("\\u003c");
                        break;
                    default:
                        out.write(ch);
                        break;
                }
            }
        }
    }

    /**
     * <p>
     * Returns an upper case hexadecimal <code>String</code> for the given
     * character.
     * </p>
     *
     * @param ch
     *            The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    public static String hex(char ch)
    {
        return Integer.toHexString(ch).toUpperCase();
    }
}

