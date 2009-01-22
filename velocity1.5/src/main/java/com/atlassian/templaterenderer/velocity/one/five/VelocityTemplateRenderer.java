package com.atlassian.templaterenderer.velocity.one.five;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.EscapeTool;
import org.apache.velocity.tools.generic.log.CommonsLogLogSystem;

import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.templaterenderer.RenderingException;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.templaterenderer.velocity.HtmlSafeDirective;
import com.atlassian.templaterenderer.velocity.introspection.TemplateRendererAnnotationBoxingUberspect;

public class VelocityTemplateRenderer implements TemplateRenderer
{
    private final ClassLoader classLoader;
    private final I18nResolver i18n;
    private final WebResourceManager webResourceManager;

    private final VelocityEngine velocity;

    public VelocityTemplateRenderer(ClassLoader classLoader, I18nResolver i18n, WebResourceManager webResourceManager, Map<String, String> properties)
    {
        this.classLoader = classLoader;
        this.i18n = i18n;
        this.webResourceManager = webResourceManager;
        
        velocity = new VelocityEngine();
        velocity.addProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, CommonsLogLogSystem.class.getName());
        velocity.addProperty(Velocity.RESOURCE_LOADER, "classpath");
        velocity.addProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocity.addProperty("runtime.introspector.uberspect", TemplateRendererAnnotationBoxingUberspect.class.getName());
        velocity.addProperty("userdirective", HtmlSafeDirective.class.getName());
        for (Map.Entry<String, String> prop : properties.entrySet())
        {
            velocity.addProperty(prop.getKey(), prop.getValue());
        }
        
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try
        {
            velocity.init();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }
    
    public void render(String templateName, Writer writer) throws RenderingException, IOException
    {
        render(templateName, Collections.<String, Object>emptyMap(), writer);
    }

    public void render(String templateName, Map<String, Object> context, Writer writer) throws RenderingException, IOException
    {
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try
        {
            Template template = velocity.getTemplate(templateName);
            template.merge(createContext(context), writer);
            writer.flush();
        }
        catch (IOException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RenderingException(e);
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }
    
    public String renderFragment(String fragment, Map<String, Object> context)
    {
        try
        {
            StringWriter tempWriter = new StringWriter(fragment.length());
            velocity.evaluate(createContext(context), tempWriter, "renderFragment", fragment);
            return tempWriter.toString();
        }
        catch (Exception e)
        {
            throw new RenderingException(e);
        }

    }
    
    private VelocityContext createContext(Map<String, Object> contextParams)
    {
        VelocityContext context = new VelocityContext();
        context.put("i18n", i18n);
        context.put("webResourceManager", webResourceManager);
        context.put("esc", new EscapeTool());
        for (Map.Entry<String, Object> entry : contextParams.entrySet())
        {
            context.put(entry.getKey(), entry.getValue());
        }
        return context;
    }
}
