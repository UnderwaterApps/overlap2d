package com.uwsoft.editor.renderer.data;

public class LayerItemVO {

	public String layerName = "";
	public boolean isLocked = false;
	public boolean isVisible = false;
	
	public LayerItemVO() {
		
	}

    public LayerItemVO(String name) {
        layerName = new String(name);
		isVisible = true;
    }
	
	public LayerItemVO(LayerItemVO vo) {
		layerName = new String(vo.layerName);
		isLocked = vo.isLocked;
		isVisible = vo.isVisible;
	}

	public static LayerItemVO createDefault() {
		LayerItemVO layerItemVO = new LayerItemVO();
		layerItemVO.layerName = "Default";
		layerItemVO.isVisible = true;
		return layerItemVO;
	}
	
}
