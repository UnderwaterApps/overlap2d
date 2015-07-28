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

package com.uwsoft.editor.view.frame;

import com.uwsoft.editor.Overlap2DFacade;

import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

/**
 * Created by azakhary on 7/21/2015.
 */
public class FileDropListener implements DropTargetListener {

    private static final String CLASS_NAME = "com.uwsoft.editor.view.frame.FileDropListener";
    public static final String ACTION_DRAG_ENTER = CLASS_NAME + "ACTION_DRAG_ENTER";
    public static final String ACTION_DRAG_OVER = CLASS_NAME + "ACTION_DRAG_OVER";
    public static final String ACTION_DRAG_EXIT = CLASS_NAME + "ACTION_DRAG_EXIT";
    public static final String ACTION_DROP = CLASS_NAME + "ACTION_DROP";

    public void sendNotification(String notification) {
        sendNotification(notification, null);
    }

    public void sendNotification(String notification, Object data) {
        Overlap2DFacade facade = Overlap2DFacade.getInstance();
        if(facade != null) {
            facade.sendNotification(notification, data);
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        sendNotification(ACTION_DRAG_ENTER, dtde);
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        sendNotification(ACTION_DRAG_OVER, dtde);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // Do we even need this?
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        sendNotification(ACTION_DRAG_EXIT);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        sendNotification(ACTION_DROP, dtde);
    }
}
