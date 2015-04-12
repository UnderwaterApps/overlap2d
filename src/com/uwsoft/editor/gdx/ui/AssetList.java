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

package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.thumbnailbox.*;
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.data.TextBoxVO;
import com.uwsoft.editor.gdx.ui.thumbnailbox.*;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;

import java.util.HashMap;

public class AssetList extends Group {
    private Label searchLbl;
    private TextBoxItem searchText;
    private  TextureAtlas atlas;
    private Table container;
    private Table table;
    private UIStage stage;
    private HashMap<String, SpineAnimData> spineAnimations;
    private HashMap<String, TextureAtlas> spriteAnimations;
    private HashMap<String, FileHandle> spriterAnimations;

    private final Overlap2DFacade facade;
    private final TextureManager textureManager;

    public AssetList(final UIStage s, float width, float height) {
        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(TextureManager.NAME);
        this.setWidth(width);
        this.setHeight(height);
        this.stage  =   s;
        container = new Table();
        table = new Table();
        container.setX(0);
        container.setY(0);
        container.setWidth(getWidth() - 1);
        container.setHeight(getHeight() - 20);
        final ScrollPane scroll = new ScrollPane(table, stage.textureManager.editorSkin);
        container.add(scroll).colspan(4).width(getWidth());
        container.row();
        scroll.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return true;
            }
        });
        scroll.setHeight(getHeight() - 20);
        scroll.setFlickScroll(false);
        atlas = s.textureManager.getProjectAssetsList();
        spineAnimations     = stage.textureManager.getProjectSpineAnimationsList();
        spriteAnimations    = stage.textureManager.getProjectSpriteAnimationsList();
        spriterAnimations   = stage.textureManager.getProjectSpriterAnimationsList();

        if (atlas == null) return;
        drawItems();
        //-----------------Search Box
        Group searchGroup   =   new Group();
        container.add(searchGroup);
        searchGroup.setWidth(200);
        searchGroup.setHeight(30);
        searchLbl   =   new Label("Search :", s.textureManager.editorSkin);
        searchGroup.addActor(searchLbl);

        searchText = new TextBoxItem(new TextBoxVO(),s.essentials);
        searchText.setX(searchLbl.getTextBounds().width);
        searchGroup.addActor(searchText);
        searchText.setTextFieldListener(new TextField.TextFieldListener() {
            public void keyTyped (TextField textField, char key) {
                drawItems(searchText.getText());
            }
        });


    }
    private void drawItems(String searchText) {
        table.clearChildren();
        int itemIter = 0;
        for (int i = 0; i < atlas.getRegions().size; i++) {
            if(!atlas.getRegions().get(i).name.contains(searchText))continue;
            TextureAtlas.AtlasRegion atlasRegion = atlas.getRegions().get(i);
            boolean is9patch = atlasRegion.splits != null;
            final ImageThumbnailBox thumb = is9patch ? new Image9patchThumbnailBox(stage, atlasRegion) : new ImageThumbnailBox(stage, atlasRegion);

            table.add(thumb).pad(3);


            if ((itemIter - 7) % 4 == 0) {
                table.row();
            }
            itemIter++;
        }

        for (String animationName : spineAnimations.keySet()) {
            if(!animationName.contains(searchText))continue;
            final SpineAnimationThumbnailBox thumb = new SpineAnimationThumbnailBox(stage, spineAnimations.get(animationName));

            table.add(thumb).size(50, 50).pad(3);
            if ((itemIter - 7) % 4 == 0) {

                table.row();
            }
            itemIter++;
        }

        for (String animationName : spriteAnimations.keySet()) {
            if(!animationName.contains(searchText))continue;
            final SpriteAnimationThumbnailBox thumb = new SpriteAnimationThumbnailBox(stage, animationName);

            table.add(thumb).size(50, 50).pad(3);
            if ((itemIter - 7) % 4 == 0) {

                table.row();
            }
            itemIter++;
        }

        for (String animationName : spriterAnimations.keySet()) {
            if(!animationName.contains(searchText))continue;
            final SpriterAnimationThumbnailBox thumb = new SpriterAnimationThumbnailBox(stage, animationName);

            table.add(thumb).size(50, 50).pad(3);
            if ((itemIter - 7) % 4 == 0) {

                table.row();
            }
            itemIter++;
        }


        addActor(container);


    }
    private void drawItems() {
        table.clearChildren();
        int itemIter = 0;
        for (int i = 0; i < atlas.getRegions().size; i++) {
            TextureAtlas.AtlasRegion atlasRegion = atlas.getRegions().get(i);
            boolean is9patch = atlasRegion.splits != null;
            final ImageThumbnailBox thumb = is9patch ? new Image9patchThumbnailBox(stage, atlasRegion) : new ImageThumbnailBox(stage, atlasRegion);

            table.add(thumb).pad(3);


            if ((itemIter - 7) % 4 == 0) {
                table.row();
            }
            itemIter++;
        }

        for (String animationName : spineAnimations.keySet()) {
            final SpineAnimationThumbnailBox thumb = new SpineAnimationThumbnailBox(stage, spineAnimations.get(animationName));

            table.add(thumb).size(50, 50).pad(3);
            if ((itemIter - 7) % 4 == 0) {

                table.row();
            }
            itemIter++;
        }

        for (String animationName : spriteAnimations.keySet()) {
            final SpriteAnimationThumbnailBox thumb = new SpriteAnimationThumbnailBox(stage, animationName);

            table.add(thumb).size(50, 50).pad(3);
            if ((itemIter - 7) % 4 == 0) {

                table.row();
            }
            itemIter++;
        }

        for (String animationName : spriterAnimations.keySet()) {
            final SpriterAnimationThumbnailBox thumb = new SpriterAnimationThumbnailBox(stage, animationName);

            table.add(thumb).size(50, 50).pad(3);
            if ((itemIter - 7) % 4 == 0) {

                table.row();
            }
            itemIter++;
        }


        addActor(container);


    }
}
