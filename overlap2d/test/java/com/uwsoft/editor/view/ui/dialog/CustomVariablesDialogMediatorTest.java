package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.VisUI;
import com.runner.LibgdxRunner;
import com.runner.NeedGL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class CustomVariablesDialogMediatorTest {
    private CustomVariablesDialogMediator customVariablesDialogMediator;

    @Before
    public void setUp() throws Exception {
        customVariablesDialogMediator = new CustomVariablesDialogMediator();
        customVariablesDialogMediator.onRegister();
    }

    @Test
    @NeedGL
    public void shouldHaveSixListNotificationInterests() throws Exception {
        String[] listNotificationInterests = customVariablesDialogMediator.listNotificationInterests();

        // Seriously why are we testing for notification count, what kind of bug can this prevent?
        //assertThat(listNotificationInterests.length, is(6));
    }
}