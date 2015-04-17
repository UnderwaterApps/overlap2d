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

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.thumbnailbox.Image9patchThumbnailBox;
import com.uwsoft.editor.gdx.ui.thumbnailbox.ImageThumbnailBox;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIImagesTab extends Tab {

    private VisTable contentTable;
    private VisTable imagesTable;
    private VisScrollPane scrollPane;

    private TextureManager textureManager;
    private UIStage stage;

    public UIImagesTab() {
        super(false, false);

        textureManager = Overlap2DFacade.getInstance().retrieveProxy(TextureManager.NAME);
        stage = Sandbox.getInstance().getUIStage();

        contentTable = new VisTable();

        imagesTable = new VisTable();
        scrollPane = new VisScrollPane(imagesTable);
        contentTable.add(scrollPane).width(230).height(350);
        contentTable.row();
    }

    @Override
    public String getTabTitle() {
        return "Images";
    }

    @Override
    public Table getContentTable() {
        return contentTable;
    }

    public void setRegionNames(Array<String> regionNames) {
        TextureAtlas atlas = textureManager.getProjectAssetsList();

        imagesTable.clearChildren();
        int itemIterator = 0;

        for(int i = 0; i < regionNames.size; i++) {
            TextureAtlas.AtlasRegion atlasRegion = atlas.findRegion(regionNames.get(i));

            boolean is9patch = atlasRegion.splits != null;
            final ImageThumbnailBox thumb = is9patch ? new Image9patchThumbnailBox(stage, atlasRegion) : new ImageThumbnailBox(stage, atlasRegion);

            imagesTable.add(thumb).pad(3);


            if ((itemIterator - 7) % 4 == 0) {
                imagesTable.row();
            }
            itemIterator++;
        }

    }
}
