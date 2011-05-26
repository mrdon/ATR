package com.atlassian.templaterenderer.velocity.tests;

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
import com.atlassian.templaterenderer.TemplateRenderer;
import com.google.common.collect.ImmutableMap;

public abstract class AbstractVelocityRendererImplTest
{
    protected TemplateRenderer renderer;

    protected abstract TemplateRenderer createVelocityRenderer(TemplateContextFactory contextFactory, 
            ClassLoader classLoader,
            String string,
            Map<String, String> initProperties);

    @Before
    public void initRenderer()
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
        
        // ATR-38:  To ensure that property overriding works correctly, pass this property
        // that is also included in our defaults, which is only allowed to have a single value.
        Map<String, String> initProperties = ImmutableMap.of(
            "classpath.resource.loader.cache", "true");
        
        renderer = createVelocityRenderer(contextFactory, getClass().getClassLoader(), "test", initProperties);
    }

    @Test
    public void assertThatHtmlEntitiesInVariablesAreEscaped()
    {
        assertThat(
            renderer.renderFragment("$value", ImmutableMap.<String, Object>of("value", "This & that")),
            is(equalTo("This &amp; that"))
        );
    }

    @Test
    public void assertThatAtrHtmlSafeCausesHtmlEntitiesNotToBeEscaped()
    {
        Object o = new AtrHtmlUnescaped("< This & that >");
        assertThat(
            renderer.renderFragment("$o.string", ImmutableMap.of("o", o)),
            is(equalTo("< This & that >"))
        );
    }

    @Test
    public void assertThatVhsHtmlSafeCausesHtmlEntitiesNotToBeEscaped()
    {
        Object o = new VhsHtmlUnescaped("< This & that >");
        assertThat(
            renderer.renderFragment("$o.string", ImmutableMap.of("o", o)),
            is(equalTo("< This & that >"))
        );
    }
    
    public final class AtrHtmlUnescaped
    {
        private final String string;

        public AtrHtmlUnescaped(String string)
        {
            this.string = string;
        }

        @com.atlassian.templaterenderer.annotations.HtmlSafe
        public String getString()
        {
            return string;
        }
    }

    public final class VhsHtmlUnescaped
    {
        private final String string;

        public VhsHtmlUnescaped(String string)
        {
            this.string = string;
        }

        @com.atlassian.velocity.htmlsafe.HtmlSafe
        public String getString()
        {
            return string;
        }
    }
}
