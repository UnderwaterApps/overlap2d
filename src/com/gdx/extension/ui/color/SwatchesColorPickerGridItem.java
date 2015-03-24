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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.gdx.extension.ui.grid.GridSelectionItem;

/**
 * Create a view of the color to add in the {@link SwatchesColorPicker swatches color picker}.
 * 
 * @author Kyu
 *
 */
public class SwatchesColorPickerGridItem extends GridSelectionItem
{

    private Image image;
    private Color color;
    
    /**
     * Create an item with a style.
     * 
     * @param style the style to use
     * @param image the image of the color to display
     * @param color the color of the image
     */
    public SwatchesColorPickerGridItem(GridSelectionItemStyle style, Image image, Color color)
    {
	super(style);
	
	this.image = image;
	this.color = color;
	
	setActor(image);
    }
    
    /**
     * @return the color of this item
     */
    public Color getSampleColor()
    {
	return color;
    }

}
