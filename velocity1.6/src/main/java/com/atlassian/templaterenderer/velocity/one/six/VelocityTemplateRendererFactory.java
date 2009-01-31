package com.atlassian.templaterenderer.velocity.one.six;

import java.util.Map;

import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.TemplateRendererFactory;

public interface VelocityTemplateRendererFactory extends TemplateRendererFactory
{
    TemplateRenderer getInstance(ClassLoader classLoader, Map<String, String> properties);
}
