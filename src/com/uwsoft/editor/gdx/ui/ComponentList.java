package com.uwsoft.editor.gdx.ui;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.thumbnailbox.ComponentThumbnailBox;

public class ComponentList extends Group {

	public ComponentList(final UIStage s, float width, float height) {
		this.setWidth(width);
		this.setHeight(height);
		final Table container = new Table();
		Table table = new Table();
		Group listContainer = new Group();
		container.setX(0);
		container.setY(0);
		container.setWidth(getWidth()-1);
		container.setHeight(getHeight()-20);
		listContainer.setWidth(getWidth()-20);
		listContainer.setHeight(getHeight()-25);
		final ScrollPane scroll = new ScrollPane(table, s.textureManager.editorSkin);
		container.add(scroll).colspan(4).width(getWidth());
		container.row();
		scroll.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return true;
			}
		});
		scroll.setHeight(getHeight()-20);
		scroll.setY(0);
		scroll.setFlickScroll(false);
		
		HashMap<String, String> components = new HashMap<String, String>();
		
		components.put("Label", "Label");
		components.put("TextBoxItem", "Text Field");
		components.put("ButtonItem", "Text Button");
		components.put("CheckBox", "CheckBox");
		components.put("SelectBox", "SelectBox");
		
		Label dummyTst = new Label("dummy", s.textureManager.editorSkin);
		if(components.size()*dummyTst.getHeight() > listContainer.getHeight()) listContainer.setHeight(components.size()*dummyTst.getHeight());
		dummyTst = null;
		
		int iter = 1;
		for (final Object value : components.values()) {

            ComponentThumbnailBox thumb = new ComponentThumbnailBox(s, getWidth(), (String)value);
            thumb.setX(0);
            thumb.setY(listContainer.getHeight() - thumb.getHeight()*iter);
            listContainer.addActor(thumb);
			iter++;
		}

		table.add(listContainer);
		table.row();
		
		addActor(container);
	}
}
