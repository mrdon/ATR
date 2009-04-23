package com.atlassian.templaterenderer.plugins;

import org.osgi.framework.Bundle;
import org.apache.commons.lang.Validate;
import org.apache.commons.collections.iterators.IteratorEnumeration;
import com.atlassian.plugin.util.resource.AlternativeResourceLoader;
import com.atlassian.plugin.util.resource.NoOpAlternativeResourceLoader;
import com.atlassian.plugin.util.resource.AlternativeDirectoryResourceLoader;

import java.net.URL;
import java.util.Enumeration;
import java.util.Arrays;
import java.io.IOException;

/**
 * Utility methods for accessing a bundle as if it was a classloader.  Copied from atlassian-plugins-osgi, as
 * this class is package private there.  If we are ever given access (currently not) to the class loader that comes
 * from the OsgiPluginHelper, this can probably be got rid of.
 */
public class BundleClassLoaderAccessor
{
    public static ClassLoader getClassLoader(final Bundle bundle)
    {
        return new BundleClassLoader(bundle, new AlternativeDirectoryResourceLoader());
    }

    ///CLOVER:OFF
    /**
     * Fake classloader that delegates to a bundle
     */
    private static class BundleClassLoader extends ClassLoader
    {
        private final Bundle bundle;
        private final AlternativeResourceLoader altResourceLoader;

        public BundleClassLoader(final Bundle bundle, AlternativeResourceLoader altResourceLoader)
        {
            Validate.notNull(bundle, "The bundle must not be null");
            if (altResourceLoader == null)
            {
                altResourceLoader = new NoOpAlternativeResourceLoader();
            }
            this.altResourceLoader = altResourceLoader;
            this.bundle = bundle;

        }

        @Override
        public Class<?> findClass(final String name) throws ClassNotFoundException
        {
            return bundle.loadClass(name);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Enumeration<URL> findResources(final String name) throws IOException
        {
            Enumeration<URL> e = bundle.getResources(name);

            // For some reason, getResources() sometimes returns nothing, yet getResource() will return one.  This code
            // handles that strange case
            if (!e.hasMoreElements())
            {
                final URL resource = findResource(name);
                if (resource != null)
                {
                    e = new IteratorEnumeration(Arrays.asList(resource).iterator());
                }
            }
            return e;
        }

        @Override
        public URL findResource(final String name)
        {
            URL url = altResourceLoader.getResource(name);
            if (url == null)
            {
                url = bundle.getResource(name);
            }
            return url;
        }
    }
}