package com.uwsoft.editor.data.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.FontSizePair;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sargis on 3/23/15.
 */
public class SceneDataManager {
    private final DataManager dataManager;

    public SceneDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public SceneVO createNewScene(String name) {
        SceneVO vo = new SceneVO();
        vo.sceneName = name;
        try {
            String projPath = dataManager.getCurrentWorkingPath() + "/" + dataManager.currentProjectVO.projectName;
            FileUtils.writeStringToFile(new File(projPath + "/project.dt"), dataManager.currentProjectInfoVO.constructJsonString(), "utf-8");
            FileUtils.writeStringToFile(new File(projPath + "/scenes/" + vo.sceneName + ".dt"), vo.constructJsonString(), "utf-8");
            dataManager.currentProjectInfoVO.scenes.add(vo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vo;
    }

    public void preloadSceneSpecificData(SceneVO sceneVO, String resolution) {
        if (sceneVO == null || sceneVO.composite == null) return;

        FontSizePair[] fonts = sceneVO.composite.getRecursiveFontList();

        dataManager.textureManager.loadBitmapFonts(fonts, dataManager.resolutionManager.getCurrentMul());
    }

    public String getCurrProjectScenePathByName(String sceneName) {
        return dataManager.getCurrentWorkingPath() + "/" + dataManager.currentProjectVO.projectName + "/scenes/" + sceneName + ".dt";
    }

    public void saveScene(SceneVO vo) {
        try {
            FileUtils.writeStringToFile(new File(dataManager.getCurrentWorkingPath() + "/" + dataManager.currentProjectVO.projectName + "/scenes/" + vo.sceneName + ".dt"),
                    vo.constructJsonString(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteCurrentScene() {
        if (dataManager.currentProjectVO.lastOpenScene.equals("MainScene")) {
            return;
        }
        deleteScene(dataManager.currentProjectVO.lastOpenScene);
    }

    private void deleteScene(String sceneName) {
        ArrayList<SceneVO> scenes = dataManager.currentProjectInfoVO.scenes;
        SceneVO sceneToDelete = null;
        for (SceneVO scene : scenes) {
            if (scene.sceneName.equals(sceneName)) {
                sceneToDelete = scene;
                break;
            }
        }
        if (sceneToDelete != null) {
            scenes.remove(sceneToDelete);
        }
        dataManager.currentProjectInfoVO.scenes = scenes;
        String projPath = dataManager.getCurrentWorkingPath() + "/" + dataManager.currentProjectVO.projectName;
        try {
            FileUtils.writeStringToFile(new File(projPath + "/project.dt"), dataManager.currentProjectInfoVO.constructJsonString(), "utf-8");
            FileUtils.forceDelete(new File(projPath + "/scenes/" + sceneName + ".dt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buildScenes(String targetPath) {
        String srcPath = dataManager.getCurrentWorkingPath() + "/" + dataManager.currentProjectVO.projectName + "/scenes";
        FileHandle scenesDirectoryHandle = Gdx.files.absolute(srcPath);
        File fileTarget = new File(targetPath + "/" + scenesDirectoryHandle.name());
        try {
            FileUtils.copyDirectory(scenesDirectoryHandle.file(), fileTarget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //copy project dt
        try {
            FileUtils.copyFile(new File(dataManager.getCurrentWorkingPath() + "/" + dataManager.currentProjectVO.projectName + "/project.dt"), new File(targetPath + "/project.dt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
