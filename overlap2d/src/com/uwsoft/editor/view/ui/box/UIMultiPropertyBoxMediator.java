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

package com.uwsoft.editor.view.ui.box;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.commons.MsgAPI;
import com.puremvc.patterns.mediator.Mediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.AddComponentToItemCommand;
import com.uwsoft.editor.controller.commands.RemoveComponentFromItemCommand;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.components.ShaderComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.stage.SandboxMediator;
import com.uwsoft.editor.view.stage.tools.TextTool;
import com.uwsoft.editor.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.view.ui.properties.UIAbstractPropertiesMediator;
import com.uwsoft.editor.view.ui.properties.panels.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIMultiPropertyBoxMediator extends PanelMediator<UIMultiPropertyBox> {

    private static final String TAG = UIMultiPropertyBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private HashMap<String, ArrayList<String>> classToMediatorMap;

    private ArrayList<UIAbstractPropertiesMediator> currentRegisteredPropertyBoxes = new ArrayList<>();

    public UIMultiPropertyBoxMediator() {
        super(NAME, new UIMultiPropertyBox());

        // TODO: shouldn't this be initialized by default?
        facade = Overlap2DFacade.getInstance();

        initMap();
    }

    private void initMap() {
        classToMediatorMap = new HashMap<>();

        classToMediatorMap.put(Entity.class.getName(), new ArrayList<>());
        classToMediatorMap.get(Entity.class.getName()).add(UIBasicItemPropertiesMediator.NAME);

        classToMediatorMap.put(SceneVO.class.getName(), new ArrayList<>());
        classToMediatorMap.get(SceneVO.class.getName()).add(UIScenePropertiesMediator.NAME);

        classToMediatorMap.put(TextTool.class.getName(), new ArrayList<>());
        classToMediatorMap.get(TextTool.class.getName()).add(UITextToolPropertiesMediator.NAME);
    }

    private void initEntityProperties( ArrayList<String> mediatorNames, Entity entity) {

        int entityType = EntityUtils.getType(entity);

        if(entityType == EntityFactory.IMAGE_TYPE) {
            mediatorNames.add(UIImageItemPropertiesMediator.NAME);
        }

        if(entityType == EntityFactory.COMPOSITE_TYPE) {
            mediatorNames.add(UICompositeItemPropertiesMediator.NAME);
        }
        if(entityType == EntityFactory.LABEL_TYPE) {
            mediatorNames.add(UILabelItemPropertiesMediator.NAME);
        }
        if(entityType == EntityFactory.SPRITE_TYPE) {
            mediatorNames.add(UISpriteAnimationItemPropertiesMediator.NAME);
        }
        if(entityType == EntityFactory.SPINE_TYPE) {
            mediatorNames.add(UISpineAnimationItemPropertiesMediator.NAME);
        }
        if(entityType == EntityFactory.LIGHT_TYPE) {
            mediatorNames.add(UILightItemPropertiesMediator.NAME);
        }

        // optional panels based on components
        PolygonComponent polygonComponent = ComponentRetriever.get(entity, PolygonComponent.class);
        PhysicsBodyComponent physicsComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);
        ShaderComponent shaderComponent = ComponentRetriever.get(entity, ShaderComponent.class);
        if(polygonComponent != null) {
            mediatorNames.add(UIPolygonComponentPropertiesMediator.NAME);
        }
        if(physicsComponent != null) {
            mediatorNames.add(UIPhysicsPropertiesMediator.NAME);
        }
        if(shaderComponent != null) {
            mediatorNames.add(UIShaderPropertiesMediator.NAME);
        }
    }

    @Override
    public String[] listNotificationInterests() {
        String[] parentNotifications = super.listNotificationInterests();
        return Stream.of(parentNotifications, new String[]{
                MsgAPI.SCENE_LOADED,
                MsgAPI.EMPTY_SPACE_CLICKED,
                MsgAPI.ITEM_DATA_UPDATED,
                MsgAPI.ITEM_SELECTION_CHANGED,
                MsgAPI.DELETE_ITEMS_COMMAND_DONE,
                SandboxMediator.SANDBOX_TOOL_CHANGED,
                AddComponentToItemCommand.DONE,
                RemoveComponentFromItemCommand.DONE
        }).flatMap(Stream::of).toArray(String[]::new);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case MsgAPI.SCENE_LOADED:
                initAllPropertyBoxes(null);
                break;
            case MsgAPI.EMPTY_SPACE_CLICKED:
                initAllPropertyBoxes(null);
                break;
            case MsgAPI.ITEM_SELECTION_CHANGED:
                Set<Entity> selection = notification.getBody();
                if(selection.size() == 1) {
                    initAllPropertyBoxes(selection.iterator().next());
                }
                break;
            case RemoveComponentFromItemCommand.DONE:
                initAllPropertyBoxes(notification.getBody());
                break;
            case AddComponentToItemCommand.DONE:
                initAllPropertyBoxes(notification.getBody());
                break;
            case SandboxMediator.SANDBOX_TOOL_CHANGED:
                initAllPropertyBoxes(notification.getBody());
                break;
            case MsgAPI.DELETE_ITEMS_COMMAND_DONE:
                initAllPropertyBoxes(null);
                break;
            default:
                break;
        }
    }

    private void initAllPropertyBoxes(Object observable) {

        if(observable == null) {
            // if there is nothing to observe, always observe current scene
            observable = Sandbox.getInstance().sceneControl.getCurrentSceneVO();
        }
        
        String mapName = observable.getClass().getName();

        if(classToMediatorMap.get(mapName) == null) return;

        // retrieve a list of property panels to show
        ArrayList<String> mediatorNames = new ArrayList<>(classToMediatorMap.get(mapName));

        // TODO: this is not uber cool, gotta think a new way to make this class know nothing about entities
        if(observable instanceof Entity){
            initEntityProperties(mediatorNames, (Entity) observable);
        }

        if(mediatorNames == null) return;

        //clear all current enabled panels
        viewComponent.clearAll();

        //unregister all current mediators
        for(UIAbstractPropertiesMediator mediator: currentRegisteredPropertyBoxes) {
            facade.removeMediator(mediator.getMediatorName());
        }
        currentRegisteredPropertyBoxes.clear();

        for (String mediatorName : mediatorNames) {
            try {
                facade.registerMediator((Mediator) ClassReflection.newInstance(ClassReflection.forName(mediatorName)));

                UIAbstractPropertiesMediator<Object, UIAbstractProperties> propertyBoxMediator = facade.retrieveMediator(mediatorName);
                currentRegisteredPropertyBoxes.add(propertyBoxMediator);
                propertyBoxMediator.setItem(observable);
                viewComponent.addPropertyBox(propertyBoxMediator.getViewComponent());
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
        }
    }
}
