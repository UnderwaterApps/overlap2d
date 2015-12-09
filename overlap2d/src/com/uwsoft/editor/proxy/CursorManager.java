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

package com.uwsoft.editor.proxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.puremvc.patterns.proxy.BaseProxy;
import com.uwsoft.editor.view.ui.widget.CursorData;

/**
 * Created by azakhary on 5/15/2015.
 */
public class CursorManager extends BaseProxy {
    private static final String TAG = CursorManager.class.getCanonicalName();
    public static final String NAME = TAG;

    public static CursorData NORMAL = new CursorData("normal", 8, 6);
    public static CursorData CROSS = new CursorData("cross", 14, 14);
    public static CursorData TEXT = new CursorData("label", 15, 17);
    public static CursorData TEXT_TOOL = new CursorData("label-tool", 15, 17);
    public static CursorData FINGER = new CursorData("fingerpoint", 16, 9);

    public static CursorData ROTATION_LB = new CursorData("left-rotate-down", 15, 18);
    public static CursorData ROTATION_LT = new CursorData("left-rotate-up", 15, 15);
    public static CursorData ROTATION_RT = new CursorData("right-rotate-up", 18, 15);
    public static CursorData ROTATION_RB = new CursorData("right-rotate-down", 18, 18);

    public static CursorData TRANSFORM_LEFT_RIGHT = new CursorData("left-down-up", 17, 16);
    public static CursorData TRANSFORM_RIGHT_LEFT = new CursorData("right-down-up", 17, 16);
    public static CursorData TRANSFORM_HORIZONTAL = new CursorData("left-right", 17, 16);
    public static CursorData TRANSFORM_VERTICAL = new CursorData("up-down", 17, 16);

    private CursorData cursor;
    private CursorData overrideCursor = null;

    public CursorManager() {
        super(NAME);

        setCursor(NORMAL);
    }

    public void setCursor(CursorData cursor) {
        this.cursor = cursor;
        setCursorPixmap();
    }

    public void setOverrideCursor(CursorData cursor) {
        overrideCursor = cursor;
        setCursorPixmap();
    }

    public void removeOverrideCursor() {
        setOverrideCursor(null);
    }

    private void setCursorPixmap() {
        CursorData currentCursor = overrideCursor;
        if(currentCursor == null) {
            currentCursor = cursor;
        }

        Pixmap pm = new Pixmap(Gdx.files.internal("cursors/" + currentCursor.region + ".png"));
        Cursor cursorObj = Gdx.graphics.newCursor(pm, cursor.getHotspotX(), currentCursor.getHotspotY());
        Gdx.graphics.setCursor(cursorObj);
        pm.dispose();
    }
}
