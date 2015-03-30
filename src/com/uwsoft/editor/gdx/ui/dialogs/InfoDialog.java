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
