package com.uwsoft.editor.renderer.data;

public class CompositeItemVO extends MainItemVO {

	public CompositeVO composite;
	
	public float scissorX; 
	public float scissorY;
	public float scissorWidth; 
	public float scissorHeight;

	
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
        tmp.isFlipedH = isFlipedH;
        tmp.isFlipedV = isFlipedV;
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
		
		return tmp;
	}
}
