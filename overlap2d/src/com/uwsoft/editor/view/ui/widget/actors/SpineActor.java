package com.uwsoft.editor.view.ui.widget.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;
import com.esotericsoftware.spine.attachments.WeightedMeshAttachment;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

public class SpineActor extends Actor {

    private String animationName;
    public SkeletonData skeletonData;
    private SkeletonRenderer renderer;
    private Skeleton skeleton;
    private AnimationState state;
    private IResourceRetriever irr;
    private SkeletonJson skeletonJson;
    private float minX = 0;
    private float minY = 0;


    public SpineActor(String animationName, IResourceRetriever irr) {
        this.irr = irr;
        this.renderer = new SkeletonRenderer();
        this.animationName = animationName;
        initSkeletonData();
        initSpine();
    }

    private void computeBoundBox() {
        skeleton.updateWorldTransform();
        minX = Float.MAX_VALUE;
        minY = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
        for (int i = 0, n = skeleton.getSlots().size; i < n; i++) {
            Slot slot = skeleton.getSlots().get(i);
            Attachment attachment = slot.getAttachment();
            if (attachment == null) continue;
            if (!((attachment instanceof RegionAttachment) || (attachment instanceof MeshAttachment) || (attachment instanceof WeightedMeshAttachment))) continue;
            float[] vertices = new float[0];
            if ((attachment instanceof RegionAttachment)) {
                RegionAttachment region = (RegionAttachment) attachment;
                region.updateWorldVertices(slot, false);
                vertices = region.getWorldVertices();
            }
            if ((attachment instanceof MeshAttachment)) {
                MeshAttachment region = (MeshAttachment) attachment;
                region.updateWorldVertices(slot, false);
                vertices = region.getWorldVertices();
            }
            if ((attachment instanceof WeightedMeshAttachment)) {
                WeightedMeshAttachment region = (WeightedMeshAttachment) attachment;
                region.updateWorldVertices(slot, false);
                vertices = region.getWorldVertices();
            }


            for (int ii = 0, nn = vertices.length; ii < nn; ii += 5) {
                minX = Math.min(minX, vertices[ii]);
                minY = Math.min(minY, vertices[ii + 1]);
                maxX = Math.max(maxX, vertices[ii]);
                maxY = Math.max(maxY, vertices[ii + 1]);
            }
        }

        setWidth(maxX - minX);
        setHeight(maxY - minY);
    }

    private void initSkeletonData() {
        skeletonJson = new SkeletonJson(irr.getSkeletonAtlas(animationName));
        skeletonData = skeletonJson.readSkeletonData((irr.getSkeletonJSON(animationName)));
    }

    private void initSpine() {
        BoneData root = skeletonData.getBones().get(0);
        root.setScale(getScaleX(), getScaleY());
        skeleton = new Skeleton(skeletonData);
        AnimationStateData stateData = new AnimationStateData(skeletonData);
        state = new AnimationState(stateData);
        computeBoundBox();
        setAnimation(skeletonData.getAnimations().get(0).getName());
    }

    public Array<Animation> getAnimations() {
        return skeletonData.getAnimations();
    }

    public void setAnimation(String animName) {
        state.setAnimation(0, animName, true);
    }

    public AnimationState getState() {
        return state;
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale);
        initSpine();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        renderer.draw((PolygonSpriteBatch)batch, skeleton);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        skeleton.updateWorldTransform(); //
        state.update(delta);
        state.apply(skeleton);
        skeleton.setPosition(getX() - minX, getY() - minY);
        super.act(delta);
    }
}
