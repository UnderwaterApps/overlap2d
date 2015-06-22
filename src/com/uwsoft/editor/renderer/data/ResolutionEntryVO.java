package com.uwsoft.editor.renderer.data;

public class ResolutionEntryVO {

    public String name;

    public int width;
    public int height;
    public int base;

    @Override
    public String toString() {
        if (width == 0 && height == 0) {
            return name;
        }
        return width + "x" + height + " (" + name + ")";
    }
}
