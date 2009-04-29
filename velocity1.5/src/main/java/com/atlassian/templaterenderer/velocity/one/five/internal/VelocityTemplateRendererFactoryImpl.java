package com.atlassian.templaterenderer.velocity.one.five.internal;

import java.util.Collections;
import java.util.Map;

import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.TemplateContextFactory;
import com.atlassian.templaterenderer.velocity.one.five.VelocityTemplateRendererFactory;

public class VelocityTemplateRendererFactoryImpl implements VelocityTemplateRendererFactory
{
    private final TemplateContextFactory templateContextFactory;
    private final ClassLoader classLoader;
    private final String pluginKey;

    public VelocityTemplateRendererFactoryImpl(TemplateContextFactory templateContextFactory, ClassLoader classLoader,
        String pluginKey)
    {
        this.templateContextFactory = templateContextFactory;
        this.classLoader = classLoader;
        this.pluginKey = pluginKey;
    }

    public TemplateRenderer getInstance(ClassLoader classLoader)
    {
        return getInstance(classLoader, Collections.<String, String>emptyMap());
    }
    
    public TemplateRenderer getInstance(ClassLoader classLoader, Map<String, String> properties)
    {
        return new VelocityTemplateRendererImpl(templateContextFactory, classLoader, pluginKey, properties);
    }

    public TemplateRenderer getInstance(Map<String, String> properties)
    {
        return new VelocityTemplateRendererImpl(templateContextFactory, classLoader, pluginKey, properties);
    }
}
