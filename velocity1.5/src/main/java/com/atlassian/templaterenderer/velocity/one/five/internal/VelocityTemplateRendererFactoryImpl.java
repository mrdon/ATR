package com.atlassian.templaterenderer.velocity.one.five.internal;

import java.util.Collections;
import java.util.Map;

import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.velocity.one.five.VelocityTemplateRendererFactory;

public class VelocityTemplateRendererFactoryImpl implements VelocityTemplateRendererFactory
{
    private final I18nResolver i18n;
    private final WebResourceManager webResourceManager;

    public VelocityTemplateRendererFactoryImpl(I18nResolver i18n, WebResourceManager webResourceManager)
    {
        this.i18n = i18n;
        this.webResourceManager = webResourceManager;
    }
    
    public TemplateRenderer getInstance(ClassLoader classLoader)
    {
        return getInstance(classLoader, Collections.<String, String>emptyMap());
    }
    
    public TemplateRenderer getInstance(ClassLoader classLoader, Map<String, String> properties)
    {
        return new VelocityTemplateRenderer(classLoader, i18n, webResourceManager, properties);        
    }
}
