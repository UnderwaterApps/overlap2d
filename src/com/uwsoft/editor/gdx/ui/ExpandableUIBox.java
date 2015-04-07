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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;

/**
 * Created by sargis on 9/15/14.
 */
public abstract class ExpandableUIBox extends UIBox {
    protected final float expandedHeight;
    private final Overlap2DFacade facade;
    private final TextureManager textureManager;
    private boolean isExpanded;
    private Image expandImg;

    public ExpandableUIBox(UIStage s, float width, float height) {
        super(s, width, height);
        expandedHeight = height;
        isExpanded = true;
        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(TextureManager.NAME);
    }

    @Override
    public void initPanel() {
        super.initPanel();
        expandImg = new Image(textureManager.getEditorAsset("expand16"));
        expandImg.setOrigin(expandImg.getWidth() / 2, expandImg.getHeight() / 2);
        expandImg.setScale(.7f);
        expandImg.setX(getWidth() - expandImg.getWidth() - 2);
        expandImg.setY(getHeight() - expandImg.getHeight() - 1);
        expandImg.setRotation(isExpanded ? 180 : 0);
        backLayer.addActor(expandImg);
        expandImg.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                backLayer.clear();
                if (isExpanded) {
                    collapse();
                } else {
                    expand();
                }
                isExpanded = !isExpanded;
                initPanel();
                stage.uiMainTable.rightTable.invalidate();
                stage.uiMainTable.rightTable.layout();
                stage.uiMainTable.leftTable.invalidate();
                stage.uiMainTable.leftTable.layout();
                stage.uiMainTable.layout();
            }
        });
    }

    protected abstract void expand();

    protected abstract void collapse();
}
