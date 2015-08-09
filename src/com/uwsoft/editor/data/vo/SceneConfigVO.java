package com.uwsoft.editor.data.vo;

import java.util.ArrayList;

/**
 * Created by azakhary on 7/18/2015.
 *
 * Here we keep all editor specific but not runtime specific data per scene. (like for example last camera position)
 */
public class SceneConfigVO {

    public String sceneName = "";

    // 0 = x, 1 = y
    public float[] cameraPosition = new float[2];
}
