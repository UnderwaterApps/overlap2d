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

package com.uwsoft.editor.view.stage.tools;

import com.badlogic.ashley.core.Entity;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.AddComponentToItemCommand;
import com.uwsoft.editor.controller.commands.RemoveComponentFromItemCommand;
import com.uwsoft.editor.renderer.components.MeshComponent;
import com.uwsoft.editor.view.MidUIMediator;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.followers.BasicFollower;
import com.uwsoft.editor.view.ui.followers.MeshFollower;
import com.uwsoft.editor.view.ui.followers.NormalSelectionFollower;

import java.util.Set;


/**
 * Created by azakhary on 7/2/2015.
 */
public class MeshTool extends SelectionTool {

    public static final String NAME = "MESH_TOOL";

    private MidUIMediator midUIMediator;

    @Override
    public void initTool() {
        super.initTool();

        midUIMediator = Overlap2DFacade.getInstance().retrieveMediator(MidUIMediator.NAME);

        updateSubFollowerList();
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case AddComponentToItemCommand.DONE:
                updateSubFollowerList();
                break;
            case RemoveComponentFromItemCommand.DONE:
                updateSubFollowerList();
                break;
            case Overlap2D.ITEM_SELECTION_CHANGED:
                updateSubFollowerList();
                break;
        }
    }

    private void updateSubFollowerList() {
        Sandbox sandbox = Sandbox.getInstance();
        Set<Entity> selectedEntities = sandbox.getSelector().getSelectedItems();
        for(Entity entity: selectedEntities) {
            NormalSelectionFollower follower = (NormalSelectionFollower) midUIMediator.getFollower(entity);
            follower.removeSubFollower(MeshFollower.class);
            follower.addSubfollower(new MeshFollower(entity));
        }
    }
}
