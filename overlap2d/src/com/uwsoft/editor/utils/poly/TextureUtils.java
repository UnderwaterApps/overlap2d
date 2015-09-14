/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.utils.poly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class TextureUtils {
	private static final int[] potWidths = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 5096};

	// -------------------------------------------------------------------------

	public static TextureRegion getPOTTexture(String path) {
		if (path == null) return null;

		FileHandle file = Gdx.files.absolute(path);
		if (!file.exists()) return null;

		Pixmap pixmap = new Pixmap(file);
		int origW = pixmap.getWidth();
		int origH = pixmap.getHeight();
		int w = getNearestPOT(origW);
		int h = getNearestPOT(origH);
		int len = Math.max(w, h);

		Pixmap potPixmap = new Pixmap(len, len, pixmap.getFormat());
		potPixmap.drawPixmap(pixmap, 0, 0, 0, 0, origW, origH);
		pixmap.dispose();

		Texture texture = new Texture(potPixmap);
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		return new TextureRegion(texture, 0, 0, origW, origH);
	}

	public static Pixmap getPOTPixmap(String path) {
		if (path == null) return null;

		FileHandle file = Gdx.files.absolute(path);
		if (!file.exists()) return null;

		Pixmap pixmap = new Pixmap(file);
		int origW = pixmap.getWidth();
		int origH = pixmap.getHeight();
		int w = getNearestPOT(origW);
		int h = getNearestPOT(origH);
		int len = Math.max(w, h);

		Pixmap potPixmap = new Pixmap(len, len, pixmap.getFormat());
		potPixmap.drawPixmap(pixmap, 0, 0, 0, 0, origW, origH);
		pixmap.dispose();

		return potPixmap;
	}

	// -------------------------------------------------------------------------

	private static int getNearestPOT(int d) {
		for (int i=0; i<potWidths.length; i++)
			if (d <= potWidths[i])
				return potWidths[i];
		return -1;
	}

	public static Pixmap getPOTPixmap(Texture texture) {
		if (texture == null) return null;
		texture.getTextureData().prepare();
		Pixmap pixmap = texture.getTextureData().consumePixmap();
		int origW = pixmap.getWidth();
		int origH = pixmap.getHeight();
		int w = getNearestPOT(origW);
		int h = getNearestPOT(origH);
		int len = Math.max(w, h);

		Pixmap potPixmap = new Pixmap(len, len, pixmap.getFormat());
		potPixmap.drawPixmap(pixmap, 0, 0, 0, 0, origW, origH);
		pixmap.dispose();

		return potPixmap;
	}
	
	public static TextureRegion getPOTTexture(Texture texture) {
		if (texture == null) return null;

		

		texture.getTextureData().prepare();
		Pixmap pixmap = texture.getTextureData().consumePixmap();
		int origW = pixmap.getWidth();
		int origH = pixmap.getHeight();
		int w = getNearestPOT(origW);
		int h = getNearestPOT(origH);
		int len = Math.max(w, h);

		Pixmap potPixmap = new Pixmap(len, len, pixmap.getFormat());
		potPixmap.drawPixmap(pixmap, 0, 0, 0, 0, origW, origH);
		pixmap.dispose();

		Texture otherTexture = new Texture(potPixmap);
		otherTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		return new TextureRegion(otherTexture, 0, 0, origW, origH);
	}
}
