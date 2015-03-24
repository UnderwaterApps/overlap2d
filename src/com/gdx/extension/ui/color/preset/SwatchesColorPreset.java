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
package com.gdx.extension.ui.color.preset;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdx.extension.ui.color.SwatchesColorPicker;

/**
 * Base class for presets of {@link SwatchesColorPicker swatches color picker}.
 * The default color range cover all 256 shades.
 * 
 * @author Kyu
 *
 */
public class SwatchesColorPreset
{

    protected Array<Color> colors = new Array<Color>();

    protected Vector2 colorRange = new Vector2(0f, 255f);

    protected int itemCountX;
    protected int itemCountY;

    /**
     * Create a preset with the specified size.
     * If itemCountX * itemCountY is higher than 256, then it will be clamp.
     * 
     * @param itemCountX colors per rows
     * @param itemCountY colors per columns
     */
    public SwatchesColorPreset(int itemCountX, int itemCountY)
    {
	this.itemCountX = itemCountX;
	this.itemCountY = itemCountY;
    }

    /**
     * @return the color range to display
     */
    public Vector2 getColorRange()
    {
	return colorRange;
    }

    /**
     * Define the color range to display.
     * 
     * @param colorRange
     */
    public void setColorRange(Vector2 colorRange)
    {
	this.colorRange = colorRange;
    }

    /**
     * @return the number of columns to display
     */
    public int getItemCountX()
    {
	return itemCountX;
    }

    /**
     * Define the number of columns to display.
     * 
     * @param itemCountX the number of columns
     */
    public void setItemCountX(int itemCountX)
    {
	this.itemCountX = itemCountX;
    }

    /**
     * @return the number of rows to display
     */
    public int getItemCountY()
    {
	return itemCountY;
    }

    /**
     * Define the number of rows to display.
     * 
     * @param itemCountY the number of rows
     */
    public void setItemCountY(int itemCountY)
    {
	this.itemCountY = itemCountY;
    }

    /**
     * @return the colors of this preset
     */
    public Array<Color> getColors()
    {
	return colors;
    }

}
