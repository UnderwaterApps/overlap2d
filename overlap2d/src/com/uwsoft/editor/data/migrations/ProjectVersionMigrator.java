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

package com.uwsoft.editor.data.migrations;

import java.io.IOException;

import com.uwsoft.editor.data.migrations.migrators.VersionMigTo009;
import com.vo.ProjectVO;
import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.data.migrations.migrators.DummyMig;
import com.uwsoft.editor.data.migrations.migrators.VersionMigTo005;

/**
 * Created by azakhary on 9/28/2014.
 */
public class ProjectVersionMigrator {

    private String projectPath;
    private ProjectVO projectVo;

    private int safetyIterator = 0;

    /**
     * this is the current supported version, change when data format is changed, and add migration script
     */
    public static String dataFormatVersion = "0.0.9";

    private Json json = new Json();

    public ProjectVersionMigrator(String projectPath, ProjectVO projectVo) {
        this.projectPath = projectPath;
        this.projectVo = projectVo;

        json.setOutputType(JsonWriter.OutputType.json);
    }

    public void start() {

        if (projectVo.projectVersion == null || projectVo.projectVersion.equals("")) {
            projectVo.projectVersion = "0.0.4";
        }

        migrationIterator();
    }

    private void migrationIterator() {
        if (projectVo.projectVersion.equals(dataFormatVersion)) return;

        if (safetyIterator > 100) {
            System.out.println("Emergency exit from version migration process due to safety lock");
            return;
        }
        safetyIterator++;

        if (projectVo.projectVersion.equals("0.0.4")) {
            VersionMigTo005 vmt = new VersionMigTo005();
            doMigartion(vmt, "0.0.5");
        }
        if (projectVo.projectVersion.equals("0.0.5") || projectVo.projectVersion.equals("0.0.6") || projectVo.projectVersion.equals("0.0.7")) {
            DummyMig vmt = new DummyMig();
            doMigartion(vmt, "0.0.8");
        }
        if (projectVo.projectVersion.equals("0.0.8")) {
            VersionMigTo009 vmt = new VersionMigTo009();
            doMigartion(vmt, "0.0.9");
        }
    }

    private void doMigartion(IVersionMigrator vmt, String nextVersion) {
        vmt.setProject(projectPath);

        boolean successs = vmt.doMigration();

        if (successs) {
            setVersion(nextVersion);
            migrationIterator();
        } else {
            return;
        }
    }

    private void setVersion(String version) {
        projectVo.projectVersion = version;
        String projectVoJson = json.toJson(projectVo, ProjectVO.class);
        try {
            FileUtils.writeStringToFile(new java.io.File(projectPath + "/project.pit"), projectVoJson, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
