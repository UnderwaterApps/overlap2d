package com.uwsoft.editor.event;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.uwsoft.editor.Overlap2DFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Overlap2DFacade.class)
public class CheckBoxChangeListenerTest {
    @Mock
    private Overlap2DFacade overlap2DFacade;
    private CheckBoxChangeListener checkBoxChangeListener;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(Overlap2DFacade.class);
        when(Overlap2DFacade.getInstance()).thenReturn(overlap2DFacade);
        checkBoxChangeListener = new CheckBoxChangeListener("test");
    }

    @Test
    public void shouldSendCheckBoxStatus() throws Exception {
        VisCheckBox visCheckBox = mock(VisCheckBox.class);
        given(visCheckBox.isChecked()).willReturn(true);
        checkBoxChangeListener.changed(mock(ChangeListener.ChangeEvent.class), visCheckBox);

        verify(overlap2DFacade).sendNotification(eq("test"), eq(true));
    }
}