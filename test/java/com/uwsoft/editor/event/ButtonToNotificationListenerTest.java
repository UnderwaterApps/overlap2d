package com.uwsoft.editor.event;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Overlap2DFacade.class)
public class ButtonToNotificationListenerTest {
    private ButtonToNotificationListener buttonToNotificationListener;
    @Mock
    private Overlap2DFacade overlap2DFacade;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(Overlap2DFacade.class);
        when(Overlap2DFacade.getInstance()).thenReturn(overlap2DFacade);
        buttonToNotificationListener = new ButtonToNotificationListener("test");
    }

    @Test
    public void shouldSendNotifyAfterButtonClicked() throws Exception {
        buttonToNotificationListener.touchUp(new InputEvent(), 0, 0, 0, 0);

        verify(overlap2DFacade).sendNotification(eq("test"));
    }
}