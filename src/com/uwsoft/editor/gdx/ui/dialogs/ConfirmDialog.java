package com.uwsoft.editor.gdx.ui.dialogs;


import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.gdx.stage.UIStage;

public class ConfirmDialog extends CompositeDialog {

	public interface ConfirmDialogListener extends DialogListener {
        public void onConfirm();
        public void onCancel();
    }

    private ConfirmDialogListener listener;
	
	public ConfirmDialog(UIStage s) {
		super(s, "yesNoDialog", 250, 100);

		setTitle("Are you sure?");
		
		ui.getTextButtonById("okBtn").addListener(new ClickListener() {
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(listener != null) {
					listener.onConfirm();
					
					remove();
				}
			}
		});
		
		ui.getTextButtonById("cancelBtn").addListener(new ClickListener() {
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(listener != null) {
					listener.onCancel();
					
					remove();
				}
			}
		});
	}
	
	public void setDescription(String text) {
		Label bodyLabel = ui.getLabelById("dialogBody");
		bodyLabel.setWrap(true);
		bodyLabel.setWidth(getWidth());
		bodyLabel.setText(text);
	}
}
