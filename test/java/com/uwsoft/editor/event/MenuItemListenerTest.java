package com.uwsoft.editor.event;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.uwsoft.editor.Overlap2DFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Overlap2DFacade.class)
public class MenuItemListenerTest {
    @Mock
    private Overlap2DFacade overlap2DFacade;
    private MenuItemListener menuItemListener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(Overlap2DFacade.class);
        when(Overlap2DFacade.getInstance()).thenReturn(overlap2DFacade);
        menuItemListener = new MenuItemListener("test");
    }

    @Test
    public void shouldSendCommandName() throws Exception {
        menuItemListener.changed(mock(ChangeListener.ChangeEvent.class), mock(Actor.class));

        verify(overlap2DFacade).sendNotification(eq("test"));
    }

    @Test
    public void shouldSendCommandNameAndData() throws Exception {
        menuItemListener = new MenuItemListener("test", "data");

        menuItemListener.changed(mock(ChangeListener.ChangeEvent.class), mock(Actor.class));

        verify(overlap2DFacade).sendNotification(eq("test"), eq("data"));
    }

    @Test
    public void shouldSendAll() throws Exception {
        menuItemListener = new MenuItemListener("test", "data", "type");

        menuItemListener.changed(mock(ChangeListener.ChangeEvent.class), mock(Actor.class));

        verify(overlap2DFacade).sendNotification(eq("test"), eq("data"), eq("type"));
    }
}