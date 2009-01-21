package com.atlassian.templaterenderer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public interface TemplateRenderer
{
    void render(String templateName, Writer writer)
            throws RenderingException, IOException;

    void render(String templateName, Map<String, Object> context, Writer writer)
            throws RenderingException, IOException;

    String renderFragment(String fragment, Map<String, Object> context) throws IOException;
}
