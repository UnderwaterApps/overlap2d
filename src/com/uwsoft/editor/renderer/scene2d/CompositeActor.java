package com.uwsoft.editor.renderer.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Created by azakhary on 7/26/2015.
 */
public class CompositeActor extends Group {

    private IResourceRetriever ir;

    private float pixelsPerWU;

    protected CompositeItemVO vo;

    protected HashMap<String, Actor> itemMap = new HashMap<String, Actor>();
    private HashMap<Integer, Actor> indexes = new HashMap<Integer, Actor>();
    private HashMap<String, LayerItemVO> layerMap = new HashMap<String, LayerItemVO>();
    private HashMap<String, Array<Actor>> itemLayerMap = new HashMap<String, Array<Actor>>();

    public CompositeActor(CompositeItemVO vo, IResourceRetriever ir) {
        this.ir= ir;
        this.vo = vo;

        pixelsPerWU = ir.getProjectVO().pixelToWorld;
        makeLayerMap(vo);
        build(vo);
    }

    private void makeLayerMap(CompositeItemVO vo) {
        layerMap.clear();
        itemLayerMap.clear();
        for(int i = 0; i < vo.composite.layers.size(); i++) {
            layerMap.put(vo.composite.layers.get(i).layerName,vo.composite.layers.get(i));
            itemLayerMap.put(vo.composite.layers.get(i).layerName, new Array<Actor>());
        }
    }

    private void build(CompositeItemVO vo) {
        buildImages(vo.composite.sImages);
        build9PatchImages(vo.composite.sImage9patchs);
        buildLabels(vo.composite.sLabels);
        buildComposites(vo.composite.sComposites);
        processZIndexes();
        recalculateSize();

        if(vo.tags != null && Arrays.asList(vo.tags).contains("button")) {
            addListener(new ButtonClickListener());
        }
    }

    private void buildComposites(ArrayList<CompositeItemVO> composites) {
        for(int i = 0; i < composites.size(); i++) {
            CompositeActor actor = new CompositeActor(composites.get(i), ir);
            processMain(actor, composites.get(i));
            addActor(actor);
        }
    }

    private void buildImages(ArrayList<SimpleImageVO> images) {
        for(int i = 0; i < images.size(); i++) {
            Image image = new Image(ir.getTextureRegion(images.get(i).imageName));
            processMain(image, images.get(i));
            addActor(image);
        }
    }

    private void build9PatchImages(ArrayList<Image9patchVO> patches) {
        for(int i = 0; i < patches.size(); i++) {
            TextureAtlas.AtlasRegion region = (TextureAtlas.AtlasRegion) ir.getTextureRegion(patches.get(i).imageName);
            NinePatch ninePatch = new NinePatch(region, region.splits[0], region.splits[1], region.splits[2], region.splits[3]);
            Image image = new Image(ninePatch);
            image.setWidth(patches.get(i).width*pixelsPerWU);
            image.setHeight(patches.get(i).height*pixelsPerWU);
            processMain(image, patches.get(i));
            addActor(image);
        }
    }

    private void buildLabels(ArrayList<LabelVO> labels) {
        for(int i = 0; i < labels.size(); i++) {
            Label.LabelStyle style = new Label.LabelStyle(ir.getBitmapFont(labels.get(i).style, labels.get(i).size), Color.WHITE);
            Label label = new Label(labels.get(i).text, style);
            label.setAlignment(labels.get(i).align);
            label.setWidth(labels.get(i).width*pixelsPerWU);
            label.setHeight(labels.get(i).height*pixelsPerWU);
            processMain(label, labels.get(i));
            addActor(label);
        }
    }

    private void processMain(Actor actor, MainItemVO vo) {
        actor.setPosition(vo.x*pixelsPerWU, vo.y*pixelsPerWU);
        actor.setOrigin(vo.originX*pixelsPerWU, vo.originY*pixelsPerWU);
        actor.setScale(vo.scaleX, vo.scaleY);
        actor.setRotation(vo.rotation);
        actor.setColor(new Color(vo.tint[0], vo.tint[1], vo.tint[2], vo.tint[3]));

        if(vo.itemIdentifier != null && vo.itemIdentifier.length() > 0) {
            itemMap.put(vo.itemIdentifier, actor);
        }

        indexes.put(getLayerIndex(vo.layerName)+vo.zIndex, actor);
        itemLayerMap.get(vo.layerName).add(actor);

        if(layerMap.get(vo.layerName).isVisible) {
            actor.setVisible(true);
        } else {
            actor.setVisible(false);
        }
    }

    private int getLayerIndex(String name) {
        return vo.composite.layers.indexOf(layerMap.get(name));
    }

    private void processZIndexes() {
        Object[] indexArray = indexes.keySet().toArray();
        Arrays.sort(indexArray);

        for(int i = 0; i < indexArray.length; i++) {
            indexes.get(indexArray[i]).setZIndex(i);
        }
    }

    public Actor getItem(String id) {
        return itemMap.get(id);
    }

    public void recalculateSize() {
        float lowerX = 0, lowerY = 0, upperX = 0, upperY = 0;
        for (int i = 0; i < getChildren().size; i++) {
            Actor value = getChildren().get(i);
            if (i == 0) {
                if (value.getScaleX() > 0 && value.getWidth() * value.getScaleX() > 0) {
                    lowerX = value.getX();
                    upperX = value.getX() + value.getWidth() * value.getScaleX();
                } else {
                    upperX = value.getX();
                    lowerX = value.getX() + value.getWidth() * value.getScaleX();
                }

                if (value.getScaleY() > 0 && value.getHeight() * value.getScaleY() > 0) {
                    lowerY = value.getY();
                    upperY = value.getY() + value.getHeight() * value.getScaleY();
                } else {
                    upperY = value.getY();
                    lowerY = value.getY() + value.getHeight() * value.getScaleY();
                }
            }
            if (value.getScaleX() > 0 && value.getWidth() > 0) {
                if (lowerX > value.getX()) lowerX = value.getX();
                if (upperX < value.getX() + value.getWidth() * value.getScaleX())
                    upperX = value.getX() + value.getWidth() * value.getScaleX();
            } else {
                if (upperX < value.getX()) upperX = value.getX();
                if (lowerX > value.getX() + value.getWidth() * value.getScaleX())
                    lowerX = value.getX() + value.getWidth() * value.getScaleX();
            }
            if (value.getScaleY() > 0 && value.getHeight() * value.getScaleY() > 0) {
                if (lowerY > value.getY()) lowerY = value.getY();
                if (upperY < value.getY() + value.getHeight() * value.getScaleY())
                    upperY = value.getY() + value.getHeight() * value.getScaleY();
            } else {
                if (upperY < value.getY()) upperY = value.getY();
                if (lowerY > value.getY() + value.getHeight() * value.getScaleY())
                    lowerY = value.getY() + value.getHeight() * value.getScaleY();
            }

        }

        setWidth(upperX - 0);
        setHeight(upperY - 0);
    }

    public void setLayerVisibility(String layerName, boolean isVisible) {
        layerMap.get(layerName).isVisible = isVisible;
        for(Actor actor: itemLayerMap.get(layerName)) {
            actor.setVisible(isVisible);
        }
    }

    /**
     * return all custom vars for this composite
     * @return
     */
    public CustomVariables getCustomVars() {
        CustomVariables vars = new CustomVariables();
        vars.loadFromString(vo.customVars);
        return vars;
    }

    /**
     * gets custom var value by key
     * @param key
     * @return
     */
    public String getCustomVar(String key) {
        CustomVariables vars = new CustomVariables();
        vars.loadFromString(vo.customVars);
        return vars.getStringVariable(key);
    }

    /**
     * gets array of all tags on this composite
     * @return
     */
    public String[] getTags() {
        return vo.tags;
    }

    /**
     * get's list of children that contain a specified tag.
     * Only works for Composite children.
     * Does not yet go in depth.
     *
     * @param tag
     * @return
     */
    public Array<CompositeActor> getItemsByTag(String tag) {
        Array<CompositeActor> items = new Array<CompositeActor>();
        for(Actor actor: itemMap.values()) {
            if(actor instanceof CompositeActor) {
                CompositeActor tmp = (CompositeActor) actor;
                if(Arrays.asList(tmp.getTags()).contains(tag)) {
                    items.add(tmp);
                }
            }
        }

        return items;
    }

    /**
     * returns children of this actor that are on specified layer
     * @param layerName
     * @return
     */
    public Array<Actor> getItemsByLayer(String layerName) {
        return itemLayerMap.get(layerName);
    }

    public CompositeItemVO getVo() {
        return vo;
    }
}
