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

package com.uwsoft.editor.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by sargis on 4/29/15.
 */
public class UICollapsibleBox extends VisWindow {
    private final VisImageButton collapsibleButton;
    private final VisTable mainTable;
    protected CollapsibleWidget collapsibleWidget;

    public UICollapsibleBox(String title, int width) {
        super(title);
        mainTable = new VisTable();
        mainTable.top();
        setStyle(VisUI.getSkin().get("box", WindowStyle.class));
        collapsibleButton = new VisImageButton("close-box");
        collapsibleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                collapsibleWidget.setCollapsed(!collapsibleWidget.isCollapsed());
            }
        });
        getTitleLabel().setAlignment(Align.left);
        getTitleTable().add(collapsibleButton).right();
        add(mainTable).width(width).padBottom(4);

        // by default all collapsible panels are not visible
        setVisible(false);
    }

    protected void createCollapsibleWidget(Table table) {
        collapsibleWidget = new CollapsibleWidget(table);
        mainTable.add(collapsibleWidget).expand().top();
    }
}
