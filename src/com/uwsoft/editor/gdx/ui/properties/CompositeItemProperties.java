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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;

public class CompositeItemProperties extends PropertyBox implements IPropertyBox<CompositeItem>  {

	private 	CheckBox scissorsInabled;
	private  	CompositeItem item;
	
	public CompositeItemProperties(SceneLoader scene) {
		super(scene, "CompositeItemProperties");
	}

	@Override
	public void setObject(CompositeItem object) {
		item = object;
		
		scissorsInabled = ui.getCheckBoxById("scissorsInabled");
		
		
		setListeners();
	}


    @Override
    public void updateView() {

    }

	private void setListeners() {
		scissorsInabled.addListener(new ClickListener() {
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);		
			}
		});		
	}

}
