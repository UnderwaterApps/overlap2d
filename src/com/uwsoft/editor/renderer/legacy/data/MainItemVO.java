package com.uwsoft.editor.renderer.legacy.data;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.LayerMapComponent;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;

import java.util.Arrays;


public class MainItemVO {
	public String itemIdentifier = "";
	public String itemName = "";
    public String[] tags = null;
    public String customVars = "";
	public float x; 
	public float y;
	public float scaleX	=	1f; 
	public float scaleY	=	1f;
	public float originX	=	Float.NaN;
	public float originY	=	Float.NaN;
	public float rotation;
	public int zIndex = 0;
	public String layerName = "";
	public float[] tint = {1, 1, 1, 1};
	public boolean isFlipedH = false;
	public boolean isFlipedV = false;
	
	public String meshId = "-1";
	public PhysicsBodyDataVO physicsBodyData = null;
	
	public MainItemVO() {
		
	}
	
	public MainItemVO(MainItemVO vo) {
		itemIdentifier = new String(vo.itemIdentifier);
		itemName = new String(vo.itemName);
        if(tags != null) tags = Arrays.copyOf(vo.tags, vo.tags.length);
        customVars = new String(vo.customVars);
		x = vo.x; 
		y = vo.y;
		rotation = vo.rotation;
		zIndex = vo.zIndex;
		layerName = new String(vo.layerName);
		//if(tint != null) tint = Arrays.copyOf(vo.tint, vo.tint.length);
		if(vo.tint != null) tint = Arrays.copyOf(vo.tint, vo.tint.length);
		isFlipedH 	= vo.isFlipedH;
		isFlipedV 	= vo.isFlipedV;
		scaleX 		= vo.scaleX;
		scaleY 		= vo.scaleY;
		
		meshId = vo.meshId;
		if(vo.physicsBodyData != null){
			physicsBodyData = new PhysicsBodyDataVO(vo.physicsBodyData);
		}
	}

	public void loadFromEntity(Entity entity) {
		MainItemComponent mainItemComponent = entity.getComponent(MainItemComponent.class);
		TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
		TintComponent tintComponent = entity.getComponent(TintComponent.class);

		itemIdentifier = mainItemComponent.itemIdentifier;
		itemName = mainItemComponent.itemName;
		tags = mainItemComponent.tags;
		customVars = mainItemComponent.customVars;

		x = transformComponent.x;
		y = transformComponent.y;
		scaleX = transformComponent.scaleX;
		scaleY = transformComponent.scaleY;
		originX = transformComponent.originX;
		originY = transformComponent.originY;
		rotation = transformComponent.rotation;
		//zIndex =
		//layerName
		//tint = tintComponent.color;

	}
}
