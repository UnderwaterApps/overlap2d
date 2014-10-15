package com.uwsoft.editor.utils.poly.tracer;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.utils.poly.TextureUtils;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class Tracer {
	public static Vector2[][] trace(Texture texture, float hullTolerance, int alphaTolerance, boolean multiPartDetection, boolean holeDetection) {
		Blending blending = Pixmap.getBlending();
		Pixmap.setBlending(Blending.None);
		Pixmap pixmap = TextureUtils.getPOTPixmap(texture);

		int w = pixmap.getWidth();
		int h = pixmap.getHeight();

		int size = w * h;
		int[] array = new int[size];

		for (int y=0; y<h; y++) {
			for (int x=0; x<w; x++) {
				int color = pixmap.getPixel(x, y);
				array[x + y*w] = color;
			}
		}

		pixmap.dispose();
		Pixmap.setBlending(blending);

		Array<Array<Vector2>> outlines;
		try {
			outlines = TextureConverter.createPolygon(array, w, h, hullTolerance, alphaTolerance, multiPartDetection, holeDetection);
		} catch (Exception e) {
			return null;
		}

		TextureRegion region = TextureUtils.getPOTTexture(texture);
		float tw = region.getRegionWidth();
		float th = region.getRegionHeight();

		Vector2[][] polygons = new Vector2[outlines.size][];

		for (int i=0; i<outlines.size; i++) {
			Array<Vector2> outline = outlines.get(i);
			polygons[i] = new Vector2[outline.size];
			for (int ii=0; ii<outline.size; ii++) {
				polygons[i][ii] = outline.get(ii);
				polygons[i][ii].x /= tw;
				polygons[i][ii].y /= tw;
				polygons[i][ii].y = 1*th/tw - polygons[i][ii].y;
			}
		}

		return polygons;
	}
}
