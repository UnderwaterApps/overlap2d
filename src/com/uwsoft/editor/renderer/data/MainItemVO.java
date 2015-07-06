package com.uwsoft.editor.renderer.data;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.*;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyPropertiesComponent;
import com.uwsoft.editor.renderer.scripts.IScript;

import java.util.ArrayList;
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
	
	public ShapeVO shape = null;
	public PhysicsBodyDataVO physics = null;

    public ArrayList<String> commonScripts = new ArrayList<>();
	
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

		if(vo.shape != null) {
			shape = vo.shape.clone();
		}

		if(vo.physics != null){
            physics = new PhysicsBodyDataVO(vo.physics);
		}

        commonScripts = (ArrayList<String>) vo.commonScripts.clone();
    }

	public void loadFromEntity(Entity entity) {
		MainItemComponent mainItemComponent = entity.getComponent(MainItemComponent.class);
		TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
		TintComponent tintComponent = entity.getComponent(TintComponent.class);
		ZindexComponent zindexComponent = entity.getComponent(ZindexComponent.class);

		itemIdentifier = mainItemComponent.itemIdentifier;
		itemName = mainItemComponent.libraryLink;
		tags = mainItemComponent.tags;
		customVars = mainItemComponent.customVars;

		x = transformComponent.x;
		y = transformComponent.y;
		scaleX = transformComponent.scaleX;
		scaleY = transformComponent.scaleY;
		originX = transformComponent.originX;
		originY = transformComponent.originY;
		rotation = transformComponent.rotation;

        if(scriptComponent != null) {
            for (IScript name : scriptComponent.scripts) {
                commonScripts.add(name.getClass().getName());
            }
        }

        layerName = mainItemComponent.layer;

		tint = new float[4];
		tint[0] = tintComponent.color.r;
		tint[1] = tintComponent.color.g;
		tint[2] = tintComponent.color.b;
		tint[3] = tintComponent.color.a;

		zIndex = zindexComponent.zIndex;

		/**
		 * Secondary components
		 */
		PolygonComponent polygonComponent = entity.getComponent(PolygonComponent.class);
		if(polygonComponent != null && polygonComponent.vertices != null) {
			shape = new ShapeVO();
			shape.polygons = polygonComponent.vertices;
		}
        PhysicsBodyPropertiesComponent physicsComponent = entity.getComponent(PhysicsBodyPropertiesComponent.class);
        if(physicsComponent != null) {
            physics = new PhysicsBodyDataVO();
            physics.loadFromComponent(physicsComponent);
        }
	}
}
