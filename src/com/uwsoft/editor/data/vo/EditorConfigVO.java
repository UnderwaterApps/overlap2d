package com.uwsoft.editor.data.vo;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

/**
 * Created by sargis on 8/1/14.
 */
public class EditorConfigVO {
    public static final String EDITOR_CONFIG_FILE = "config.pit";
    public String lastOpenedSystemPath = "";

    public String constructJsonString() {
        String str = "";
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        str = json.toJson(this);
        return str;
    }
}
