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

import com.badlogic.gdx.scenes.scene2d.Group;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;

public class PropertyBox extends Group {

	protected IBaseItem item = null;
	private SceneLoader sceneLoader;
	protected CompositeItem ui;
	private String elemId;
	
	public PropertyBox(SceneLoader sceneLoader, String id) {
		this.sceneLoader = sceneLoader;
		elemId = id;
		initView();
	}
	
	public void initView() {
		clear();
		ui =  sceneLoader.getCompositeElementById(elemId);	
		ui.setX(0);
		ui.setY(0);		
		setWidth(ui.getWidth());
		setHeight(ui.getHeight());
		addActor(ui);
	}
	
	double roundTwoDecimals(double d) {
		//DecimalFormat twoDForm = new DecimalFormat("#.##");
		//return Double.valueOf(twoDForm.format(d));
		return Math.round(d * 100.0) / 100.0;
	}
}
