package com.uwsoft.editor.mvc.view.ui.box.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;

/**
 * Created by CyberJoe on 4/26/2015.
 */
public class TextToolSettings implements ToolSettings {

    private String fontFamily;
    private boolean isBold;
    private boolean isItalic;
    private int fontSize;
    private int letterSpacing;
    private Color color;
    private boolean kerningEnabled;
    private int align;

    public TextToolSettings() {
        fontFamily = "arial";
        fontSize = 12;
        color = Color.WHITE;
        kerningEnabled = true;
        align = Align.left;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean isBold) {
        this.isBold = isBold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean isItalic) {
        this.isItalic = isItalic;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getLetterSpacing() {
        return letterSpacing;
    }

    public void setLetterSpacing(int letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isKerningEnabled() {
        return kerningEnabled;
    }

    public void setKerningEnabled(boolean kerningEnabled) {
        this.kerningEnabled = kerningEnabled;
    }

}
