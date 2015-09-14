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

package com.uwsoft.editor.view.ui.box.resourcespanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.uwsoft.editor.Overlap2DFacade;

/**
 * Created by sargis on 5/4/15.
 */
public abstract class UIResourcesTab extends Tab {
    private VisTable contentTable;
    private VisScrollPane scrollPane;
    private VisTextField visTextField;
    public String searchString  =   "";
    public static final String SEARCH = "com.uwsoft.editor.view.ui.box.resourcespanel.UIResourcesTab" + ".SEARCH";
    public UIResourcesTab() {
        super(false, false);
        contentTable = new VisTable();
        contentTable.add(createLabel("Search:")).padLeft(1).padBottom(6);
        contentTable.add(createTextField()).padLeft(0).padRight(7).fillX().padBottom(4);
        contentTable.row();
        scrollPane = crateScrollPane();
        contentTable.add(scrollPane).colspan(2).maxHeight(Gdx.graphics.getHeight() * 0.22f).expandX().fillX();
        contentTable.padTop(4);
    }

    protected VisLabel createLabel(String text, int alignment) {
        VisLabel visLabel = new VisLabel(text, alignment);
        visLabel.setStyle(VisUI.getSkin().get("small", Label.LabelStyle.class));
        return visLabel;
    }
    protected VisTextField createTextField() {
        visTextField = new VisTextField();
        final String notification = SEARCH;
        visTextField.setTextFieldListener(new VisTextField.TextFieldListener() {

            @Override
            public void keyTyped(VisTextField textField, char c) {
                searchString    =   textField.getText();
                Overlap2DFacade facade = Overlap2DFacade.getInstance();
                facade.sendNotification(notification);
            }
        });
        return visTextField;
    }

    @Override
    public Table getContentTable() {
        return contentTable;
    }

    protected VisLabel createLabel(String text) {
        return createLabel(text, Align.right);
    }

    protected abstract VisScrollPane crateScrollPane();

}
