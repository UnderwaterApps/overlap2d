package com.uwsoft.editor.gdx.ui.properties;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.gdx.ui.dialogs.EditAnimationDialog;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.SelectBoxItem;
import com.uwsoft.editor.renderer.actor.SpriteAnimation;
import com.uwsoft.editor.renderer.actor.SpriterActor;
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.actor.TextButtonItem;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.data.SpriterVO;

/**
 * Created by hayk on 12/22/14.
 */
public class SpriterAnimationProperties extends PropertyBox implements IPropertyBox<SpriterActor> {
    private final SandboxStage sandboxStage;
    private TextBoxItem scaleLabel;
    private SelectBoxItem<String> animationSelectbox;
    private SelectBoxItem<String> entitySelectbox;

    public SpriterAnimationProperties(SandboxStage sandboxStage, SceneLoader sceneLoader) {
        super(sceneLoader, "spriterAnimationProperties");
        this.sandboxStage = sandboxStage;
    }

    @Override
    public void setObject(SpriterActor object) {
        item = object;

        scaleLabel 			= ui.getTextBoxById("scaleLabel");
        animationSelectbox 	= ui.getSelectBoxById("animationSelectbox");
        entitySelectbox 	= ui.getSelectBoxById("entitySelectbox");
        animationSelectbox.setWidth(90);
        entitySelectbox.setWidth(90);
        
        Array<String> entities = new Array<>();
        for (int i = 0; i < object.getEntities().size(); i++) {
        	entities.add(object.getEntities().get(i));
        }
        entitySelectbox.setItems(entities);
        entitySelectbox.setSelectedIndex(object.getCurrentEntityIndex());
        Array<String> animations = new Array<>();
        for (int i = 0; i < object.getAnimations().size(); i++) {
            animations.add(object.getAnimations().get(i));
        }
        animationSelectbox.setItems(animations);
        animationSelectbox.setSelectedIndex(object.getCurrentAnimationIndex());
       

        scaleLabel.setText(String.valueOf(((SpriterVO) item.getDataVO()).scale));
        setListeners();

    }

    private void setListeners() {

    	 entitySelectbox.addListener(new ChangeListener() {
         	
         	@Override
         	public void changed(ChangeEvent event, Actor actor) {
         		((SpriterActor) item).setEntity(entitySelectbox.getSelectedIndex());
         		 Array<String> animations = new Array<>();
                 for (int i = 0; i <  ((SpriterActor) item).getAnimations().size(); i++) {
                     animations.add(((SpriterActor) item).getAnimations().get(i));
                 }
                 animationSelectbox.setItems(animations);
                 animationSelectbox.setSelectedIndex(0);                 
         		 item.renew();
         	}
         });
    	 
        animationSelectbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((SpriterActor) item).setAnimation(animationSelectbox.getSelectedIndex());                               
                item.renew();
            }
        });
       

        scaleLabel.addListener(new ClickListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    // set item id to
                    String text = scaleLabel.getText();
                    if (item != null) {
                        ((SpriterVO) item.getDataVO()).scale = Float.parseFloat(text);
                        item.renew();
                    }
                }
                return true;
            }
        });
    }

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		
	}
}

