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

package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.CompositeVO;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.data.LightVO;

import java.util.ArrayList;

/**
 * Provides interface for user actions/intentions like creating items
 * copying and pasting items and doing redo/undo.
 */
public class UserActionController {

    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private Sandbox sandbox;

    public UserActionController(Sandbox sandbox) {
        this.sandbox = sandbox;
        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
    }

    public void createImage(String regionName, float x, float y) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        sandbox.getItemFactory().createImageItem(layer, regionName, x, y);
    }

    public void create9Patch(String name, float x, float y) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        sandbox.getItemFactory().create9patchItem(layer, name, x, y);
    }

    public void createParticleItem(String name, float x, float y) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        sandbox.getItemFactory().createParticleItem(layer, name, x, y);

    }

    public void createSpriteAnimation(String name, float x, float y) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        sandbox.getItemFactory().createSpriteAnimation(layer, name, x, y);
    }

    public void createSpriterAnimation(String name, float x, float y) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        sandbox.getItemFactory().createSpriterAnimation(layer, name, x, y);
    }

    public void createSpineAnimation(String name, float x, float y) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        sandbox.getItemFactory().createSpineAnimation(layer, name, x, y);
    }

    public void createLight(LightVO vo, float x, float y) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        sandbox.getItemFactory().createLight(layer, vo, x, y);
    }


    public void createItemFromLibrary(String name, float x, float y) {
        LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        sandbox.getItemFactory().createItemFromLibrary(layer, name, x, y);
    }

    public void createComponent(final String name, final float x, final float y) {
        final LayerItemVO layer = sandbox.getSelectedLayer();
        if (layer == null) return;

        // creating component requires skin
        if (!sandbox.isComponentSkinAvailable() && !name.equals("Label")) {
            DialogUtils.showConfirmDialog(sandbox.getUIStage(),
                    "Warning",
                    "There is no style imported yet. Do you want to add default style instead to make this work?",
                    new String[]{"OK", "Cancel"}, new Integer[]{0, 1},
                    result -> {
                        if (result == 0) {
                            projectManager.copyDefaultStyleIntoProject();
                            sandbox.getItemFactory().createComponent(layer, name, x, y);
                        }

                    });
        } else {
            sandbox.getItemFactory().createComponent(layer, name, x, y);
        }
    }

    /**
     *
     */
    public void undo() {
        FlowActionEnum lastFlowAction = sandbox.flow.getFlowLastAction();
        CompositeItemVO compositeItemVO = sandbox.flow.undo();
        switch (lastFlowAction) {
            case GET_INTO_COMPOSITE:
                sandbox.enterIntoPrevComposite();
                break;
            case GET_OUT_COMPOSITE:
                sandbox.enterIntoComposite(compositeItemVO);
                break;
            default:
                sandbox.reconstructFromSceneVo(compositeItemVO);
                break;
        }
        sandbox.sceneControl.getCurrentScene().updateDataVO();
    }

    public void redo() {
        CompositeItemVO compositeItemVO = sandbox.flow.redo();
        FlowActionEnum lastFlowAction = sandbox.flow.getFlowLastAction();
        switch (lastFlowAction) {
            case GET_INTO_COMPOSITE:
                sandbox.enterIntoComposite(compositeItemVO);
                break;
            case GET_OUT_COMPOSITE:
                sandbox.enterIntoPrevComposite();
                break;
            default:
                sandbox.reconstructFromSceneVo(compositeItemVO);
                break;
        }
        sandbox.sceneControl.getCurrentScene().updateDataVO();
    }


    public void cutAction() {
        ArrayList<IBaseItem> items = sandbox.getSelector().getSelectedItems();
        putItemsToClipboard(items);
        sandbox.getSelector().removeCurrentSelectedItems();
    }

    public void copyAction() {
        sandbox.getCurrentScene().updateDataVO();
        ArrayList<IBaseItem> items = sandbox.getSelector().getSelectedItems();
        putItemsToClipboard(items);
    }

    public void deleteAction() {
        sandbox.getSelector().removeCurrentSelectedItems();
    }

    public void pasteAction(float x, float y, boolean ignoreCameraPos) {
        CompositeVO tempHolder;
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        tempHolder = json.fromJson(CompositeVO.class, sandbox.fakeClipboard);

        if (tempHolder == null) return;

        CompositeItemVO fakeVO = new CompositeItemVO();

        fakeVO.composite = tempHolder;
        CompositeItem fakeItem = new CompositeItem(fakeVO, sandbox.sceneControl.getEssentials());

        ArrayList<IBaseItem> finalItems = new ArrayList<IBaseItem>();
        Actor firstItem = (Actor) fakeItem.getItems().get(0);
        float offsetX = firstItem.getX() * sandbox.sceneControl.getCurrentScene().mulX;
        float offsetY = firstItem.getY() * sandbox.sceneControl.getCurrentScene().mulY;
        for (int i = 1; i < fakeItem.getItems().size(); i++) {
            Actor item = (Actor) fakeItem.getItems().get(i);
            if (item.getX() * sandbox.sceneControl.getCurrentScene().mulX < offsetX) {
                offsetX = item.getX() * sandbox.sceneControl.getCurrentScene().mulX;
            }
            if (item.getY() * sandbox.sceneControl.getCurrentScene().mulY < offsetY) {
                offsetY = item.getY() * sandbox.sceneControl.getCurrentScene().mulY;
            }
        }
        Vector3 cameraPos = ignoreCameraPos ? new Vector3(0, 0, 0) : ((OrthographicCamera) sandbox.getSandboxStage().getCamera()).position;
        for (int i = 0; i < fakeItem.getItems().size(); i++) {
            IBaseItem itm = fakeItem.getItems().get(i);
            itm.getDataVO().layerName = sandbox.getUIStage().getCurrentSelectedLayer().layerName;
            sandbox.sceneControl.getCurrentScene().addItem(itm);
            ((Actor) itm).setX(x + ((Actor) itm).getX() - offsetX + (cameraPos.x + sandbox.copedItemCameraOffset.x));
            ((Actor) itm).setY(y + ((Actor) itm).getY() - offsetY + (cameraPos.y + sandbox.copedItemCameraOffset.y));
            itm.updateDataVO();
            sandbox.getSandboxInputAdapter().initItemListeners(itm);
            finalItems.add(itm);
        }

        sandbox.getSelector().setSelections(finalItems, true);
//        sandbox.getUIStage().getItemsBox().init();
    }

    private void putItemsToClipboard(ArrayList<IBaseItem> items) {
        CompositeVO tempHolder = new CompositeVO();
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        Actor actor = (Actor) items.get(0);
        Vector3 cameraPos = ((OrthographicCamera) sandbox.getSandboxStage().getCamera()).position;
        Vector3 vector3 = new Vector3(actor.getX() - cameraPos.x, actor.getY() - cameraPos.y, 0);
        for (IBaseItem item : items) {
            tempHolder.addItem(item.getDataVO());
            actor = (Actor) item;
            if (actor.getX() - cameraPos.x < vector3.x) {
                vector3.x = actor.getX() - cameraPos.x;
            }
            if (actor.getY() - cameraPos.y < vector3.y) {
                vector3.y = actor.getY() - cameraPos.y;
            }
        }
        sandbox.fakeClipboard = json.toJson(tempHolder);
        sandbox.copedItemCameraOffset = vector3;
    }
}
