package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.mediators.ItemControlMediator;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.renderer.actor.*;
import com.uwsoft.editor.renderer.data.*;

import java.io.File;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class ItemFactory {

    private Sandbox sandbox;
    private SandboxStage sandboxStage;
    private SceneControlMediator sceneControl;
    private ItemControlMediator itemControl;

    public ItemFactory(Sandbox sandbox) {
        this.sandbox = sandbox;
        sandboxStage = sandbox.getSandboxStage();
        sceneControl = sandbox.getSceneControl();
    }

    private void prepareVO(MainItemVO vo, String layerName, float x, float y) {
        vo.layerName = layerName;
        vo.x = x + sandboxStage.getCamera().position.x - sandboxStage.getWidth() / 2;
        vo.y = y + sandboxStage.getCamera().position.y - sandboxStage.getHeight() / 2;
    }

    private void addItem(IBaseItem item, MainItemVO vo) {
        Actor itemActor = (Actor) item;
        sceneControl.getCurrentScene().addItem(item);
        sandbox.getInputHandler().initItemListeners(item);
        sandbox.saveSceneCurrentSceneData();
        sandbox.getUIStage().getItemsBox().initContent();
        itemActor.setX(vo.x);
        itemActor.setY(vo.y);
    }

    private void initSpritePhysicsData(SimpleImageVO vo) {
        String regionName = vo.imageName;
        if (DataManager.getInstance().getCurrentProjectInfoVO().assetMeshMap.containsKey(regionName)) {
            vo.meshId = DataManager.getInstance().getCurrentProjectInfoVO().assetMeshMap.get(regionName);
            PhysicsBodyDataVO data = DataManager.getInstance().getCurrentProjectInfoVO().meshes.get(vo.meshId).initialProperties;
            if(data != null){
                vo.physicsBodyData = new PhysicsBodyDataVO(data);
            }else{
                vo.physicsBodyData = new PhysicsBodyDataVO();
            }
        }
    }

    public void createImageItem(LayerItemVO layer, String regionName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        SimpleImageVO vo = new SimpleImageVO();
        vo.imageName = regionName;

        initSpritePhysicsData(vo);
        prepareVO(vo, layer.layerName, x, y);

        ImageItem itm = new ImageItem(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);
    }

    public void createParticleItem(LayerItemVO layer, String particleName, String layerName, float x, float y) {
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

        addItem(itm);
    }

    public void createItemFromLibrary(LayerItemVO layer, String libraryName, float x, float y) {
        currentScene.updateDataVO();
        CompositeItemVO vo = currentSceneVo.libraryItems.get(libraryName).clone();

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;
        vo.itemName = libraryName;
        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        CompositeItem itm = new CompositeItem(vo, sceneLoader.essentials, currentScene);

        addItem(itm);
    }

    public void createSpriteAnimation(LayerItemVO layer, String animName, float x, float y) {
        currentScene.updateDataVO();
        SpriteAnimationVO vo = new SpriteAnimationVO();
        vo.animationName = animName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        SpriteAnimation anim = new SpriteAnimation(vo, sceneLoader.essentials, currentScene);
        //anim.setAnimation(anim.skeletonData.getAnimations().get(0).getName());

        addItem(itm);
    }

    public void createSpineAnimation(LayerItemVO layer, String animName, float x, float y) {
        currentScene.updateDataVO();
        SpineVO vo = new SpineVO();
        vo.animationName = animName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        SpineActor anim = new SpineActor(vo, sceneLoader.essentials, currentScene);
        anim.setAnimation(anim.skeletonData.getAnimations().get(0).getName());

        addItem(itm);
    }
    public void createSpriterAnimation(LayerItemVO layer, String animationsName, float x, float y) {
        currentScene.updateDataVO();
        SpriterVO vo = new SpriterVO();
        vo.animationName = animationsName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        SpriterActor anim = new SpriterActor(vo, sceneLoader.essentials, currentScene);

        addItem(itm);

    }
    public void createLight(LayerItemVO layer, LightVO vo) {
        currentScene.updateDataVO();
        vo.x += getCamera().position.x - getWidth() / 2;
        vo.y += getCamera().position.y - getHeight() / 2;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        LightActor light = new LightActor(vo, sceneLoader.essentials, currentScene);

        addItem(itm);

    }

    public void create9patchItem(LayerItemVO layer, String regionName, float x, float y) {
        currentScene.updateDataVO();

        Image9patchVO vo = new Image9patchVO();
        vo.imageName = regionName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        Image9patchItem itm = new Image9patchItem(vo, essentials, currentScene);

        addItem(itm);
    }

    public void createComponent(LayerItemVO layer, String type, float x, float y) {
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

        addItem(itm);
    }
}
