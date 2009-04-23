package com.atlassian.templaterenderer.plugins;

import com.atlassian.plugin.descriptors.AbstractModuleDescriptor;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.StateAware;
import com.atlassian.plugin.hostcontainer.HostContainer;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.dom4j.Element;
import org.dom4j.Attribute;

/**
 * Module descriptor for template context items.  These may either be a class that gets instantiated on each lookup, or
 * they may reference a component using component-ref.
 */
public class TemplateContextItemModuleDescriptor extends AbstractModuleDescriptor<Object> implements
    ApplicationContextAware, StateAware
{
    private boolean global = false;
    private String contextKey;
    private String componentRef = null;
    private Object component = null;

    private final HostContainer hostContainer;
    private ApplicationContext applicationContext;

    public TemplateContextItemModuleDescriptor(HostContainer hostContainer)
    {
        this.hostContainer = hostContainer;
    }

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

    public Object getModule()
    {
        // We don't cache componentRefs, because if the user wants to use a prototype component, then
        // caching it would undermine that.  It's just a hash map lookup anyway.
        if (componentRef != null)
        {
            return applicationContext.getBean(componentRef);
        }
        else
        {
            synchronized (this)
            {
                if (component == null)
                {
                    component = hostContainer.create(getModuleClass());
                }
                return component;
            }
        }
    }

    @Override public synchronized void disabled()
    {
        super.disabled();
        component = null;
    }

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
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
