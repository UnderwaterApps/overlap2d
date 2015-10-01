package com.uwsoft.editor.view.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.runner.LibgdxRunner;
import com.runner.NeedGL;
import com.runner.util.UIHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class O2DLogoTest {
    @Test
    @NeedGL
    public void shouldAbleToGetLogo() throws Exception {
        O2DLogo logo = new O2DLogo();

        List<Actor> allActors = UIHelper.getAllActors(logo);

        assertThat(logo, not(nullValue()));
        assertThat(allActors, hasSize(1));
    }
}