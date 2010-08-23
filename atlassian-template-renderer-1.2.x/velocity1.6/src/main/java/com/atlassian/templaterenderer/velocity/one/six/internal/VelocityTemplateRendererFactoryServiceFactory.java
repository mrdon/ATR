package com.atlassian.templaterenderer.velocity.one.six.internal;

import org.osgi.framework.ServiceFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;
import com.atlassian.templaterenderer.BundleClassLoaderAccessor;
import com.atlassian.templaterenderer.TemplateContextFactory;
import com.atlassian.templaterenderer.velocity.one.six.VelocityTemplateRendererFactory;
import com.atlassian.templaterenderer.TemplateRenderer;

import java.util.Map;

/**
 * Service factory for instantiating velocity template renderer factories.  This will make sure the factory knows about
 * the importing bundles class loader and plugin key.
 */
////////////////////
// TODO: Once plugins has upgraded to Spring DM 1.2, remove implements VelocityTemplateRendererFactory.  This exists to
// workaround the bug fixed in https://fisheye.springsource.org/changelog/spring-osgi?cs=2071
////////////////////
public class VelocityTemplateRendererFactoryServiceFactory implements ServiceFactory, VelocityTemplateRendererFactory
{
    /**
     * This can be replaced with OsgiPlugin.ATLASSIAN_PLUGIN_KEY once we update to the latest version
     */
    private static final String ATLASSIAN_PLUGIN_KEY = "Atlassian-Plugin-Key";
    private final TemplateContextFactory templateContextFactory;

    public VelocityTemplateRendererFactoryServiceFactory(TemplateContextFactory templateContextFactory)
    {
        this.templateContextFactory = templateContextFactory;
    }

    public Object getService(Bundle bundle, ServiceRegistration serviceRegistration)
    {
        String pluginKey = (String) bundle.getHeaders().get(ATLASSIAN_PLUGIN_KEY);
        // We want velocity to use the callers classloader
        ClassLoader bundleClassLoader = BundleClassLoaderAccessor.getClassLoader(bundle);
        return new VelocityTemplateRendererFactoryImpl(templateContextFactory, pluginKey, bundleClassLoader);
    }

    public void ungetService(Bundle bundle, ServiceRegistration serviceRegistration, Object o)
    {
    }

    ///////////////////
    // TODO: Once plugins has upgraded to Spring DM 1.2, remove this code
    ///////////////////
    public TemplateRenderer getInstance(ClassLoader classLoader, Map<String, String> properties)
    {
        throw new UnsupportedOperationException();
    }

    public TemplateRenderer getInstance(Map<String, String> properties)
    {
        throw new UnsupportedOperationException();
    }

    public TemplateRenderer getInstance(ClassLoader classLoader)
    {
        throw new UnsupportedOperationException();
    }
    //////////////////
    // END TODO
    //////////////////
}
