package com.atlassian.templaterenderer.velocity.one.six.internal;

import java.util.Collections;
import java.util.Map;

import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.TemplateContextFactory;
import com.atlassian.templaterenderer.velocity.one.six.VelocityTemplateRendererFactory;

/**
 * Factory for instantiating customised velocity template renderers.  This factory will ensure that the velocity engine
 * created has the right class loader, and that all templates are resolved from the right classloader.
 */
public class VelocityTemplateRendererFactoryImpl implements VelocityTemplateRendererFactory
{
    private final TemplateContextFactory templateContextFactory;
    private final String pluginKey;
    private final ClassLoader classLoader;

    public VelocityTemplateRendererFactoryImpl(TemplateContextFactory templateContextFactory, String pluginKey,
        ClassLoader classLoader)
    {
        this.templateContextFactory = templateContextFactory;
        this.pluginKey = pluginKey;
        this.classLoader = classLoader;
    }

    public TemplateRenderer getInstance(ClassLoader classLoader)
    {
        return getInstance(classLoader, Collections.<String, String>emptyMap());
    }

    public TemplateRenderer getInstance(ClassLoader classLoader, Map<String, String> properties)
    {
        return new VelocityTemplateRendererImpl(classLoader, pluginKey, properties, templateContextFactory);
    }

    public TemplateRenderer getInstance(Map<String, String> properties)
    {
        return new VelocityTemplateRendererImpl(classLoader, pluginKey, properties, templateContextFactory);
    }
}
