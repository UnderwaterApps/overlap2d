package com.uwsoft.editor.data.vo;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class ProjectVO {

    public String projectName = "";

    public String projectVersion = null;

    public String projectMainExportPath = "";

    public String projectSceneExportPath = "";
    public String projectAtlasExportPath = "";
    public String projectEffectsExportPath = "";
    public String projectSpineExportPath = "";
    public String projectFontsExportPath = "";
    public String lastOpenScene = "";
    public String lastOpenResolution = "";    
    public String texturepackerWidth	=	"4096";
    public String texturepackerHeight  =	"4096";

    public String constructJsonString() {
        String str = "";
        Json json = new Json();
        json.setOutputType(OutputType.json);
        str = json.toJson(this);
        return str;
    }
}
