package com.uwsoft.editor.data.vo;

import java.util.ArrayList;

/**
 * Created by CyberJoe on 7/18/2015.
 */
public class SceneConfigVO {

    public String sceneName = "";

    public ArrayList<Float> verticalGuides = new ArrayList<>();
    public ArrayList<Float> horizontalGuides = new ArrayList<>();

    // 0 = x, 1 = y
    public float[] cameraPosition = new float[2];
}
