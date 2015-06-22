package com.uwsoft.editor.view.ui.widget.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

/**
 * Created by CyberJoe on 6/18/2015.
 */
public class SpriteAnimationActor extends Actor {

    private String animationName;
    private IResourceRetriever rm;

    private Animation animation;

    private float stateTime = 0;
    private int fps;

    private boolean paused = false;

    public SpriteAnimationActor(String animationName, IResourceRetriever rm) {
        this.animationName = animationName;
        this.rm = rm;
        animation = new Animation(1/24f, rm.getSpriteAnimation(animationName).getRegions());
        animation.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion region = animation.getKeyFrame(stateTime);
        setWidth(region.getRegionWidth());
        setHeight(region.getRegionHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion region = animation.getKeyFrame(stateTime);
        setWidth(region.getRegionWidth());
        setHeight(region.getRegionHeight());
        batch.setColor(getColor());
        batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    @Override
    public void act(float delta) {
        if(paused) return;
        stateTime+=delta;
    }

    public void setFPS(int fps) {
        this.fps = fps;
        animation.setFrameDuration(1f/fps);
    }

    public int getFPS() {
        return fps;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
