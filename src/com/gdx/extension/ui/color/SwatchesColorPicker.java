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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gdx.extension.ui.color.preset.SwatchesColorPreset;
import com.gdx.extension.ui.grid.GridSelection;
import com.gdx.extension.ui.grid.GridSelectionItem.GridSelectionItemStyle;


/**
 * Create a grid where you can select a color.
 * You can display up to 256 shades.
 * 
 * @author Kyu
 *
 */
public class SwatchesColorPicker extends GridSelection<SwatchesColorPickerGridItem>
{
    
    private Skin skin;
    private GridSelectionItemStyle style;
    
    private SwatchesColorPreset preset;
    
    /**
     * Create a {@link SwatchesColorPicker} using a {@link SwatchesColorPreset preset} and a default style.
     * 
     * @param preset the preset to use
     * @param isVertical if the swatches color picker should be in vertical mode
     * @param skin the skin to use
     */
    public SwatchesColorPicker(SwatchesColorPreset preset, boolean isVertical, Skin skin)
    {
	this(preset, isVertical, skin, "default");
    }
    
    /**
     * Create a {@link SwatchesColorPicker} using a {@link SwatchesColorPreset preset} and a specified style.
     * 
     * @param preset the preset to use
     * @param isVertical if the swatches color picker should be in vertical mode
     * @param skin the skin to use
     * @param styleName the style to apply
     */
    public SwatchesColorPicker(SwatchesColorPreset preset, boolean isVertical, Skin skin, String styleName)
    {
	super(isVertical, ((isVertical) ? preset.getItemCountX() : preset.getItemCountY()));
	
	this.skin = skin;
	this.style = skin.get(styleName, GridSelectionItemStyle.class);
	this.preset = preset;
	
	itemWidth = 16f;
	itemHeight = 16f;
	
	initialize();
    }
    
    /**
     * Initialize the color picker by adding items.
     */
    protected void initialize()
    {
	for (Color _color : preset.getColors())
	{
	    Pixmap _pixMap = new Pixmap((int) getItemWidth(), (int) getItemHeight(), Format.RGBA8888);
	    _pixMap.setColor(_color);
	    _pixMap.fill();
	    SwatchesColorPickerGridItem _item = new SwatchesColorPickerGridItem(style, new Image(new Texture(_pixMap)), _color);
	    _pixMap.dispose();
	    addItem(_item);
	}
    }
    
    /**
     * @return the selected {@link Color color} or null if no color selected
     */
    public Color getSelectedColor()
    {
	SwatchesColorPickerGridItem _item = getSelection().first();
	if(_item == null)
	    return null;
	
	return _item.getSampleColor();
    }
    
}
