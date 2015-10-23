package com.uwsoft.editor.proxy;

import com.commons.plugins.O2DPlugin;
import com.commons.plugins.PluginAPI;
import com.uwsoft.editor.view.stage.Sandbox;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Sandbox.class)
public class PluginManagerTest {
    private PluginManager pluginManager;
    @Mock
    private O2DPlugin plugin;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(Sandbox.class);
        when(Sandbox.getInstance()).thenReturn(mock(Sandbox.class));
        pluginManager = new PluginManager();
        pluginManager.onRegister();
    }

    @Test
    public void shouldAbleToRegisterPlugin() throws Exception {
        O2DPlugin o2DPlugin = pluginManager.registerPlugin(plugin);

        ArrayList<O2DPlugin> plugins = (ArrayList<O2DPlugin>) Whitebox.getInternalState(pluginManager, "plugins");

        assertThat(o2DPlugin, not(nullValue()));
        assertThat(plugins, hasSize(1));
    }

    @Test
    public void shouldSetEverythingAfterInit() throws Exception {
        pluginManager.initPlugin(plugin);

        verify(plugin).setAPI(any(PluginAPI.class));
    }

    @Test
    public void shouldDoNothingWhenInitSamePluginSecondTime() throws Exception {
        pluginManager.initPlugin(plugin);
        reset(plugin);
        pluginManager.initPlugin(plugin);
        verify(plugin, never()).setAPI(any(PluginAPI.class));
    }
}