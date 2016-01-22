package com.uwsoft.editor.renderer.resources;

/**
 * Created by azakhary on 9/9/2014.
 */
public class FontSizePair {

    public String fontName;
    public int fontSize;

    public FontSizePair(String name, int size) {
        fontName = name;
        fontSize = size;
    }

    @Override
    public boolean equals(Object arg0) {
        FontSizePair arg = (FontSizePair)arg0;
        if(arg.fontName.equals(fontName) && arg.fontSize == fontSize) return true;

        return false;
    }

    @Override
    public String toString() {
        return fontName + "_" + fontSize;
    }

    @Override
    public int hashCode() {
        return (fontName + "_" + fontSize).hashCode();
    }


}
