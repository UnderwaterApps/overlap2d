package com.uwsoft.editor.event;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.uwsoft.editor.view.ui.widget.EditableSelectBox;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class EditableSelectBoxChangeListenerTest {

    private EditableSelectBoxChangeListener editableSelectBoxChangeListener;

    @Before
    public void setUp() throws Exception {
        editableSelectBoxChangeListener = new EditableSelectBoxChangeListener("test");
    }

    @Test
    public void shouldUpdateLastValue() throws Exception {
        EditableSelectBox editableSelectBox = mock(EditableSelectBox.class);
        given(editableSelectBox.getSelected()).willReturn("last-value");
        editableSelectBoxChangeListener.changed(mock(ChangeListener.ChangeEvent.class), editableSelectBox);

        Object lastSelected = Whitebox.getInternalState(editableSelectBoxChangeListener, "lastSelected");
        assertThat(lastSelected, is("last-value"));
    }
}