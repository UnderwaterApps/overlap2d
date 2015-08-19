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

package com.overlap2d.plugins.ninepatch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.puremvc.patterns.facade.SimpleFacade;
import com.commons.UIDraggablePanel;

/**
 * Created by azakhary on 8/18/2015.
 */
public class MainPanel extends UIDraggablePanel {
    public static final String CLASS_NAME = "com.uwsoft.editor.plugins.ninepatch.MainPanel";

    public static final String SAVE_CLICKED = CLASS_NAME + ".SAVE_CLICKED";

    private SimpleFacade facade;

    private VisTable mainTable;
    private TextureRegion texture;

    private VisTable editingTable;
    private VisTable previewTable;

    private EditingZone editingZone;
    private PreviewWidget previewWidget;

    public MainPanel() {
        super("Nine Patch");
        addCloseButton();

        facade = SimpleFacade.getInstance();

        mainTable = new VisTable();
        add(mainTable).width(520).height(310).padBottom(7);
        editingTable = new VisTable();
        previewTable = new VisTable();

        mainTable.add(editingTable).width(310).expandY();
        mainTable.add(previewTable).expandX().expandY();
        mainTable.row();
    }

    private void initView() {
        editingTable.clear();
        editingZone = new EditingZone();
        editingZone.setTexture(texture);
        editingTable.add(editingZone);

        editingZone.setWidth(310);
        editingZone.setHeight(310);

        editingZone.setListener(new EditingZone.PatchChangeListener() {
            @Override
            public void changed(int[] splits) {
                previewWidget.update((TextureAtlas.AtlasRegion) texture, splits);
            }
        });
    }

    private void initPreView() {
        previewTable.clear();
        previewWidget = new PreviewWidget();
        previewWidget.setHeight(205);
        previewTable.add(previewWidget).width(200).height(205).top();
        previewTable.row();
        previewWidget.update((TextureAtlas.AtlasRegion) texture, ((TextureAtlas.AtlasRegion) texture).splits);

        VisLabel label = new VisLabel("Note: after saving, your \n scene will reload to \n apply changes.");
        label.setAlignment(Align.center);
        previewTable.add(label).pad(10).fillY().expandY();
        previewTable.row();

        VisTextButton saveBtn = new VisTextButton("apply and save");
        previewTable.add(saveBtn).pad(5);
        previewTable.row();

        saveBtn.addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                facade.sendNotification(SAVE_CLICKED);
            }
        });
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;

        initView();
        initPreView();
    }

    public void setListeners(Stage stage) {
        stage.addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                editingZone.zoomBy(amount);
                return false;
            }
        });
    }

    public int[] getSplits() {
        return editingZone.getSplits();
    }

}
