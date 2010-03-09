package com.atlassian.templaterenderer;

import com.atlassian.plugin.web.renderer.WebPanelRenderer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public abstract class AbstractVelocityWebPanelRenderer implements WebPanelRenderer
{
    public String getResourceType()
    {
        return "velocity";
    }

    protected abstract TemplateRenderer getRenderer(ClassLoader classLoader);

    public void render(String s, ClassLoader classLoader, Map<String, Object> stringObjectMap, Writer writer) throws IOException
    {
        getRenderer(classLoader).render(s, stringObjectMap, writer);
    }

    public String renderFragment(String s, ClassLoader classLoader, Map<String, Object> stringObjectMap)
    {
        return getRenderer(classLoader).renderFragment(s, stringObjectMap);
    }
}
