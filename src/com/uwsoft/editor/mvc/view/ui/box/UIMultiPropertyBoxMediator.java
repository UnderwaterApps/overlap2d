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

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.puremvc.patterns.mediator.Mediator;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractProperties;
import com.uwsoft.editor.mvc.view.ui.properties.UIAbstractPropertiesMediator;
import com.uwsoft.editor.mvc.view.ui.properties.boxes.*;
import com.uwsoft.editor.renderer.actor.*;
import com.uwsoft.editor.renderer.data.SceneVO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by azakhary on 4/15/2015.
 */
public class UIMultiPropertyBoxMediator extends SimpleMediator<UIMultiPropertyBox> {

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

        classToMediatorMap.put(ImageItem.class.getName(), new ArrayList<>());
        classToMediatorMap.get(ImageItem.class.getName()).add(UIBasicItemPropertiesMediator.NAME);

        classToMediatorMap.put(SpriteAnimation.class.getName(), new ArrayList<>());
        classToMediatorMap.get(SpriteAnimation.class.getName()).add(UIBasicItemPropertiesMediator.NAME);
        classToMediatorMap.get(SpriteAnimation.class.getName()).add(UISpriteAnimationItemPropertiesMediator.NAME);

        classToMediatorMap.put(SpineActor.class.getName(), new ArrayList<>());
        classToMediatorMap.get(SpineActor.class.getName()).add(UIBasicItemPropertiesMediator.NAME);
        classToMediatorMap.get(SpineActor.class.getName()).add(UISpineAnimationItemPropertiesMediator.NAME);

        classToMediatorMap.put(SpriterActor.class.getName(), new ArrayList<>());
        classToMediatorMap.get(SpriterActor.class.getName()).add(UIBasicItemPropertiesMediator.NAME);

        classToMediatorMap.put(CompositeItem.class.getName(), new ArrayList<>());
        classToMediatorMap.get(CompositeItem.class.getName()).add(UIBasicItemPropertiesMediator.NAME);
        classToMediatorMap.get(CompositeItem.class.getName()).add(UICompositeItemPropertiesMediator.NAME);

        classToMediatorMap.put(ParticleItem.class.getName(), new ArrayList<>());
        classToMediatorMap.get(ParticleItem.class.getName()).add(UIBasicItemPropertiesMediator.NAME);

        classToMediatorMap.put(LightActor.class.getName(), new ArrayList<>());
        classToMediatorMap.get(LightActor.class.getName()).add(UIBasicItemPropertiesMediator.NAME);

        classToMediatorMap.put(SceneVO.class.getName(), new ArrayList<>());
        classToMediatorMap.get(SceneVO.class.getName()).add(UIScenePropertiesMediator.NAME);
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2D.PROJECT_OPENED,
                Overlap2D.EMPTY_SPACE_CLICKED,
                Overlap2D.ITEM_DATA_UPDATED,
                Overlap2D.ITEM_SELECTED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        switch (notification.getName()) {
            case Overlap2D.PROJECT_OPENED:
                initAllPropertyBoxes(Sandbox.getInstance().sceneControl.getCurrentSceneVO());
                break;
            case Overlap2D.EMPTY_SPACE_CLICKED:
                initAllPropertyBoxes(Sandbox.getInstance().sceneControl.getCurrentSceneVO());
                break;
            case Overlap2D.ITEM_SELECTED:
                initAllPropertyBoxes(notification.getBody());
                break;
            default:
                break;
        }
    }

    private void initAllPropertyBoxes(Object observable) {
        // retrieve a list of property boxes to show
        ArrayList<String> mediatorNames = classToMediatorMap.get(observable.getClass().getName());

        if(mediatorNames == null) return;

        //clear all current enabled boxes
        viewComponent.clearAll();

        //unregister all current mediators
        for(UIAbstractPropertiesMediator mediator: currentRegisteredPropertyBoxes) {
            facade.removeMediator(mediator.NAME);
        }
        currentRegisteredPropertyBoxes.clear();

        for (String mediatorName : mediatorNames) {
            try {
                facade.registerMediator((Mediator) ClassReflection.newInstance(ClassReflection.forName(mediatorName)));

                UIAbstractPropertiesMediator<Object, UIAbstractProperties> propertyBoxMediator = facade.retrieveMediator(mediatorName);
                propertyBoxMediator.setItem(observable);
                viewComponent.addPropertyBox(propertyBoxMediator.getViewComponent());
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
        }
    }
}
