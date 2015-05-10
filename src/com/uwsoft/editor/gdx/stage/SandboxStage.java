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

package com.uwsoft.editor.gdx.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.uwsoft.editor.data.TypeConstants;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.SandboxUI;

public class SandboxStage extends BaseStage implements TypeConstants {
    public static SandboxStage instance;

    public UIStage uiStage;
    public SandboxUI ui;
    public Group frontUI;

    public PixelRect selectionRec;
    private FPSLogger fpsLogger;


    public Group mainBox;

    public Sandbox sandbox;
    
    private final static Vector2 temp = new Vector2();

    public SandboxStage() {
        super();

        instance = this;

        physiscStopped = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        
        // update X and Y labels if scene is initialized and not panning
        if(!sandbox.cameraPanOn
      	  && mainBox != null)
        {
      	  temp.set( Gdx.input.getX(), Gdx.input.getY() );
      	  uiStage.uiMainTable.updateXandY(this.screenToStageCoordinates(temp));
        }

    }

    public void setUIStage(UIStage uiStage) {
        this.uiStage = uiStage;
    }


    public void initView() {
        mainBox  = new Group();
        clear();
        getCamera().position.set(0, 0, 0);

        frontUI = new Group();

        ui = new SandboxUI(this);
        addActor(ui);

        selectionRec = new PixelRect(0, 0);
        selectionRec.setFillColor(new Color(1, 1, 1, 0.1f));
        selectionRec.setOpacity(0.0f);
        selectionRec.setTouchable(Touchable.disabled);
        frontUI.addActor(selectionRec);

        addActor(mainBox);

        addActor(frontUI);

    }

    public void setKeyboardFocus() {
        setKeyboardFocus(mainBox);
    }
}
