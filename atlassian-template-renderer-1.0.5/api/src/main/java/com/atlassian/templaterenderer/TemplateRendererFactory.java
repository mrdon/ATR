package com.atlassian.templaterenderer;

/**
 * Factory for getting template renderers with a given classloader.
 *
 * @deprecated This factory is no longer needed. Plugins can import TemplateRenderer, and they will get a template
 *             renderer appropriately initialised with the right bundle context.
 */
@Deprecated
public interface TemplateRendererFactory
{
    TemplateRenderer getInstance(ClassLoader classLoader);
}
