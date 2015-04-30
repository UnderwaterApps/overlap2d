package com.uwsoft.editor.renderer;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brashmonkey.spriter.Player;
import com.uwsoft.editor.renderer.conponents.CompositeTransformComponent;
import com.uwsoft.editor.renderer.conponents.LayerMapComponent;
import com.uwsoft.editor.renderer.conponents.ViewPortComponent;
import com.uwsoft.editor.renderer.conponents.ZindexComponent;
import com.uwsoft.editor.renderer.conponents.NodeComponent;
import com.uwsoft.editor.renderer.conponents.TextureRegionComponent;
import com.uwsoft.editor.renderer.conponents.TransformComponent;
import com.uwsoft.editor.renderer.conponents.particle.ParticleCompononet;
import com.uwsoft.editor.renderer.conponents.spine.SpineDataComponent;
import com.uwsoft.editor.renderer.conponents.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.conponents.spriter.SpriterComponent;
import com.uwsoft.editor.renderer.conponents.spriter.SpriterDrawerComponent;


//TODO drawabale mechanics
public class Overlap2dRenderer extends IteratingSystem {
	
	private ComponentMapper<ViewPortComponent> viewPortMapper = ComponentMapper.getFor(ViewPortComponent.class);
	private ComponentMapper<CompositeTransformComponent> compositeTransformMapper = ComponentMapper.getFor(CompositeTransformComponent.class);
	private ComponentMapper<NodeComponent> nodeMapper = ComponentMapper.getFor(NodeComponent.class);
	private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
	private ComponentMapper<TextureRegionComponent> textureRegionMapper = ComponentMapper.getFor(TextureRegionComponent.class);
	private ComponentMapper<ParticleCompononet> particleMapper = ComponentMapper.getFor(ParticleCompononet.class);
	private ComponentMapper<SpriterDrawerComponent> spriterDrawerMapper = ComponentMapper.getFor(SpriterDrawerComponent.class);
	private ComponentMapper<SpriterComponent> spriterMapper = ComponentMapper.getFor(SpriterComponent.class);
	private ComponentMapper<SpineDataComponent> spineMapper = ComponentMapper.getFor(SpineDataComponent.class);

	private CompositeTransformComponent compositeTransformComponent;
	private NodeComponent nodeComponent;
	
	private Entity currentComposite = null;
	
	public Batch batch;
	public Overlap2dRenderer(Batch batch) {
		super(Family.all(ViewPortComponent.class).get());
		this.batch = batch;
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		ViewPortComponent ViewPortComponent = viewPortMapper.get(entity);
		Camera camera = ViewPortComponent.viewPort.getCamera();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		drawRecursively(entity);
		batch.end();
		
		//TODO add rayHandler thing and Spine rendere thing
	}

	private void drawRecursively(Entity rootEntity) {
		
		
		currentComposite = rootEntity;
		compositeTransformComponent = compositeTransformMapper.get(currentComposite);
		nodeComponent = nodeMapper.get(currentComposite);
		
		if (compositeTransformComponent.transform) applyTransform(batch, computeTransform());
		drawChildren(rootEntity, batch);
		if (compositeTransformComponent.transform) resetTransform(batch);
	}

	private void drawChildren(Entity rootEntity, Batch batch) {
		Entity[] children = nodeComponent.children.begin();
		if (compositeTransformComponent.transform) {
			for (int i = 0, n = nodeComponent.children.size; i < n; i++) {
				Entity child = children[i];
				
				//TODO visibility and parent Alpha thing
				//if (!child.isVisible()) continue;
				//child.draw(batch, parentAlpha);
				//new Group()
				 
				TextureRegionComponent childTextureRegionComponent = textureRegionMapper.get(child);
				TransformComponent childTransformComponent = transformMapper.get(child); 
				ParticleCompononet particleComponent = particleMapper.get(child);
				SpriterDrawerComponent spriterDrawerComponent = spriterDrawerMapper.get(child);
				SpineDataComponent spineDataComponent = spineMapper.get(child);
				
				NodeComponent childNodeComponent = nodeMapper.get(child);
				if(childTextureRegionComponent != null){
					batch.draw(childTextureRegionComponent.region, childTransformComponent.x, childTransformComponent.y, 0, 0, childTextureRegionComponent.region.getRegionWidth(), childTextureRegionComponent.region.getRegionHeight(), childTransformComponent.scaleX, childTransformComponent.scaleY, childTransformComponent.rotation);
				}
				
				if(particleComponent != null){
					//TODO need to add transformation matrixes for scaling and rotation now thay dosn't work
					particleComponent.particleEffect.draw(batch);
				}
				
				if(spriterDrawerComponent != null){
					SpriterComponent spriter = spriterMapper.get(child);
					Player player = spriter.player;
					
					player.setPosition(childTransformComponent.x, childTransformComponent.y);
					//TODO dimentions 
					//player.setPivot(getWidth() / 2, getHeight() / 2);
					player.setScale(spriter.scale );
					player.rotate(childTransformComponent.rotation - player.getAngle());
					player.update();
					spriterDrawerComponent.drawer.beforeDraw(player, batch);
				}
				
				if(spineDataComponent != null){
					//TODO parent alpha thing
					renderer.draw(batch, spineDataComponent.skeleton);
				}
				
				if(childNodeComponent !=null){
					drawRecursively(child);
				}
			}
		} else {
			// No transform for this group, offset each child.

			float offsetX = compositeTransformComponent.x, offsetY = compositeTransformComponent.y;
			compositeTransformComponent.x = 0;
			compositeTransformComponent.y = 0;
			for (int i = 0, n = nodeComponent.children.size; i < n; i++) {
				Entity child = children[i];
				
				TextureRegionComponent childTextureRegionComponent = textureRegionMapper.get(child);
				TransformComponent childTransformComponent = transformMapper.get(child); 
				NodeComponent childNodeComponent = nodeMapper.get(child);
				
				//TODO visibility and parent Alpha thing
				//if (!child.isVisible()) continue;
				//if (!child.isVisible()) continue;
				
				float cx = childTransformComponent.x, cy = childTransformComponent.y;
				childTransformComponent.x = cx + offsetX;
				childTransformComponent.y = cy + offsetY;
				if(childTextureRegionComponent != null){
					batch.draw(childTextureRegionComponent.region, childTransformComponent.x, childTransformComponent.y, 0, 0, childTextureRegionComponent.region.getRegionWidth(), childTextureRegionComponent.region.getRegionHeight(), childTransformComponent.scaleX, childTransformComponent.scaleY, childTransformComponent.rotation);
				}
				childTransformComponent.x = cx;
				childTransformComponent.y = cy;
				
				
				//TODO other things lights, particles, sprite spine
				
				if(childNodeComponent !=null){
					drawRecursively(child);
				}
			}
			compositeTransformComponent.x = offsetX;
			compositeTransformComponent.y = offsetY;
		}
	}

	/** Returns the transform for this group's coordinate system. */
	protected Matrix4 computeTransform () {
		
		Affine2 worldTransform = compositeTransformComponent.worldTransform;
		//TODO orogin thing
		float originX = 0;
		float originY = 0;
		float rotation = compositeTransformComponent.rotation;
		float scaleX = compositeTransformComponent.scaleX;
		float scaleY = compositeTransformComponent.scaleY;

		worldTransform.setToTrnRotScl(compositeTransformComponent.x + originX, compositeTransformComponent.y + originY, rotation, scaleX, scaleY);
		if (originX != 0 || originY != 0) worldTransform.translate(-originX, -originY);

		// Find the first parent that transforms.
		
		CompositeTransformComponent parentTransformComponent = null;
		NodeComponent parentNodeComponent;
		
		Entity parentEntity = nodeComponent.parentEntity;
		while (parentEntity != null) {
			parentTransformComponent = compositeTransformMapper.get(parentEntity);
			parentNodeComponent = nodeMapper.get(parentEntity);
			if (parentTransformComponent.transform) break;
			parentEntity = parentNodeComponent.parentEntity;
		}
		if (parentEntity != null) compositeTransformComponent.worldTransform.preMul(parentTransformComponent.worldTransform);

		compositeTransformComponent.computedTransform.set(worldTransform);
		return compositeTransformComponent.computedTransform;
	}

	/** Set the batch's transformation matrix, often with the result of {@link #computeTransform()}. Note this causes the batch to
	 * be flushed. {@link #resetTransform(Batch)} will restore the transform to what it was before this call. */
	protected void applyTransform (Batch batch, Matrix4 transform) {
		compositeTransformComponent.oldTransform.set(batch.getTransformMatrix());
		batch.setTransformMatrix(transform);
	}

	/** Restores the batch transform to what it was before {@link #applyTransform(Batch, Matrix4)}. Note this causes the batch to be
	 * flushed. */
	protected void resetTransform (Batch batch) {
		batch.setTransformMatrix(compositeTransformComponent.oldTransform);
	}
	
}

