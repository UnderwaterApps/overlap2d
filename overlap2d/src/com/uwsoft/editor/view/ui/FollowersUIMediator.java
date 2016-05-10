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

package com.uwsoft.editor.view.ui;

import com.badlogic.ashley.core.Entity;
import com.commons.MsgAPI;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.BaseNotification;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.CompositeCameraChangeCommand;
import com.uwsoft.editor.controller.commands.ConvertToCompositeCommand;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.stage.SandboxMediator;
import com.uwsoft.editor.view.stage.tools.PanTool;
import com.uwsoft.editor.view.ui.followers.BasicFollower;
import com.uwsoft.editor.view.ui.followers.FollowerFactory;
import com.uwsoft.editor.view.ui.followers.NormalSelectionFollower;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by azakhary on 5/20/2015.
 */
public class FollowersUIMediator extends SimpleMediator<FollowersUI> {
    private static final String TAG = FollowersUIMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private HashMap<Entity, BasicFollower> followers = new HashMap<>();

    public FollowersUIMediator() {
        super(NAME, new FollowersUI());
    }

    @Override
    public void onRegister() {
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                MsgAPI.SCENE_LOADED,
                MsgAPI.ITEM_DATA_UPDATED,
                MsgAPI.ITEM_SELECTION_CHANGED,
                MsgAPI.SHOW_SELECTIONS,
                MsgAPI.HIDE_SELECTIONS,
                MsgAPI.NEW_ITEM_ADDED,
                PanTool.SCENE_PANNED,
                MsgAPI.TOOL_SELECTED,
                MsgAPI.ITEM_PROPERTY_DATA_FINISHED_MODIFYING,
                CompositeCameraChangeCommand.DONE,
                MsgAPI.ZOOM_CHANGED,
                ConvertToCompositeCommand.DONE
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case CompositeCameraChangeCommand.DONE:
                createFollowersForAllVisible();
            case MsgAPI.SCENE_LOADED:
                createFollowersForAllVisible();
                break;
            case MsgAPI.NEW_ITEM_ADDED:
                createFollower(notification.getBody());
                break;
            case MsgAPI.ITEM_PROPERTY_DATA_FINISHED_MODIFYING:
                BasicFollower follower = followers.get(notification.getBody());
                if(follower != null) {
                    follower.update();
                }
                break;
            case MsgAPI.ITEM_DATA_UPDATED:
                follower = followers.get(notification.getBody());
                if(follower != null) {
                    follower.update();
                }
                break;
            case PanTool.SCENE_PANNED:
                updateAllFollowers();
                break;
            case MsgAPI.ITEM_SELECTION_CHANGED:
                clearAllSubFollowersExceptNew(notification.getBody());
                setNewSelectionConfiguration(notification.getBody());
                break;
            case MsgAPI.HIDE_SELECTIONS:
                hideAllFollowers(notification.getBody());
                break;
            case MsgAPI.SHOW_SELECTIONS:
                showAllFollowers(notification.getBody());
                break;
            case MsgAPI.TOOL_SELECTED:
                pushNotificationToFollowers(notification);
                break;
            case MsgAPI.ZOOM_CHANGED:
                updateAllFollowers();
                break;
            case ConvertToCompositeCommand.DONE:
                // because entities changed their parent, it's better to re-make all followers
                removeAllfollowers();
                createFollowersForAllVisible();
                break;
        }
    }

    public void pushNotificationToFollowers(Notification notification) {
        for (BasicFollower follower : followers.values()) {
            follower.handleNotification(notification);
        }
    }

    private void clearAllSubFollowersExceptNew(Set<Entity> items) {
        for (BasicFollower follower : followers.values()) {
            if(!items.contains(follower)) {
                if(follower instanceof NormalSelectionFollower) {
                    ((NormalSelectionFollower)follower).clearSubFollowers();
                }
            }
        }
    }

    private void setNewSelectionConfiguration(Set<Entity> items) {
        followers.values().forEach(com.uwsoft.editor.view.ui.followers.BasicFollower::hide);
        for (Entity item : items) {
            followers.get(item).show();
        }
    }

    private void createFollowersForAllVisible() {
        removeAllfollowers();
        Sandbox sandbox = Sandbox.getInstance();
        NodeComponent nodeComponent = ComponentRetriever.get(sandbox.getCurrentViewingEntity(), NodeComponent.class);

        for (Entity entity: nodeComponent.children) {
            createFollower(entity);
        }
    }

    private void removeAllfollowers() {
        followers.values().forEach(com.uwsoft.editor.view.ui.followers.BasicFollower::remove);
        followers.clear();
    }

    private void hideAllFollowers(Set<Entity> items) {
        for (Entity item : items) {
            followers.get(item).hide();
        }
    }

    private void showAllFollowers(Set<Entity> items) {
        for (Entity item : items) {
            followers.get(item).show();
        }
    }

    private void updateAllFollowers() {
        followers.values().forEach(com.uwsoft.editor.view.ui.followers.BasicFollower::update);
    }

    public void createFollower(Entity entity) {
        BasicFollower follower = FollowerFactory.createFollower(entity);
        viewComponent.addActor(follower);
        followers.put(entity, follower);

        SandboxMediator sandboxMediator = facade.retrieveMediator(SandboxMediator.NAME);
        follower.handleNotification(new BaseNotification(MsgAPI.TOOL_SELECTED, sandboxMediator.getCurrentSelectedToolName()));
    }

    public void removeFollower(Entity entity) {
//        if (followers.get(entity) != null) {
            followers.get(entity).remove();
            followers.remove(entity);
//        }
    }

    public void clearAllListeners() {
        followers.values().forEach(com.uwsoft.editor.view.ui.followers.BasicFollower::clearFollowerListener);
    }

    public BasicFollower getFollower(Entity entity) {
        return followers.get(entity);
    }
}
