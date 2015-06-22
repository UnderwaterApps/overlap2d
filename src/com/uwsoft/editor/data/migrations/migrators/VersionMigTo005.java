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

package com.uwsoft.editor.data.migrations.migrators;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.data.migrations.IVersionMigrator;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.proxy.ResolutionManager;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;

/**
 * Created by azakhary on 9/28/2014.
 */
public class VersionMigTo005 implements IVersionMigrator {

    private String projectPath;

    private Json json = new Json();
    private JsonReader jsonReader = new JsonReader();
    private Overlap2DFacade facade;
    private ProjectManager projectManager;

    @Override
    public void setProject(String path) {
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        projectPath = path;
        json.setOutputType(JsonWriter.OutputType.json);
    }

    @Override
    public boolean doMigration() {

        // Rename folder animations to spine-animations in orig (if exist);
        File animationsDir = new File(projectPath + File.separator + "assets" + File.separator + "orig" + File.separator + "animations");
        if (animationsDir.exists() && animationsDir.isDirectory()) {
            File spineAnimationsDir = new File(projectPath + File.separator + "assets" + File.separator + "orig" + File.separator + "spine-animations");
            animationsDir.renameTo(spineAnimationsDir);
        }

        // get list of resolutions
        String prjInfoFilePath = projectPath + "/project.dt";
        FileHandle projectInfoFile = Gdx.files.internal(prjInfoFilePath);
        String projectInfoContents = null;
        try {
            projectInfoContents = FileUtils.readFileToString(projectInfoFile.file());
            ProjectInfoVO currentProjectInfoVO = json.fromJson(ProjectInfoVO.class, projectInfoContents);
            projectManager.currentProjectInfoVO = currentProjectInfoVO;

            // run through all resolutions and remake animations for all
            for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions) {
                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                resolutionManager.createResizedAnimations(resolutionEntryVO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // change sLights to sLights
        File scenesDir = new File(projectPath + File.separator + "scenes");
        for (File entry : scenesDir.listFiles()) {
            if (!entry.isDirectory()) {
                try {
                    String content = FileUtils.readFileToString(new FileHandle(entry).file());
                    content = content.replaceAll("\"slights\":", "\"sLights\":");
                    FileUtils.writeStringToFile(new File(entry.getAbsolutePath()), content, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return true;
    }
}
