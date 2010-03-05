package com.atlassian.templaterenderer.velocity.one.six.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.atlassian.templaterenderer.TemplateContextFactory;
import com.atlassian.templaterenderer.velocity.one.six.VelocityTemplateRenderer;
import com.google.common.collect.ImmutableMap;

public class VelocityTemplateRendererImplTest
{
    VelocityTemplateRenderer renderer;
    
    @Before
    public void setUp()
    {
        TemplateContextFactory contextFactory = mock(TemplateContextFactory.class);
        when(contextFactory.createContext(anyString(), anyMap())).thenAnswer(new Answer<Map<String, Object>>()
        {
            @SuppressWarnings("unchecked")
            public Map<String, Object> answer(InvocationOnMock invocation) throws Throwable
            {
                return (Map<String, Object>) invocation.getArguments()[1];
            }
        });
        
        Map<String, String> initProperties = ImmutableMap.of();
        renderer = new VelocityTemplateRendererImpl(getClass().getClassLoader(), "test", initProperties, contextFactory);
    }
    
    @Test
    public void assertThatHtmlEntitiesInVariablesAreEscaped()
    {
        assertThat(
            renderer.renderFragment("$value", ImmutableMap.<String, Object>of("value", "This & that")),
            is(equalTo("This &amp; that"))
        );
    }
}
