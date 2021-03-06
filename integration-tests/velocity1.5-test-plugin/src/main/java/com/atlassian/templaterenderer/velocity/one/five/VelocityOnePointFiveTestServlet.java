package com.atlassian.templaterenderer.velocity.one.five;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.templaterenderer.TemplateRenderer;

public class VelocityOnePointFiveTestServlet extends HttpServlet
{
    private final TemplateRenderer renderer;

    public VelocityOnePointFiveTestServlet(TemplateRenderer renderer)
    {
        this.renderer = renderer;
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html");
        renderer.render("velocity-1.5.vm", response.getWriter());
    }
}
