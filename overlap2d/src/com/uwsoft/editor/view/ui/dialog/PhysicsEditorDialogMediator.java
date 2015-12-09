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

package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.ashley.core.Entity;
import com.commons.MsgAPI;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.SceneDataManager;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.stage.UIStage;
import com.uwsoft.editor.view.ui.UIDropDownMenu;

/**
 * Created by azakhary on 4/28/2015.
 */
public class PhysicsEditorDialogMediator extends SimpleMediator<PhysicsEditorDialog> {

    public static final String TAG = PhysicsEditorDialogMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private Entity currentItem;


    public PhysicsEditorDialogMediator() {
        super(NAME, new PhysicsEditorDialog());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                MsgAPI.SCENE_LOADED,
                MsgAPI.ACTION_EDIT_PHYSICS,
                UIDropDownMenu.ACTION_EDIT_RESOURCE_PHYSICS,
                PhysicsEditorDialog.CLEAR_MESH_CLICKED,
                PhysicsEditorDialog.CREATE_FRESH_COPY_CLICKED,
                PhysicsEditorDialog.RETRACE_CLICKED,
                PhysicsEditorDialog.SAVE_CLICKED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case MsgAPI.SCENE_LOADED:
                Sandbox sandbox = Sandbox.getInstance();
              //TODO fix and uncomment
                //viewComponent.getItemPhysicsEditor().resVec = new Vector2(commands.getCurrentScene().mulX, commands.getCurrentScene().mulY);
                break;
            case MsgAPI.ACTION_EDIT_PHYSICS:
                setItem((Entity) notification.getBody());
                break;
            case UIDropDownMenu.ACTION_EDIT_RESOURCE_PHYSICS:
                setItem((String) notification.getBody());
                break;
            default:
                break;
        }

    }

    public void setData() {
    	//TODO fix and uncomment
//        ItemPhysicsEditor itemPhysicsEditor = viewComponent.getItemPhysicsEditor();
//        PhysicsBodyDataVO currentPhysicsDataVO = itemPhysicsEditor.physicsBodyDataVO;
//
//        viewComponent.setBodyType(currentPhysicsDataVO.bodyType);
//        viewComponent.setMass(String.valueOf(currentPhysicsDataVO.mass));
//        viewComponent.setCenterOfMass(new Vector2(currentPhysicsDataVO.centerOfMass));
//        viewComponent.setRotationalIntertia(String.valueOf(currentPhysicsDataVO.mass));
//        viewComponent.setDumping(String.valueOf(currentPhysicsDataVO.damping));
//        viewComponent.setGravityScale(String.valueOf(currentPhysicsDataVO.gravityScale));
//        viewComponent.setDensity(String.valueOf(currentPhysicsDataVO.density));
//        viewComponent.setFriction(String.valueOf(currentPhysicsDataVO.friction));
//        viewComponent.setRestitution(String.valueOf(currentPhysicsDataVO.restitution));
//        viewComponent.setAllowSleep(currentPhysicsDataVO.allowSleep);
//        viewComponent.setAwake(currentPhysicsDataVO.awake);
//        viewComponent.setBullet(currentPhysicsDataVO.bullet);
//        viewComponent.setPoligonyzer("BAYAZIT");
//        viewComponent.setHullTolerance("2.5");
//        viewComponent.setAlphaTolerance("128");
//        viewComponent.setMultiPartDetection(false);
//        viewComponent.setHoleDetection(false);
    }

    private void collectData() {
        /*
        ItemPhysicsEditor itemPhysicsEditor = viewComponent.getItemPhysicsEditor();
        if (viewComponent.getBodyType().equals("STATIC")) {
            itemPhysicsEditor.physicsBodyDataVO.bodyType = BodyDef.BodyType.StaticBody.getValue();
        } else if (viewComponent.getBodyType().equals("KINEMATIC")) {
            itemPhysicsEditor.physicsBodyDataVO.bodyType = BodyDef.BodyType.KinematicBody.getValue();
        } else {
            itemPhysicsEditor.physicsBodyDataVO.bodyType = BodyDef.BodyType.DynamicBody.getValue();
        }

        itemPhysicsEditor.physicsBodyDataVO.mass = Float.parseFloat(viewComponent.getMass());
        itemPhysicsEditor.physicsBodyDataVO.centerOfMass = new Vector2(viewComponent.getCenterOfMass());
        itemPhysicsEditor.physicsBodyDataVO.rotationalInertia = Float.parseFloat(viewComponent.getRotationalIntertia());
        itemPhysicsEditor.physicsBodyDataVO.damping = Float.parseFloat(viewComponent.getDumping());
        itemPhysicsEditor.physicsBodyDataVO.gravityScale = Float.parseFloat(viewComponent.getGravityScale());
        itemPhysicsEditor.physicsBodyDataVO.density = Float.parseFloat(viewComponent.getDensity());
        itemPhysicsEditor.physicsBodyDataVO.friction = Float.parseFloat(viewComponent.getFriction());
        itemPhysicsEditor.physicsBodyDataVO.restitution = Float.parseFloat(viewComponent.getRestitution());
        itemPhysicsEditor.physicsBodyDataVO.allowSleep = !itemPhysicsEditor.physicsBodyDataVO.allowSleep;
        itemPhysicsEditor.physicsBodyDataVO.awake = !itemPhysicsEditor.physicsBodyDataVO.awake;
        itemPhysicsEditor.physicsBodyDataVO.bullet = !itemPhysicsEditor.physicsBodyDataVO.bullet;*/
    }

    public void setItem(Entity item) {
    	//TODO fix and uncomment
//        Sandbox commands = Sandbox.getInstance();
//        UIStage uiStage = commands.getUIStage();
//
//        currentItem = item;
//        viewComponent.getItemPhysicsEditor().originalItem = item;
//
//        viewComponent.show(uiStage);
//        viewComponent.getCreateFreshCopyButton().setDisabled(false);
//
//        viewComponent.setItem(duplicateItem(item));
//        setData();
    }

    public void setItem(String asset) {
        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();

        viewComponent.show(uiStage);
        viewComponent.getCreateFreshCopyButton().setDisabled(false);

        viewComponent.setItem(asset);
        setData();
    }

    private Entity duplicateItem(Entity item) {
    	return null;
    	//TODO fix and uncomment
//        MainItemVO data = item.getDataVO();
//        String className = data.getClass().getSimpleName();
//
//        IBaseItem itemCopy = null;
//        Essentials essentials = Sandbox.getInstance().getSandboxStage().essentials;
//
//        if (className.equals("SimpleImageVO")) {
//            itemCopy = new ImageItem((SimpleImageVO) data, essentials);
//        }
//        if (className.equals("Image9patchVO")) {
//            return null;
//        }
//        if (className.equals("TextBoxVO")) {
//            return null;
//        }
//        if (className.equals("ButtonVO")) {
//            return null;
//        }
//        if (className.equals("LabelVO")) {
//            return null;
//        }
//        if (className.equals("CompositeItemVO")) {
//            itemCopy = new CompositeItem((CompositeItemVO) data, essentials);
//        }
//        if (className.equals("CheckBoxVO")) {
//            return null;
//        }
//        if (className.equals("SelectBoxVO")) {
//            return null;
//        }
//        if (className.equals("ParticleEffectVO")) {
//            itemCopy = new ParticleItem((ParticleEffectVO) data, essentials);
//        }
//        if (className.equals("LightVO")) {
//            itemCopy = new ParticleItem((ParticleEffectVO) data, essentials);
//        }
//        if (className.equals("SpineVO")) {
//            itemCopy = new SpineActor((SpineVO) data, essentials);
//        }
//        if (className.equals("SpriteAnimationVO")) {
//            itemCopy = new SpriteAnimation((SpriteAnimationVO) data, essentials);
//        }
//
//        return itemCopy;
    }
}
