package com.uwsoft.editor.renderer.actor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.utils.CustomVariables;

/**
 * Created by sargis on 8/19/14.
 */
public class SpriteAnimation extends Actor implements IBaseItem {
    private final Essentials essentials;
    public AnimationCompleteListener animationCompleteListener;
    public float mulX = 1f;
    public float mulY = 1f;
    public SpriteAnimationVO dataVO;
    public boolean looping;
    protected int layerIndex = 0;
    protected boolean reverse = false;
    private boolean isLockedByLayer = false;
    private CompositeItem parentItem = null;
    private float lastFrame;
    private int frameIndex;
    private TextureAtlas.AtlasRegion[] animationAtlasRegions;
    private int frameHeight;
    private int frameWidth;
    private int framesCount;
    private int firstFrame;
    private int endFrame;
    private float frameDuration;
    private int playingTo = -1;
    private float normalSpeed = 1.0f;
    private boolean paused = true;
    private Map<String, Animation> animations = new HashMap<>();

    private CustomVariables customVariables = new CustomVariables();
    private String currentAnimationName = "";

    private Body body;

    public SpriteAnimation(SpriteAnimationVO vo, Essentials e, CompositeItem parent) {
        this(vo, e);
        setParentItem(parent);
    }

    public SpriteAnimation(SpriteAnimationVO vo, Essentials e) {
        essentials = e;
        dataVO = vo;
        setX(dataVO.x);
        setY(dataVO.y);
        setScaleX(dataVO.scaleX);
        setScaleY(dataVO.scaleY);
        customVariables.loadFromString(dataVO.customVars);
        animations = Animation.constructJsonObject(dataVO.animations);
        this.setRotation(dataVO.rotation);

        if (dataVO.zIndex < 0) dataVO.zIndex = 0;

        if (dataVO.tint == null) {
            setTint(new Color(1, 1, 1, 1));
        } else {
            setTint(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
        }

        initSpriteAnimation();
    }

    public String getCurrentAnimationName() {
        return currentAnimationName;
    }

    private void initSpriteAnimation() {
        lastFrame = 0;
        frameIndex = -1;
        setOriginX(0);
        setOriginY(0);

        Array<TextureAtlas.AtlasRegion> regions = essentials.rm.getSpriteAnimation(dataVO.animationName).getRegions();

        animationAtlasRegions = new TextureAtlas.AtlasRegion[regions.size];

        frameHeight = regions.get(0).originalHeight;
        frameWidth = regions.get(0).originalWidth;

        setWidth(frameWidth);
        setHeight(frameHeight);

        framesCount = regions.size;
        for (int i = 0; i < regions.size; i++) {
            String regName = regions.get(i).name;

            animationAtlasRegions[regNameToFrame(regName) - 1] = regions.get(i);
        }

        setAnimation(dataVO.fps, true);
    }

    public int getFramesCount() {
        return animationAtlasRegions.length;
    }

    public void setAnimation(boolean looping) {
        setAnimation(24, looping);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1, 1, 1, parentAlpha * getColor().a);
        batch.draw(animationAtlasRegions[frameIndex], getX() + animationAtlasRegions[frameIndex].offsetX, getY() + animationAtlasRegions[frameIndex].offsetY,
                getOriginX(), getOriginY(), animationAtlasRegions[frameIndex].getRegionWidth(),
                animationAtlasRegions[frameIndex].getRegionHeight(), getScaleX(), getScaleY(), getRotation());
        super.draw(batch, parentAlpha);
    }

    public TextureAtlas.AtlasRegion getAtlasRegionAt(int index) {
        return animationAtlasRegions[index];
    }

    public void setAnimation(String name) {
        Animation animation = animations.get(name);
        if (animation != null) {
            setAnimation(animation.startFrame, animation.endFrame, dataVO.fps, looping);
            currentAnimationName = name;
        }
    }

    public void setAnimation(int fps, boolean looping) {
        firstFrame = 0;
        endFrame = framesCount - 1;
        playingTo = endFrame;

        frameIndex = 0;

        frameDuration = 1.0f / fps;
        this.looping = looping;

        reverse = firstFrame > endFrame;
    }

    public void setAnimation(int firstFrame, int endFrame, int fps, boolean looping) {
        this.firstFrame = firstFrame;
        this.endFrame = endFrame;
        playingTo = this.endFrame;

        frameIndex = firstFrame;

        this.frameDuration = 1.0f / fps;
        this.looping = looping;

        if (firstFrame > endFrame) {
            reverse = true;
        } else {
            reverse = false;
        }
    }

    private int regNameToFrame(String name) {
        final Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
        Matcher matcher = lastIntPattern.matcher(name);
        if (matcher.find()) {
            String someNumberStr = matcher.group(1);
            return Integer.parseInt(someNumberStr);
        }
        throw new RuntimeException("Frame name should be something like this '*0001', but not " + name + ".");
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

    @Override
    public MainItemVO getDataVO() {
        return dataVO;
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

    @Override
    public void renew() {
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        setScaleX(dataVO.scaleX);
        setScaleY(dataVO.scaleY);
        setRotation(dataVO.rotation);
        setColor(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]);
        if (currentAnimationName.isEmpty()) {
            setAnimation(dataVO.fps, looping);
        } else {
            setAnimation(currentAnimationName);
        }
        customVariables.loadFromString(dataVO.customVars);
        animations = Animation.constructJsonObject(dataVO.animations);
    }

    @Override
    public int getLayerIndex() {
        return layerIndex;
    }

    @Override
    public void setLayerIndex(int index) {
        layerIndex = index;
    }

    @Override
    public void updateDataVO() {
        dataVO.x = getX() / this.mulX;
        dataVO.y = getY() / this.mulY;
        dataVO.rotation = getRotation();

        if (getZIndex() >= 0) {
            dataVO.zIndex = getZIndex();
        }

        if (dataVO.layerName == null || dataVO.layerName.equals("")) {
            dataVO.layerName = "Default";
        }

        dataVO.customVars = customVariables.saveAsString();
        dataVO.animations = Animation.constructJsonString(animations);
    }

    @Override
    public void applyResolution(float mulX, float mulY) {
        this.mulX = mulX;
        this.mulY = mulY;
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        updateDataVO();
    }

    @Override
    public CompositeItem getParentItem() {
        return parentItem;
    }

    @Override
    public void setParentItem(CompositeItem parent) {
        parentItem = parent;
    }

    @Override
    public void setScale(float scale) {
        super.setScale(scale, scale);
        dataVO.scaleX = scale;
        dataVO.scaleY = scale;
        renew();
    }

    @Override
    public void act(float delta) {
        if (paused) return;

        float usedFrameDuration = frameDuration / normalSpeed;

        lastFrame += delta;
        if (lastFrame > usedFrameDuration) {
            if (!paused) {
                if (reverse) {
                    frameIndex--;
                } else {
                    frameIndex++;
                }
            }
            lastFrame = 0;
            if ((frameIndex > playingTo && !reverse) || (frameIndex < playingTo && reverse)) // animation ended
            {
                if (looping) {
                    frameIndex = firstFrame;
                } else if (animationCompleteListener != null) {
                    animationCompleteListener.complete(this);
                    paused = true;
                    if (frameIndex > endFrame) frameIndex = endFrame;
                } else {
                    paused = true;
                    if (frameIndex > endFrame) frameIndex = endFrame;
                }
            }
            if (!looping && (playingTo != -1 && frameIndex == playingTo)) {
                playingTo = -1;
                paused = true;
                if (animationCompleteListener != null) {
                    animationCompleteListener.complete(this);
                }
            }
        }

        super.act(delta);
    }

    public void pause() {
        paused = true;
    }

    public void start() {
        frameIndex = 0;
        paused = false;
    }

    public void resume() {
        paused = false;
    }

    public void playTo(int frame) {
        int playingToCurr = frame;
        if (playingToCurr >= framesCount) playingToCurr = framesCount - 1;
        if (playingToCurr < 0) playingToCurr = 0;
        if (frameIndex == playingToCurr) return;
        if (frameIndex > playingToCurr) {
            reverse = true;
        } else {
            reverse = false;
        }
        playingTo = playingToCurr;
        paused = false;
    }

    public void setCurrFrame(int frame) {
        frameIndex = frame;
        playingTo = -1;
        if (frameIndex >= framesCount) frameIndex = framesCount - 1;
        if (frameIndex < 0) frameIndex = 0;
    }

    private int getFrameNumberFromName(String name) {
        int iter = 0;
        String result = "";
        while (Character.isDigit(name.charAt(iter))) {
            result += name.charAt(iter);
            iter++;
        }

        return Integer.parseInt(result);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void dispose() {
        if(essentials.world != null && getBody() != null) essentials.world.destroyBody(getBody());
        setBody(null);
    }

    public void setNormalSpeed(float speed) {
        normalSpeed = speed;
    }

    public int getCurrentFrameIndex() {
        return this.frameIndex;
    }

    public void setCompletionListener(AnimationCompleteListener animationCompleteListener) {
        this.animationCompleteListener = animationCompleteListener;
    }

    public CustomVariables getCustomVariables() {
        return customVariables;
    }

    public Map<String, Animation> getAnimations() {
        return animations;
    }


    public interface AnimationCompleteListener {
        public void complete(SpriteAnimation sprite);
    }

    public static class Animation {
        public int startFrame;
        public int endFrame;
        public String name;

        public Animation(int startFrame, int endFrame, String name) {
            this.startFrame = startFrame;
            this.endFrame = endFrame;
            this.name = name;
        }

        public Animation() {
        }

        public static String constructJsonString(Map<String, Animation> animations) {
            String str = "";
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            str = json.toJson(animations);
            return str;
        }

        public static Map<String, Animation> constructJsonObject(String animations) {
            if (animations.isEmpty()) {
                return new HashMap<>();
            }
            Json json = new Json();
            return json.fromJson(HashMap.class, animations);
        }
    }
}
