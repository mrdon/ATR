package com.atlassian.templaterenderer.velocity.htmlsafe;

import com.atlassian.templaterenderer.velocity.introspection.BoxedValue;

/**
 * Simple wrapper class for adding HtmlSafe values directly to a Velocity context.
 */
public final class HtmlFragment implements BoxedValue<Object>
{
    private final Object fragment;

    public HtmlFragment(Object fragment)
    {
        this.fragment = fragment;
    }

    @HtmlSafe
    public String toString()
    {
        return fragment.toString();
    }

    public Object unbox()
    {
        return fragment;
    }
}
