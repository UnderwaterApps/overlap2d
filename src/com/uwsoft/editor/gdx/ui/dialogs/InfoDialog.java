package com.uwsoft.editor.gdx.ui.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.renderer.actor.LabelItem;

/**
 * Created by azakhary on 8/1/2014.
 */
public class InfoDialog extends CompositeDialog {

    private LabelItem desc;

    public InfoDialog(UIStage s) {
        super(s, "infoDlg", 270, 90);

        setTitle("Info");

        desc = ui.getLabelById("text");

        desc.setWidth(260);
        desc.setWrap(true);
        //desc.setAlignment(Align.left);

        ui.getTextButtonById("okBtn").addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                remove();
            }
        });
    }

    public void setDescription(String text) {
        desc.setText(text);
    }
}
