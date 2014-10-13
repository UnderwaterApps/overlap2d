package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.Image9patchVO;

public class Image9patchItem extends ImageItem implements IBaseItem {

    private final int[] splits;
    private Image9patchVO image9patchVO;

    private Body body;

    public Image9patchItem(Image9patchVO vo, Essentials e) {
        this(vo, e, (TextureAtlas.AtlasRegion) e.rm.getTextureRegion(vo.imageName));
    }

    private Image9patchItem(Image9patchVO vo, Essentials e, TextureAtlas.AtlasRegion atlasRegion) {
        super(vo, e, new NinePatch(atlasRegion, atlasRegion.splits[0], atlasRegion.splits[1], atlasRegion.splits[2], atlasRegion.splits[3]));
        splits = atlasRegion.splits;
        image9patchVO = vo;
    }

    public Image9patchItem(Image9patchVO vo, Essentials e, CompositeItem parent) {
        this(vo, e);
        image9patchVO.width = image9patchVO.width == 0 ? getWidth() : image9patchVO.width;
        image9patchVO.height = image9patchVO.height == 0 ? getHeight() : image9patchVO.height;
        setParentItem(parent);
        setWidth(image9patchVO.width);
        setHeight(image9patchVO.height );
    }

    @Override
    public void setScaleX(float scaleX) {
        float value = getWidth() * scaleX;
        if (splits != null && value < splits[0] + splits[1]) {
            value = splits[0] + splits[1];
        }
        setWidth(value);
    }

    @Override
    public void setScaleY(float scaleY) {
        float value = getHeight() * scaleY;
        if (splits != null && value < splits[2] + splits[3]) {
            value = splits[2] + splits[3];
        }
        setHeight(value);
    }
    
    @Override
    public float getMinWidth() {
    	return splits[0] + splits[1];
    }
    
    @Override
    public float getMinHeight() {
    	return splits[0] + splits[1];
    }    

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void dispose() {
        if (essentials.world != null && getBody() != null) essentials.world.destroyBody(getBody());
        setBody(null);
    }

    public void setScale(float scaleX, float scaleY) {
        setScaleX(scaleX);
        setScaleY(scaleY);
    }


    public void renew() {
        setWidth(image9patchVO.width * mulX);
        setHeight(image9patchVO.height * mulY);
        super.renew();
    }

    public void updateDataVO() {
        image9patchVO.width = getWidth() / mulX;
        image9patchVO.height = getHeight() / mulY;
        super.updateDataVO();

    }

    public void applyResolution(float mulX, float mulY) {
        Image9patchVO image9patchVO = (Image9patchVO) dataVO;
        setWidth(image9patchVO.width * mulX);
        setHeight(image9patchVO.height * mulY);
        super.applyResolution(mulX, mulY);
    }
}
