package com.uwsoft.editor.gdx.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.uwsoft.editor.data.SpineAnimData;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.thumbnailbox.Image9patchThumbnailBox;
import com.uwsoft.editor.gdx.ui.thumbnailbox.ImageThumbnailBox;
import com.uwsoft.editor.gdx.ui.thumbnailbox.SpineAnimationThumbnailBox;
import com.uwsoft.editor.gdx.ui.thumbnailbox.SpriteAnimationThumbnailBox;
import com.uwsoft.editor.gdx.ui.thumbnailbox.SpriterAnimationThumbnailBox;

public class AssetList extends Group {

    public AssetList(final UIStage s, float width, float height) {
        this.setWidth(width);
        this.setHeight(height);
        final Table container = new Table();
        Table table = new Table();
        container.setX(0);
        container.setY(0);
        container.setWidth(getWidth() - 1);
        container.setHeight(getHeight() - 20);
        final ScrollPane scroll = new ScrollPane(table, s.textureManager.editorSkin);
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
        TextureAtlas atlas = s.textureManager.getProjectAssetsList();

        HashMap<String, SpineAnimData> spineAnimations = s.textureManager.getProjectSpineAnimationsList();
        HashMap<String, TextureAtlas> spriteAnimations = s.textureManager.getProjectSpriteAnimationsList();
        HashMap<String, FileHandle> spriterAnimations = s.textureManager.getProjectSpriterAnimationsList();

        if (atlas == null) return;

        int itemIter = 0;
        for (int i = 0; i < atlas.getRegions().size; i++) {
            TextureAtlas.AtlasRegion atlasRegion = atlas.getRegions().get(i);
            boolean is9patch = atlasRegion.splits != null;
            final ImageThumbnailBox thumb = is9patch ? new Image9patchThumbnailBox(s, atlasRegion) : new ImageThumbnailBox(s, atlasRegion);

            table.add(thumb).pad(3);


            if ((itemIter - 7) % 4 == 0) {
                table.row();
            }
            itemIter++;
        }

        for (String animationName : spineAnimations.keySet()) {
            final SpineAnimationThumbnailBox thumb = new SpineAnimationThumbnailBox(s, spineAnimations.get(animationName));

            table.add(thumb).size(50, 50).pad(3);
            if ((itemIter - 7) % 4 == 0) {

                table.row();
            }
            itemIter++;
        }

        for (String animationName : spriteAnimations.keySet()) {
            final SpriteAnimationThumbnailBox thumb = new SpriteAnimationThumbnailBox(s, animationName);

            table.add(thumb).size(50, 50).pad(3);
            if ((itemIter - 7) % 4 == 0) {

                table.row();
            }
            itemIter++;
        }
        
        for (String animationName : spriterAnimations.keySet()) {
        	final SpriterAnimationThumbnailBox thumb = new SpriterAnimationThumbnailBox(s, animationName);
        	
        	table.add(thumb).size(50, 50).pad(3);
        	if ((itemIter - 7) % 4 == 0) {
        		
        		table.row();
        	}
        	itemIter++;
        }


        addActor(container);
    }
}
