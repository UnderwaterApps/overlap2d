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

package com.uwsoft.editor.gdx.ui.properties;

import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.SelectBoxItem;

public class SelectBoxItemProperties extends PropertyBox implements IPropertyBox<SelectBoxItem<String>> {

	
	
	public SelectBoxItemProperties(SceneLoader scene) {
		super(scene, "SelectBoxItemProperties");
	}

	@Override
	public void setObject(SelectBoxItem<String> object) {
		item = object;		
		
		setListeners();
	}


    @Override
    public void updateView() {

    }

	private void setListeners() {
				
	}
	
}
