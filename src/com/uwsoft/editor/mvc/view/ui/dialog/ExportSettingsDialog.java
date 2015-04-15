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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.ui.widget.InputFileWidget;

/**
 * Created by sargis on 7/31/14.
 */
public class ExportSettingsDialog extends VisDialog {
    public static final String SAVE_SETTINGS_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.dialog.ExportSettingsDialog" + ".SAVE_SETTINGS_BTN_CLICKED";
    public static final String SAVE_SETTINGS_AND_EXPORT_BTN_CLICKED = "com.uwsoft.editor.mvc.view.ui.dialog.ExportSettingsDialog" + ".SAVE_SETTINGS_AND_EXPORT_BTN_CLICKED";
    private final ExportSettingsInputFileWidget exportSettingsInputFileWidget;
    private final AtlasMaxSizeWidget atlasMaxSizeWidget;

    public ExportSettingsDialog() {
        super("Export settings");
        addCloseButton();
        VisTable mainTable = new VisTable();
//        mainTable.debug();
        exportSettingsInputFileWidget = new ExportSettingsInputFileWidget("Assets folder:", false);
        mainTable.add(exportSettingsInputFileWidget);
        mainTable.row().padTop(10);
        atlasMaxSizeWidget = new AtlasMaxSizeWidget();
        mainTable.add(atlasMaxSizeWidget).right();
        mainTable.row().padTop(10);
        VisTable buttonsTable = new VisTable();
        VisTextButton saveSettingsBtn = new VisTextButton("Save Settings");
        saveSettingsBtn.addListener(new ExportSettingsDialogBtnListener(SAVE_SETTINGS_BTN_CLICKED));
        VisTextButton saveSettingsAndExportBtn = new VisTextButton("Save Settings and Export");
        saveSettingsAndExportBtn.addListener(new ExportSettingsDialogBtnListener(SAVE_SETTINGS_AND_EXPORT_BTN_CLICKED));
        buttonsTable.add(saveSettingsBtn).padRight(15);
        buttonsTable.add(saveSettingsAndExportBtn);
        mainTable.add(buttonsTable).right();
        mainTable.pad(15);
        add(mainTable);
    }

    private class ExportSettingsDialogBtnListener extends ClickListener {
        private final String command;

        public ExportSettingsDialogBtnListener(String command) {
            super();
            this.command = command;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Overlap2DFacade facade = Overlap2DFacade.getInstance();
            ExportSettingsVO exportSettingsVO = new ExportSettingsVO();
            exportSettingsVO.width = atlasMaxSizeWidget.widthSelectBox.getSelected();
            exportSettingsVO.height = atlasMaxSizeWidget.heightSelectBox.getSelected();
            exportSettingsVO.fileHandle = exportSettingsInputFileWidget.getValue();
            facade.sendNotification(command, exportSettingsVO);
        }
    }

    private class AtlasMaxSizeWidget extends VisTable {
        private final VisSelectBox<Integer> widthSelectBox;
        private final VisSelectBox<Integer> heightSelectBox;

        public AtlasMaxSizeWidget() {
            Integer[] data = {512, 1024, 2048, 4096};
//            debug();
            add("Atlas Max Size : ").padRight(15);
            widthSelectBox = new VisSelectBox<>();
            widthSelectBox.setItems(data);
            widthSelectBox.setSelectedIndex(2);
            add(widthSelectBox).padRight(5);
            add("X").padRight(5);
            heightSelectBox = new VisSelectBox<>();
            heightSelectBox.setItems(data);
            heightSelectBox.setSelectedIndex(2);
            add(heightSelectBox);
        }
    }

    private class ExportSettingsInputFileWidget extends VisTable {
        private final String title;
        private final InputFileWidget inputFileWidget;

        public ExportSettingsInputFileWidget(String title, boolean multiselectionEnabled) {
            this.title = title;
            inputFileWidget = new InputFileWidget(FileChooser.Mode.OPEN, FileChooser.SelectionMode.DIRECTORIES, multiselectionEnabled);
            inputFileWidget.setTextFieldWidth(300);
            add(title).right().padRight(5);
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

    class ExportSettingsVO {
        public int width;
        public int height;
        public FileHandle fileHandle;
    }
}
