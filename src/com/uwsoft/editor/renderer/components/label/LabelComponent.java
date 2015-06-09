package com.uwsoft.editor.renderer.components.label;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

public class LabelComponent extends Component {
	public  LabelStyle style;
	public final GlyphLayout layout = new GlyphLayout();
	public final StringBuilder text = new StringBuilder();
	public BitmapFontCache cache;
	public int labelAlign = Align.left;
	public int lineAlign = Align.left;
	public boolean wrap;
	public float fontScaleX;
	public float fontScaleY;
	public Vector2 prefSize;
	
	
	public LabelComponent (CharSequence text, Skin skin) {
		this(text, skin.get(LabelStyle.class));
	}

	public LabelComponent (CharSequence text, Skin skin, String styleName) {
		this(text, skin.get(styleName, LabelStyle.class));
	}

	/** Creates a label, using a {@link LabelStyle} that has a BitmapFont with the specified name from the skin and the specified
	 * color. */
	public LabelComponent (CharSequence text, Skin skin, String fontName, Color color) {
		this(text, new LabelStyle(skin.getFont(fontName), color));
	}

	/** Creates a label, using a {@link LabelStyle} that has a BitmapFont with the specified name and the specified color from the
	 * skin. */
	public LabelComponent (CharSequence text, Skin skin, String fontName, String colorName) {
		this(text, new LabelStyle(skin.getFont(fontName), skin.getColor(colorName)));
	}

	public LabelComponent (CharSequence text, LabelStyle style) {
		if (text != null) this.text.append(text);
		setStyle(style);
		//TODO
		//if (text != null && text.length() > 0) setSize(getPrefWidth(), getPrefHeight());
	}

	public void setStyle (LabelStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		if (style.font == null) throw new IllegalArgumentException("Missing LabelStyle font.");
		this.style = style;
		cache = style.font.newFontCache();
	}
	
	public LabelStyle getStyle () {
		return style;
	}

	/** @param newText May be null, "" will be used. */
	public void setText (CharSequence newText) {
		if (newText == null) newText = "";
		if (newText instanceof StringBuilder) {
			if (text.equals(newText)) return;
			text.setLength(0);
			text.append((StringBuilder)newText);
		} else {
			if (textEquals(newText)) return;
			text.setLength(0);
			text.append(newText);
		}
	}
	
	public boolean textEquals (CharSequence other) {
		int length = text.length;
		char[] chars = text.chars;
		if (length != other.length()) return false;
		for (int i = 0; i < length; i++)
			if (chars[i] != other.charAt(i)) return false;
		return true;
	}

	public StringBuilder getText () {
		return text;
	}
	
	public void setFontScale (float fontScale) {
		this.fontScaleX = fontScale;
		this.fontScaleY = fontScale;
	}

	public void setFontScale (float fontScaleX, float fontScaleY) {
		this.fontScaleX = fontScaleX;
		this.fontScaleY = fontScaleY;
	}

	public float getFontScaleX () {
		return fontScaleX;
	}

	public void setFontScaleX (float fontScaleX) {
		this.fontScaleX = fontScaleX;
	}

	public float getFontScaleY () {
		return fontScaleY;
	}

	public void setFontScaleY (float fontScaleY) {
		this.fontScaleY = fontScaleY;
	}
	
}
