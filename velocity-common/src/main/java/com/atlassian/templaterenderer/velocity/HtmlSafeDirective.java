package com.atlassian.templaterenderer.velocity;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * Marker directive to indicate that this template has been designed to work correctly with Anti-XSS measures.
 */
public final class HtmlSafeDirective extends Directive
{
    public String getName()
    {
        return "htmlSafe";
    }

    public int getType()
    {
        return Directive.LINE;
    }

    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException
    {
        return true;
    }
}
