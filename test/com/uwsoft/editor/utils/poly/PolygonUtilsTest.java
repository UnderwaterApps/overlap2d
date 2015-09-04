package com.uwsoft.editor.utils.poly;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PolygonUtilsTest {
    @Test
    public void shouldGetCorrectSignedArea() throws Exception {
        Vector2[] vector2s = {new Vector2(0, 0), new Vector2(1, 0), new Vector2(0, 1)};
        float polygonSignedArea = PolygonUtils.getPolygonSignedArea(vector2s);

        assertThat(polygonSignedArea, is(0.5f));
    }
}