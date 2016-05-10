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

package com.uwsoft.editor.view.ui.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.CursorManager;
import com.vo.CursorData;

/**
 * Created by NateS
 */
public class CursorListener extends InputListener {
    private CursorData cursor;
    private CursorManager cursorManager;

    private CursorData prevCursor;

    public CursorListener (CursorData cursor) {
        this.cursor = cursor;
        cursorManager = Overlap2DFacade.getInstance().retrieveProxy(CursorManager.NAME);
    }

    public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
        if (pointer == -1) cursorManager.setOverrideCursor(cursor);
    }

    public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (pointer == -1) cursorManager.setOverrideCursor(null);
    }
}