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


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.gdx.stage.UIStage;

public class ConfirmDialog extends CompositeDialog {

	public interface ConfirmDialogListener extends DialogListener {
        void onConfirm();
        void onCancel();
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

    public void setListener(ConfirmDialogListener listener) {
        this.listener = listener;
    }
}
