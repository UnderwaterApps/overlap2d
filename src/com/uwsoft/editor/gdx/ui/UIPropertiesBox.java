package com.uwsoft.editor.gdx.ui;

import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.properties.*;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.*;

import java.util.ArrayList;

public class UIPropertiesBox extends ExpandableUIBox {

    private IBaseItem currentItem;

    private BasicItemProperties basicBox;
    private ArrayList<IPropertyBox> additionalBoxes = new ArrayList<>();

    public UIPropertiesBox(UIStage s) {
        super(s, 250, 270);

        initView();
    }

    public void initView() {
        mainLayer.clear();
        additionalBoxes.clear();

        if (currentItem == null) return;
        SceneLoader sceneLoader = stage.sceneLoader;

        basicBox = new BasicItemProperties(stage.sandboxStage, sceneLoader);
        basicBox.setObject(currentItem);
        basicBox.setX(getWidth() / 2 - basicBox.getWidth() / 2);
        basicBox.setY(getHeight() - basicBox.getHeight() - 20);
        mainLayer.addActor(basicBox);

        // Additional Boxes
        String className = currentItem.getClass().getSimpleName().toString();

        if (className.equals("LabelItem")) {
            LabelItemProperties lblBox = new LabelItemProperties(stage.sandboxStage, sceneLoader);
            lblBox.setObject((LabelItem) currentItem);
            lblBox.setX(getWidth() / 2 - basicBox.getWidth() / 2);
            lblBox.setY(basicBox.getY() - lblBox.getHeight() - 10);
            mainLayer.addActor(lblBox);
            additionalBoxes.add(lblBox);
        }
        if (className.equals("TextButtonItem")) {
            ButtonItemProperties btnBox = new ButtonItemProperties(sceneLoader);
            btnBox.setObject((TextButtonItem) currentItem);
            btnBox.setX(getWidth() / 2 - basicBox.getWidth() / 2);
            btnBox.setY(basicBox.getY() - btnBox.getHeight() - 10);
            mainLayer.addActor(btnBox);
            additionalBoxes.add(btnBox);
        }
        if (className.equals("SelectBoxItem")) {
            SelectBoxItemProperties selectBox = new SelectBoxItemProperties(sceneLoader);
            selectBox.setObject((SelectBoxItem) currentItem);
            selectBox.setX(getWidth() / 2 - basicBox.getWidth() / 2);
            selectBox.setY(basicBox.getY() - selectBox.getHeight() - 10);
            mainLayer.addActor(selectBox);
            additionalBoxes.add(selectBox);
        }
        if (className.equals("CheckBoxItem")) {
            CheckboxItemProperties checkBox = new CheckboxItemProperties(sceneLoader);
            checkBox.setObject((CheckBoxItem) currentItem);
            checkBox.setX(getWidth() / 2 - basicBox.getWidth() / 2);
            checkBox.setY(basicBox.getY() - checkBox.getHeight() - 10);
            mainLayer.addActor(checkBox);
            additionalBoxes.add(checkBox);
        }
        if (className.equals("CompositeItem")) {
            CompositeItemProperties compositekBox = new CompositeItemProperties(sceneLoader);
            compositekBox.setObject((CompositeItem) currentItem);
            compositekBox.setX(getWidth() / 2 - basicBox.getWidth() / 2);
            compositekBox.setY(basicBox.getY() - compositekBox.getHeight() - 10);
            mainLayer.addActor(compositekBox);
            additionalBoxes.add(compositekBox);
        }
        if (className.equals("ParticleItem")) {
            ParticleItemProperties particleBox = new ParticleItemProperties(stage.sandboxStage.sceneLoader);
            particleBox.setObject((ParticleItem) currentItem);
            particleBox.setX(getWidth() / 2 - basicBox.getWidth() / 2);
            particleBox.setY(basicBox.getY() - particleBox.getHeight() - 10);
            mainLayer.addActor(particleBox);
            additionalBoxes.add(particleBox);
        }
        if (className.equals("LightActor")) {
            LightItemProperties lightBox = new LightItemProperties(sceneLoader);
            lightBox.setObject((LightActor) currentItem);
            lightBox.setX(getWidth() / 2 - basicBox.getWidth() / 2);
            lightBox.setY(basicBox.getY() - lightBox.getHeight());
            mainLayer.addActor(lightBox);
            additionalBoxes.add(lightBox);
        }
        if (className.equals("SpriteAnimation")) {
            SpriteAnimationProperties spriteAnimationProperties = new SpriteAnimationProperties(stage.sandboxStage, sceneLoader);
            spriteAnimationProperties.setObject((SpriteAnimation) currentItem);
            spriteAnimationProperties.setX(getWidth() / 2 - basicBox.getWidth() / 2);
            spriteAnimationProperties.setY(basicBox.getY() - spriteAnimationProperties.getHeight() - 25);
            mainLayer.addActor(spriteAnimationProperties);
            additionalBoxes.add(spriteAnimationProperties);
        }
        if (className.equals("SpineActor")) {
            SpineAnimationProperties spineAnimationProperties = new SpineAnimationProperties(sceneLoader);
            spineAnimationProperties.setObject((SpineActor) currentItem);
            spineAnimationProperties.setX(getWidth() / 2 - basicBox.getWidth() / 2);
            spineAnimationProperties.setY(basicBox.getY() - spineAnimationProperties.getHeight() - 25);
            mainLayer.addActor(spineAnimationProperties);
            additionalBoxes.add(spineAnimationProperties);
        }
    }

    public void setItem(IBaseItem item) {
        currentItem = item;
        initView();
    }

    public void cleanContent() {
        mainLayer.clear();
        currentItem = null;
    }

    public void updateState() {
        basicBox.updateView();
        for (int i = 0; i < additionalBoxes.size(); i++) {
            additionalBoxes.get(i).updateView();
        }
    }

    @Override
    protected void expand() {
        setHeight(expandedHeight);
        if (mainLayer != null) {
            mainLayer.setVisible(true);
        }
    }

    @Override
    protected void collapse() {
        setHeight(topImg.getHeight());
        if (mainLayer != null) {
            mainLayer.setVisible(false);
        }
    }

    public void showPhysicsParams() {
        initView();
        PhysicsPropertiesBox physicsPropertiesBox = new PhysicsPropertiesBox(stage);
//        physicsPropertiesBox.setObject(currentItem);
        physicsPropertiesBox.setX(getWidth() / 2 - physicsPropertiesBox.getWidth() / 2);
        physicsPropertiesBox.setY(getHeight() - physicsPropertiesBox.getHeight() - 20);
        mainLayer.addActor(physicsPropertiesBox);
    }
}
