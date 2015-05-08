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

package com.uwsoft.editor.mvc.view.stage.tools;

import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.renderer.legacy.data.LightVO;

/**
 * Created by azakhary on 4/30/2015.
 */
public class ConeLightTool extends SimpleTool {

    public static final String NAME = "CONE_LIGHT_TOOL";

    @Override
    public boolean stageMouseDown(float x, float y) {
    	//TODO fix and uncomment
        //LayerItemVO layer = Sandbox.getInstance().getItemFactory().getSelectedLayer();
        LightVO vo = new LightVO();
        vo.type = LightVO.LightType.CONE;
        vo.distance = 300;
        vo.coneDegree = 45;
      //TODO fix and uncomment
        //Sandbox.getInstance().getItemFactory().createLight(layer, vo, x, y);
        return false;
    }

    @Override
    public void stageMouseUp(float x, float y) {

    }
}
