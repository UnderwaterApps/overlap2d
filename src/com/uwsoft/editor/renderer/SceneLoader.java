package com.uwsoft.editor.renderer;

import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.script.IScript;


/**
 * SceneLoader is importatn part of runtime that
 * utilizes provided IResourceRetriever (or creates default one shipped with runtime)
 * in order to load entire scene data into viewable actors
 * provides the functionality to get root actor of scene and load scenes.
 *
 * Created by azakhary on 9/9/2014.
 */
public class SceneLoader {
	
	private String curResolution = "orig";
	
	private SceneVO sceneVO;
	
	public CompositeItem sceneActor;
	public Essentials essentials;


    /**
     * Empty constructor is intended for easy use,
     * it will create default ResourceManager, and load
     * all possible resources into memory that have been exported with editor
     */
    public SceneLoader() {
        ResourceManager rm = new ResourceManager();
        rm.initAllResources();
        Essentials emptyEssentuials = new Essentials();
        emptyEssentuials.rm = rm;
        essentials = emptyEssentuials;
    }

    /**
     * intended for easy use,
     * it will create default ResourceManager, and load
     * all possible resources into memory that have been exported with editor
     * @param resolution - String resolution name to load everything for 9default is "orig"
     */
    public SceneLoader(String resolution) {
        ResourceManager rm = new ResourceManager();
        rm.setWorkingResolution(resolution);
        rm.initAllResources();
        Essentials emptyEssentuials = new Essentials();
        emptyEssentuials.rm = rm;
        essentials = emptyEssentuials;
        curResolution = resolution;
    }

    /**
     * Sets your implementation of IResourceRetriever, and does not load anything
     *
     * @param rm - Implementation of IResourceRetriever
     */
    public SceneLoader(IResourceRetriever rm) {
        Essentials emptyEssentuials = new Essentials();
        emptyEssentuials.rm = rm;
        essentials = emptyEssentuials;
    }

    /**
     * Sets essentials container with or without content for later use
     * This the most dummy contructor
     * @param e - Essentials container
     */
	public SceneLoader(Essentials e) {
		this.essentials = e;
	}

    /**
     * Sets resolution of the screen, and applies it to existing actors if already loaded
     * @param resolutionName - String resolution name to load everything for 9default is "orig"
     */
	public void setResolution(String resolutionName) {
		curResolution = resolutionName;
		if(sceneActor != null){
			sceneActor.applyResolution(resolutionName);
		}
	}

    /**
     *
     * @return SceneVO data if scene is already loaded with loadScene
     */
	public SceneVO getSceneVO() {
		return sceneVO;
	}

    /**
     * Asks IResourceRetriever for sceneVO data, checks scene for errors
     * Applies resolution set previously with setResolution method or uses "orig" default resolution
     * Sets default ambient light using scene data
     *
     * @param sceneName - String scene name without ".dt" extension
     * @param createActors - if true the root composite with entire actor list will be created
     * @return SceneVO data file of loaded scene (you don't really need it at this point though...)
     */
    public SceneVO loadScene(String sceneName, boolean createActors) {
        sceneVO 	= essentials.rm.getSceneVO(sceneName);

        // init physics world
        PhysicsPropertiesVO physicsProperties = sceneVO.physicsPropertiesVO;
        //if(sceneVO.physicsPropertiesVO != null && sceneVO.physicsPropertiesVO.enabled == true)

        if(sceneVO.physicsPropertiesVO != null && sceneVO.physicsPropertiesVO.enabled == true) {
            essentials.world = new World(new Vector2(physicsProperties.gravityX, physicsProperties.gravityY), true);
            essentials.rayHandler.setWorld(essentials.world);
        }

        invalidateSceneVO(sceneVO);

        if(createActors) {
            sceneActor = getSceneAsActor();
            if (!curResolution.equals("orig")) sceneActor.applyResolution(curResolution);
        }

        setAmbienceInfo(sceneVO);
        return sceneVO;
    }


    /**
     * Asks IResourceRetriever for sceneVO data, checks scene for errors
     * and Recreates a big Actor tree that you can later add to your stage for rendering
     * Applies resolution set previously with setResolution method or uses "orig" default resolution
     * Sets default ambient light using scene data
     *
     * @param sceneName - String scene name without ".dt" extension
     * @return SceneVO data file of loaded scene (you don't really need it at this point though...)
     */
	public SceneVO loadScene(String sceneName) {
        return loadScene(sceneName, true);
	}

    /**
     * Checks scene for continuity errors
     *
     * @param vo - Scene data file to invalidate
     */
    public void invalidateSceneVO(SceneVO vo) {
        removeMissingImages(vo.composite);
    }

    /**
     * Checks if composite data contains any scene items with images not provided by resource retriever, and removes them from composite
     * to at least show what is not missing
     *
     * @param vo - Scene data file to invalidate
     */
    public void removeMissingImages(CompositeVO vo) {
        if(vo == null) return;
        for(SimpleImageVO img: vo.sImages) {
            if(essentials.rm.getTextureRegion(img.imageName) == null) {
                vo.sImages.remove(img);
            }
        }
        for(CompositeItemVO cmp: vo.sComposites) {
            removeMissingImages(cmp.composite);
        }
    }

    /**
     * Sets ambient light to the one specified in scene from editor
     *
     * @param vo - Scene data file to invalidate
     */
	public void setAmbienceInfo(SceneVO vo) {
		if(essentials.rayHandler != null && vo.ambientColor !=null){
			Color clr = new Color(vo.ambientColor[0], vo.ambientColor[1], vo.ambientColor[2], vo.ambientColor[3]);
			essentials.rayHandler.setAmbientLight(clr);
		}
	}

    /**
     * Creates CompositeItem from sceneVo
     *      *
     * @return CompositeItem
     */
	public CompositeItem getSceneAsActor() {
		CompositeItemVO vo = new CompositeItemVO(sceneVO.composite);
		
		if(vo.composite == null) vo.composite = new CompositeVO();
		CompositeItem cnt = new CompositeItem(vo, essentials);

		return cnt;
	}

    /**
     * Loads CompositeItem from Library by using it's library name
     * So you can get item that is not on the scene, but is stored in library for later use
     * Works great for dialogs and thigns like that
     *
     * TODO: this should be also renamed as name is confusing
     * @param name String - library item name
     * @return CompositeItem Actor
     */
	public CompositeItem getLibraryAsActor(String name) {
		CompositeItemVO vo = new CompositeItemVO(sceneVO.libraryItems.get(name));
		if(vo.composite == null) vo.composite = new CompositeVO();
		CompositeItem cnt = new CompositeItem(vo, essentials);
		cnt.dataVO.itemName = name;
		cnt.applyResolution(curResolution);
		cnt.setX(0);
		cnt.setY(0);
		return cnt;
	}

    /**
     * Returns CompositeItem that is inside rootScene identified by unique id set in Editor
     * does not perform deep search inside other composites
     *
     * @param id - String uniqe identifier
     * @return - CompositeItem
     */
	public CompositeItem getCompositeElementById(String id) {
		CompositeItem cnt = getCompositeElement(sceneActor.getCompositeById(id).getDataVO());
		
		return cnt;
	}

    /**
     * Creates CompositeItem by provided CompositeItemVO data class
     *
     * @param vo CompositeItemVO data class
     * @return CompositeItem
     */
	public CompositeItem getCompositeElement(CompositeItemVO vo) {
		CompositeItem cnt = new CompositeItem(vo, essentials);
		return cnt;
	}

	public void addScriptTo(String name,IScript iScript){
		sceneActor.addScriptTo(name, iScript);
	}
	
	public void addScriptTo(String name,ArrayList<IScript> iScripts){
		sceneActor.addScriptTo(name, iScripts);
	}

    /**
     *
     * @return IResourceRetriever instance to load any resources already in memory
     */
    public IResourceRetriever getRm() {
        return essentials.rm;
    }

    /**
     *
     * @return CompositeItem - root element of the scene
     */
    public CompositeItem getRoot() {
        return sceneActor;
    }
}
