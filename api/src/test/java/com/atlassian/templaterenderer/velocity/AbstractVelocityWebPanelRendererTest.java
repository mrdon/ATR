package com.atlassian.templaterenderer.velocity;

import com.atlassian.plugin.Plugin;
import com.atlassian.templaterenderer.AbstractVelocityWebPanelRenderer;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

public class AbstractVelocityWebPanelRendererTest
{
    @Test
    public void testRenderersAreCached() throws IOException
    {
        final TemplateRenderer mockTemplateRenderer = mock(TemplateRenderer.class);
        final AtomicInteger createCalled = new AtomicInteger(0);

        AbstractVelocityWebPanelRenderer renderer = new AbstractVelocityWebPanelRenderer() {
            @Override
            protected Map<String, TemplateRenderer> createCacheMap()
            {
                //don't use a WeakHashMap for tests
                return new HashMap<String, TemplateRenderer>();
            }
            @Override
            protected TemplateRenderer createRenderer(final Plugin plugin)
            {
                createCalled.incrementAndGet();
                return mockTemplateRenderer;
            }
        };

        assertEquals("velocity", renderer.getResourceType());

        Plugin pluginOne = mock(Plugin.class);
        when(pluginOne.getKey()).thenReturn("plugin-one");

        renderer.render("testResource", pluginOne, Collections.<String, Object>emptyMap(), null);
        renderer.render("testResource", pluginOne, Collections.<String, Object>emptyMap(), null);
        renderer.renderFragment("testFragment", pluginOne, Collections.<String, Object>emptyMap());
        renderer.renderFragment("testFragment", pluginOne, Collections.<String, Object>emptyMap());

        verify(mockTemplateRenderer, times(2)).render("testResource", Collections.<String, Object>emptyMap(), null);
        verify(mockTemplateRenderer, times(2)).renderFragment("testFragment", Collections.<String, Object>emptyMap());

        assertEquals(1, createCalled.get());

        Plugin pluginTwo = mock(Plugin.class);
        when(pluginTwo.getKey()).thenReturn("plugin-two");

        renderer.render("testResource", pluginTwo, Collections.<String, Object>emptyMap(), null);
        renderer.render("testResource", pluginTwo, Collections.<String, Object>emptyMap(), null);
        renderer.renderFragment("testFragment", pluginTwo, Collections.<String, Object>emptyMap());
        renderer.renderFragment("testFragment", pluginTwo, Collections.<String, Object>emptyMap());

        verify(mockTemplateRenderer, times(4)).render("testResource", Collections.<String, Object>emptyMap(), null);
        verify(mockTemplateRenderer, times(4)).renderFragment("testFragment", Collections.<String, Object>emptyMap());

        assertEquals(2, createCalled.get());
    }

}

