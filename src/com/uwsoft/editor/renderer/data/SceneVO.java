package com.uwsoft.editor.renderer.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import java.util.Arrays;
import java.util.HashMap;


public class SceneVO {

    public String sceneName = "";

    public CompositeVO composite;

    public float[] ambientColor = {0.5f, 0.5f, 0.5f, 1f};

    public HashMap<String, CompositeItemVO> libraryItems = new HashMap<String, CompositeItemVO>();

    public PhysicsPropertiesVO physicsPropertiesVO = new PhysicsPropertiesVO();

    public SceneVO() {

    }

    public SceneVO(SceneVO vo) {
        sceneName = new String(vo.sceneName);
        composite = new CompositeVO(vo.composite);
        ambientColor = Arrays.copyOf(vo.ambientColor, vo.ambientColor.length);
        physicsPropertiesVO = new PhysicsPropertiesVO(vo.physicsPropertiesVO);
    }

    public String constructJsonString() {
        String str = "";
        Json json = new Json();
        json.setOutputType(OutputType.json);
        str = json.toJson(this);
        return str;
    }
}
