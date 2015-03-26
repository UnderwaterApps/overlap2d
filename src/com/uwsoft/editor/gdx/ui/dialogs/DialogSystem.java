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
