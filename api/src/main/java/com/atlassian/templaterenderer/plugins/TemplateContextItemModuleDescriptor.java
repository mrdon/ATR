package com.atlassian.templaterenderer.plugins;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.springframework.context.ApplicationContext;

import com.atlassian.plugin.AutowireCapablePlugin;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.StateAware;
import com.atlassian.plugin.descriptors.AbstractModuleDescriptor;
import com.atlassian.plugin.osgi.factory.OsgiPlugin;

/**
 * Module descriptor for template context items.  These may either be a class that gets instantiated on each lookup, or
 * they may reference a component using component-ref.
 */
public class TemplateContextItemModuleDescriptor extends AbstractModuleDescriptor<Object> implements
    StateAware
{
    private Logger log = Logger.getLogger(TemplateContextItemModuleDescriptor.class);
    private boolean global = false;
    private String contextKey;
    private String componentRef = null;
    private Object component = null;

    private ApplicationContext applicationContext;

    @Override public void init(Plugin plugin, Element element) throws PluginParseException
    {
        super.init(plugin, element);
        Attribute globalAtt = element.attribute("global");
        if (globalAtt != null)
        {
            global = Boolean.parseBoolean(globalAtt.getValue());
        }
        Attribute contextKeyAtt = element.attribute("context-key");
        if (contextKeyAtt == null)
        {
            throw new PluginParseException("context-key must be specified");
        }
        contextKey = contextKeyAtt.getValue();
        Attribute componentRefAttr = element.attribute("component-ref");
        Attribute classAttr = element.attribute("class");
        if (componentRefAttr != null)
        {
            if (classAttr != null)
            {
                throw new PluginParseException("You may not specify both a class and a component-ref");
            }
            componentRef = componentRefAttr.getValue();
        }
        else if (classAttr == null)
        {
            throw new PluginParseException("You must specify a class or a component-ref");
        }
    }

    @Override
    public synchronized Object getModule()
    {
        // We don't cache componentRefs, because if the user wants to use a prototype component, then
        // caching it would undermine that.  It's just a hash map lookup anyway.
        if (componentRef != null)
        {
            return getApplicationContext().getBean(componentRef);
        }
        else
        {
            if (component == null)
            {
                component = ((AutowireCapablePlugin) getPlugin()).autowire(getModuleClass());
            }
            return component;
        }
    }

    private ApplicationContext getApplicationContext()
    {
        // Get the bundles applicationContext
        if (applicationContext == null)
        {
            OsgiPlugin osgiPlugin = (OsgiPlugin) getPlugin();
            BundleContext bundleContext = osgiPlugin.getBundle().getBundleContext();
            try
            {
                // Read chapter 4 of the Spring DM guide to understand what on earth is going on here
                ServiceReference[] srs = bundleContext.getServiceReferences(ApplicationContext.class.getName(),
                    "(org.springframework.context.service.name=" + osgiPlugin.getBundle().getSymbolicName() + ")");
                if (srs.length != 1)
                {
                    log.error(
                        "Spring DM is being evil, there is not exactly one ApplicationContext for the bundle " +
                            osgiPlugin.getBundle().getSymbolicName() + ", there are " + srs.length);
                }
                applicationContext = (ApplicationContext) bundleContext.getService(srs[0]);
            }
            catch (InvalidSyntaxException ise)
            {
                log.error("Bad filter", ise);
            }
        }
        return applicationContext;
    }

    @Override public synchronized void disabled()
    {
        super.disabled();
        component = null;
        applicationContext = null;
    }

    public boolean isGlobal()
    {
        return global;
    }

    public String getContextKey()
    {
        return contextKey;
    }
}
