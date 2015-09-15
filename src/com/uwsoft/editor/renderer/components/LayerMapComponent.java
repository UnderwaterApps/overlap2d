package com.uwsoft.editor.renderer.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.uwsoft.editor.renderer.data.LayerItemVO;

public class LayerMapComponent implements Component {
	public boolean autoIndexing = true;
	private ArrayList<LayerItemVO> layers = new ArrayList<LayerItemVO>();

	private HashMap<String, LayerItemVO> layerMap = new HashMap<String, LayerItemVO>();

	public void setLayers(ArrayList<LayerItemVO> layers) {
		this.layers = layers;
		layerMap.clear();
		for (LayerItemVO vo : layers) {
			layerMap.put(vo.layerName, vo);
		}
	}

	public LayerItemVO getLayer(String name) {
		return layerMap.get(name);
	}

	public int getIndexByName(String name) {
		if(layerMap.containsKey(name)) {
			return layers.indexOf(layerMap.get(name));
		}

		return 0;
	}

	public boolean isVisible(String name) {
		LayerItemVO vo = getLayer(name);
		if (vo != null) {
			return vo.isVisible;
		}

		return true;
	}

	public void addLayer(int index, LayerItemVO layerVo) {
		layers.add(index, layerVo);
		layerMap.put(layerVo.layerName, layerVo);
	}

	public void addLayer(LayerItemVO layerVo) {
		layers.add(layerVo);
		layerMap.put(layerVo.layerName, layerVo);
	}

	public ArrayList<LayerItemVO> getLayers() {
		return layers;
	}

	public void deleteLayer(String layerName) {
		layers.remove(getIndexByName(layerName));
		layerMap.remove(layerName);
	}

	public void rename(String prevName, String newName) {
		LayerItemVO vo = layerMap.get(prevName);
		vo.layerName = newName;
		layerMap.remove(prevName);
		layerMap.put(newName, vo);
	}

	public void swap(String source, String target) {
		LayerItemVO sourceVO = getLayer(source);
		LayerItemVO targetVO = getLayer(target);
		Collections.swap(layers, layers.indexOf(sourceVO), layers.indexOf(targetVO));
	}
}
