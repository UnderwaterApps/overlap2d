package com.uwsoft.editor.controller;

import com.puremvc.patterns.observer.BaseNotification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.PluginManager;
import com.uwsoft.editor.proxy.ProjectManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Overlap2DFacade.class, PluginManager.class, BootstrapPlugins.class})
public class BootstrapPluginsTest {
    private BootstrapPlugins bootstrapPlugins;
    @Mock
    private Overlap2DFacade overlap2DFacade;

    @Mock
    private ProjectManager projectManager;
    @Mock
    private PluginManager pluginManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(Overlap2DFacade.class);
        PowerMockito.when(Overlap2DFacade.getInstance()).thenReturn(overlap2DFacade);
        whenNew(PluginManager.class).withNoArguments().thenReturn(pluginManager);
        given(overlap2DFacade.retrieveProxy(ProjectManager.NAME)).willReturn(projectManager);
        bootstrapPlugins = new BootstrapPlugins();
    }

    @Test
    public void shouldInitAllPlugins() throws Exception {
        bootstrapPlugins.execute(new BaseNotification("baseNotification"));

        verify(overlap2DFacade).registerProxy(this.pluginManager);
    }
}