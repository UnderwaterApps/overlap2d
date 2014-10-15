package com.uwsoft.editor.gdx.ui.thumbnailbox;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.uwsoft.editor.gdx.stage.UIStage;

/**
 * Created by sargis on 8/21/14.
 */
public class Image9patchThumbnailBox extends ImageThumbnailBox {
    public Image9patchThumbnailBox(UIStage s, TextureAtlas.AtlasRegion region) {
        super(s, region);
    }

    protected void itemDropped(String assetName, float x, float y) {
        stage.sandboxStage.create9patchItem(assetName, x, y);
    }

    @Override
    public void initAdditionalListeners() {
        // do nothing for now
    }
}
