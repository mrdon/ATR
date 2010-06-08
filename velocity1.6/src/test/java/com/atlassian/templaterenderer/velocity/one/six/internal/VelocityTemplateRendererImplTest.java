package com.atlassian.templaterenderer.velocity.one.six.internal;

import java.util.Map;

import com.atlassian.templaterenderer.TemplateContextFactory;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.velocity.tests.AbstractVelocityRendererImplTest;

public class VelocityTemplateRendererImplTest extends AbstractVelocityRendererImplTest
{
    @Override
    protected TemplateRenderer createVelocityRenderer(TemplateContextFactory contextFactory, 
            ClassLoader classLoader,
            String pluginKey,
            Map<String, String> initProperties)
    {
        return new VelocityTemplateRendererImpl(classLoader, pluginKey, initProperties, contextFactory);
    }
}
