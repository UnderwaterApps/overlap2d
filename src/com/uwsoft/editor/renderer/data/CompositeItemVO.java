package com.uwsoft.editor.renderer.data;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class CompositeItemVO extends MainItemVO {

	public CompositeVO composite;
	
	public float scissorX; 
	public float scissorY;
	public float scissorWidth; 
	public float scissorHeight;

	public float width;
	public float height;
	
	public CompositeItemVO() {
		composite = new CompositeVO();
	}
	
	public CompositeItemVO(CompositeVO vo) {
		composite = new CompositeVO(vo);
	}
	
	public CompositeItemVO(CompositeItemVO vo) {
		super(vo);
		composite = new CompositeVO(vo.composite);
	}
	
	public void update(CompositeItemVO vo) {
		composite = new CompositeVO(vo.composite);
	}
	
	public CompositeItemVO clone() {
		CompositeItemVO tmp = new CompositeItemVO();
		tmp.composite = composite;
        tmp.itemName = itemName;
        tmp.layerName = layerName;
        tmp.rotation = rotation;
        tmp.tint = tint;
        tmp.x = x;
        tmp.y = y;
        tmp.zIndex = zIndex;

        tmp.scissorX = scissorX;
        tmp.scissorY = scissorY;
        tmp.scissorWidth = scissorWidth;
        tmp.scissorHeight = scissorHeight;

		tmp.width = width;
		tmp.height = height;
		
		return tmp;
	}

	@Override
	public void loadFromEntity(Entity entity) {
		super.loadFromEntity(entity);
		//scissorsX
		//scissorsY
		composite = new CompositeVO();
		composite.loadFromEntity(entity);

		DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

		width = dimensionsComponent.width;
		height = dimensionsComponent.height;
	}
}
