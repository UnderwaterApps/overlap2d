package com.uwsoft.editor.gdx.mediators;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.renderer.actor.*;
import com.uwsoft.editor.renderer.data.*;

import java.io.File;
import java.util.HashMap;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class ItemControlMediator {

    private SceneControlMediator sceneControl;

    public ItemControlMediator(SceneControlMediator sceneControl) {
        this.sceneControl = sceneControl;
    }


    private void itemZIndexChange( HashMap<IBaseItem, SelectionRectangle> currentSelection, boolean isUp) {
        for (SelectionRectangle value : currentSelection.values()) {
            sceneControl.getCurrentScene().updateDataVO();

            int ammount = 1;
            if (!isUp) ammount = -1;

            int setting = value.getHostAsActor().getZIndex() + ammount;
            if (setting < 0) setting = 0;
            Group parent = value.getHostAsActor().getParent();
            parent.swapActor(value.getHostAsActor().getZIndex(), setting);

            sceneControl.getCurrentScene().updateDataVO();
        }
    }

    public void moveItemBy(Actor actor, float x, float y) {
        actor.setX(actor.getX() + x);
        actor.setY(actor.getY() + y);
    }

    public void removeItem(Actor actor) {
        IBaseItem item = (IBaseItem) actor;
        actor.remove();
        sceneControl.getCurrentScene().removeItem(item);
        item.dispose();
    }

    public void createParticleItem(String pName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();
        ParticleEffectVO vo = new ParticleEffectVO();
        vo.particleName = pName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;
        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        ParticleItem itm = new ParticleItem(vo, essentials, currentScene);
        currentScene.addItem(itm);
        itm.forceContinuous();
        itm.start();
        initItemListeners(itm);
        updateSceneTree();
        itm.setX(vo.x);
        itm.setY(vo.y);
    }

    public void createItemFromLibrary(String libraryName, float x, float y) {
        currentScene.updateDataVO();
        CompositeItemVO vo = currentSceneVo.libraryItems.get(libraryName).clone();

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;
        vo.itemName = libraryName;
        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        CompositeItem itm = new CompositeItem(vo, sceneLoader.essentials, currentScene);

        currentScene.addItem(itm);
        initItemListeners(itm);
        saveSceneCurrentSceneData();
        updateSceneTree();
        itm.setX(vo.x);
        itm.setY(vo.y);
    }

    public void createSpriteAnimation(String animName, float x, float y) {
        currentScene.updateDataVO();
        SpriteAnimationVO vo = new SpriteAnimationVO();
        vo.animationName = animName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        SpriteAnimation anim = new SpriteAnimation(vo, sceneLoader.essentials, currentScene);
        //anim.setAnimation(anim.skeletonData.getAnimations().get(0).getName());

        currentScene.addItem(anim);
        initItemListeners(anim);
        updateSceneTree();
        anim.setX(vo.x);
        anim.setY(vo.y);
        anim.start();
    }

    public void createSpineAnimation(String animName, float x, float y) {
        currentScene.updateDataVO();
        SpineVO vo = new SpineVO();
        vo.animationName = animName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        SpineActor anim = new SpineActor(vo, sceneLoader.essentials, currentScene);
        anim.setAnimation(anim.skeletonData.getAnimations().get(0).getName());

        currentScene.addItem(anim);
        initItemListeners(anim);
        updateSceneTree();
        anim.setX(vo.x);
        anim.setY(vo.y);
    }
    public void createSpriterAnimation(String animationsName, float x, float y) {
        currentScene.updateDataVO();
        SpriterVO vo = new SpriterVO();
        vo.animationName = animationsName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        SpriterActor anim = new SpriterActor(vo, sceneLoader.essentials, currentScene);

        currentScene.addItem(anim);
        initItemListeners(anim);
        updateSceneTree();
        anim.setX(vo.x);
        anim.setY(vo.y);

    }
    public void createLight(LightVO vo) {
        currentScene.updateDataVO();
        vo.x += getCamera().position.x - getWidth() / 2;
        vo.y += getCamera().position.y - getHeight() / 2;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        LightActor light = new LightActor(vo, sceneLoader.essentials, currentScene);
        currentScene.addItem(light);
        initItemListeners(light);
        saveSceneCurrentSceneData();
        updateSceneTree();
        light.setX(vo.x);
        light.setY(vo.y);

    }

    public void createItem(String regionName, float x, float y) {
        currentScene.updateDataVO();

        SimpleImageVO vo = new SimpleImageVO();
        vo.imageName = regionName;

        if (dataManager.getCurrentProjectInfoVO().assetMeshMap.containsKey(regionName)) {
            vo.meshId = dataManager.getCurrentProjectInfoVO().assetMeshMap.get(regionName);
            PhysicsBodyDataVO data = dataManager.getCurrentProjectInfoVO().meshes.get(vo.meshId).initialProperties;
            if(data != null){
                vo.physicsBodyData = new PhysicsBodyDataVO(data);
            }else{
                vo.physicsBodyData = new PhysicsBodyDataVO();
            }
        }

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        ImageItem itm = new ImageItem(vo, essentials, currentScene);

        currentScene.addItem(itm);
        initItemListeners(itm);
        saveSceneCurrentSceneData();
        updateSceneTree();
        itm.setX(vo.x);
        itm.setY(vo.y);
    }

    public void create9patchItem(String regionName, float x, float y) {
        currentScene.updateDataVO();

        Image9patchVO vo = new Image9patchVO();
        vo.imageName = regionName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        Image9patchItem itm = new Image9patchItem(vo, essentials, currentScene);

        currentScene.addItem(itm);
        initItemListeners(itm);
        saveSceneCurrentSceneData();
        updateSceneTree();
        itm.setX(vo.x);
        itm.setY(vo.y);
    }

    public void createComponent(String type, float x, float y) {
        currentScene.updateDataVO();
        MainItemVO vo = new MainItemVO();

        if (checkForNoLayersSelectedDialog()) return;
        if (!type.equals("Label") && checkForNoSkinDialog(type, x, y)) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;
        IBaseItem itm = null;
        if (type.equals("Text Field")) {
            vo = new TextBoxVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            itm = new TextBoxItem((TextBoxVO) vo, essentials, currentScene);
            ((TextBoxItem) itm).setDisabled(true);
        }
        if (type.equals("Text Button")) {
            vo = new ButtonVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            itm = new TextButtonItem((ButtonVO) vo, essentials, currentScene);
            ((TextButtonItem) itm).setDisabled(true);
        }
        if (type.equals("Label")) {
            if (checkForNoLabelStyleDialog(type, x, y)) return;
            vo = new LabelVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            File folder = new File(DataManager.getInstance().getFreeTypeFontPath());
            String fontName = folder.listFiles()[0].getName();
            fontName = fontName.substring(0, fontName.lastIndexOf('.'));
            ((LabelVO) vo).style = fontName;
            itm = new LabelItem((LabelVO) vo, essentials, currentScene);
        }
        if (type.equals("CheckBox")) {
            vo = new CheckBoxVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            itm = new CheckBoxItem((CheckBoxVO) vo, essentials, currentScene);
        }
        if (type.equals("SelectBox")) {
            vo = new SelectBoxVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            itm = new SelectBoxItem((SelectBoxVO) vo, essentials, currentScene);
        }

        currentScene.addItem(itm);
        initItemListeners(itm);
        saveSceneCurrentSceneData();
        updateSceneTree();
        ((Actor) itm).setX(vo.x);
        ((Actor) itm).setY(vo.y);
    }
}
