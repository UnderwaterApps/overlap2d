package com.uwsoft.editor.gdx.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.uwsoft.editor.gdx.actors.basic.PixelLine;
import com.uwsoft.editor.gdx.stage.BaseStage;

public class GridView extends Group {

	public GridView(BaseStage s) {
		float gridSize = 50;
		float gridLinesCount = 200;
		
		for(int i = 0; i <gridLinesCount; i++) {
			PixelLine tmp = new PixelLine(s.textureManager, i*gridSize, 0, i*gridSize, gridSize*gridLinesCount);
			opacityHandler(i, tmp);
			addActor(tmp);
			tmp.setX(tmp.getX()-(gridLinesCount/2-1)*gridSize);
			tmp.setY(tmp.getY()-(gridLinesCount/2-1)*gridSize);
		}
		
		for(int i = 0; i <gridLinesCount; i++) {
			PixelLine tmp = new PixelLine(s.textureManager, 0, i*gridSize, gridSize*gridLinesCount,  i*gridSize);
			opacityHandler(i, tmp);
			addActor(tmp);
			tmp.setX(tmp.getX()-(gridLinesCount/2-1)*gridSize);
			tmp.setY(tmp.getY()-(gridLinesCount/2-1)*gridSize);
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
}