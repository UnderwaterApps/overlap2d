package com.uwsoft.editor.renderer.data;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.spriter.SpriterComponent;

public class SpriterVO extends MainItemVO {

    public int 	entity;
    public int 	animation;
    public String animationName = "";

    //wtf is this?
    public float scale	=	1f;

    public SpriterVO() {

    }

    public SpriterVO(SpriterVO vo) {
        super(vo);
        entity 			= vo.entity;
        animation		= vo.animation;
        animationName 	= vo.animationName;
        scale 			= vo.scale;
    }

    @Override
    public void loadFromEntity(Entity entity) {
        super.loadFromEntity(entity);

        SpriterComponent spriterComponent = entity.getComponent(SpriterComponent.class);
        animationName = spriterComponent.animationName;
        animation = spriterComponent.animation;
    }

}
