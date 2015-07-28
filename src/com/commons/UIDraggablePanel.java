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

package com.commons;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;

/**
 * Created by azakhary on 5/12/2015.
 */
public class UIDraggablePanel extends O2DDialog {
    public UIDraggablePanel(String title) {
        super(title);
        setMovable(true);
        setModal(false);
        setStyle(VisUI.getSkin().get("box", WindowStyle.class));
        getTitleLabel().setAlignment(Align.left);
    }

    @Override
    public void addCloseButton() {
        VisImageButton closeButton = new VisImageButton("close-panel");
        this.getTitleTable().add(closeButton).padBottom(2);
        closeButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                UIDraggablePanel.this.close();
            }
        });
        if (this.getTitleTable().getChildren().size == 2) {
            this.getTitleTable().getCell(this.getTitleLabel()).padLeft(closeButton.getWidth() * 2.0F);
        }
    }

    public void invalidateHeight() {
        float heightOld = getHeight();
        pack();
        float heightDiff = heightOld - getHeight();
        setPosition(getX(), getY() + heightDiff);
    }

}
