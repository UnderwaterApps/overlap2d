package com.uwsoft.editor.renderer.data;

public class ResolutionEntryVO {

    public String name = "";

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResolutionEntryVO other = (ResolutionEntryVO) obj;
        return other.name.equals(name);
    }

    public float getMultiplier(ResolutionEntryVO originalResolution) {
        float mul;
        if(base == 0) {
            mul = (float)originalResolution.width/width;
        } else {
            mul = (float)originalResolution.height/height;
        }
        return mul;
    }
}
