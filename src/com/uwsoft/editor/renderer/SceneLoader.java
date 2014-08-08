package com.uwsoft.editor.renderer;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.script.IScript;

public class SceneLoader {
	
	private String curResolution = "orig";
	
	private SceneVO sceneVO;
	
	public CompositeItem sceneActor;
	public Essentials essentials;

    public SceneLoader() {
    }

	public SceneLoader(Essentials e) {
		this.essentials = e;
	}
	
//	public void setRayHandler(RayHandler rayHandler){
//		this.rayHandler = rayHandler;
//	}
	
	public void setResolution(String resolutionName) {
		curResolution = resolutionName;
		if(sceneActor != null){
			sceneActor.applyResolution(resolutionName);
		}
	}
	
	public SceneVO getSceneVO() {
		return sceneVO;
	}
	
	public SceneVO loadScene(FileHandle file) {	
		return loadScene(file.readString());
	}
	
	public SceneVO loadScene(String jsonString) {		
		Json json 	= new Json();
		sceneVO 	= json.fromJson(SceneVO.class, jsonString);
        invalidateSceneVO(sceneVO);
		sceneActor 	= getSceneAsActor();
		setAmbienceInfo(sceneVO);		
		return sceneVO;
	}

    public void invalidateSceneVO(SceneVO vo) {
        removeMissingImages(vo.composite);
    }

    public void removeMissingImages(CompositeVO vo) {
        if(vo == null) return;
        for(SimpleImageVO img: vo.sImages) {
            if(essentials.rm.getAtlas().findRegion(img.imageName) == null) {
                vo.sImages.remove(img);
            }
        }
        for(CompositeItemVO cmp: vo.sComposites) {
            removeMissingImages(cmp.composite);
        }
    }

	public void setAmbienceInfo(SceneVO vo) {
		if(essentials.rayHandler != null && vo.ambientColor !=null){
			Color clr = new Color(vo.ambientColor[0], vo.ambientColor[1], vo.ambientColor[2], vo.ambientColor[3]);
			essentials.rayHandler.setAmbientLight(clr);
		}
	}
	
	public CompositeItem getSceneAsActor() {
		CompositeItemVO vo = new CompositeItemVO(sceneVO.composite);
		
		if(vo.composite == null) vo.composite = new CompositeVO();
		CompositeItem cnt = new CompositeItem(vo, essentials);

		return cnt;
	}
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
	
	public CompositeItem getCompositeElementById(String id) {
		CompositeItem cnt = getCompositeElement(sceneActor.getCompositeById(id).getDataVO());
		
		return cnt;
	}
	
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


    public IResource getRm() {
        return essentials.rm;
    }
}
