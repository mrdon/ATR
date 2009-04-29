package com.atlassian.templaterenderer;

import java.util.Map;

/**
 * Factory for creating the context for template renderers
 */
public interface TemplateContextFactory
{
    /**
     * Create a context for a template renderer
     *
     * @param contextParams Any extra context parameters that should be added to the context
     * @param pluginKey The plugin to create the context for
     * @return A map of the context
     */
    public Map<String, Object> createContext(String pluginKey, Map<String, Object> contextParams);
}
