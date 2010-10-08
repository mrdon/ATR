package com.atlassian.templaterenderer.velocity.integrationtest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class TestRunner
{
    private static final String PORT = System.getProperty("http.port", "5990");
    private static final String CONTEXT = System.getProperty("context.path", "/refapp");

    public static void runServletCheck(String servletName, String expectedString) throws IOException
    {
        HttpClient client = new HttpClient();
        String url = String.format("http://localhost:%s%s/plugins/servlet/%s", PORT, CONTEXT, servletName);
        GetMethod method = new GetMethod(url);
        int status = client.executeMethod(method);

        assertEquals(status, HttpStatus.SC_OK);

        String content = method.getResponseBodyAsString();
        assertTrue(method.getResponseBodyAsString().contains(expectedString));
    }
}
