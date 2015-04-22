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

package com.uwsoft.editor.gdx.ui.dialogs;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.actor.SpriteAnimation;

import java.util.Map;

/**
 * Created by azakhary on 8/28/2014.
 */
public class EditAnimationDialog extends SimpleDialog {

    private final Map<String, SpriteAnimation.Animation> animations;
    private final Overlap2DFacade facade;
    private final TextureManager textureManager;
    UIStage uiStage;
    private Group listContainer;
    private float maxHeight = 250;
    private IBaseItem item;
    private ScrollPane scroll;
    private Group wrapper;
    private Group topWrapper;

    public EditAnimationDialog(UIStage s, final SpriteAnimation item) {
        super(s, 320, 310);
        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(TextureManager.NAME);
        this.uiStage = s;
        setX(200);
        setY(200);

        setTitle("Add animation");

        animations = item.getAnimations();
        this.item = item;

        topWrapper = new Group();
        topWrapper.setX(5);
        topWrapper.setY(getHeight() - maxHeight - 20);
        topWrapper.setHeight(maxHeight);
        topWrapper.setWidth(getWidth() - 10);


        listContainer = new Group();
        wrapper = new Group();

        renderMainList();
        scroll = new ScrollPane(wrapper, textureManager.editorSkin);

        scroll.setWidth(topWrapper.getWidth());
        scroll.setHeight(maxHeight);
        scroll.setFlickScroll(false);
        scroll.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return true;
            }
        });


        wrapper.addActor(listContainer);
        scroll.setName("scroll");
        topWrapper.addActor(scroll);
        addActor(topWrapper);


        final CompositeItem newAnimationControl = null;//stage.sceneLoader.getLibraryAsActor("newAnimationControl");
        addActor(newAnimationControl);
        newAnimationControl.setX(getWidth() / 2 - newAnimationControl.getWidth() / 2);
        newAnimationControl.setY(topWrapper.getY() - newAnimationControl.getHeight() - 5);

        TextButton addBtn = newAnimationControl.getTextButtonById("addBtn");

        addBtn.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    int fromValue = Integer.parseInt(newAnimationControl.getTextBoxById("fromValue").getText());
                    int toValue = Integer.parseInt(newAnimationControl.getTextBoxById("toValue").getText());
                    String name = newAnimationControl.getTextBoxById("animationName").getText();
                    if (!(fromValue > toValue || fromValue < 0 || toValue < 0 || toValue > item.getFramesCount())) {
                        animations.put(name, new SpriteAnimation.Animation(fromValue, toValue, name));
                        renderMainList();
                    }

                } catch (NumberFormatException ignored) {

                }
                newAnimationControl.getTextBoxById("fromValue").setText("");
                newAnimationControl.getTextBoxById("toValue").setText("");
                newAnimationControl.getTextBoxById("animationName").setText("");
            }
        });

    }

    public void renderMainList() {
        item.updateDataVO();
        listContainer.clear();

        float itmHeight = 25;
        int cnt = animations.size();

        wrapper.setHeight(itmHeight * cnt);
        scroll = new ScrollPane(wrapper, textureManager.editorSkin);
        scroll.setWidth(topWrapper.getWidth());
        scroll.setHeight(maxHeight);
        scroll.setFlickScroll(false);
        scroll.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return true;
            }
        });
        if (topWrapper.findActor("scroll") != null) {
            topWrapper.findActor("scroll").remove();
        }

        topWrapper.addActor(scroll);


        if (itmHeight * cnt < maxHeight - 10) {
            listContainer.setHeight(maxHeight - 10);
        } else {
            listContainer.setHeight(itmHeight * cnt);
        }

        int iterator = 0;
        for (Map.Entry<String, SpriteAnimation.Animation> entry : animations.entrySet()) {
            final String name = entry.getKey();
            SpriteAnimation.Animation value = entry.getValue();
            final CompositeItem itm = null;//stage.sceneLoader.getLibraryAsActor("animationValueControl");
            itm.getLabelById("name").setText("\"" + name + "\" frames: " + value.startFrame + " - " + value.endFrame);
            listContainer.addActor(itm);
            itm.setX(2);
            itm.setY(listContainer.getHeight() - (itm.getHeight() + 3) * (iterator + 1));
            iterator++;
            itm.getTextButtonById("deleteBtn").addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    animations.remove(name);
                    renderMainList();
                }
            });
        }

        uiStage.updateCurrentItemState();
    }
}
