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

package com.uwsoft.editor.view.ui.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

/**
 * Created by azakhary on 4/30/2015.
 */
public class EditableSelectBox extends VisTable {

    private String styleName;

    VisTextField textField;
    VisSelectBox<String> selectBox;

    private Array<ChangeListener> listeners = new Array<>();

    public EditableSelectBox(String styleName) {
        super();
        this.styleName = styleName;
        this.init();
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    private void init() {

        selectBox = new VisSelectBox<>(styleName);
        textField = new VisTextField();

        add(selectBox).fillX().expandX();
        addActor(textField);
        textField.setWidth(98);
        textField.setHeight(selectBox.getHeight());

        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                textField.setText(selectBox.getSelected());
                fireChanged();
            }
        });

        textField.addListener(new InputListener() {
            @Override
            public boolean keyUp (InputEvent event, int keycode) {
                if(keycode == Input.Keys.ENTER) {
                    fireChanged();
                }
                return super.keyUp(event, keycode);
            }
        });
    }

    private void fireChanged() {
        for (int i = 0, n = listeners.size; i < n; i++) {
            ChangeListener listener = listeners.get(i);
            listener.changed(new ChangeListener.ChangeEvent(), EditableSelectBox.this);
        }
    }

    @Override
    public boolean addListener (EventListener listener) {
        if (!listeners.contains((ChangeListener)listener, true)) {
            listeners.add((ChangeListener)listener);
            return true;
        }
        return false;
    }
    public void setText(String text) {
        textField.setText(text);
    }

    public void setItems(String... newItems) {
        selectBox.setItems(newItems);
        textField.setText(selectBox.getSelected());
    }

    public void setItems(Array<String> newItems) {
        selectBox.setItems(newItems);
        textField.setText(selectBox.getSelected());
    }

    public String getSelected() {
        return textField.getText();
    }

    public void setSelected(String selected) {
        selectBox.setSelected(selected);
        textField.setText(selectBox.getSelected());
    }
}
