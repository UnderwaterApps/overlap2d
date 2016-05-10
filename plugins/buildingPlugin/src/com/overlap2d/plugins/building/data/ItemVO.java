package com.overlap2d.plugins.building.data;

import com.badlogic.gdx.utils.Array;

/**
 * Created by mariam on 5/5/16.
 */
public class ItemVO {

    public Array<BlockVO> blocks;
    public Array<DecorVO> decors;

    public ItemVO() {
        if (blocks == null) blocks = new Array<>();
        if (decors == null) decors = new Array<>();
    }
}
