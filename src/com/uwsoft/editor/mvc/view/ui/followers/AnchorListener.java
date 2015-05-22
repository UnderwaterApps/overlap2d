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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by azakhary on 5/22/2015.
 */
public class AnchorListener extends ClickListener {

    private int anchorId;
    protected FollowerTransformationListener listener = new EmptyTransformationListener();

    public AnchorListener(FollowerTransformationListener listener, int anchorId) {
        this.listener = listener;
        this.anchorId = anchorId;
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);
        listener.anchorDown(anchorId, event.getStageX(), event.getStageY());
        event.stop();
        return true;
    }
    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        listener.anchorDragged(anchorId, event.getStageX(), event.getStageY());
    }
    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        listener.anchorUp(anchorId, event.getStageX(), event.getStageY());
    }
    @Override
    public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        listener.mouseEnter(anchorId, event.getStageX(), event.getStageY());
    }
    @Override
    public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        listener.mouseExit(anchorId, event.getStageX(), event.getStageY());
    }
}
