package com.overlap2d.plugins.building.data;

/**
 * Created by mariam on 5/5/16.
 */
public class BlockVO {

    public String regionName = "";
    public int hp;
    public float x;
    public float y;

    public BlockVO() {
    }

    public BlockVO(String regionName) {
        this.regionName = regionName;
    }
}
