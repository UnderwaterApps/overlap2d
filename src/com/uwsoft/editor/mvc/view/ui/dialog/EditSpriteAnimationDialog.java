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

package com.uwsoft.editor.mvc.view.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.renderer.actor.SpriteAnimation;
import com.uwsoft.editor.renderer.utils.CustomVariables;
import com.uwsoft.editor.utils.StandardWidgetsFactory;

import java.util.Map;

/**
 * Created by azakhary on 5/12/2015.
 */
public class EditSpriteAnimationDialog extends UIDraggablePanel {
    public static final String PREFIX = "com.uwsoft.editor.mvc.view.ui.dialog.EditSpriteAnimationDialog";
    public static final String ADD_BUTTON_PRESSED = PREFIX + ".ADD_BUTTON_PRESSED";
    public static final String DELETE_BUTTON_PRESSED = PREFIX + ".DELETE_BUTTON_PRESSED";

    private Overlap2DFacade facade;

    private VisTextField nameField;
    private NumberSelector fromFrameField;
    private NumberSelector toFrameField;
    private VisTextButton addButton;

    private VisTable animationsList;

    public EditSpriteAnimationDialog() {
        super("Edit Sprite Animation Ranges");
        addCloseButton();

        facade = Overlap2DFacade.getInstance();

        VisTable mainTable = new VisTable();

        animationsList = new VisTable();

        mainTable.add(animationsList);
        mainTable.row();
        mainTable.add(createNewAnimationTable());
        mainTable.row();

        add(mainTable);

        initListeners();
    }

    private VisTable createNewAnimationTable() {
        VisTable newAnimationTable = new VisTable();

        nameField = StandardWidgetsFactory.createTextField();
        fromFrameField = StandardWidgetsFactory.createNumberSelector(0, 100);
        toFrameField = StandardWidgetsFactory.createNumberSelector(0, 100);
        addButton = new VisTextButton("Add");

        newAnimationTable.add(nameField).width(100);
        newAnimationTable.add(fromFrameField);
        newAnimationTable.add(toFrameField);
        newAnimationTable.add(addButton).padLeft(5);
        newAnimationTable.row();

        return newAnimationTable;
    }

    public void updateView(Map<String, SpriteAnimation.Animation> animations) {
        animationsList.clear();

        for (Map.Entry<String, SpriteAnimation.Animation> entry : animations.entrySet()) {
            VisTable row = new VisTable();
            final String name = entry.getKey();
            SpriteAnimation.Animation animationData = entry.getValue();

            VisImageButton trashBtn = new VisImageButton("trash-button");

            row.add(StandardWidgetsFactory.createLabel(name)).width(100).left();
            row.add(StandardWidgetsFactory.createLabel(animationData.startFrame+"")).width(50).left();
            row.add(StandardWidgetsFactory.createLabel(animationData.endFrame +"")).width(50).left();
            row.add(trashBtn).padLeft(10);
            row.row();

            animationsList.add(row).left().width(254);
            animationsList.row();

            trashBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    facade.sendNotification(DELETE_BUTTON_PRESSED, name);
                }
            });
        }

        invalidateHeight();
    }

    private void initListeners() {
        addButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facade.sendNotification(ADD_BUTTON_PRESSED);
            }
        });
    }

    public String getName() {
        return nameField.getText();
    }

    public int getFrameFrom() {
        return fromFrameField.getValue();
    }

    public int getFrameTo() {
        return toFrameField.getValue();
    }


    public void setName(String name) {
        nameField.setText(name);
    }

    public void setFrameFrom(int from) {
        fromFrameField.setValue(from);
    }

    public void setFrameTo(int to) {
        toFrameField.setValue(to);
    }
}
