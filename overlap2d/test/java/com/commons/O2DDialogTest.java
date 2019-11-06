package com.commons;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.runner.LibgdxRunner;
import com.runner.NeedGL;

/**
 * TODO: enable this test back when possible
 */

@RunWith(LibgdxRunner.class)
public class O2DDialogTest {
    private O2DDialog o2DDialog;

    @Before
    public void setUp() throws Exception {
        o2DDialog = new O2DDialog("O2DDialog Test");
    }

    @Test
    @NeedGL
    public void shouldCreateTextFieldWithGivenText() throws Exception {
        VisTextField textField = o2DDialog.createTextField("givenText");

        assertThat(textField, not(nullValue()));
        assertThat(textField.getText(), is("givenText"));
        assertThat(textField.getListeners().size, greaterThan(0));
    }

    @Test
    @NeedGL
    public void shouldCreateVisValidatableTextFieldWithInputValidator() throws Exception {
        VisValidatableTextField visValidatableTextField = o2DDialog.createValidableTextField("inputText", input -> true);

        assertThat(visValidatableTextField, not(nullValue()));
        assertThat(visValidatableTextField.getText(), is("inputText"));
        assertThat(visValidatableTextField.getListeners().size, greaterThan(0));
    }

    @Test
    @NeedGL
    public void shouldAddCloseButton() throws Exception {
        o2DDialog.addCloseButton();

        Table titleTable = o2DDialog.getTitleTable();
        assertThat(titleTable.getCells().size, is(2));
        assertThat(titleTable.getCells(), hasItems(
                hasProperty("actor", instanceOf(Label.class)),
                hasProperty("actor", instanceOf(VisImageButton.class))));
        
    }
}