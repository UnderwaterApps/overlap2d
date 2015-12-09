package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;
import com.runner.LibgdxRunner;
import com.runner.NeedGL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class NewProjectDialogTest {
    private NewProjectDialog newProjectDialog;

    @Before
    public void setUp() throws Exception {
        newProjectDialog = new NewProjectDialog();
    }

    @Test
    @NeedGL
    public void shouldFillDifferentValue() throws Exception {
        assertThat(newProjectDialog.getTitleLabel().getText().toString(), is("Create New Project"));
        assertThat(newProjectDialog.getOriginWidth(), is("1920"));
        assertThat(newProjectDialog.getOriginHeight(), is("1200"));
        assertThat(newProjectDialog.getPixelPerWorldUnit(), is("80"));
    }
}