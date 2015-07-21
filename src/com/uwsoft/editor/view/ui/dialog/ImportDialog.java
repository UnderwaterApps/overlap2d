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

package com.uwsoft.editor.view.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.view.ui.widget.InputFileWidget;

public class ImportDialog extends O2DDialog {
    public static final String START_IMPORTING_BTN_CLICKED = "com.uwsoft.editor.view.ui.dialog.ImportDialog" + ".START_IMPORTING_BTN_CLICKED";

    private Overlap2DFacade facade;

    ImportDialog() {
        super("Import new Assets");
        setMovable(true);
        setModal(false);
        setStyle(VisUI.getSkin().get("box", WindowStyle.class));
        getTitleLabel().setAlignment(Align.left);

        facade = Overlap2DFacade.getInstance();

        VisTable mainTable = new VisTable();

        add(mainTable);
    }

    @Override
    public void hide(Action action) {
        super.hide(action);

    }

}
