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
package com.gdx.extension.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * @author Kyu
 */
public class ProgressBar extends Widget
{

    private ProgressBarStyle style;

    private float percent;

    /**
     * Create a {@link ProgressBar} with default style
     * 
     * @param skin the skin to use
     */
    public ProgressBar(Skin skin)
    {
	this(skin, "default");
    }

    /**
     * Create a {@link ProgressBar} with a specified style.
     * 
     * @param skin the skin to use
     * @param styleName the style to use
     */
    public ProgressBar(Skin skin, String styleName)
    {
	setStyle(skin.get(styleName, ProgressBarStyle.class));

	setSize(200f, 25f);
    }

    /**
     * Set the style of this {@link ProgressBar}.
     * 
     * @param style the style to apply
     */
    public void setStyle(ProgressBarStyle style)
    {
	if (style == null)
	    throw new IllegalArgumentException("style cannot be null.");

	this.style = style;

	if (style.background == null)
	    throw new NullPointerException("background cannot be null");
	if (style.progressBackground == null)
	    throw new NullPointerException("progressBackground cannot be null");

	invalidateHierarchy();
    }

    /**
     * Get the current style of this {@link ProgressBar}.
     * 
     * @return the current style
     */
    public ProgressBarStyle getStyle()
    {
	return style;
    }

    /**
     * Draw the {@link ProgressBar} by the percent of completion.
     */
    @Override
    public void draw(Batch batch, float parentAlpha)
    {
	super.draw(batch, parentAlpha);

	Color color = getColor();

	batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

	style.background.draw(batch, getX(), getY(), getWidth(), getHeight());

	float _progressWidth = percent * (getWidth() - style.background.getRightWidth() - style.background.getLeftWidth());
	if (_progressWidth > 0f) {
	    style.progressBackground.draw(batch,
		    getX() + style.background.getLeftWidth(),
		    getY() + style.background.getBottomHeight(),
		    _progressWidth,
		    getHeight() - style.background.getTopHeight() - style.background.getBottomHeight());
	}
    }

    /**
     * @return the prefHeight
     */
    @Override
    public float getPrefHeight()
    {
	return getHeight();
    }

    /**
     * @return the prefWidth
     */
    @Override
    public float getPrefWidth()
    {
	return getWidth();
    }

    /**
     * Set the current percentage of this {@link ProgressBar}. (Range 0f - 1f)
     * 
     * @param percent new percentage
     */
    public void setPercent(float percent)
    {
	if (percent < 0f)
	    percent = 0f;
	else if (percent > 1f)
	    percent = 1f;

	this.percent = percent;
    }

    /**
     * Get the current percentage of completion of this {@link ProgressBar}. (Range 0f - 1f)
     * 
     * @return the current percentage of completion
     */
    public float getPercent()
    {
	return percent;
    }

    /** The style for a {@link ProgressBar}. */
    public static class ProgressBarStyle
    {

	private Drawable background, progressBackground;

	public ProgressBarStyle()
	{
	}

	public ProgressBarStyle(Drawable background, Drawable progressBackground)
	{
	    this.background = background;
	    this.progressBackground = progressBackground;
	}

	public ProgressBarStyle(ProgressBarStyle style)
	{
	    this.background = style.background;
	    this.progressBackground = style.progressBackground;
	}
    }

}
