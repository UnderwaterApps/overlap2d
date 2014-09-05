package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.uwsoft.editor.renderer.IResource;
import com.uwsoft.editor.renderer.data.Image9patchVO;

public class Image9patchItem extends ImageItem implements IBaseItem {

    private final int[] splits;

    public Image9patchItem(Image9patchVO vo, IResource rm) {
        this(vo, rm, (TextureAtlas.AtlasRegion) rm.getAsset(vo.imageName));
    }

    private Image9patchItem(Image9patchVO vo, IResource rm, TextureAtlas.AtlasRegion atlasRegion) {
        super(vo, rm, new NinePatch(atlasRegion, atlasRegion.splits[0], atlasRegion.splits[1], atlasRegion.splits[2], atlasRegion.splits[3]));
        splits = atlasRegion.splits;
    }

    public Image9patchItem(Image9patchVO vo, IResource rm, CompositeItem parent) {
        this(vo, rm);
        Image9patchVO image9patchVO = (Image9patchVO) dataVO;
        image9patchVO.width = image9patchVO.width == 0 ? getWidth() : image9patchVO.width;
        image9patchVO.height = image9patchVO.height == 0 ? getHeight() : image9patchVO.height;
        setParent(parent);
        setWidth(image9patchVO.width);
        setHeight(image9patchVO.height);
    }

    public void renew() {
        Image9patchVO image9patchVO = (Image9patchVO) dataVO;
        setWidth(image9patchVO.width * mulX);
        setHeight(image9patchVO.height * mulY);
        super.renew();
    }

    public void updateDataVO() {
        Image9patchVO image9patchVO = (Image9patchVO) dataVO;
        image9patchVO.width = getWidth() / mulX;
        image9patchVO.height = getHeight() / mulY;
        super.updateDataVO();
    }

    public void applyResolution(float mulX, float mulY) {
        Image9patchVO image9patchVO = (Image9patchVO) dataVO;
        setWidth(mulX * image9patchVO.width);
        setHeight(mulY * image9patchVO.height);
        super.applyResolution(mulX, mulY);
    }

    @Override
    public void setHeight(float height) {
        if (splits != null && height < splits[2] + splits[3]) {
            height = splits[2] + splits[3];
        }
        super.setHeight(height);
    }

    @Override
    public void setWidth(float width) {
        if (splits != null && width < splits[0] + splits[1]) {
            width = splits[0] + splits[1];
        }
        super.setWidth(width);
    }

    @Override
    public void setScaleX(float scaleX) {
        if (splits != null && getWidth() * scaleX < splits[0] + splits[1]) {
            return;
        }
        super.setScaleX(scaleX);
    }

    @Override
    public void setScaleY(float scaleY) {
        if (splits != null && getHeight() * scaleY < splits[2] + splits[3]) {
            return;
        }
        super.setScaleY(scaleY);
    }
}
