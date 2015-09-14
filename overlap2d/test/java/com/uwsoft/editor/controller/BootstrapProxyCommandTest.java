package com.uwsoft.editor.controller;

import com.puremvc.patterns.observer.BaseNotification;
import com.puremvc.patterns.proxy.Proxy;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.CursorManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Overlap2DFacade.class, CursorManager.class, BootstrapProxyCommand.class})
public class BootstrapProxyCommandTest {
    private BootstrapProxyCommand bootstrapProxyCommand;

    @Mock
    private Overlap2DFacade overlap2DFacade;
    @Mock
    private CursorManager cursorManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(Overlap2DFacade.class);
        PowerMockito.when(Overlap2DFacade.getInstance()).thenReturn(overlap2DFacade);
        PowerMockito.whenNew(CursorManager.class).withNoArguments().thenReturn(cursorManager);
        bootstrapProxyCommand = new BootstrapProxyCommand();
    }

    @Test
    public void shouldRegisterAllProxy() throws Exception {
        bootstrapProxyCommand.execute(new BaseNotification("baseNotification"));

        verify(overlap2DFacade, times(8)).registerProxy(any(Proxy.class));
    }
}