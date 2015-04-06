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

package com.uwsoft.editor.gdx.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.uwsoft.editor.gdx.actors.basic.PixelLine;
import com.uwsoft.editor.gdx.stage.BaseStage;

import java.util.AbstractList;
import java.util.ArrayList;

public class GridView extends Group {

	 private AbstractList<PixelLine> lines = new ArrayList<PixelLine>();

	public GridView(BaseStage s) {
		float gridSize = 50;
		float gridLinesCount = 200;
		
		for(int i = 0; i <gridLinesCount; i++) {
			PixelLine tmp = new PixelLine(s.textureManager, i*gridSize, 0, i*gridSize, gridSize*gridLinesCount);
			opacityHandler(i, tmp);
			addActor(tmp);
			tmp.setX(tmp.getX()-(gridLinesCount/2-1)*gridSize);
			tmp.setY(tmp.getY()-(gridLinesCount/2-1)*gridSize);

			 lines.add(tmp);
		}
		
		for(int i = 0; i <gridLinesCount; i++) {
			PixelLine tmp = new PixelLine(s.textureManager, 0, i*gridSize, gridSize*gridLinesCount,  i*gridSize);
			opacityHandler(i, tmp);
			addActor(tmp);
			tmp.setX(tmp.getX()-(gridLinesCount/2-1)*gridSize);
			tmp.setY(tmp.getY()-(gridLinesCount/2-1)*gridSize);

			 lines.add(tmp);
		}
		
		this.setWidth(gridSize*gridLinesCount);
		this.setHeight(gridSize*gridLinesCount);
		
		Label lbl = new Label("0.0", s.textureManager.editorSkin);
		lbl.setX(-5 - lbl.getWidth());
		lbl.setY( - lbl.getHeight());
		lbl.setColor(new Color(1, 1, 1, 0.4f));
		addActor(lbl);
	}
	
	
	private void opacityHandler(float i, PixelLine tmp) {
		i-=3;
		if(i % 6 == 0) {
			tmp.setOpacity(0.1f); 
		} else if(i % 2 == 0) {
			tmp.setOpacity(0.05f); 
		} else {
			tmp.setOpacity(0.02f); 
		}
	}

	 @Override
	 public void act(float delta) {
		  super.act(delta);
		  OrthographicCamera camera = (OrthographicCamera)getStage().getCamera();
		  for(int i = 0; i <lines.size(); i++) {
				lines.get(i).setThickness(camera.zoom);
		  }
	 }
}
