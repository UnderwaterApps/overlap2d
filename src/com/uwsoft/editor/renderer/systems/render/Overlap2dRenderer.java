package com.uwsoft.editor.renderer.systems.render;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.commons.IExternalItemType;
import com.uwsoft.editor.renderer.components.*;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.systems.render.logic.DrawableLogicMapper;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;


public class Overlap2dRenderer extends IteratingSystem {
	private final float TIME_STEP = 1f/60;
	
	private ComponentMapper<ViewPortComponent> viewPortMapper = ComponentMapper.getFor(ViewPortComponent.class);
	private ComponentMapper<CompositeTransformComponent> compositeTransformMapper = ComponentMapper.getFor(CompositeTransformComponent.class);
	private ComponentMapper<NodeComponent> nodeMapper = ComponentMapper.getFor(NodeComponent.class);
	private ComponentMapper<ParentNodeComponent> parentNodeMapper = ComponentMapper.getFor(ParentNodeComponent.class);
	private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
	private ComponentMapper<MainItemComponent> mainItemComponentMapper = ComponentMapper.getFor(MainItemComponent.class);
	
	private DrawableLogicMapper drawableLogicMapper;
	private RayHandler rayHandler;
	private World world;
	private boolean isPhysicsOn = true;
	
	private float accumulator = 0;
	//private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	public static float timeRunning = 0;
	
	public Batch batch;

	public Overlap2dRenderer(Batch batch) {
		super(Family.all(ViewPortComponent.class).get());
		this.batch = batch;
		drawableLogicMapper = new DrawableLogicMapper();
	}

	public void addDrawableType(IExternalItemType itemType) {
		drawableLogicMapper.addDrawableToMap(itemType.getTypeId(), itemType.getDrawable());
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		timeRunning+=deltaTime;

		ViewPortComponent ViewPortComponent = viewPortMapper.get(entity);
		Camera camera = ViewPortComponent.viewPort.getCamera();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		drawRecursively(entity, 1f);
		batch.end();

		
		//TODO kinda not cool (this should be done in separate lights renderer maybe?
		if(rayHandler != null) {
			rayHandler.setCulling(false);
			OrthographicCamera orthoCamera = (OrthographicCamera) camera;
			camera.combined.scl(1f/PhysicsBodyLoader.getScale());
			rayHandler.setCombinedMatrix(orthoCamera); 
			rayHandler.updateAndRender();
		}
		
		if(world != null && isPhysicsOn) {
			doPhysicsStep(deltaTime);
        }

		//debugRenderer.render(world, camera.combined);
		//TODO Spine rendere thing
	}

	private void doPhysicsStep(float deltaTime) {
	    // fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(deltaTime, 0.25f);
	    accumulator += frameTime;
	    while (accumulator >= TIME_STEP) {
	        world.step(TIME_STEP, 6, 2);
	        accumulator -= TIME_STEP;
	    }
	}

	private void drawRecursively(Entity rootEntity, float parentAlpha) {
		
		
		//currentComposite = rootEntity;
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		
		
		if (curCompositeTransformComponent.transform){
			computeTransform(rootEntity);
			applyTransform(rootEntity, batch);
		}
        TintComponent tintComponent = ComponentRetriever.get(rootEntity, TintComponent.class);
        parentAlpha *= tintComponent.color.a;

		drawChildren(rootEntity, batch, curCompositeTransformComponent, parentAlpha);
		if (curCompositeTransformComponent.transform) resetTransform(rootEntity, batch);
	}

	private void drawChildren(Entity rootEntity, Batch batch, CompositeTransformComponent curCompositeTransformComponent, float parentAlpha) {
		NodeComponent nodeComponent = nodeMapper.get(rootEntity);
		Entity[] children = nodeComponent.children.begin();

		if (curCompositeTransformComponent.transform) {
			for (int i = 0, n = nodeComponent.children.size; i < n; i++) {
				Entity child = children[i];

				LayerMapComponent rootLayers = ComponentRetriever.get(rootEntity, LayerMapComponent.class);
				ZIndexComponent childZIndexComponent = ComponentRetriever.get(child, ZIndexComponent.class);

				if(!rootLayers.isVisible(childZIndexComponent.layerName)) {
					continue;
				}

				MainItemComponent childMainItemComponent = mainItemComponentMapper.get(child);
				if(!childMainItemComponent.visible){
					continue;
				}
				
				int entityType = childMainItemComponent.entityType;

				NodeComponent childNodeComponent = nodeMapper.get(child);
				
				
				if(childNodeComponent ==null){
					//Find logic from the mapper and draw it
					drawableLogicMapper.getDrawable(entityType).draw(batch, child, parentAlpha);
				}else{
					//Step into Composite
					drawRecursively(child, parentAlpha);
				}
			}
		} else {
			// No transform for this group, offset each child.
			TransformComponent compositeTransform = transformMapper.get(rootEntity);
			
			float offsetX = compositeTransform.x, offsetY = compositeTransform.y;
			
			if(viewPortMapper.has(rootEntity)){
				offsetX = 0;
				offsetY = 0;
			}
			
			for (int i = 0, n = nodeComponent.children.size; i < n; i++) {
				Entity child = children[i];

				TransformComponent childTransformComponent = transformMapper.get(child);
				float cx = childTransformComponent.x, cy = childTransformComponent.y;
				childTransformComponent.x = cx + offsetX;
				childTransformComponent.y = cy + offsetY;
				
				NodeComponent childNodeComponent = nodeMapper.get(child);
				int entityType = mainItemComponentMapper.get(child).entityType;
				
				if(childNodeComponent ==null){
					//Find the logic from mapper and draw it
					drawableLogicMapper.getDrawable(entityType).draw(batch, child, parentAlpha);
				}else{
					//Step into Composite
					drawRecursively(child, parentAlpha);
				}
				childTransformComponent.x = cx;
				childTransformComponent.y = cy;
				
				if(childNodeComponent !=null){
					drawRecursively(child, parentAlpha);
				}
			}
		}
		nodeComponent.children.end();
	}

	/** Returns the transform for this group's coordinate system. 
	 * @param rootEntity */
	protected Matrix4 computeTransform (Entity rootEntity) {
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		//NodeComponent nodeComponent = nodeMapper.get(rootEntity);
		ParentNodeComponent parentNodeComponent = parentNodeMapper.get(rootEntity);
		TransformComponent curTransform = transformMapper.get(rootEntity);
		Affine2 worldTransform = curCompositeTransformComponent.worldTransform;
		//TODO origin thing
		float originX = 0;
		float originY = 0;
		float x = curTransform.x;
		float y = curTransform.y;
		float rotation = curTransform.rotation;
		float scaleX = curTransform.scaleX;
		float scaleY = curTransform.scaleY;

		worldTransform.setToTrnRotScl(x + originX, y + originY, rotation, scaleX, scaleY);
		if (originX != 0 || originY != 0) worldTransform.translate(-originX, -originY);

		// Find the first parent that transforms.
		
		CompositeTransformComponent parentTransformComponent = null;
		//NodeComponent parentNodeComponent;
		
		Entity parentEntity = null;
		if(parentNodeComponent != null){
			parentEntity = parentNodeComponent.parentEntity;
		}
//		if (parentEntity != null){
//			
//		}
		
//		while (parentEntity != null) {
//			parentNodeComponent = nodeMapper.get(parentEntity);
//			if (parentTransformComponent.transform) break;
//			System.out.println("Gand");
//			parentEntity = parentNodeComponent.parentEntity;
//			parentTransformComponent = compositeTransformMapper.get(parentEntity);
//			
//		}
		
		if (parentEntity != null){
			parentTransformComponent = compositeTransformMapper.get(parentEntity);
			worldTransform.preMul(parentTransformComponent.worldTransform);
			//MainItemComponent main = parentEntity.getComponent(MainItemComponent.class);
			//System.out.println("NAME " + main.itemIdentifier);
		}

		curCompositeTransformComponent.computedTransform.set(worldTransform);
		return curCompositeTransformComponent.computedTransform;
	}

	protected void applyTransform (Entity rootEntity, Batch batch) {
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		curCompositeTransformComponent.oldTransform.set(batch.getTransformMatrix());
		batch.setTransformMatrix(curCompositeTransformComponent.computedTransform);
	}

	protected void resetTransform (Entity rootEntity, Batch batch) {
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		batch.setTransformMatrix(curCompositeTransformComponent.oldTransform);
	}
	
	public void setRayHandler(RayHandler rayHandler){
		this.rayHandler = rayHandler;
	}

	public void setBox2dWorld(World world) {
		this.world = world;
	}
	
	public void setPhysicsOn(boolean isPhysicsOn) {
		this.isPhysicsOn = isPhysicsOn;
	}

    public Batch getBatch() {
        return batch;
    }
}

