package com.uwsoft.editor.proxy;

import com.badlogic.gdx.utils.Array;
import com.runner.LibgdxRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class FontManagerTest {
    private FontManager fontManager;

    @Before
    public void setUp() throws Exception {
        fontManager = new FontManager();
        fontManager.onRegister();
    }

    @Test
    public void shouldValidGetFontPath() throws Exception {
        Array<String> fontNames = fontManager.getFontNamesFromMap();

        assertThat(fontNames.size, greaterThanOrEqualTo(0));

        if (fontNames.size > 0) {
            String fontFilePath = fontManager.getFontFilePath(fontNames.random());
            assertThat(new File(fontFilePath).exists(), is(true));
        }
    }

    @Test
    public void shouldFontMap() throws Exception {
        HashMap<String, String> fontNamesFromMap = fontManager.getFontsMap();

        assertThat(fontNamesFromMap.size(), greaterThanOrEqualTo(0));
    }
}