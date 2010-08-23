package com.atlassian.templaterenderer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * A service used to abstract the rendering engine used from your code.  You can get specific instances of a
 * TemplateRenderer using the TemplateRendererFactory.
 */
public interface TemplateRenderer
{
    /**
     * Renders the template to the writer with a default context providing an {@code I18nResolver} and {@code
     * WebResourceManager}.
     *
     * @param templateName file name of the template to render
     * @param writer where to write the rendered template
     * @throws RenderingException thrown if there is an internal exception when rendering the template
     * @throws IOException thrown if there is a problem reading the template file or writing to the writer
     */
    void render(String templateName, Writer writer)
        throws RenderingException, IOException;

    /**
     * Renders the template to the writer, using the given context and adding an {@code I18nResolver} and {@code
     * WebResourceManager}.
     *
     * @param templateName file name of the template to render
     * @param context Map of objects to make available in the template rendering process
     * @param writer where to write the rendered template
     * @throws RenderingException thrown if there is an internal exception when rendering the template
     * @throws IOException thrown if there is a problem reading the template file or writing to the writer
     */
    void render(String templateName, Map<String, Object> context, Writer writer)
        throws RenderingException, IOException;

    /**
     * Renders the {@code fragment} using the given context and adding {@code I18nResolver} and {@code
     * WebResourceManager}.
     *
     * @param fragment template fragment to render
     * @param context Map of objects to make available in the template rendering process
     * @return rendered template
     * @throws RenderingException thrown if there is an internal exception when rendering the template
     */
    String renderFragment(String fragment, Map<String, Object> context) throws RenderingException;

    /**
     * Check whether the given template exists or not
     *
     * @param templateName The name of the template to resolve
     * @return True if the template exists
     */
    boolean resolve(String templateName);
}
