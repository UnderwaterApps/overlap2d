package com.overlap2d.plugins.tiled.data;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by mariam on 5/13/16.
 */
public class TileVO {

    public String regionName = "";
    public Vector2 gridOffset;

    public TileVO() {
        gridOffset = new Vector2();
    }

    public TileVO(String regionName) {
        this.regionName = regionName;
        gridOffset = new Vector2();
    }

    public TileVO(String regionName, Vector2 offset) {
        this.regionName = regionName;
        this.gridOffset = offset;
    }
}
