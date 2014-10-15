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
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.actor.TextButtonItem;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;

/**
 * Created by sargis on 8/25/14.
 */
public class SpriteAnimationProperties extends PropertyBox implements IPropertyBox<SpriteAnimation> {
    private final SandboxStage sandboxStage;
    private TextBoxItem fpsLabel;
    private TextButtonItem addAnimationButton;
    private SelectBoxItem<String> animationSelectbox;

    public SpriteAnimationProperties(SandboxStage sandboxStage, SceneLoader sceneLoader) {
        super(sceneLoader, "spriteAnimationProperties");
        this.sandboxStage = sandboxStage;
    }

    @Override
    public void setObject(SpriteAnimation object) {
        item = object;

        fpsLabel = ui.getTextBoxById("fpsLabel");
        animationSelectbox = ui.getSelectBoxById("animationSelectbox");
        animationSelectbox.setWidth(90);
        Array<String> animations = new Array<>();
        animations.add("");
        for (String name : object.getAnimations().keySet()) {
            animations.add(name);
        }
        animationSelectbox.setItems(animations);
        animationSelectbox.setSelected(object.getCurrentAnimationName());
        addAnimationButton = ui.getTextButtonById("addAnimationButton");
        addAnimationButton.setText("Add");
        fpsLabel.setText(String.valueOf(((SpriteAnimationVO) item.getDataVO()).fps));
        setListeners();
    }


    @Override
    public void updateView() {
        Array<String> animations = new Array<>();

        animations.add("");
        for (String name : ((SpriteAnimation)item).getAnimations().keySet()) {
            animations.add(name);
        }
        animationSelectbox.setItems(animations);
        animationSelectbox.setSelected(((SpriteAnimation)item).getCurrentAnimationName());
    }

    private void setListeners() {

        animationSelectbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((SpriteAnimation) item).setAnimation(animationSelectbox.getSelected());
            }
        });

        fpsLabel.addListener(new ClickListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    // set item id to
                    String text = fpsLabel.getText();
                    if (item != null) {
                        ((SpriteAnimationVO) item.getDataVO()).fps = Integer.parseInt(text);
                        item.renew();
                    }
                }
                return true;
            }
        });


        addAnimationButton.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                EditAnimationDialog dlg = new EditAnimationDialog(sandboxStage.uiStage, (SpriteAnimation) item);
                sandboxStage.uiStage.addActor(dlg);
            }
        });

    }
}

