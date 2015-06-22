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

package com.uwsoft.editor.mvc.view.ui.box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.puremvc.patterns.mediator.Mediator;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.mvc.view.stage.SandboxMediator;
import com.uwsoft.editor.mvc.view.stage.tools.TextTool;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractPropertiesMediator;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UIBasicItemPropertiesMediator;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UICompositeItemPropertiesMediator;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UILabelItemPropertiesMediator;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UILightItemPropertiesMediator;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UIScenePropertiesMediator;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UISpineAnimationItemPropertiesMediator;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UISpriteAnimationItemPropertiesMediator;
import com.uwsoft.editor.mvc.view.ui.properties.panels.UITextToolPropertiesMediator;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.legacy.data.SceneVO;
import com.uwsoft.editor.utils.runtime.EntityUtils;

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

        Entity asd = new Entity();
        
        classToMediatorMap.put("Entity"+EntityFactory.IMAGE_TYPE, new ArrayList<>());
        classToMediatorMap.get("Entity"+EntityFactory.IMAGE_TYPE).add(UIBasicItemPropertiesMediator.NAME);

        classToMediatorMap.put("Entity"+EntityFactory.NINE_PATCH, new ArrayList<>());
        classToMediatorMap.get("Entity"+EntityFactory.NINE_PATCH).add(UIBasicItemPropertiesMediator.NAME);

        classToMediatorMap.put("Entity"+EntityFactory.LABEL_TYPE, new ArrayList<>());
        classToMediatorMap.get("Entity"+EntityFactory.LABEL_TYPE).add(UIBasicItemPropertiesMediator.NAME);
        classToMediatorMap.get("Entity"+EntityFactory.LABEL_TYPE).add(UILabelItemPropertiesMediator.NAME);

        classToMediatorMap.put("Entity"+EntityFactory.SPRITE_TYPE, new ArrayList<>());
        classToMediatorMap.get("Entity"+EntityFactory.SPRITE_TYPE).add(UIBasicItemPropertiesMediator.NAME);
        classToMediatorMap.get("Entity"+EntityFactory.SPRITE_TYPE).add(UISpriteAnimationItemPropertiesMediator.NAME);

        classToMediatorMap.put("Entity"+EntityFactory.SPINE_TYPE, new ArrayList<>());
        classToMediatorMap.get("Entity"+EntityFactory.SPINE_TYPE).add(UIBasicItemPropertiesMediator.NAME);
        classToMediatorMap.get("Entity"+EntityFactory.SPINE_TYPE).add(UISpineAnimationItemPropertiesMediator.NAME);

        classToMediatorMap.put("Entity"+EntityFactory.SPRITER_TYPE, new ArrayList<>());
        classToMediatorMap.get("Entity"+EntityFactory.SPRITER_TYPE).add(UIBasicItemPropertiesMediator.NAME);

        classToMediatorMap.put("Entity"+EntityFactory.COMPOSITE_TYPE, new ArrayList<>());
        classToMediatorMap.get("Entity"+EntityFactory.COMPOSITE_TYPE).add(UIBasicItemPropertiesMediator.NAME);
        classToMediatorMap.get("Entity"+EntityFactory.COMPOSITE_TYPE).add(UICompositeItemPropertiesMediator.NAME);

        classToMediatorMap.put("Entity"+EntityFactory.PARTICLE_TYPE, new ArrayList<>());
        classToMediatorMap.get("Entity"+EntityFactory.PARTICLE_TYPE).add(UIBasicItemPropertiesMediator.NAME);

        classToMediatorMap.put("Entity"+EntityFactory.LIGHT_TYPE, new ArrayList<>());
        classToMediatorMap.get("Entity"+EntityFactory.LIGHT_TYPE).add(UIBasicItemPropertiesMediator.NAME);
        classToMediatorMap.get("Entity"+EntityFactory.LIGHT_TYPE).add(UILightItemPropertiesMediator.NAME);

        classToMediatorMap.put(SceneVO.class.getName(), new ArrayList<>());
        classToMediatorMap.get(SceneVO.class.getName()).add(UIScenePropertiesMediator.NAME);

        classToMediatorMap.put(TextTool.class.getName(), new ArrayList<>());
        classToMediatorMap.get(TextTool.class.getName()).add(UITextToolPropertiesMediator.NAME);
    }

    @Override
    public String[] listNotificationInterests() {
        String[] parentNotifications = super.listNotificationInterests();
        return Stream.of(parentNotifications, new String[]{
                SceneDataManager.SCENE_LOADED,
                Overlap2D.EMPTY_SPACE_CLICKED,
                Overlap2D.ITEM_DATA_UPDATED,
                Overlap2D.ITEM_SELECTION_CHANGED,
                SandboxMediator.SANDBOX_TOOL_CHANGED
        }).flatMap(Stream::of).toArray(String[]::new);
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:
                initAllPropertyBoxes(null);
                break;
            case Overlap2D.EMPTY_SPACE_CLICKED:
                initAllPropertyBoxes(null);
                break;
            case Overlap2D.ITEM_SELECTION_CHANGED:
                Set<Entity> selection = notification.getBody();
                if(selection.size() == 1) {
                    initAllPropertyBoxes(selection.iterator().next());
                }
                break;
            case SandboxMediator.SANDBOX_TOOL_CHANGED:
                initAllPropertyBoxes(notification.getBody());
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
        
        //TODO this condition must be changes later it's a temporary solution for {@link Entity}
        if(observable instanceof Entity){
        	mapName = "Entity" + EntityUtils.getType(((Entity) observable));
        }

        // retrieve a list of property panels to show
        ArrayList<String> mediatorNames = classToMediatorMap.get(mapName);

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
