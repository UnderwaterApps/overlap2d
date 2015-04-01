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
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.utils.CustomVariables;

import java.util.Map;

/**
 * Created by azakhary on 8/28/2014.
 */
public class CustomVariablesDialog extends SimpleDialog {

    private Group listContainer;
    private float maxHeight = 250;
    private CustomVariables vars;
    private IBaseItem item;

    private ScrollPane scroll;

    private Group wrapper;
    private Group topWrapper;
    UIStage uiStagel;

    public CustomVariablesDialog(UIStage s, final IBaseItem item) {
        super(s, 320, 310);
              
        this.uiStagel	=	s;
        setX(200);
        setY(200);

        setTitle("Custom Variables Dialog");

        vars = item.getCustomVariables();
        this.item = item;
        
        topWrapper = new Group();
        topWrapper.setX(5);
        topWrapper.setY(getHeight()-maxHeight-20);
        topWrapper.setHeight(maxHeight);        
        topWrapper.setWidth(getWidth()-10);
        

        listContainer = new Group();                
        wrapper = new Group();
        
        renderMainList();
        scroll = new ScrollPane(wrapper, s.textureManager.editorSkin);
        
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
        
        
        

        final CompositeItem newRow = stage.sceneLoader.getLibraryAsActor("newKeyValuePair");
        addActor(newRow);
        newRow.setX(getWidth()/2 - newRow.getWidth()/2);
        newRow.setY(topWrapper.getY() - newRow.getHeight() - 2);

        TextButton addBtn = newRow.getTextButtonById("addBtn");

        addBtn.addListener(new ClickListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

               String key = newRow.getTextBoxById("key").getText();
               String value = newRow.getTextBoxById("value").getText();

               key.replace(";", ""); key.replace(":", "");

               if(key.length() > 0) {
                   vars.setVariable(key, value);
                   renderMainList();

                   newRow.getTextBoxById("key").setText("");
                   newRow.getTextBoxById("value").setText("");
               }
            }
        });

    }

    public void renderMainList() {
        item.updateDataVO();
        listContainer.clear();

        float itmHeight = 25;
        int cnt = vars.getCount();

        wrapper.setHeight(itmHeight*cnt);
        scroll = new ScrollPane(wrapper, uiStagel.textureManager.editorSkin);        
        scroll.setWidth(topWrapper.getWidth());
        scroll.setHeight(maxHeight);
        scroll.setFlickScroll(false);
        scroll.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return true;
            }
        });
        if(topWrapper.findActor("scroll")!=null){
        	topWrapper.findActor("scroll").remove();
        }
        
        topWrapper.addActor(scroll);
        
        
        if(itmHeight*cnt < maxHeight-10) {
            listContainer.setHeight(maxHeight-10);
        } else {
            listContainer.setHeight(itmHeight*cnt);
        }
        
        int iterator = 0;
        for (Map.Entry<String, String> entry : vars.getHashMap().entrySet()) {
            final String key = entry.getKey();
            String value = entry.getValue();
            // ...
            final CompositeItem itm = stage.sceneLoader.getLibraryAsActor("KeyValuePairRow");

            itm.getLabelById("key").setAlignment(Align.left);

            itm.getLabelById("key").setText(key);
            itm.getTextBoxById("value").setText(value);

            listContainer.addActor(itm);
            itm.setX(2);
            itm.setY(listContainer.getHeight()-(itm.getHeight() + 3)*(iterator+1));
            iterator++;


            itm.getTextButtonById("updateBtn").addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    vars.setVariable(key, itm.getTextBoxById("value").getText());
                    renderMainList();
                }
            });


            itm.getTextButtonById("deleteBtn").addListener(new ClickListener() {
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
                public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                    vars.removeVariable(key);
                    renderMainList();
                }
            });
        }
    }
}
