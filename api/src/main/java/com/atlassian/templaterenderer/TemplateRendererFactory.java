package com.atlassian.templaterenderer;

public interface TemplateRendererFactory
{
    TemplateRenderer getInstance(ClassLoader classLoader);
}
