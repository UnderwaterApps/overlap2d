package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.renderer.IResource;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class ImageItem extends Image implements IBaseItem {

    public SimpleImageVO dataVO;
    public IResource rm;
    public float mulX = 1f;
    public float mulY = 1f;
    protected int layerIndex = 0;
    private CustomVariables customVariables = new CustomVariables();
    private boolean isLockedByLayer = false;
    private CompositeItem parentItem = null;

    public ImageItem(SimpleImageVO vo, IResource rm, CompositeItem parent) {
        this(vo, rm);
        setParentItem(parent);
    }

    public ImageItem(SimpleImageVO vo, IResource rm) {
        super(rm.getAsset(vo.imageName));
        init(vo, rm);
    }

    protected ImageItem(SimpleImageVO vo, IResource rm, NinePatch ninePatch) {
        super(ninePatch);
        init(vo, rm);
    }

    private void init(SimpleImageVO vo, IResource rm) {
        dataVO = vo;
        this.rm = rm;
        setX(dataVO.x);
        setY(dataVO.y);
        setScaleX(dataVO.scaleX);
        setScaleY(dataVO.scaleY);
        customVariables.loadFromString(dataVO.customVars);
        this.setRotation(dataVO.rotation);
        if (dataVO.zIndex < 0) dataVO.zIndex = 0;
        if (dataVO.tint == null) {
            setTint(new Color(1, 1, 1, 1));
        } else {
            setTint(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
        }

    }

    public void setTint(Color tint) {
        float[] clr = new float[4];
        clr[0] = tint.r;
        clr[1] = tint.g;
        clr[2] = tint.b;
        clr[3] = tint.a;
        this.getDataVO().tint = clr;
        this.setColor(tint);
    }

    public SimpleImageVO getDataVO() {
        //updateDataVO();
        return dataVO;
    }


    public void renew() {
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        setScaleX(dataVO.scaleX);
        setScaleY(dataVO.scaleY);
        setRotation(dataVO.rotation);
        setColor(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]);
        customVariables.loadFromString(dataVO.customVars);
    }

    @Override
    public boolean isLockedByLayer() {
        return isLockedByLayer;
    }

    @Override
    public void setLockByLayer(boolean isLocked) {
        isLockedByLayer = isLocked;
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    public void updateDataVO() {
        dataVO.x = getX() / this.mulX;
        dataVO.y = getY() / this.mulY;
        dataVO.scaleX = getScaleX();
        dataVO.scaleY = getScaleY();
        dataVO.rotation = getRotation();

        if (getZIndex() >= 0) {
            dataVO.zIndex = getZIndex();
        }

        if (dataVO.layerName == null || dataVO.layerName.equals("")) {
            dataVO.layerName = "Default";
        }
        dataVO.customVars = customVariables.saveAsString();
    }

    public void applyResolution(float mulX, float mulY) {
        this.mulX = mulX;
        this.mulY = mulY;
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        setScaleX(dataVO.scaleX );
        setScaleY(dataVO.scaleY);
        updateDataVO();
    }

    @Override
    public int getLayerIndex() {
        return layerIndex;
    }

    @Override
    public void setLayerIndex(int index) {
        layerIndex = index;
    }

    public CompositeItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(CompositeItem parentItem) {
        this.parentItem = parentItem;
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }


    public CustomVariables getCustomVariables() {
        return customVariables;
    }
}
