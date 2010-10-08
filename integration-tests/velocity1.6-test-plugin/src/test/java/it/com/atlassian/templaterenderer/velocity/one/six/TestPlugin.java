package it.com.atlassian.templaterenderer.velocity.one.six;

import com.atlassian.templaterenderer.velocity.integrationtest.TestRunner;
import org.junit.Test;

import java.io.IOException;

public class TestPlugin
{
    @Test
    public void run() throws IOException
    {
        TestRunner.runServletCheck("velocity1.6", "velocity-1.6");
    }
}
