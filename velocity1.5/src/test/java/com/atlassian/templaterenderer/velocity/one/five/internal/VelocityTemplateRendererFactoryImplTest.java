package com.atlassian.templaterenderer.velocity.one.five.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.templaterenderer.TemplateRendererFactory;

@RunWith(MockitoJUnitRunner.class)
public class VelocityTemplateRendererFactoryImplTest
{
    @Mock I18nResolver i18n;
    @Mock WebResourceManager webResourceManager;
    
    TemplateRendererFactory rendererFactory;
    
    @Before
    public void setUp()
    {
        rendererFactory = new VelocityTemplateRendererFactoryImpl(i18n, webResourceManager);
    }
    
    @Test
    public void assertThatGetInstanceReturnsVelocityTemplateRenderer()
    {
        assertThat(rendererFactory.getInstance(getClass().getClassLoader()), is(instanceOf(VelocityTemplateRenderer.class)));
    }
}
