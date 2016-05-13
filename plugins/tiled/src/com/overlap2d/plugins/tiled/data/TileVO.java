package com.overlap2d.plugins.tiled.data;

/**
 * Created by mariam on 5/13/16.
 */
public class TileVO {

    public String regionName = "";
    public float gridOffset;

    public TileVO() {
    }

    public TileVO(String regionName) {
        this.regionName = regionName;
    }

    public TileVO(String regionName, float offset) {
        this.regionName = regionName;
        this.gridOffset = offset;
    }
}
