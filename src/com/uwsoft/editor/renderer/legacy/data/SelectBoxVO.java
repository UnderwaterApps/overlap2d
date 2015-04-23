package com.uwsoft.editor.renderer.data;

import java.util.ArrayList;

public class SelectBoxVO extends MainItemVO {
	public ArrayList<String>	list	=	new ArrayList<String>();
	public String	style	=  "";
	public float width = 0;
	public float height = 0;
	
	public SelectBoxVO() {
		super();
	}
	
	public SelectBoxVO(SelectBoxVO vo) {
		super(vo);
		width 		= vo.width;
		height 		= vo.height;
		style = new String(vo.style);
	}
}
