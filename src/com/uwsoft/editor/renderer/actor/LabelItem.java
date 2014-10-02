package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.LabelVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class LabelItem extends Label implements IBaseItem {

    private static int labelDefaultSize = 12;
    private static BitmapFont font;
    private static LabelStyle style;
    public LabelVO dataVO;
    public Essentials essentials;
    public float mulX = 1f;
    public float mulY = 1f;
    protected int layerIndex = 0;
    private boolean isLockedByLayer = false;
    private CompositeItem parentItem = null;
    private String fontName;
    private int fontSize;
    private CustomVariables customVariables = new CustomVariables();

    private Body body;

    public LabelItem(LabelVO vo, Essentials e, CompositeItem parent) {
        this(vo, e);
        setParentItem(parent);
    }

    public LabelItem(LabelVO vo, Essentials e) {
        //super(vo.text, rm.getSkin(), vo.style.isEmpty()?"default":vo.style);
        super(vo.text, generateStyle(e.rm, vo.style, vo.size));
        dataVO = vo;
        this.essentials = e;
        setX(dataVO.x);
        setY(dataVO.y);
        		
        this.setRotation(dataVO.rotation);

        if (dataVO.zIndex < 0) dataVO.zIndex = 0;

        if (dataVO.tint == null) {
            setTint(new Color(1, 1, 1, 1));
        } else {
            setTint(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
        }
        if(dataVO.align==0){
        	setAlignment(Align.center);
        }else{
        	setAlignment(dataVO.align);
        }
       // pack();
       //layout();

        renew();
    }

    private static LabelStyle generateStyle(IResourceRetriever rManager, String fontName, int size) {

        if (size == 0) {
            size = labelDefaultSize;
        }
        style = new LabelStyle(rManager.getBitmapFont(fontName, size), null);
        return style;
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

    public LabelVO getDataVO() {
        //updateDataVO();
        return dataVO;
    }

    @Override
    public void renew() {
        if (!(fontName != null && fontName.equals(dataVO.style) && fontSize == dataVO.size)) {
            if (dataVO.size == 0) dataVO.size = labelDefaultSize;
            font = essentials.rm.getBitmapFont(dataVO.style, dataVO.size);
            style = new LabelStyle(font, null);
            setStyle(style);
            fontName = dataVO.style;
            fontSize = dataVO.size;
        }

        setText(dataVO.text);
        customVariables.loadFromString(dataVO.customVars);
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        setRotation(dataVO.rotation);
        setColor(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]);
        setScale(dataVO.scaleX, dataVO.scaleY);
        setAlignment(dataVO.align);
        pack();
        layout();
    }

    @Override
    public float getScaleX() {
        return getFontScaleX();
    }

    @Override
    public float getScaleY() {
        return getFontScaleY();
    }

    @Override
    public void setScaleX(float x) {
        if (x <= 0) return;
        //super.setScaleX(x);
        setFontScaleX(x);
    }

    @Override
    public void setScaleY(float y) {
        if (y <= 0) return;
        //super.setScaleY(y);
        setFontScaleY(y);
    }

    @Override
    public void setScale(float x, float y) {
        if (x <= 0 || y <= 0) return;
        //super.setScale(x, y);
        setFontScale(x, y);
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

        dataVO.y = getY() / this.mulY;
        dataVO.x = getX() / this.mulX;
        dataVO.rotation = getRotation();

        if (getZIndex() >= 0) {
            dataVO.zIndex = getZIndex();
        }

        if (dataVO.layerName == null || dataVO.layerName.equals("")) {
            dataVO.layerName = "Default";
        }

        dataVO.customVars = customVariables.saveAsString();

        dataVO.scaleX = getScaleX();
        dataVO.scaleY = getScaleY();
    }

    public void applyResolution(float mulX, float mulY) {
        this.mulX = mulX;
        this.mulY = mulY;
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        setScale(dataVO.scaleX, dataVO.scaleY);
        updateDataVO();
        pack();
        layout();
    }


    @Override
    public int getLayerIndex() {
        return layerIndex;
    }

    @Override
    public void setLayerIndex(int index) {
        layerIndex = index;
    }

    public void setStyle(String styleName, int size) {
    	dataVO.style = styleName;
    	dataVO.size = size;
    }
    public void setAlign(int align) {       
        dataVO.align = align;
    }

    public CompositeItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(CompositeItem parentItem) {
        this.parentItem = parentItem;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void dispose() {
        if(essentials.world != null && getBody() != null)essentials.world.destroyBody(getBody());
        setBody(null);
    }

    public CustomVariables getCustomVariables() {
        return customVariables;
    }

}
