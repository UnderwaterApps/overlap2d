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

package com.uwsoft.editor.view.ui.properties;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * Created by azakhary on 4/15/2015.
 */
public abstract class UIItemCollapsibleProperties extends UIItemProperties {
    protected final String title;
    protected VisTable mainTable;
    protected VisTable header;
    protected CollapsibleWidget collapsibleWidget;

    public UIItemCollapsibleProperties(String title) {
        this.title = title;
        mainTable = new VisTable();
        addSeparator().padTop(9).padBottom(6);
        row();
        add(crateHeaderTable()).expandX().fillX().padBottom(7);
        createCollapsibleWidget();
    }

    public Table crateHeaderTable() {
        header = new VisTable();
        header.setBackground(VisUI.getSkin().getDrawable("expandable-properties-active-bg"));
        header.add(createLabel(title)).right().expandX().padRight(6);
        VisImageButton button = new VisImageButton("expandable-properties-button");
        header.add(button).padRight(3);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                collapse();
            }
        });
        return header;
    }

    public void collapse() {
        collapsibleWidget.setCollapsed(!collapsibleWidget.isCollapsed());
        header.setBackground(VisUI.getSkin().getDrawable("expandable-properties-" + (collapsibleWidget.isCollapsed() ? "inactive" : "active") + "-bg"));
    }

    private void createCollapsibleWidget() {
        mainTable = new VisTable();
        collapsibleWidget = new CollapsibleWidget(mainTable);
        row();
        add(collapsibleWidget).expand();
    }
}
