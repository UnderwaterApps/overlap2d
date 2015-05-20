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

package com.uwsoft.editor.mvc.view;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.mvc.view.ui.followers.BasicFollower;
import com.uwsoft.editor.mvc.view.ui.followers.LightFollower;
import com.uwsoft.editor.mvc.view.ui.followers.SelectionFollower;

import java.util.HashMap;

/**
 * Created by azakhary on 5/20/2015.
 */
public class MidUIMediator extends SimpleMediator<MidUI> {
    private static final String TAG = MidUIMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private HashMap<Entity, BasicFollower> followers = new HashMap<>();

    public MidUIMediator() {
        super(NAME, new MidUI());
    }

    @Override
    public void onRegister() {

    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SceneDataManager.SCENE_LOADED,
                Overlap2D.ITEM_DATA_UPDATED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:
                //createFollowersForAllItems();
                break;
            case Overlap2D.ITEM_DATA_UPDATED:
                BasicFollower follower = followers.get(notification.getBody());
                if(follower != null) {
                    follower.update();
                }
                break;
        }
    }

    private void createFollowersForAllItems() {
        Sandbox sandbox = Sandbox.getInstance();
        ImmutableArray<Entity> entities = sandbox.getEngine().getEntities();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            createFollower(entity);
        }
    }

    private void removeAllfollowers() {
        followers.values().forEach(com.uwsoft.editor.mvc.view.ui.followers.BasicFollower::remove);
        followers.clear();
    }

    private void createFollower(Entity entity) {
        BasicFollower follower = new SelectionFollower(entity);
        viewComponent.addActor(follower);
        followers.put(entity, follower);
    }

    private void removeFollower(Entity entity) {
        followers.get(entity).remove();
        followers.remove(entity);
    }
}
