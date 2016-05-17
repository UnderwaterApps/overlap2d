package com.overlap2d.plugins.tiled.data;

/**
 * Created by mariam on 3/31/16.
 */
public class AttributeVO {

    public String title;
    public float value;
    public boolean acceptNegativeValues;

    public AttributeVO() {
    }

    public AttributeVO(String title) {
        this.title = title+": ";
    }

    public AttributeVO(String title, boolean acceptNegativeValues) {
        this.title = title+": ";
        this.acceptNegativeValues = acceptNegativeValues;
    }

    public AttributeVO(String title, float value) {
        this.value = value;
        this.title = title+": ";
    }

    public AttributeVO(String title, float value, boolean acceptNegativeValues) {
        this.title = title+": ";
        this.value = value;
        this.acceptNegativeValues = acceptNegativeValues;
    }
}
