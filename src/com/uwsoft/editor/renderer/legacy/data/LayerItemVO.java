package com.uwsoft.editor.renderer.legacy.data;

public class LayerItemVO {

	public String layerName = "";
	public boolean isLocked = false;
	public boolean isVisible = true;
	
	public LayerItemVO() {
		
	}

    public LayerItemVO(String name) {
        layerName = new String(name);
    }
	
	public LayerItemVO(LayerItemVO vo) {
		layerName = new String(vo.layerName);
		isLocked = vo.isLocked;
		isVisible = vo.isVisible;
	}

	public static LayerItemVO createDefault() {
		LayerItemVO layerItemVO = new LayerItemVO();
		layerItemVO.layerName = "Default";
		return layerItemVO;
	}
	
}
