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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CheckBoxItem;

public class CheckboxItemProperties extends PropertyBox implements IPropertyBox<CheckBoxItem> {

	private TextField checkBoxTxtBox;
	
	public CheckboxItemProperties(SceneLoader scene) {
		super(scene, "CheckboxItemProperties");
	}

	@Override
	public void setObject(CheckBoxItem object) {
		item = object;
		
		checkBoxTxtBox = ui.getTextBoxById("checkboxText");
		checkBoxTxtBox.setText(((CheckBoxItem)item).getDataVO().text);
		
		setListeners();
	}


    @Override
    public void updateView() {

    }

	private void setListeners() {
		checkBoxTxtBox.addListener(new ClickListener() {
			public boolean  keyUp(InputEvent event, int keycode) {
				if(keycode == 66)  {
					// set item id to 
					String text = checkBoxTxtBox.getText();
					if(item != null) {
						((CheckBoxItem)item).getDataVO().text = text;						
						((CheckBoxItem)item).renew();
					}
				}
				return true;
			}
		});
		
	}
	
}
