package com.uwsoft.editor.gdx.ui.properties;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.TextButtonItem;

public class ButtonItemProperties extends PropertyBox implements IPropertyBox<TextButtonItem>  {

	private TextField lblTxtBox;
	private TextButtonItem item;
	
	public ButtonItemProperties(SceneLoader scene) {
		super(scene, "ButtonItemProperties");
	}

	@Override
	public void setObject(TextButtonItem object) {
		item = object;
		
		lblTxtBox = ui.getTextBoxById("buttonText");
		lblTxtBox.setText(item.getDataVO().text);
		
		setListeners();
	}


    @Override
    public void updateView() {

    }

	private void setListeners() {
		lblTxtBox.addListener(new ClickListener() {
			public boolean  keyUp(InputEvent event, int keycode) {
				if(keycode == 66)  {
					// set item id to 
					String text = lblTxtBox.getText();
					if(item != null) {
						(item).getDataVO().text = text;
						item.renew();
					}
				}
				return true;
			}
		});
	}

}
