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

package com.uwsoft.editor.mvc.view.ui.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.ui.widget.InputFileWidget;

public class AssetsImportDialog extends O2DDialog {
    public static final String START_IMPORTING_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.dialog.NewProjectDialog" + ".START_IMPORTING_BTN_CLICKED";
    private final AssetsImportInputFileWidget imagesInputFileWidget;
    private final AssetsImportInputFileWidget fontsInputFileWidget;
    private final AssetsImportInputFileWidget spineSpriterInputFileWidget;
    private final AssetsImportInputFileWidget spriteAnimationInputFileWidget;
    private final AssetsImportInputFileWidget particleEffectInputFileWidget;
    private final AssetsImportInputFileWidget stylesInputFileWidget;
    private final VisProgressBar progressBar;

    private HashMap<String, ArrayList<File>> paths = new HashMap<>();

    AssetsImportDialog() {
        super("Import new Assets");
        addCloseButton();
        VisTable mainTable = new VisTable();
//        mainTable.debug();
        imagesInputFileWidget = new AssetsImportInputFileWidget("Multiple images", true);
        mainTable.add(imagesInputFileWidget).padRight(50);
        fontsInputFileWidget = new AssetsImportInputFileWidget("Fonts", true);
        mainTable.add(fontsInputFileWidget);
        mainTable.row().padTop(15);
        spineSpriterInputFileWidget = new AssetsImportInputFileWidget("Spine / Spriter", true);
        mainTable.add(spineSpriterInputFileWidget).padRight(50);
        spriteAnimationInputFileWidget = new AssetsImportInputFileWidget("Sprite animation", true);
        mainTable.add(spriteAnimationInputFileWidget);
        mainTable.row().padTop(15);
        particleEffectInputFileWidget = new AssetsImportInputFileWidget("Particle effect", true);
        mainTable.add(particleEffectInputFileWidget).padRight(50);
        stylesInputFileWidget = new AssetsImportInputFileWidget("Styles (not working yet)", false);
        mainTable.add(stylesInputFileWidget);
        mainTable.row().padTop(15);
        VisTextButton importBtn = new VisTextButton("Start Importing");
        importBtn.addListener(new StartImportingBtnClickListener());
        mainTable.add(importBtn).colspan(2).right();
        mainTable.row().padTop(15);
        progressBar = new VisProgressBar(0, 100, 1, false);
        mainTable.add(progressBar).colspan(2).fillX();
        mainTable.pad(15);

        add(mainTable);
    }

    @Override
    public void hide(Action action) {
        super.hide(action);
        imagesInputFileWidget.resetData();
        fontsInputFileWidget.resetData();
        spineSpriterInputFileWidget.resetData();
        spriteAnimationInputFileWidget.resetData();
        particleEffectInputFileWidget.resetData();
        stylesInputFileWidget.resetData();
    }

    public Array<FileHandle> getImageFiles() {
        return imagesInputFileWidget.getValues();
    }

    public Array<FileHandle> getFontFiles() {
        return fontsInputFileWidget.getValues();
    }

    public Array<FileHandle> getSpineSpriterFiles() {
        return spineSpriterInputFileWidget.getValues();
    }

    public Array<FileHandle> getSpriteAnimationFiles() {
        return spriteAnimationInputFileWidget.getValues();
    }

    public Array<FileHandle> getParticleEffectFiles() {
        return particleEffectInputFileWidget.getValues();
    }

    public FileHandle getStyleFiles() {
        return stylesInputFileWidget.getValue();
    }

    private class AssetsImportInputFileWidget extends VisTable {
        private final String title;
        private final InputFileWidget inputFileWidget;

        public AssetsImportInputFileWidget(String title, boolean multiselectionEnabled) {
            this.title = title;
            inputFileWidget = new InputFileWidget(FileChooser.Mode.OPEN, FileChooser.SelectionMode.FILES, multiselectionEnabled);
            inputFileWidget.setTextFieldWidth(300);
            add(title).left();
            row().padTop(2);
            add(inputFileWidget);
        }

        public FileHandle getValue() {
            return inputFileWidget.getValue();
        }

        public Array<FileHandle> getValues() {
            return inputFileWidget.getValues();
        }

        public void resetData() {
            inputFileWidget.resetData();
        }
    }

    private class StartImportingBtnClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            facade.sendNotification(START_IMPORTING_BTN_CLICKED);
        }
    }
}
