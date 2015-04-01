/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.data.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.puremvc.patterns.proxy.BaseProxy;
import com.uwsoft.editor.controlles.ResolutionManager;
import com.uwsoft.editor.mvc.proxy.DataManager;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.FontSizePair;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sargis on 3/23/15.
 */
public class SceneDataManager extends BaseProxy {
    private static final String TAG = SceneDataManager.class.getCanonicalName();
    public static final String NAME = TAG;

    public SceneDataManager() {
        super(NAME);
    }

    public SceneVO createNewScene(String name) {
        SceneVO vo = new SceneVO();
        vo.sceneName = name;
        DataManager dataManager = facade.retrieveProxy(DataManager.NAME);
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
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
        textureManager.loadBitmapFonts(fonts, resolutionManager.getCurrentMul());
    }

    public String getCurrProjectScenePathByName(String sceneName) {
        DataManager dataManager = facade.retrieveProxy(DataManager.NAME);
        return dataManager.getCurrentWorkingPath() + "/" + dataManager.currentProjectVO.projectName + "/scenes/" + sceneName + ".dt";
    }

    public void saveScene(SceneVO vo) {
        DataManager dataManager = facade.retrieveProxy(DataManager.NAME);
        try {
            FileUtils.writeStringToFile(new File(dataManager.getCurrentWorkingPath() + "/" + dataManager.currentProjectVO.projectName + "/scenes/" + vo.sceneName + ".dt"),
                    vo.constructJsonString(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteCurrentScene() {
        DataManager dataManager = facade.retrieveProxy(DataManager.NAME);
        if (dataManager.currentProjectVO.lastOpenScene.equals("MainScene")) {
            return;
        }
        deleteScene(dataManager.currentProjectVO.lastOpenScene);
    }

    private void deleteScene(String sceneName) {
        DataManager dataManager = facade.retrieveProxy(DataManager.NAME);
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
        DataManager dataManager = facade.retrieveProxy(DataManager.NAME);
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
