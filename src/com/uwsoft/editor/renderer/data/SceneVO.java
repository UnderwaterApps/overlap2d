package com.uwsoft.editor.renderer.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class SceneVO {

    public String sceneName = "";

    public CompositeVO composite;

    public boolean lightSystemEnabled = false;

    public float[] ambientColor = {1f, 1f, 1f, 1f};

    public PhysicsPropertiesVO physicsPropertiesVO = new PhysicsPropertiesVO();

    public ArrayList<Float> verticalGuides = new ArrayList<Float>();
    public ArrayList<Float> horizontalGuides = new ArrayList<Float>();

    public SceneVO() {

    }

    public SceneVO(SceneVO vo) {
        sceneName = new String(vo.sceneName);
        composite = new CompositeVO(vo.composite);
        ambientColor = Arrays.copyOf(vo.ambientColor, vo.ambientColor.length);
        physicsPropertiesVO = new PhysicsPropertiesVO(vo.physicsPropertiesVO);
        lightSystemEnabled = vo.lightSystemEnabled;
    }

    public String constructJsonString() {
        String str = "";
        Json json = new Json();
        json.setOutputType(OutputType.json);
        str = json.toJson(this);
        return str;
    }
}
