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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.commons.O2DDialog;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.view.ui.widget.InputFileWidget;

/**
 * Created by sargis on 7/31/14.
 */
public class ExportSettingsDialog extends O2DDialog {
    public static final String SAVE_SETTINGS_BTN_CLICKED = "com.uwsoft.editor.view.ui.dialog.ExportSettingsDialog" + ".SAVE_SETTINGS_BTN_CLICKED";
    public static final String SAVE_SETTINGS_AND_EXPORT_BTN_CLICKED = "com.uwsoft.editor.view.ui.dialog.ExportSettingsDialog" + ".SAVE_SETTINGS_AND_EXPORT_BTN_CLICKED";
    private final InputFileWidget exportSettingsInputFileWidget;
    private VisCheckBox duplicateCheckBox;
    private VisSelectBox<Integer> widthSelectBox;
    private VisSelectBox<Integer> heightSelectBox;

    public ExportSettingsDialog() {
        super("Export settings");
        addCloseButton();
        VisTable mainTable = new VisTable();
        mainTable.padTop(6).padRight(6).padBottom(22);
//        mainTable.debug();
        mainTable.add("Assets folder:").right().padRight(5);
        exportSettingsInputFileWidget = new InputFileWidget(FileChooser.Mode.OPEN, FileChooser.SelectionMode.DIRECTORIES, false);
        exportSettingsInputFileWidget.setTextFieldWidth(250);
        mainTable.add(exportSettingsInputFileWidget);
        mainTable.row().padTop(10);
        mainTable.add("Atlas Max Size:").right().top().padRight(5);
        mainTable.add(getDimensionsTable()).left();
        mainTable.row().padTop(10);
        duplicateCheckBox = new VisCheckBox("Duplicate edge pixels in atlas");
        mainTable.add(duplicateCheckBox).colspan(2);
        mainTable.row().padTop(23);
        VisTable buttonsTable = new VisTable();
        VisTextButton saveSettingsBtn = new VisTextButton("Save Settings");
        saveSettingsBtn.addListener(new ExportSettingsDialogBtnListener(SAVE_SETTINGS_BTN_CLICKED));
        VisTextButton saveSettingsAndExportBtn = new VisTextButton("Save Settings and Export");
        saveSettingsAndExportBtn.addListener(new ExportSettingsDialogBtnListener(SAVE_SETTINGS_AND_EXPORT_BTN_CLICKED));
        buttonsTable.add(saveSettingsBtn).padRight(17).height(24).width(100);
        buttonsTable.add(saveSettingsAndExportBtn).height(24).width(170);
        mainTable.add(buttonsTable).colspan(2);
        add(mainTable);
    }

    private Table getDimensionsTable() {
        Integer[] data = {512, 1024, 2048, 4096};
        VisTable dimensionsTable = new VisTable();
        widthSelectBox = new VisSelectBox<>("white");
        widthSelectBox.setItems(data);
        widthSelectBox.setSelectedIndex(2);
        dimensionsTable.add(new VisLabel("Width:")).left().padRight(3);
        dimensionsTable.add(widthSelectBox).width(85).height(21).padRight(3);
        dimensionsTable.row().padTop(10);
        heightSelectBox = new VisSelectBox<>("white");
        heightSelectBox.setItems(data);
        heightSelectBox.setSelectedIndex(2);
        dimensionsTable.add(new VisLabel("Height:")).left().padRight(3);
        dimensionsTable.add(heightSelectBox).width(85).height(21).left();
        return dimensionsTable;
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
            exportSettingsVO.width = widthSelectBox.getSelected();
            exportSettingsVO.height = heightSelectBox.getSelected();
            exportSettingsVO.duplicate = duplicateCheckBox.isChecked();
            exportSettingsVO.fileHandle = exportSettingsInputFileWidget.getValue();
            facade.sendNotification(command, exportSettingsVO);
        }
    }

    class ExportSettingsVO {
        public int width;
        public int height;
        public FileHandle fileHandle;
        public boolean duplicate;
    }
}