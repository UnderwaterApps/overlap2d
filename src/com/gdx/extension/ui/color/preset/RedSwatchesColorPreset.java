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


/**
 * A preset that represent all red shades.
 * 
 * @author Kyu
 *
 */
public class RedSwatchesColorPreset extends SwatchesColorPreset
{
    
    /**
     * Create a preset for all red shades.
     * 
     * @param itemCountX number of colors per rows
     * @param itemCountY number of color per columns
     */
    public RedSwatchesColorPreset(int itemCountX, int itemCountY)
    {
	super(itemCountX, itemCountY);
	
	int _itemCount = this.itemCountX * this.itemCountY;
	for(int i = 1; i <= _itemCount; i++)
	{
	    float _color = ((i + colorRange.x) * (colorRange.y / _itemCount));
	    colors.add(new Color(_color / 256, 0f, 0f, 1f)); 
	}
    }
    
}
