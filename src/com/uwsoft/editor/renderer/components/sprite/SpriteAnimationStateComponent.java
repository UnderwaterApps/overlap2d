package com.uwsoft.editor.renderer.components.sprite;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.data.FrameRange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpriteAnimationStateComponent implements Component {
    public Array<TextureAtlas.AtlasRegion> allRegions;
	public Animation currentAnimation;
	public float time = 0.0f;

    public  boolean paused = false;

    public SpriteAnimationStateComponent(Array<TextureAtlas.AtlasRegion> allRegions) {
        this.allRegions = sortAndGetRegions(allRegions);
    }
	
	public Animation get() {
		return currentAnimation;
	}

    public void set(SpriteAnimationComponent sac) {
        set(sac.frameRangeMap.get(sac.currentAnimation), sac.fps, sac.playMode);
    }

    public void set(FrameRange range, int fps, Animation.PlayMode playMode) {
        Array<TextureAtlas.AtlasRegion> textureRegions = new Array<TextureAtlas.AtlasRegion>(range.endFrame - range.startFrame + 1);
        for (int r = range.startFrame; r <= range.endFrame; r++) {
            textureRegions.add(allRegions.get(r));
        }
        currentAnimation =  new Animation(1f/fps, textureRegions, playMode);
        time = 0.0f;
    }

    private Array<TextureAtlas.AtlasRegion> sortAndGetRegions(Array<TextureAtlas.AtlasRegion> regions) {
        regions.sort(new SortRegionsComparator());

        return regions;
    }

    private class SortRegionsComparator implements Comparator<TextureAtlas.AtlasRegion> {
        @Override
        public int compare(TextureAtlas.AtlasRegion o1, TextureAtlas.AtlasRegion o2) {
            int index1 = regNameToFrame(o1.name);
            int index2 = regNameToFrame(o2.name);
            return index1 < index2 ? -1 : index1 == index2 ? 0 : 1;
        }
    }

    private int regNameToFrame(String name) {
        final Pattern lastIntPattern = Pattern.compile("[^0-9]+([0-9]+)$");
        Matcher matcher = lastIntPattern.matcher(name);
        if (matcher.find()) {
            String someNumberStr = matcher.group(1);
            return Integer.parseInt(someNumberStr);
        }
        throw new RuntimeException(
                "Frame name should be something like this '*0001', but not "
                        + name + ".");
    }
}
