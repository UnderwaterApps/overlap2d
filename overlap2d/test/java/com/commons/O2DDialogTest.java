package com.commons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidableTextField;
import com.runner.LibgdxRunner;
import com.runner.NeedGL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class O2DDialogTest {
    private O2DDialog o2DDialog;

    @Before
    public void setUp() throws Exception {
        VisUI visUI = new VisUI();
        visUI.load(Gdx.files.local("overlap2d/assets/style/uiskin.json"));
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
    public void shouldCreateVisValidableTextFieldWithInputValidator() throws Exception {
        VisValidableTextField visValidableTextField = o2DDialog.createValidableTextField("inputText", input -> true);

        assertThat(visValidableTextField, not(nullValue()));
        assertThat(visValidableTextField.getText(), is("inputText"));
        assertThat(visValidableTextField.getListeners().size, greaterThan(0));
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