package com.atlassian.templaterenderer.velocity;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Enumeration;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;

/**
 * A CompositeClassLoader that will try to load classes/resources using multiple classloaders.  It will try to load
 * using the order in which classloaders where supplied and will return the first match.
 */
public class CompositeClassLoader extends ClassLoader
{
    private static final Logger log = Logger.getLogger(CompositeClassLoader.class);

    private final Set<ClassLoader> classLoaders;

    /**
     * Creates a new composite class loader.  At least one class loader needs to be supplied. Please note that order of
     * the supplied class loaders is the order in which they will be queried.
     *
     * @param classLoaders A list of class loaders to query.
     */
    public CompositeClassLoader(final ClassLoader... classLoaders)
    {
        super();
        if (classLoaders == null || classLoaders.length == 0)
        {
            throw new IllegalArgumentException("At least one classLoader must be supplied!");
        }
        this.classLoaders = new LinkedHashSet<ClassLoader>(Arrays.asList(classLoaders));
    }


    @Override
    public void clearAssertionStatus()
    {
        for (ClassLoader classLoader : classLoaders)
        {
            classLoader.clearAssertionStatus();
        }
    }

    @Override
    public URL getResource(final String name)
    {
        for (ClassLoader classLoader : classLoaders)
        {
            final URL resource = classLoader.getResource(name);
            if (resource != null)
            {
                return resource;
            }
        }
        return null;
    }

    @Override
    public InputStream getResourceAsStream(final String name)
    {
        for (ClassLoader classLoader : classLoaders)
        {
            final InputStream resource = classLoader.getResourceAsStream(name);
            if (resource != null)
            {
                return resource;
            }
        }
        return null;
    }

    @Override
    public Enumeration<URL> getResources(final String name) throws IOException
    {
        IOException ioe = null;
        for (ClassLoader classLoader : classLoaders)
        {
            final Enumeration<URL> resources;
            try
            {
                resources = classLoader.getResources(name);
                if (resources != null && resources.hasMoreElements())
                {
                    return resources;
                }
            }
            catch (IOException e)
            {
                ioe = e;
            }
        }
        if (ioe != null)
        {
            throw ioe;
        }
        return null;
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException
    {
        for (ClassLoader classLoader : classLoaders)
        {
            final Class<?> aClass;
            try
            {
                aClass = classLoader.loadClass(name);
                if (aClass != null)
                {
                    return aClass;
                }
            }
            catch (ClassNotFoundException e)
            {
                log.debug("Underlying classloader '" + classLoader + "' couldn't find class", e);
            }

        }
        throw new ClassNotFoundException("Class '" + name + "' could not be found!");
    }

    @Override
    public void setClassAssertionStatus(final String className, final boolean enabled)
    {
        for (ClassLoader classLoader : classLoaders)
        {
            classLoader.setClassAssertionStatus(className, enabled);
        }
    }

    @Override
    public void setDefaultAssertionStatus(final boolean enabled)
    {
        for (ClassLoader classLoader : classLoaders)
        {
            classLoader.setDefaultAssertionStatus(enabled);
        }
    }

    @Override
    public void setPackageAssertionStatus(final String packageName, final boolean enabled)
    {
        for (ClassLoader classLoader : classLoaders)
        {
            classLoader.setPackageAssertionStatus(packageName, enabled);
        }
    }
}
