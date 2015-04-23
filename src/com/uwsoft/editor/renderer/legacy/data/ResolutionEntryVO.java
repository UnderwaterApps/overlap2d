package com.uwsoft.editor.renderer.data;

public class ResolutionEntryVO {

    public String name;

    public int width;
    public int height;
    public int base;

    @Override
    public String toString() {
        return width + "x" + height + " (" + name + ")";
    }
}
