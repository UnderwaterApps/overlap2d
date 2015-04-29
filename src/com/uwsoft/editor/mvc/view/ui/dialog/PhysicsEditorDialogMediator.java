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

package com.uwsoft.editor.mvc.view.ui.dialog;

import com.badlogic.gdx.math.Vector2;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.renderer.actor.*;
import com.uwsoft.editor.renderer.data.*;

/**
 * Created by azakhary on 4/28/2015.
 */
public class PhysicsEditorDialogMediator extends SimpleMediator<PhysicsEditorDialog> {

    public static final String TAG = PhysicsEditorDialogMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private IBaseItem currentItem;
    private PhysicsBodyDataVO currentPhysicsDataVO;

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
                Sandbox.ACTION_EDIT_PHYSICS
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case Sandbox.ACTION_EDIT_PHYSICS:
                setItem((IBaseItem) notification.getBody());
                break;
            default:
                break;
        }

    }

    public void setVariables() {

        if (currentItem.getDataVO().physicsBodyData != null) {
            currentPhysicsDataVO = new PhysicsBodyDataVO(currentItem.getDataVO().physicsBodyData);
        } else {
            currentPhysicsDataVO = new PhysicsBodyDataVO();
        }

        viewComponent.setBodyType(currentPhysicsDataVO.bodyType);
        viewComponent.setMass(String.valueOf(currentPhysicsDataVO.mass));
        viewComponent.setCenterOfMass(new Vector2(currentPhysicsDataVO.centerOfMass));
        viewComponent.setRotationalIntertia(String.valueOf(currentPhysicsDataVO.mass));
        viewComponent.setDumping(String.valueOf(currentPhysicsDataVO.damping));
        viewComponent.setGravityScale(String.valueOf(currentPhysicsDataVO.gravityScale));
        viewComponent.setDensity(String.valueOf(currentPhysicsDataVO.density));
        viewComponent.setFriction(String.valueOf(currentPhysicsDataVO.friction));
        viewComponent.setRestitution(String.valueOf(currentPhysicsDataVO.restitution));
        viewComponent.setAllowSleep(currentPhysicsDataVO.allowSleep);
        viewComponent.setAwake(currentPhysicsDataVO.awake);
        viewComponent.setBullet(currentPhysicsDataVO.bullet);
        viewComponent.setPoligonyzer("BAYAZIT");
        viewComponent.setHullTolerance("2.5");
        viewComponent.setAlphaTolerance("128");
        viewComponent.setMultiPartDetection(false);
        viewComponent.setHoleDetection(false);
        /*
        massVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.mass));
        centerOfMassXVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.centerOfMass.x));
        centerOfMassYVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.centerOfMass.y));
        rotationalInertiaVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.rotationalInertia));
        dampingVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.damping));
        gravityVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.gravityScale));
        densityVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.density));
        frictionVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.friction));
        restitutionVal.setText(String.valueOf(itemPhysicsEditor.physicsBodyDataVO.restitution));
        allowSleepVal.setChecked(itemPhysicsEditor.physicsBodyDataVO.allowSleep);
        awakeVal.setChecked(itemPhysicsEditor.physicsBodyDataVO.awake);
        bulletVal.setChecked(itemPhysicsEditor.physicsBodyDataVO.bullet);
        polygonizerVal.setSelectedIndex(0);
        hullToleranceVal.setText(String.valueOf(2.5f));
        alphaToleranceVal.setText(String.valueOf(128));
        multiPartDetectionVal.setChecked(false);
        holeDetectionVal.setChecked(false);*/
    }

    public void setItem(IBaseItem item) {
        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();

        currentItem = item;
        viewComponent.getItemPhysicsEditor().originalItem = item;

        viewComponent.show(uiStage);
        viewComponent.getCreateFreshCopyButton().setDisabled(false);

        viewComponent.setItem(duplicateItem(item));
        setVariables();
    }

    public void setItem(String asset) {
        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();
    }

    private IBaseItem duplicateItem(IBaseItem item) {
        MainItemVO data = item.getDataVO();
        String className = data.getClass().getSimpleName();

        IBaseItem itemCopy = null;
        Essentials essentials = Sandbox.getInstance().getSandboxStage().essentials;

        if (className.equals("SimpleImageVO")) {
            itemCopy = new ImageItem((SimpleImageVO) data, essentials);
        }
        if (className.equals("Image9patchVO")) {
            return null;
        }
        if (className.equals("TextBoxVO")) {
            return null;
        }
        if (className.equals("ButtonVO")) {
            return null;
        }
        if (className.equals("LabelVO")) {
            return null;
        }
        if (className.equals("CompositeItemVO")) {
            itemCopy = new CompositeItem((CompositeItemVO) data, essentials);
        }
        if (className.equals("CheckBoxVO")) {
            return null;
        }
        if (className.equals("SelectBoxVO")) {
            return null;
        }
        if (className.equals("ParticleEffectVO")) {
            itemCopy = new ParticleItem((ParticleEffectVO) data, essentials);
        }
        if (className.equals("LightVO")) {
            itemCopy = new ParticleItem((ParticleEffectVO) data, essentials);
        }
        if (className.equals("SpineVO")) {
            itemCopy = new SpineActor((SpineVO) data, essentials);
        }
        if (className.equals("SpriteAnimationVO")) {
            itemCopy = new SpriteAnimation((SpriteAnimationVO) data, essentials);
        }

        return itemCopy;
    }
}
