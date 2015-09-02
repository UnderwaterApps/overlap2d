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

package com.uwsoft.editor.data.vo;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import java.util.ArrayList;

public class ProjectVO {

    public String projectName = "";

    public String projectVersion = null;

    public String projectMainExportPath = "";

    public String lastOpenScene = "";
    public String lastOpenResolution = "";
    public String texturepackerWidth	=	"4096";
    public String texturepackerHeight  =	"4096";
    public boolean texturepackerDuplicate;

    public ArrayList<SceneConfigVO> sceneConfigs = new ArrayList<>();


    public String constructJsonString() {
        String str = "";
        Json json = new Json();
        json.setOutputType(OutputType.json);
        str = json.toJson(this);
        return str;
    }
}