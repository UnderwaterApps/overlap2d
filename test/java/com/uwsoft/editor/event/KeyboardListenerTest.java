package com.uwsoft.editor.event;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener.FocusEvent;
import com.kotcrab.vis.ui.widget.VisTextField;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import static com.badlogic.gdx.scenes.scene2d.utils.FocusListener.FocusEvent.Type.scroll;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class KeyboardListenerTest {

    private KeyboardListener keyboardListener;

    @Before
    public void setUp() throws Exception {
        keyboardListener = new KeyboardListener("test");
    }

    @Test
    public void shouldCallKeyboardHandlerForInputEvent() throws Exception {
        InputEvent inputEvent = mock(InputEvent.class);
        given(inputEvent.getType()).willReturn(InputEvent.Type.keyUp);
        boolean handle = keyboardListener.handle(inputEvent);

        assertThat(handle, is(true));
    }

    @Test
    public void shouldGetValueForInputEventOnTextField() throws Exception {
        InputEvent inputEvent = mock(InputEvent.class);
        VisTextField visTextField = mock(VisTextField.class);
        given(inputEvent.getType()).willReturn(InputEvent.Type.keyUp);
        given(inputEvent.getKeyCode()).willReturn(Input.Keys.ENTER);
        given(inputEvent.getTarget()).willReturn(visTextField);
        given(visTextField.getText()).willReturn("test-value");
        boolean handle = keyboardListener.handle(inputEvent);

        assertThat(handle, is(true));
        String lastValue = (String) Whitebox.getInternalState(keyboardListener, "lastValue");
        assertThat(lastValue, is("test-value"));
    }

    @Test
    public void shouldHandleFocusEvent() throws Exception {
        FocusEvent focusEvent = mock(FocusEvent.class);
        VisTextField visTextField = mock(VisTextField.class);
        given(focusEvent.getTarget()).willReturn(visTextField);
        given(focusEvent.isFocused()).willReturn(true);
        given(focusEvent.getType()).willReturn(scroll);
        given(visTextField.getText()).willReturn("new-value");
        boolean handle = keyboardListener.handle(focusEvent);

        assertThat(handle, is(true));
        String lastValue = (String) Whitebox.getInternalState(keyboardListener, "lastValue");
        assertThat(lastValue, is("new-value"));
    }
}