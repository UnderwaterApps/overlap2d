package com.overlap2d.plugins.tiled.data;

import com.badlogic.gdx.utils.Array;

/**
 * Created by mariam on 2/5/16.
 */
public class CategoryVO {

    public String title = "size";
    public Array<AttributeVO> attributes;

    public CategoryVO(String title, Array<AttributeVO> attributes) {
        this.title = title;
        this.attributes = attributes;
    }
}
