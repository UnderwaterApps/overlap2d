package com.uwsoft.editor.renderer.legacy.data;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.spine.SpineDataComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;

public class SpriteAnimationVO extends MainItemVO {

    public String animationName = "";
    public int fps = 24;
    public String animations = "";

    public SpriteAnimationVO() {

    }

    public SpriteAnimationVO(SpriteAnimationVO vo) {
        super(vo);
        animationName = vo.animationName;
        fps = vo.fps;
        animations = vo.animations;
    }

    @Override
    public void loadFromEntity(Entity entity) {
        super.loadFromEntity(entity);

        SpriteAnimationComponent spriteAnimationComponent = entity.getComponent(SpriteAnimationComponent.class);
        animationName = spriteAnimationComponent.animationName;
        fps = spriteAnimationComponent.fps;
        animations = spriteAnimationComponent.animations;
    }
}
