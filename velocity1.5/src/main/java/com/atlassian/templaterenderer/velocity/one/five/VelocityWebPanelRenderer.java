package com.atlassian.templaterenderer.velocity.one.five;

import com.atlassian.plugin.Plugin;
import com.atlassian.templaterenderer.AbstractVelocityWebPanelRenderer;
import com.atlassian.templaterenderer.TemplateContextFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.velocity.one.five.internal.VelocityTemplateRendererImpl;

import java.util.Collections;

public class VelocityWebPanelRenderer extends AbstractVelocityWebPanelRenderer
{
    private final TemplateContextFactory templateContextFactory;

    public VelocityWebPanelRenderer(TemplateContextFactory templateContextFactory)
    {
        this.templateContextFactory = templateContextFactory;
    }

    @Override
    protected TemplateRenderer createRenderer(Plugin plugin)
    {
        return new VelocityTemplateRendererImpl(templateContextFactory, plugin.getClassLoader(), plugin.getKey(),
                Collections.<String, String>emptyMap());
    }
}
