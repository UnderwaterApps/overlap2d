/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.view.stage.tools;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.factory.ItemFactory;
import com.uwsoft.editor.proxy.CursorManager;
import com.uwsoft.editor.renderer.factory.EntityFactory;

/**
 * Created by azakhary on 4/30/2015.
 */
public class TextTool extends ItemDropTool {

    public static final String NAME = "TEXT_TOOL";

    private String fontFamily;
    private boolean isBold;
    private boolean isItalic;
    private int fontSize;
    private int letterSpacing;
    private Color color;
    private boolean kerningEnabled;
    private int align;

    public TextTool() {
        fontFamily = "arial";
        fontSize = 20;
        color = Color.WHITE;
        kerningEnabled = true;
        align = Align.left;
    }

    @Override
    public void initTool() {
        super.initTool();
        CursorManager cursorManager = Overlap2DFacade.getInstance().retrieveProxy(CursorManager.NAME);
        cursorManager.setCursor(CursorManager.TEXT_TOOL);
    }

    @Override
    public Entity putItem(float x, float y) {
        return ItemFactory.get().createLabel(this, new Vector2(x, y));
    }

    @Override
    public int[] listItemFilters() {
        int[] filter = {EntityFactory.LABEL_TYPE};
        return filter;
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
