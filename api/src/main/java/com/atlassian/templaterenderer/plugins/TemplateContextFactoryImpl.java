package com.atlassian.templaterenderer.plugins;

import com.atlassian.plugin.PluginAccessor;
import com.atlassian.templaterenderer.TemplateContextFactory;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Implementation of the template context factory
 */
public class TemplateContextFactoryImpl implements TemplateContextFactory
{
    private static final Logger log = Logger.getLogger(TemplateContextFactoryImpl.class);
    private final PluginAccessor pluginAccessor;

    public TemplateContextFactoryImpl(PluginAccessor pluginAccessor)
    {
        this.pluginAccessor = pluginAccessor;
    }

    /**
     * Create a context for a template renderer
     *
     * @param contextParams Any extra context parameters that should be added to the context
     * @return A map of the context
     */
    public Map<String, Object> createContext(String pluginKey, Map<String, Object> contextParams)
    {
        Map<String, Object> context = new HashMap<String, Object>();
        List<TemplateContextItemModuleDescriptor> templateContextItemPlugins =
            pluginAccessor.getEnabledModuleDescriptorsByClass(TemplateContextItemModuleDescriptor.class);
        for (TemplateContextItemModuleDescriptor desc : templateContextItemPlugins)
        {
            if (desc.isGlobal() || desc.getPluginKey().equals(pluginKey))
            {
                try
                {
                    context.put(desc.getContextKey(), desc.getModule());
                }
                catch (RuntimeException re)
                {
                    log.error("Error loading module for " + desc.getPluginKey() + ":" + desc.getKey(), re);
                }
            }
        }
        context.putAll(contextParams);
        return context;
    }
}
