package com.atlassian.templaterenderer.velocity.one.five;

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
    protected TemplateRenderer getRenderer(ClassLoader classLoader)
    {
        // TODO: what plugin key should we pass in here?
        // or can we pass in the Plugin instead of a classloader?
        return new VelocityTemplateRendererImpl(templateContextFactory, classLoader, "", Collections.<String, String>emptyMap());
    }
}
