package com.uwsoft.editor.gdx.mediators;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.SceneVO;

/**
 * Mediates scene communication between editor and current runtime
 *
 * @author azakhary
 */
public class SceneControlMediator {

    /** main holder of the scene */
    private SceneLoader sceneLoader;

    /** runtime essentials */
    private Essentials essentials;

    /** current scene data */
    private SceneVO currentSceneVo;

    /** data object of the root element of the scene */
    private CompositeItemVO rootSceneVO;

    /** current scene rendering item */
    private CompositeItem currentScene;

    public SceneControlMediator(SceneLoader sceneLoader, Essentials essentials) {
        this.sceneLoader = sceneLoader;
        this.essentials = essentials;
    }

    public void initScene(String sceneName) {
        if (essentials.world != null) {
            if (currentScene != null) currentScene.dispose();
        }

        essentials.physicsStopped = true;
        sceneLoader = new SceneLoader(essentials);
        sceneLoader.setResolution(DataManager.getInstance().curResolution);

        currentSceneVo = sceneLoader.loadScene(sceneName, false);
        essentials.world = new World(new Vector2(currentSceneVo.physicsPropertiesVO.gravityX, currentSceneVo.physicsPropertiesVO.gravityY), true);
        essentials.rayHandler.setWorld(essentials.world);

        rootSceneVO = new CompositeItemVO(currentSceneVo.composite);
    }

    public void initSceneView(CompositeItem composite) {
        disableLights(false);
        //essentials.rayHandler.removeAll();

        if (currentScene != null) currentScene.dispose();

        CompositeItemVO itemVo = new CompositeItemVO();
        itemVo.composite = compositeItemVO.composite;
        itemVo.itemIdentifier = compositeItemVO.itemIdentifier;
        itemVo.itemName = compositeItemVO.itemName;
        CompositeItem composite = new CompositeItem(itemVo, sceneLoader.essentials);
        initSceneView(composite);
    }

    public Essentials getEssentials() {
        return essentials;
    }

    public CompositeItemVO getRootSceneVO() {
        return rootSceneVO;
    }

    public CompositeItem getCurrentScene() {
        return currentScene;
    }

    public SceneVO getCurrentSceneVO() {
        return currentSceneVo;
    }

}
