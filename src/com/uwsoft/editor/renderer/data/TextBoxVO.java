package com.uwsoft.editor.renderer.data;

public class TextBoxVO extends MainItemVO  {
	
	public String defaultText = "";
	public float width = 0;
	public float height = 0;
	public String	style	=  "";
	
	public TextBoxVO() {
		super();
	}
	
	public TextBoxVO(TextBoxVO vo) {
		super(vo);
		defaultText = new String(vo.defaultText);
		width 		= vo.width;
		height 		= vo.height;
		style 		= new String(vo.style);
	}
}
