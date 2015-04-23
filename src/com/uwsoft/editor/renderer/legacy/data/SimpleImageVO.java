package com.uwsoft.editor.renderer.legacy.data;

public class SimpleImageVO extends MainItemVO {
	public String imageName = "";
	
	public SimpleImageVO() {
		super();
	}
	
	public SimpleImageVO(SimpleImageVO vo) {
		super(vo);
		imageName = new String(vo.imageName);
	}
}
