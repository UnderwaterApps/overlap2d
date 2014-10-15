package com.uwsoft.editor.gdx.ui;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Group;

public class StackableGroup extends Group {

	private ArrayList<UIBox> panels = new ArrayList<UIBox>();
	
	public StackableGroup() {
		
	}
	
	public void addPanel(UIBox panel) {
		panels.add(panel);
	}
	
	public void initView() {
		float height = 0;
		clear();
		float initialY = 0;
		float gapSize = 5;
		for(int i = 0; i < panels.size(); i ++) {
			initialY-= panels.get(i).getHeight()+gapSize; 
			panels.get(i).setY(initialY);
			panels.get(i).initPanel();
			addActor(panels.get(i));
			
			height+=panels.get(i).getHeight()+gapSize;
		}
		
		setHeight(height);
	}
}
