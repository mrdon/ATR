package com.atlassian.templaterenderer.velocity.one.five.internal;

import com.atlassian.templaterenderer.TemplateContextFactory;
import com.atlassian.templaterenderer.BundleClassLoaderAccessor;

import java.util.Collections;

import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;

/**
 * Service factory for instantiating a template renderer for the given bundle
 */
public class VelocityTemplateRendererServiceFactory implements ServiceFactory
{
    /**
     * This can be replaced with OsgiPlugin.ATLASSIAN_PLUGIN_KEY once we update to the latest version of plugins
     */
    private static final String ATLASSIAN_PLUGIN_KEY = "Atlassian-Plugin-Key";

    private final TemplateContextFactory templateContextFactory;

    public VelocityTemplateRendererServiceFactory(TemplateContextFactory templateContextFactory)
    {
        this.templateContextFactory = templateContextFactory;
    }

    public Object getService(Bundle bundle, ServiceRegistration serviceRegistration)
    {
        String pluginKey = (String) bundle.getHeaders().get(ATLASSIAN_PLUGIN_KEY);
        // We want velocity to use the callers classloader
        ClassLoader bundleClassLoader = BundleClassLoaderAccessor.getClassLoader(bundle);
        return new VelocityTemplateRendererImpl(templateContextFactory, bundleClassLoader, pluginKey, Collections.<String, String>emptyMap());
    }

    public void ungetService(Bundle bundle, ServiceRegistration serviceRegistration, Object service)
    {
        // No state is stored, so nothing to do
    }
}
