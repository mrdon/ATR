package com.atlassian.templaterenderer.velocity.one.five.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Mock;

import com.atlassian.templaterenderer.velocity.one.five.VelocityTemplateRendererFactory;
import com.atlassian.templaterenderer.velocity.one.five.VelocityTemplateRenderer;
import com.atlassian.templaterenderer.TemplateContextFactory;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class VelocityTemplateRendererFactoryImplTest
{
    @Mock TemplateContextFactory templateContextFactory;
    VelocityTemplateRendererFactory rendererFactory;

    @Before
    public void setUp()
    {
        rendererFactory = new VelocityTemplateRendererFactoryImpl(templateContextFactory,
            Thread.currentThread().getContextClassLoader(), "pluginkey");
    }

    @Test
    public void assertThatGetInstanceReturnsVelocityTemplateRenderer()
    {
        assertThat(rendererFactory.getInstance(Collections.<String, String>emptyMap()), is(instanceOf(
            VelocityTemplateRenderer.class)));
    }
}
