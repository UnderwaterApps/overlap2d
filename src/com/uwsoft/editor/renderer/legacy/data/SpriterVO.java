package com.uwsoft.editor.renderer.legacy.data;

public class SpriterVO extends MainItemVO {

    public int 	entity;
    public int 	animation;
    public String 	animationName = "";    
    public float 	scale	=	1f;

    public SpriterVO() {

    }

    public SpriterVO(SpriterVO vo) {
        super(vo);
        entity 			= vo.entity;
        animation		= vo.animation;
        animationName 	= vo.animationName;
        scale 			= vo.scale;
    }

}
