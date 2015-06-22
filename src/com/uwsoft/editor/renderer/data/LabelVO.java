package com.uwsoft.editor.renderer.data;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;

public class LabelVO extends MainItemVO {
	
	public String 	text 	= "Label";
	public String	style	=  "";
	public int		size;
	public int		align;

    public float width = 0;
    public float height = 0;

    public boolean multiline = false;
	
	public LabelVO() {
		super();
	}
	
	public LabelVO(LabelVO vo) {
		super(vo);
		text 	= new String(vo.text);
		style 	= new String(vo.style);
		size 	= vo.size;
		align 	= vo.align;
        width 	= vo.width;
        height 	= vo.height;
        multiline 	= vo.multiline;
	}

	@Override
	public void loadFromEntity(Entity entity) {
		super.loadFromEntity(entity);
		LabelComponent labelComponent = entity.getComponent(LabelComponent.class);
		DimensionsComponent dimensionsComponent = entity.getComponent(DimensionsComponent.class);
		text = labelComponent.getText().toString();
		style = labelComponent.fontName;
		size = labelComponent.fontSize;
		align = labelComponent.labelAlign;
		multiline = labelComponent.wrap;

		width = dimensionsComponent.width;
		height = dimensionsComponent.height;
	}
}
