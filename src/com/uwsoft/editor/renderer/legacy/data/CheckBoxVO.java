package com.uwsoft.editor.renderer.data;

public class CheckBoxVO extends MainItemVO {
	
	public String 	text 	= "Label";
	public String	style	=  "";
	
	public CheckBoxVO() {
		super();
	}
	
	public CheckBoxVO(CheckBoxVO vo) {
		super(vo);
		text 	= new String(vo.text);
		style 	= new String(vo.style);
	}
}
