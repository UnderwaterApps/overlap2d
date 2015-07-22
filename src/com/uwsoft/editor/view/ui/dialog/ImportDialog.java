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
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.utils.ImportUtils;
import com.uwsoft.editor.view.ui.widget.InputFileWidget;

public class ImportDialog extends UIDraggablePanel {
    public static final String CLASS_NAME = "com.uwsoft.editor.view.ui.dialog.ImportDialog";

    public static final String CANCEL_BTN_CLICKED = CLASS_NAME + ".CANCEL_BTN_CLICKED";
    public static final String IMPORT_BTN_CLICKED = CLASS_NAME + ".IMPORT_BTN_CLICKED";

    private Overlap2DFacade facade;

    private VisTable mainTable;
    private Image dropRegion;

    private VisProgressBar progressBar;

    private HashMap<Integer, String> typeNames = new HashMap<>();

    ImportDialog() {
        super("Import Resources");
        setMovable(true);
        setModal(false);
        addCloseButton();
        setStyle(VisUI.getSkin().get("box", WindowStyle.class));
        getTitleLabel().setAlignment(Align.left);

        setWidth(250);
        setHeight(100);

        facade = Overlap2DFacade.getInstance();

        fillTypeNames();

        mainTable = new VisTable();

        add(mainTable).fill().expand();

        setDroppingView();
    }

    private void fillTypeNames() {
        typeNames.clear();

        typeNames.put(ImportUtils.TYPE_ANIMATION_PNG_SEQUENCE, "PNG Sequence Animation");
        typeNames.put(ImportUtils.TYPE_BITMAP_FONT, "Bitmap Font");
        typeNames.put(ImportUtils.TYPE_IMAGE, "Texture");
        typeNames.put(ImportUtils.TYPE_PARTICLE_EFFECT, "Particle Effect");
        typeNames.put(ImportUtils.TYPE_SPINE_ANIMATION, "Spine Animation");
        typeNames.put(ImportUtils.TYPE_SPRITE_ANIMATION_ATLAS, "Animation Atlas Pack");
        typeNames.put(ImportUtils.TYPE_SPRITER_ANIMATION, "Spriter Animation");
        typeNames.put(ImportUtils.TYPE_TTF_FONT, "TTF Font");
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

        dropRegion.getColor().a = 0.3f;

        return false;
    }

    public void dragOver() {
        dropRegion.getColor().a = 0.5f;
    }

    public void dragExit() {
        dropRegion.getColor().a = 0.3f;
    }


    public void setDroppingView() {
        mainTable.clear();

        dropRegion = new Image(VisUI.getSkin().getDrawable("dropHere"));
        mainTable.add(dropRegion).padRight(6).padBottom(6);
        dragExit();

        pack();
    }

    public void setImportingView(int type, boolean isMultiple) {
        mainTable.clear();

        String typeText = typeNames.get(type);
        if(isMultiple) typeText+="'s";

        mainTable.add(new VisLabel("Ready to import: " + typeText)).left();
        mainTable.row().padBottom(5);

        progressBar = new VisProgressBar(0, 100, 1, false);
        mainTable.add(progressBar).fillX().padTop(5).width(250);
        mainTable.row().padBottom(5);

        VisTextButton cancelBtn = new VisTextButton("Cancel");
        VisTextButton importBtn = new VisTextButton("Import");

        VisTable btnTable = new VisTable();
        btnTable.add("").expand().right();
        btnTable.add(cancelBtn).width(50).padRight(5);
        btnTable.add(importBtn).width(50).right().padRight(3);

        //mainTable.add(btnTable).expand().fill();

        pack();

        initImportListeners(cancelBtn, importBtn);
    }

    private void initImportListeners(VisTextButton cancelBtn, VisTextButton importBtn) {
        cancelBtn.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                facade.sendNotification(CANCEL_BTN_CLICKED);
            }
        });
        importBtn.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                facade.sendNotification(IMPORT_BTN_CLICKED);
            }
        });
    }

    public void showError(int type) {

    }
}
