package com.uwsoft.editor.renderer.systems;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.SnapshotArray;
import com.uwsoft.editor.renderer.components.*;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class LayerSystem extends IteratingSystem {

	private Comparator<Entity> comparator = new ZComparator();
	
	private ComponentMapper<ZIndexComponent> zIndexMapper;
	private ComponentMapper<LayerMapComponent> layerMapper;
	private ComponentMapper<NodeComponent> nodeMapper;
	
	public LayerSystem() {
		super(Family.all(CompositeTransformComponent.class).get());
		zIndexMapper = ComponentMapper.getFor(ZIndexComponent.class);
		layerMapper = ComponentMapper.getFor(LayerMapComponent.class);
		nodeMapper = ComponentMapper.getFor(NodeComponent.class);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		NodeComponent nodeComponent = nodeMapper.get(entity);
		LayerMapComponent layerMapComponent = layerMapper.get(entity);
		updateLayers(nodeComponent.children, layerMapComponent);

		sort(nodeComponent.children);
		
		if(layerMapComponent.autoIndexing){
			updateZindexes(nodeComponent.children);
		}
	}
	
	private void updateLayers(SnapshotArray<Entity> children, LayerMapComponent layerMapComponent) {
		for (int i = 0; i < children.size; i++) {
			Entity entity = children.get(i);
			ZIndexComponent zindexComponent = zIndexMapper.get(entity);
			MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
			zindexComponent.layerIndex = getlayerIndexByName(zindexComponent.layerName,layerMapComponent);
			if(zindexComponent.needReOrder && layerMapComponent.autoIndexing){
				if (zindexComponent.getZIndex() < 0) throw new IllegalArgumentException("ZIndex cannot be < 0.");
				if (children.size == 1){ 
					zindexComponent.setZIndex(0);
					zindexComponent.needReOrder = false;
					return;
				}
				if (!children.removeValue(entity, true)) return;
				if (zindexComponent.getZIndex() >= children.size)
					children.add(entity);
				else
					children.insert(zindexComponent.getZIndex(), entity);
			}
        }
	}
	
	private void updateZindexes(SnapshotArray<Entity> children) {
		for (int i = 0; i < children.size; i++) {
			Entity entity = children.get(i);
			ZIndexComponent zindexComponent = zIndexMapper.get(entity);
			zindexComponent.setZIndex(i);
			zindexComponent.needReOrder = false;
        }
	}

	private void sort(SnapshotArray<Entity> children) {
		children.sort(comparator);
	}
	
	private int getlayerIndexByName(String layerName, LayerMapComponent layerMapComponent) {
		 if(layerMapComponent == null){
			 return 0;
		 }
		return layerMapComponent.getIndexByName(layerName);
	 }
	
	private class ZComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
        	ZIndexComponent zIndexComponent1 = zIndexMapper.get(e1);
        	ZIndexComponent zIndexComponent2 = zIndexMapper.get(e2);
        	return zIndexComponent1.layerIndex == zIndexComponent2.layerIndex ? Integer.signum(zIndexComponent1.getZIndex() - zIndexComponent2.getZIndex()) : Integer.signum(zIndexComponent1.layerIndex - zIndexComponent2.layerIndex);
            //return (int)Math.signum(pm.get(e1).z - pm.get(e2).z);
        }
    }

}
