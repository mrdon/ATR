package com.atlassian.templaterenderer.velocity.one.six;

import java.util.Map;

import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.TemplateRendererFactory;

/**
 * Factory for plugins wanting to pass specific properties to a velocity template renderer
 */
public interface VelocityTemplateRendererFactory extends TemplateRendererFactory
{
    /**
     * @deprecated This method is no longer needed, as the factory service imported by plugins will be one that already
     *             knows what the plugins bundles classloader should be.  Use {@link #getInstance(Map<String, String>)}
     *             instead.
     */
    @Deprecated TemplateRenderer getInstance(ClassLoader classLoader, Map<String, String> properties);

    /**
     * Get an instance of a velocity template renderer initialised with the given properties for the velocity engine
     *
     * @param properties The properties to initialise the template renderer with
     * @return A template renderer
     */
    TemplateRenderer getInstance(Map<String, String> properties);
}
