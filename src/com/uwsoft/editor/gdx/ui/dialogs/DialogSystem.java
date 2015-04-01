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

package com.uwsoft.editor.gdx.ui.dialogs;

import com.uwsoft.editor.gdx.stage.UIStage;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class DialogSystem {

    private UIStage stage;

    public DialogSystem(UIStage stage) {

        this.stage = stage;
    }

    public DlgNewProject createNewProjectDialog() {
        DlgNewProject dlg = new DlgNewProject(stage);
        return (DlgNewProject)initiateDialog(dlg);
    }

    public DlgImport showImportDialog() {
        DlgImport dlg = new DlgImport(stage);
        return (DlgImport)initiateDialog(dlg);
    }

    public CreateNewResolutionDialog showCreateNewResolutionDialog() {
        CreateNewResolutionDialog dlg = new CreateNewResolutionDialog(stage);
        return (CreateNewResolutionDialog)initiateDialog(dlg);
    }

    public ConfirmDialog showConfirmDialog() {
        ConfirmDialog dlg = new ConfirmDialog(stage);
        return (ConfirmDialog)initiateDialog(dlg);
    }

    public InputDialog showInputDialog() {
        InputDialog dlg = new InputDialog(stage);
        return (InputDialog)initiateDialog(dlg);
    }


    public InfoDialog showInfoDialog(String desc) {
        InfoDialog dlg = new InfoDialog(stage);
        dlg.setDescription(desc);

        return (InfoDialog)initiateDialog(dlg);
    }
    public InfoDialog showInfoDialogNavigateBack(String desc) {
        InfoDialog dlg = new InfoDialog(stage);
        dlg.setDescription(desc);

        return (InfoDialog)initiateDialog(dlg);
    }

    public DlgExport showExportDialog() {
        DlgExport dlg = new DlgExport(stage);

        return (DlgExport)initiateDialog(dlg);
    }

    public CompositeDialog initiateDialog(CompositeDialog dialog) {
        stage.addActor(dialog);
        dialog.show();
        return dialog;
    }
}
