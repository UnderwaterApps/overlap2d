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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.view.ui.widget.InputFileWidget;

public class ImportDialog extends O2DDialog {
    public static final String START_IMPORTING_BTN_CLICKED = "com.uwsoft.editor.view.ui.dialog.ImportDialog" + ".START_IMPORTING_BTN_CLICKED";

    private Overlap2DFacade facade;

    private Image dropRegion;

    ImportDialog() {
        super("Import new Assets");
        setMovable(true);
        setModal(false);
        setStyle(VisUI.getSkin().get("box", WindowStyle.class));
        getTitleLabel().setAlignment(Align.left);

        facade = Overlap2DFacade.getInstance();

        VisTable mainTable = new VisTable();

        TabbedPane tabbedPane = new TabbedPane();
        Tab texturesTab = new ImportTypeTab("Import Images");
        Tab animationsTab = new ImportTypeTab("Import Animations");
        Tab particleEffectsTab = new ImportTypeTab("Import Particle Effects");
        Tab fontsTab = new ImportTypeTab("Import Fonts");
        tabbedPane.add(texturesTab);
        tabbedPane.add(animationsTab);
        tabbedPane.add(particleEffectsTab);
        tabbedPane.add(fontsTab);
        mainTable.add(tabbedPane.getTable());

        mainTable.row().padBottom(3);

        dropRegion = new Image(VisUI.getSkin().getDrawable("logo"));
        mainTable.add(dropRegion);

        add(mainTable);
    }

    @Override
    public void hide(Action action) {
        super.hide(action);

    }


    public Image getDropRegion() {
        return dropRegion;
    }

    public boolean checkDropRegionHit(Vector2 mousePos) {
        Vector2 pos = new Vector2(mousePos.x-8, mousePos.y-31);
        pos = dropRegion.screenToLocalCoordinates(pos);
        if(dropRegion.hit(pos.x, pos.y, false) != null) {
            return true;
        }

        dropRegion.getColor().a = 1f;

        return false;
    }

    public void dragOver() {
        dropRegion.getColor().a = 0.5f;
    }

    public void dragExit() {
        dropRegion.getColor().a = 1f;
    }

    public void setPaths(String[] paths) {
        for(int i = 0; i < paths.length; i++) {
            System.out.println(paths[i]);
        }
    }
}
