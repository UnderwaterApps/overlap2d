package com.uwsoft.editor.gdx.ui.dialogs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.gdx.stage.UIStage;

public class InputDialog extends CompositeDialog {

    private InputDialogListener listener = null;

    public InputDialog(UIStage s) {
        super(s, "inputDialog", 270, 90);

        setTitle("Please input required data");

        final TextField inputFld = ui.getTextBoxById("inputFld");
        s.setKeyboardFocus(inputFld);

        ui.getTextButtonById("okBtn").addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (listener != null) {
                    if (inputFld.getText().length() > 0) listener.onConfirm(inputFld.getText());
                    remove();
                }
            }
        });

        inputFld.addListener(new InputListener() {
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    if (listener != null) {
                        if (inputFld.getText().length() > 0) listener.onConfirm(inputFld.getText());
                        remove();
                    }
                }

                return false;
            }
        });
    }

    public void setDescription(String text) {
        Label bodyLabel = ui.getLabelById("descriptionLbl");
        bodyLabel.setWrap(true);
        bodyLabel.setWidth(getWidth());
        bodyLabel.setText(text);
    }

    public void setListener(InputDialogListener listener) {
        this.listener = listener;
    }

    public interface InputDialogListener {
        public void onConfirm(String input);
    }

}
