package com.uwsoft.editor.renderer.data;

public class LabelVO extends MainItemVO {
	
	public String 	text 	= "Label";
	public String	style	=  "";
	public int		size;
	
	public LabelVO() {
		super();
	}
	
	public LabelVO(LabelVO vo) {
		super(vo);
		text 	= new String(vo.text);
		style 	= new String(vo.style);
		size 	= vo.size;
	}
}
