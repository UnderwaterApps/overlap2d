package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;
import com.runner.LibgdxRunner;
import com.runner.NeedGL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class ImportDialogTest {
    private ImportDialog importDialog;

    @Before
    public void setUp() throws Exception {
        importDialog = new ImportDialog();
    }

    @Test
    @NeedGL
    public void shouldFillTypeNames() throws Exception {
        HashMap typeNames = (HashMap) Whitebox.getInternalState(importDialog, "typeNames");

        assertThat(typeNames.size(), is(9));
        assertThat(typeNames.get(1), is("Texture"));
        assertThat(typeNames.get(9), is("Texture Atlas"));
    }
}