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


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.uwsoft.editor.gdx.sandbox.EditingMode;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.gdx.stage.UIStage;

public class UIToolBox extends UIBox {

    private ButtonGroup btnGroup;

    private final static int maxRows = 2;
    private final static int maxCols = 6;

    private int currRow = 0;
    private int currCol = 0;

    public UIToolBox(UIStage s) {
        super(s, 160, 30 + 20 * maxRows);

        btnGroup = new ButtonGroup();
        btnGroup.setMaxCheckCount(1);
        btnGroup.setMinCheckCount(1);

        Button mainIcon = addButton("mainIcon", "Selection", true);
        Button resizeIcon = addButton("resizeIcon", "Transform", true);
        //
        Button topIcon = addButton("alignIconL", "Top", false);
        topIcon.setTransform(true);
        topIcon.setRotation(-90);
        Button leftIcon = addButton("alignIconL", "Left", false);
        leftIcon.setTransform(true);
        Button bottomIcon = addButton("alignIconL", "Bottom", false);
        bottomIcon.setTransform(true);
        bottomIcon.setRotation(90);
        Button rightIcon = addButton("alignIconL", "Right", false);
        rightIcon.setTransform(true);
        rightIcon.setRotation(180);

        Button hCenterIcon = addButton("alignIconCH", false);
        Button vCenterIcon = addButton("alignIconCV", false);

        //TODO: replace textures
        Button leftEdgeIcon = addButton("AlignEdge", false);
        Button topEdgeIcon = addButton("AlignEdge", false);
        topEdgeIcon.setTransform(true);
		  topEdgeIcon.setRotation(270f);
        Button rightEdgeIcon = addButton("AlignEdge", false);
		  rightEdgeIcon.setTransform(true);
          rightEdgeIcon.setScaleX(-1);
		  //rightEdgeIcon.setRotation(180f);
        Button bottomEdgeIcon = addButton("AlignEdge", false);
		  bottomEdgeIcon.setTransform(true);
          bottomEdgeIcon.setScaleX(-1);
		  bottomEdgeIcon.setRotation(270f);

        topIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelections(Align.top);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        leftIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelections(Align.left);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        bottomIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelections(Align.bottom);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        rightIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelections(Align.right);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        hCenterIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelections(Align.center | Align.left);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        vCenterIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelections(Align.center | Align.bottom);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        leftEdgeIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelectionsAtEdge(Align.left);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        topEdgeIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelectionsAtEdge(Align.top);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        rightEdgeIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelectionsAtEdge(Align.right);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        bottomEdgeIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().getSelector().alignSelectionsAtEdge(Align.bottom);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        mainIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().setCurrentMode(EditingMode.SELECTION);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        resizeIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.getSandbox().setCurrentMode(EditingMode.TRANSFORM);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    
    private Button addButton(String img, boolean isCheckButton) {
        return addButton(img, null, isCheckButton);
    }
    
    private Button addButton(String img, String tooltipText, boolean isCheckButton) {
    	ButtonStyle btnStl = new ButtonStyle();
        btnStl.up = new TextureRegionDrawable(stage.textureManager.getEditorAsset(img));
        btnStl.down = new TextureRegionDrawable(stage.textureManager.getEditorAsset(img + "Checked"));


        Button btn = new Button(btnStl);
        if (tooltipText != null) new Tooltip(btn, tooltipText);

        btn.setX(10 + currCol * (btn.getWidth() + 5));
        btn.setY(getHeight() - (currRow + 1) * (btn.getHeight() + 3) - 17);
        btn.setOrigin(btn.getWidth() / 2, btn.getHeight() / 2);

        currCol++;
        if (currCol >= maxCols) {
            currCol = 0;
            currRow++;
        }

        if (currRow > maxRows) return null;
        if (isCheckButton) {
            btnStl.checked = new TextureRegionDrawable(stage.textureManager.getEditorAsset(img + "Checked"));
            btnGroup.add(btn);
        }
        addActor(btn);

        return btn;
    }

}
