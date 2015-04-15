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

package com.uwsoft.editor.gdx.ui.dialogs;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.renderer.actor.CompositeItem;

public class CompositeDialog extends SimpleDialog {

	protected CompositeItem ui;
	
	public CompositeDialog(UIStage s, String compositeId, float width, float height) {
		super(s, width, height);

		ui = s.sceneLoader.getCompositeElementById(compositeId);
		ui.setX(0);
		ui.setY(0);
		mainLayer.addActor(ui);
		
		setX(s.getWidth()/2 - getWidth()/2);
		setY(s.getHeight()/2 - getHeight()/2+100);
		
		setVisible(false);
	}
	
	public void show() {
		clearActions();
		setVisible(true);
		setScale(0);
		setOrigin(getWidth()/2, getHeight()/2);
		Action scaleIn = Actions.scaleTo(1, 1, 0.3f, Interpolation.swingOut);
		addAction(scaleIn);
	}

}
