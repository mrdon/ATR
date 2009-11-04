package com.atlassian.templaterenderer.velocity.one.six;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.templaterenderer.TemplateRenderer;

public class VelocityOnePointSixTestServlet extends HttpServlet
{
    private TemplateRenderer renderer;

    public VelocityOnePointSixTestServlet(TemplateRenderer renderer)
    {
        this.renderer = renderer;
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html");
        renderer.render("velocity-1.6.vm", response.getWriter());
    }
}
