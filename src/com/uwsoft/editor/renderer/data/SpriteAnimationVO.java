package com.uwsoft.editor.renderer.data;

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
}
