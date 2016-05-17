package com.overlap2d.plugins.building.data;

import com.badlogic.gdx.utils.Array;

/**
 * Created by mariam on 5/5/16.
 */
public class BuildingVO {

    public Array<ItemVO> items;

    public BuildingVO() {
        if (items == null) items = new Array<>();
    }
}
