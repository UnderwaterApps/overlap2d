package com.uwsoft.editor.view.ui.validator;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.apache.commons.lang3.StringUtils;

public class NewProjectDialogValidator {
    public boolean validate(Stage stage, VisValidatableTextField projectName) {
        if (StringUtils.isEmpty(projectName.getText()) || StringUtils.endsWith(projectName.getText(), " ")) {
            DialogUtils.showErrorDialog(stage, "Please input a valid project name");
            return false;
        }
        return true;
    }
}
