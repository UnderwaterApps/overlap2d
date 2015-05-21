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

package com.uwsoft.editor.mvc.view.ui.followers;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;

/**
 * Created by azakhary on 5/20/2015.
 */
public class NormalSelectionFollower extends BasicFollower {

    PixelRect pixelRect;

    public NormalSelectionFollower(Entity entity) {
        super(entity);
    }

    @Override
    public void create() {
        //TODO: create all the actors
        pixelRect = new PixelRect();
        pixelRect.setOpacity(0.4f);
        addActor(pixelRect);
        hide();
    }

    @Override
    public void update() {
        super.update();

        //Update actor dimensions and positions, according to this actor width and height
        pixelRect.setWidth(getWidth());
        pixelRect.setHeight(getHeight());
    }

    @Override
    public void setMode(FollowerMode mode) {

    }
}
