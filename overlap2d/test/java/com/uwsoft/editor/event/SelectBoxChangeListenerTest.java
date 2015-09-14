package com.uwsoft.editor.event;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SelectBoxChangeListenerTest {

    private SelectBoxChangeListener selectBoxChangeListener;

    @Before
    public void setUp() throws Exception {
        selectBoxChangeListener = new SelectBoxChangeListener("test");
    }

    @Test
    public void shouldUpdateSelectedValueWhenSelectBoxChanged() throws Exception {
        ChangeEvent changeEvent = mock(ChangeEvent.class);
        VisSelectBox visSelectBox = mock(VisSelectBox.class);
        given(visSelectBox.getSelected()).willReturn("new-selected");
        selectBoxChangeListener.changed(changeEvent, visSelectBox);

        Object lastSelected = Whitebox.getInternalState(selectBoxChangeListener, "lastSelected");
        assertThat(lastSelected, is("new-selected"));
    }
}