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
        sandbox.getSandboxInputAdapter().initItemListeners(item);
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

    public void create9patchItem(LayerItemVO layer, String regionName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        Image9patchVO vo = new Image9patchVO();
        vo.imageName = regionName;

        prepareVO(vo, layer.layerName, x, y);

        Image9patchItem itm = new Image9patchItem(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);
    }

    public void createParticleItem(LayerItemVO layer, String particleName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        ParticleEffectVO vo = new ParticleEffectVO();
        vo.particleName = particleName;

        prepareVO(vo, layer.layerName, x, y);

        ParticleItem itm = new ParticleItem(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);

        itm.forceContinuous();
        itm.start();
    }

    public void createSpriteAnimation(LayerItemVO layer, String animName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        SpriteAnimationVO vo = new SpriteAnimationVO();
        vo.animationName = animName;

        prepareVO(vo, layer.layerName, x, y);

        SpriteAnimation itm = new SpriteAnimation(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);
    }

    public void createSpriterAnimation(LayerItemVO layer, String animationsName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        SpriterVO vo = new SpriterVO();
        vo.animationName = animationsName;

        prepareVO(vo, layer.layerName, x, y);

        SpriterActor itm = new SpriterActor(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);

    }

    public void createSpineAnimation(LayerItemVO layer, String animName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        SpineVO vo = new SpineVO();
        vo.animationName = animName;

        prepareVO(vo, layer.layerName, x, y);

        SpineActor itm = new SpineActor(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
        itm.setAnimation(itm.skeletonData.getAnimations().get(0).getName());

        addItem(itm, vo);
    }

    public void createLight(LayerItemVO layer, LightVO vo, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        prepareVO(vo, layer.layerName, x, y);

        LightActor itm = new LightActor(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);

    }

    public void createItemFromLibrary(LayerItemVO layer, String libraryName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        CompositeItemVO vo = sceneControl.getCurrentSceneVO().libraryItems.get(libraryName).clone();

        prepareVO(vo, layer.layerName, x, y);
        vo.itemName = libraryName;

        CompositeItem itm = new CompositeItem(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);
    }


    public void createComponent(LayerItemVO layer, String type, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        MainItemVO vo = new MainItemVO();

        prepareVO(vo, layer.layerName, x, y);

        IBaseItem itm = null;
        if (type.equals("Text Field")) {
            vo = new TextBoxVO();
            itm = new TextBoxItem((TextBoxVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
            ((TextBoxItem) itm).setDisabled(true);
        }
        if (type.equals("Text Button")) {
            vo = new ButtonVO();
            itm = new TextButtonItem((ButtonVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
            ((TextButtonItem) itm).setDisabled(true);
        }
        if (type.equals("Label")) {
            vo = new LabelVO();
            File folder = new File(DataManager.getInstance().getFreeTypeFontPath());
            String fontName = folder.listFiles()[0].getName();
            fontName = fontName.substring(0, fontName.lastIndexOf('.'));
            ((LabelVO) vo).style = fontName;
            itm = new LabelItem((LabelVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
        }
        if (type.equals("CheckBox")) {
            vo = new CheckBoxVO();
            itm = new CheckBoxItem((CheckBoxVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
        }
        if (type.equals("SelectBox")) {
            vo = new SelectBoxVO();
            itm = new SelectBoxItem((SelectBoxVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
        }

        addItem(itm, vo);
    }
}
