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

package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;

import java.util.ArrayList;

public class CustomTabBar extends Group {

    private final Overlap2DFacade facade;
    private final EditorTextureManager textureManager;
    protected UIStage s;

    private ArrayList<String> tabs = new ArrayList<String>();

    private int selectedTabIndex = 0;
    private TabBarEvent tabEventListener;

    public CustomTabBar(UIStage s) {
        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(EditorTextureManager.NAME);
        this.s = s;
    }

    public void addTab(String tabName) {
        tabs.add(tabName);
    }

    public void initView() {
        clear();
        float currPos = 0;
        for (int i = 0; i < tabs.size(); i++) {
            boolean isSelected = false;
            if (i == selectedTabIndex) isSelected = true;
            String imgName = "tab";
            if (isSelected) {
                imgName = "tabS";
            }
            Image img = new Image(textureManager.getEditorAsset(imgName));
            img.setX(currPos);
            img.setY(0);

            Label lbl = new Label(tabs.get(i), textureManager.editorSkin);
            lbl.setX(currPos + 6);
            lbl.setY(2);
            lbl.setTouchable(Touchable.disabled);

            img.setScaleX(lbl.getWidth() + 12);
            currPos += lbl.getWidth() + 12;

            addActor(img);
            addActor(lbl);

            Image sep = new Image(textureManager.getEditorAsset("tabSep"));
            sep.setX(img.getX() + img.getScaleX());
            addActor(sep);
            currPos += 1;

            if (isSelected) {
                lbl.setColor(1, 1, 1, 1);
            } else {
                lbl.setColor(1, 1, 1, 0.65f);
            }

            final int currIndex = i;

            img.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    selectedTabIndex = currIndex;
                    initView();
                    if (tabEventListener != null) {
                        tabEventListener.tabOpened(currIndex);
                    }
                }
            });

            setHeight(img.getHeight());
        }

        float currWidth = currPos;
        if (getWidth() > currWidth) {
            float diff = getWidth() - currWidth;
            Image rest = new Image(textureManager.getEditorAsset("tab"));
            rest.setX(currPos);
            rest.setScaleX(diff);
            addActor(rest);
        }
    }

    public TabBarEvent getTabEventListener() {
        return tabEventListener;
    }

    public void setTabEventListener(TabBarEvent tabEventListener) {
        this.tabEventListener = tabEventListener;
    }

    public interface TabBarEvent {
        public void tabOpened(int index);
    }
}
