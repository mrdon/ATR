package com.atlassian.templaterenderer.velocity.one.six.internal;

import org.osgi.framework.ServiceFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;
import com.atlassian.templaterenderer.plugins.BundleClassLoaderAccessor;
import com.atlassian.templaterenderer.plugins.TemplateContextFactory;
import com.atlassian.templaterenderer.RenderingException;
import com.atlassian.templaterenderer.velocity.one.six.VelocityTemplateRenderer;

import java.util.Collections;
import java.util.Map;
import java.io.IOException;
import java.io.Writer;

/**
 * Service factory for instantiating a template renderer for the given bundle
 */
////////////////////
// TODO: Once plugins has upgraded to Spring DM 1.2, remove implements TemplateRenderer.  This exists to workaround
// the bug fixed in https://fisheye.springsource.org/changelog/spring-osgi?cs=2071
////////////////////
public class VelocityTemplateRendererServiceFactory implements ServiceFactory, VelocityTemplateRenderer
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
        return new VelocityTemplateRendererImpl(bundleClassLoader, pluginKey, Collections.<String, String>emptyMap(),
            templateContextFactory);
    }

    public void ungetService(Bundle bundle, ServiceRegistration serviceRegistration, Object service)
    {
        // No state is stored, so nothing to do
    }

    ///////////////////
    // TODO: Once plugins has upgraded to Spring DM 1.2, remove this code
    ///////////////////
    public void render(String templateName, Writer writer) throws RenderingException, IOException
    {
        throw new UnsupportedOperationException();
    }

    public void render(String templateName, Map<String, Object> context, Writer writer)
        throws RenderingException, IOException
    {
        throw new UnsupportedOperationException();
    }

    public String renderFragment(String fragment, Map<String, Object> context) throws RenderingException
    {
        throw new UnsupportedOperationException();
    }

    public boolean resolve(String templateName)
    {
        throw new UnsupportedOperationException();
    }
    //////////////////
    // END TODO
    //////////////////

}
