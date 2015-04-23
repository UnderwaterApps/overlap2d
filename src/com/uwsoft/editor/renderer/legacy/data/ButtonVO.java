package com.uwsoft.editor.renderer.data;

public class ButtonVO extends MainItemVO  {
	
	public String text 		= "Button";
	public String	style	=  "";
	
	public ButtonVO() {
		super();
	}
	
	public ButtonVO(ButtonVO vo) {
		super(vo);
		text = new String(vo.text);
		style = new String(vo.style);
	}
}
