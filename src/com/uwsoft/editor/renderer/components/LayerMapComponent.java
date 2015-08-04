package com.uwsoft.editor.renderer.components;

import java.util.ArrayList;

import com.badlogic.ashley.core.Component;
import com.uwsoft.editor.renderer.data.LayerItemVO;

public class LayerMapComponent extends Component {
	public ArrayList<LayerItemVO> layers = new ArrayList<LayerItemVO>();

	public LayerItemVO getLayer(String name) {
		for(LayerItemVO vo: layers) {
			if (vo.layerName.equals(name)) {
				return vo;
			}
		}
		return null;
	}

	public boolean isVisible(String name) {
		LayerItemVO vo = getLayer(name);
		if(vo != null) {
			return vo.isVisible;
		}

		return true;
	}
}
