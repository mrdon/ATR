package com.atlassian.templaterenderer.velocity.one.six;

import com.atlassian.templaterenderer.AbstractVelocityWebPanelRenderer;
import com.atlassian.templaterenderer.TemplateContextFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.velocity.one.six.internal.VelocityTemplateRendererImpl;

import java.util.Collections;

public class VelocityWebPanelRenderer extends AbstractVelocityWebPanelRenderer
{
    private final TemplateContextFactory templateContextFactory;

    public VelocityWebPanelRenderer(TemplateContextFactory templateContextFactory)
    {
        this.templateContextFactory = templateContextFactory;
    }

    @Override
    protected TemplateRenderer getRenderer(ClassLoader classLoader) {
        // TODO: this is relatively expensive, so cache somehow per classloader
        return new VelocityTemplateRendererImpl(classLoader, "", Collections.<String, String>emptyMap(), templateContextFactory);
    }
}
