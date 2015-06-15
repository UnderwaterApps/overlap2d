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

import java.util.HashMap;

/**
 * Created by azakhary on 5/20/2015.
 */
public class LabelFollower extends NormalSelectionFollower implements FollowerTransformationListener{

    HashMap<Integer, LabelAnchorListener> anchorListeners;

    public LabelFollower(Entity entity) {
        super(entity);
    }

    @Override
    public void create() {
        super.create();
        anchorListeners = new HashMap<>();
        for(int i = 0; i < miniRects.length; i++) {
            LabelAnchorListener listener = new LabelAnchorListener(this, this, i);
            anchorListeners.put(i, listener);
            miniRects[i].addListener(listener);
        }
        transformGroup.setVisible(true);
        miniRects[ORIGIN].setVisible(false);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void setFollowerListener(FollowerTransformationListener listener) {
        for(int i = 0; i < miniRects.length; i++) {
            anchorListeners.get(i).setListenerTransform(listener);
        }
    }
    public void setMode(SelectionMode mode) {
        super.setMode(mode);
        transformGroup.setVisible(true);
        miniRects[ORIGIN].setVisible(false);
        if(mode == SelectionMode.transform) {
            miniRects[ORIGIN].setVisible(true);
        }
    }

    @Override
    public void anchorDown(NormalSelectionFollower follower, int anchor, float x, float y) {

    }

    @Override
    public void anchorDragged(NormalSelectionFollower follower, int anchor, float x, float y) {
        update();
    }

    @Override
    public void anchorUp(NormalSelectionFollower follower, int anchor, float x, float y) {

    }

    @Override
    public void anchorMouseEnter(NormalSelectionFollower follower, int anchor, float x, float y) {

    }

    @Override
    public void anchorMouseExit(NormalSelectionFollower follower, int anchor, float x, float y) {

    }
}
