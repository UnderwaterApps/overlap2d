package com.uwsoft.editor.renderer.data;

import java.util.ArrayList;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class ProjectInfoVO {

    public ResolutionEntryVO originalResolution = new ResolutionEntryVO();

    public ArrayList<ResolutionEntryVO> resolutions = new ArrayList<>();
    public ArrayList<SceneVO> scenes = new ArrayList<>();

    public String constructJsonString() {
        String str = "";
        Json json = new Json();
        json.setOutputType(OutputType.json);
        str = json.toJson(this);
        json.prettyPrint(str);
        return str;
    }

    public ResolutionEntryVO getResolution(String name) {
        for (ResolutionEntryVO resolution : resolutions) {
            if (resolution.name.equalsIgnoreCase(name)) {
                return resolution;
            }
        }
        return null;
    }
}
