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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.uwsoft.editor.gdx.ui.UIBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.EditorTextureManager;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;

public class SimpleDialog extends UIBox {

    static private final int MOVE = 1 << 5;
    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    public SimpleDialog instance = this;
    boolean isMovable = true, isModal, isResizable;
    int resizeBorder = 8;
    boolean dragging;
    boolean keepWithinStage = true;

    private Label titleLabel;

    public SimpleDialog(UIStage s, float width, float height) {
        super(s, width, height);
        topImgName = "tab";
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        this.initPanel();
    }


    @Override
    public void initPanel() {
        super.initPanel();


        ButtonStyle stl = new ButtonStyle();
        EditorTextureManager textureManager = facade.retrieveProxy(EditorTextureManager.NAME);
        stl.down = new TextureRegionDrawable(textureManager.getEditorAsset("closeBtnClicked"));
        stl.up = new TextureRegionDrawable(textureManager.getEditorAsset("closeBtn"));
        stl.over = new TextureRegionDrawable(textureManager.getEditorAsset("closeHoverBtn"));

        Button closeBtn = new Button(stl);
        addActor(closeBtn);

        closeBtn.setX(getWidth() - closeBtn.getWidth() - 4);
        closeBtn.setY(getHeight() - closeBtn.getHeight() - 4);

        closeBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                instance.remove();
            }
        });

        titleLabel = new Label("Default Title", textureManager.editorSkin);
        titleLabel.setX(6);
        titleLabel.setY(topImg.getY() + 2);
        titleLabel.setColor(new Color(1, 1, 1, 0.6f));
        addActor(titleLabel);

        initDragDrop();
    }

    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    public void initDragDrop() {
        addListener(new InputListener() {
            int edge;
            float startX,
                    startY,
                    lastX,
                    lastY;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == 0) {
                    float width = getWidth(), height = getHeight();
                    edge = 0;
                    if (isMovable && edge == 0 && y <= height && y >= height - topImg.getY() && x >= 0 && x <= width)
                        edge = MOVE;
                    dragging = edge != 0;
                    startX = x;
                    startY = y;
                    lastX = x;
                    lastY = y;
                }
                return (edge != 0 || isModal) && y > topImg.getY();
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dragging = false;
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!dragging) return;
                float width = getWidth(), height = getHeight();
                float windowX = getX(), windowY = getY();

                float minWidth = getWidth();
                float minHeight = getHeight();
                Stage stage = getStage();
                boolean clampPosition = keepWithinStage && getParent() == stage.getRoot();

                if ((edge & MOVE) != 0) {
                    float amountX = x - startX, amountY = y - startY;
                    windowX += amountX;
                    windowY += amountY;
                }
                if ((edge & Align.left) != 0) {
                    float amountX = x - startX;
                    if (width - amountX < minWidth) amountX = -(minWidth - width);
                    if (clampPosition && windowX + amountX < 0) amountX = -windowX;
                    width -= amountX;
                    windowX += amountX;
                }
                if ((edge & Align.bottom) != 0) {
                    float amountY = y - startY;
                    if (height - amountY < minHeight) amountY = -(minHeight - height);
                    if (clampPosition && windowY + amountY < 0) amountY = -windowY;
                    height -= amountY;
                    windowY += amountY;
                }
                if ((edge & Align.right) != 0) {
                    float amountX = x - lastX;
                    if (width + amountX < minWidth) amountX = minWidth - width;
                    if (clampPosition && windowX + width + amountX > stage.getWidth())
                        amountX = stage.getWidth() - windowX - width;
                    width += amountX;
                }
                if ((edge & Align.top) != 0) {
                    float amountY = y - lastY;
                    if (height + amountY < minHeight) amountY = minHeight - height;
                    if (clampPosition && windowY + height + amountY > stage.getHeight())
                        amountY = stage.getHeight() - windowY - height;
                    height += amountY;
                }
                lastX = x;
                lastY = y;
                setBounds(Math.round(windowX), Math.round(windowY), Math.round(width), Math.round(height));
            }
        });
    }
}
