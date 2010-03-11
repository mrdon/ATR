package com.atlassian.templaterenderer.velocity.one.six;

import com.atlassian.plugin.Plugin;
import com.atlassian.templaterenderer.AbstractVelocityWebPanelRenderer;
import com.atlassian.templaterenderer.TemplateContextFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.velocity.one.six.internal.VelocityTemplateRendererImpl;

import java.util.Collections;

/**
 * @since   2.5.0
 */
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
        return new VelocityTemplateRendererImpl(plugin.getClassLoader(), plugin.getKey(),
                Collections.<String, String>emptyMap(), templateContextFactory);
    }
}
