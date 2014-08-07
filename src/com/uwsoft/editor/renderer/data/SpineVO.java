package com.uwsoft.editor.renderer.data;

public class SpineVO extends MainItemVO{
	
	public String animationName = "";

    public SpineVO() {

    }

	public SpineVO(SpineVO vo) {
		super(vo);
		animationName = new String(vo.animationName);
	}
}
