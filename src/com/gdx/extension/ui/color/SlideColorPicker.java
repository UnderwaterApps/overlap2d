/** Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdx.extension.ui.color;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gdx.extension.util.ColorUtil;
import com.gdx.extension.util.ColorUtil.Color;

/**
 * Create a slider to select the color within every possible shade of color using HSB color model.
 * 
 * @author Kyu
 * 
 */
public class SlideColorPicker extends Slider
{

    private float saturation = 1f;
    private float brightness = 1f;

    private boolean isVertical;

    private Vector2 colorRange = new Vector2(0f, 0f);
    private Pixmap pixMap;

    /**
     * Create a {@link SlideColorPicker} with default style.
     * 
     * @param vertical if the slider should be vertical
     * @param skin the skin to use
     */
    public SlideColorPicker(boolean vertical, Skin skin)
    {
	this(vertical, skin, ((vertical) ? "default-vertical" : "default-horizontal"));
    }

    /**
     * Create a {@link SlideColorPicker} with defined style.
     * 
     * @param vertical if the slider should be vertical
     * @param skin the skin to use
     * @param styleName the style to use
     */
    public SlideColorPicker(boolean vertical, Skin skin, String styleName)
    {
	this(0, 359, 1, vertical, skin, styleName);
    }

    public SlideColorPicker(float min, float max, float stepSize, boolean vertical, Skin skin, String styleName)
    {
	super(min, max, stepSize, vertical, skin.get(styleName, SlideColorPickerStyle.class));

	setColorRange(0f, 359f);
	isVertical = vertical;
    }

    /**
     * Generate the slider background.
     */
    public void initialize()
    {
	pixMap = new Pixmap((int) getWidth(), (int) getHeight(), Format.RGBA8888);
	int _colorMin = (int) colorRange.x;
	int _colorMax = (int) colorRange.y;
	int _colorCount = (_colorMax - _colorMin);
	float _scaleRatio = (float) _colorCount / ((isVertical) ? pixMap.getHeight() : pixMap.getWidth());

	for (int i = 0; i <= (isVertical ? pixMap.getHeight() : pixMap.getWidth()); i++)
	{
	    Color _color = ColorUtil.getHSBColor((i * _scaleRatio + _colorMin) / ((float) _colorMax), saturation, brightness);

	    pixMap.setColor(_color.getRed() / 256f, _color.getGreen() / 256f, _color.getBlue() / 256f, _color.getAlpha() / 256f);
	    if (isVertical)
		pixMap.drawLine(0, i, pixMap.getWidth(), i);
	    else
		pixMap.drawLine(i, 0, i, pixMap.getHeight());
	}

	TextureRegionDrawable _background = new TextureRegionDrawable(new TextureRegion(new Texture(pixMap)));
	if (isVertical)
	    _background.getRegion().flip(false, true);
	getStyle().background = _background;
	setStyle(getStyle());
	pixMap.dispose();
    }

    /**
     * Apply the new style.
     * 
     * @param style the style to apply
     */
    public void setStyle(SlideColorPickerStyle style)
    {
	super.setStyle(style);
    }

    /**
     * Get the current style.
     * 
     * setStyle(SlideColorPickerStyle) must be called if you change the style.
     */
    public SlideColorPickerStyle getStyle()
    {
	return (SlideColorPickerStyle) super.getStyle();
    }

    @Override
    public void sizeChanged()
    {
	super.sizeChanged();

	if (colorRange != null)
	    initialize();
    }

    /**
     * @return the saturation of the colors
     */
    public float getSaturation()
    {
	return saturation;
    }

    /**
     * Set the saturation of colors and apply it.
     * 
     * @param saturation the saturation to apply
     */
    public void setSaturation(float saturation)
    {
	this.saturation = saturation;

	initialize();
    }

    /**
     * @return the brightness of the colors
     */
    public float getBrightness()
    {
	return brightness;
    }

    /**
     * Set the brightness of colors and apply it.
     * 
     * @param brightness the brightness to apply
     */
    public void setBrightness(float brightness)
    {
	this.brightness = brightness;

	initialize();
    }

    /**
     * Set the saturation and brightness of colors and apply it.
     * 
     * @param saturation the saturation to apply
     * @param brightness the brightness to apply
     */
    public void setSaturationAndBrightness(float saturation, float brightness)
    {
	this.saturation = saturation;
	this.brightness = brightness;

	initialize();
    }

    /**
     * Define the color range to display. (Between 0 and 359)
     * Min and max are clamped if needed.
     * 
     * @param min the color where start display
     * @param max the color where end to display
     */
    public void setColorRange(float min, float max)
    {
	float _min = min;
	float _max = max;

	if (min < 0f)
	    _min = 0f;
	if (max >= 360f)
	    _max = 359f;

	colorRange.set(_min, _max);
	setRange(_min, _max);
	setStepSize(1 / (isVertical ? getHeight() : getWidth()));

	initialize();
    }

    /**
     * @return the range of colors to display
     */
    public Vector2 getRange()
    {
	return colorRange;
    }

    /**
     * Define the style of a {@link SlideColorPicker}.
     * 
     * @author Kyu
     * 
     */
    public static class SlideColorPickerStyle extends SliderStyle {

    }

}
