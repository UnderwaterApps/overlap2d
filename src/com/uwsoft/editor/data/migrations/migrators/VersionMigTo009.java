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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.data.migrations.IVersionMigrator;
import com.uwsoft.editor.proxy.ProjectManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by azakhary on 6/29/2015.
 */
public class VersionMigTo009 implements IVersionMigrator {

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
        // run through scene files and modify their content to new one

        // this is list of libraryItems for later
        HashMap<String, JsonValue> libraryItems = new HashMap<>();

        // fixing animations format (frameRange) and moving library items
        File scenesDir = new File(projectPath + File.separator + "scenes");
        for (File entry : scenesDir.listFiles()) {
            if (!entry.isDirectory()) {
                try {
                    String content = FileUtils.readFileToString(new FileHandle(entry).file());
                    JsonValue value = jsonReader.parse(content);
                    fixAnimations(value.get("composite"));
                    if(value.get("libraryItems") != null) {
                        JsonValue.JsonIterator libraryArr = value.get("libraryItems").iterator();
                        while (libraryArr.hasNext()) {
                            JsonValue libItem = libraryArr.next();
                            fixAnimations(libItem.get("composite"));
                            libraryItems.put(libItem.name, libItem);
                        }
                        value.remove("libraryItems");
                    }

                    content = value.prettyPrint(JsonWriter.OutputType.json, 1);
                    FileUtils.writeStringToFile(new File(entry.getAbsolutePath()), content, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        fixLibraryItemsLocation(libraryItems);

        return true;
    }

    private void fixLibraryItemsLocation(HashMap<String, JsonValue> libraryItems) {
        if(libraryItems.size() == 0) return;
        //creating libraryArrayJsonString
        String libraryArrayJsonString = "{";
        for (JsonValue entry : libraryItems.values()) {
            libraryArrayJsonString +=  "\""+entry.name+"\": " + entry.prettyPrint(JsonWriter.OutputType.json, 1) + ", ";
        }
        libraryArrayJsonString = libraryArrayJsonString.substring(0,libraryArrayJsonString.length()-2) + "}";

        //ProjectInfo data
        String prjInfoFilePath = projectPath + "/project.dt";
        FileHandle projectInfoFile = Gdx.files.internal(prjInfoFilePath);
        try {
            String projectInfoContents = FileUtils.readFileToString(projectInfoFile.file());
            JsonValue value = jsonReader.parse(projectInfoContents);
            JsonValue newVal = jsonReader.parse(libraryArrayJsonString);
            newVal.name = "libraryItems";
            newVal.prev = value.get("scenes");
            newVal.next = newVal.prev.next;
            newVal.prev.next = newVal;

            String content = value.prettyPrint(JsonWriter.OutputType.json, 1);
            FileUtils.writeStringToFile(new File(prjInfoFilePath), content, "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fixAnimations(JsonValue value) {
        if(value.get("sComposites") == null) return;

        if(value.get("sComposites") != null) {
            JsonValue.JsonIterator compositeArray = value.get("sComposites").iterator();
            while (compositeArray.hasNext()) {
                JsonValue composite = compositeArray.next();
                if (composite != null) {
                    fixAnimations(composite.get("composite"));
                }
            }
        }

        if(value.get("sSpriteAnimations") != null) {
            JsonValue.JsonIterator spriteArray = value.get("sSpriteAnimations").iterator();
            while (spriteArray.hasNext()) {
                JsonValue valAnim = spriteArray.next();
                if (valAnim.get("animations") != null) {
                    String val = valAnim.get("animations").asString();
                    JsonValue animationsInnerJson = jsonReader.parse(val);
                    JsonValue.JsonIterator innerArray = animationsInnerJson.iterator();
                    String cnt = "[";
                    while (innerArray.hasNext()) {
                        JsonValue innerVal = innerArray.next();
                        int startFrame = innerVal.get("startFrame").asInt();
                        int endFrame = innerVal.get("endFrame").asInt();
                        String currName = innerVal.get("name").asString();
                        cnt += "{\"name\":\""+currName+"\", \"startFrame\":"+startFrame+", \"endFrame\":"+endFrame+"}, ";
                    }
                    if(cnt.length() == 1) {
                        cnt = "[]";
                    } else {
                        cnt = cnt.substring(0, cnt.length() - 2) + "]";
                    }
                    JsonValue newVal = jsonReader.parse(cnt);
                    setNewKeyToJson(valAnim, "frameRangeMap", "animations", newVal);
                }
            }
        }
    }

    private void setNewKeyToJson(JsonValue container, String newKey, String oldKey, JsonValue newVal) {
        JsonValue oldVal = container.get(oldKey);
        if(oldVal.prev != null) oldVal.prev.setNext(newVal);
        if(oldVal.next != null)  oldVal.next.setPrev(newVal);
        newVal.setPrev(oldVal.prev);
        newVal.setNext(oldVal.next);
        newVal.name = newKey;
    }

}
