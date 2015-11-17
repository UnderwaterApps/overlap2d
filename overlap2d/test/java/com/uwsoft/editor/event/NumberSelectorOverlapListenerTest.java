package com.uwsoft.editor.event;

import com.uwsoft.editor.Overlap2DFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Overlap2DFacade.class)
public class NumberSelectorOverlapListenerTest {
    @Mock
    private Overlap2DFacade overlap2DFacade;
    private NumberSelectorOverlapListener numberSelectorOverlapListener;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Overlap2DFacade.class);
        PowerMockito.when(Overlap2DFacade.getInstance()).thenReturn(overlap2DFacade);
        numberSelectorOverlapListener = new NumberSelectorOverlapListener("test");
    }

    @Test
    public void shouldSendNumber() throws Exception {
        numberSelectorOverlapListener.changed(1f);

        verify(overlap2DFacade).sendNotification(eq("test"), eq(1f));
    }
}