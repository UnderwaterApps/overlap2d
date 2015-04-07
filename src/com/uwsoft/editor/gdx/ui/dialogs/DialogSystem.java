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

import com.uwsoft.editor.mvc.view.stage.UIStage;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class DialogSystem {

    private UIStage stage;

    public DialogSystem(UIStage stage) {
        this.stage = stage;
    }

    public CreateNewResolutionDialog showCreateNewResolutionDialog() {
        CreateNewResolutionDialog dlg = new CreateNewResolutionDialog(stage);
        return (CreateNewResolutionDialog) initiateDialog(dlg);
    }
//    public ExportSettingsDialog showExportDialog() {
//        ExportSettingsDialog dlg = new ExportSettingsDialog(stage);
//
//        return (ExportSettingsDialog) initiateDialog(dlg);
//    }

    public CompositeDialog initiateDialog(CompositeDialog dialog) {
        stage.addActor(dialog);
        dialog.show();
        return dialog;
    }
}
