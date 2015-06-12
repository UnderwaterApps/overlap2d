package com.uwsoft.editor.renderer;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.brashmonkey.spriter.Player;
import com.uwsoft.editor.renderer.components.CompositeTransformComponent;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.components.ParentNodeComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.ViewPortComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.components.particle.ParticleComponent;
import com.uwsoft.editor.renderer.components.spine.SpineDataComponent;
import com.uwsoft.editor.renderer.components.spriter.SpriterComponent;
import com.uwsoft.editor.renderer.components.spriter.SpriterDrawerComponent;


//TODO drawabale mechanics
public class Overlap2dRenderer extends IteratingSystem {
	
	private ComponentMapper<ViewPortComponent> viewPortMapper = ComponentMapper.getFor(ViewPortComponent.class);
	private ComponentMapper<CompositeTransformComponent> compositeTransformMapper = ComponentMapper.getFor(CompositeTransformComponent.class);
	private ComponentMapper<NodeComponent> nodeMapper = ComponentMapper.getFor(NodeComponent.class);
	private ComponentMapper<ParentNodeComponent> parentNodeMapper = ComponentMapper.getFor(ParentNodeComponent.class);
	private ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
	private ComponentMapper<TextureRegionComponent> textureRegionMapper = ComponentMapper.getFor(TextureRegionComponent.class);
	private ComponentMapper<ParticleComponent> particleMapper = ComponentMapper.getFor(ParticleComponent.class);
	private ComponentMapper<SpriterDrawerComponent> spriterDrawerMapper = ComponentMapper.getFor(SpriterDrawerComponent.class);
	private ComponentMapper<SpriterComponent> spriterMapper = ComponentMapper.getFor(SpriterComponent.class);
	private ComponentMapper<SpineDataComponent> spineMapper = ComponentMapper.getFor(SpineDataComponent.class);
	private ComponentMapper<LabelComponent> labelComponentMapper = ComponentMapper.getFor(LabelComponent.class);
	private ComponentMapper<TintComponent> tintComponentMapper = ComponentMapper.getFor(TintComponent.class);
	private ComponentMapper<DimensionsComponent> dimensionsComponentMapper = ComponentMapper.getFor(DimensionsComponent.class);
	
//	private CompositeTransformComponent curCompositeTransformComponent;
//	private NodeComponent nodeComponent;
//	private TransformComponent curTransform;
	
	//private Entity currentComposite = null;
	
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
		
		
		//currentComposite = rootEntity;
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		
		
		if (curCompositeTransformComponent.transform){
			computeTransform(rootEntity);
			applyTransform(rootEntity, batch);
		}
		drawChildren(rootEntity, batch, curCompositeTransformComponent);
		if (curCompositeTransformComponent.transform) resetTransform(rootEntity, batch);
	}

	private void drawChildren(Entity rootEntity, Batch batch, CompositeTransformComponent curCompositeTransformComponent) {
		NodeComponent nodeComponent = nodeMapper.get(rootEntity);
		Entity[] children = nodeComponent.children.begin();
		if (curCompositeTransformComponent.transform) {
			for (int i = 0, n = nodeComponent.children.size; i < n; i++) {
				Entity child = children[i];
				
				//TODO visibility and parent Alpha thing
				//if (!child.isVisible()) continue;
				//child.draw(batch, parentAlpha);
				//new Group()
				 
				TextureRegionComponent childTextureRegionComponent = textureRegionMapper.get(child);
				TransformComponent childTransformComponent = transformMapper.get(child); 
				ParticleComponent particleComponent = particleMapper.get(child);
				SpriterDrawerComponent spriterDrawerComponent = spriterDrawerMapper.get(child);
				SpineDataComponent spineDataComponent = spineMapper.get(child);
				LabelComponent labelComponent = labelComponentMapper.get(child);
				
				NodeComponent childNodeComponent = nodeMapper.get(child);
				if(childTextureRegionComponent != null){
					batch.draw(childTextureRegionComponent.region, childTransformComponent.x, childTransformComponent.y, childTransformComponent.originX, childTransformComponent.originY, childTextureRegionComponent.region.getRegionWidth(), childTextureRegionComponent.region.getRegionHeight(), childTransformComponent.scaleX, childTransformComponent.scaleY, childTransformComponent.rotation);
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
					//TODO spine renderer
					//renderer.draw(batch, spineDataComponent.skeleton);
				}
				
				if(labelComponent != null){
					DimensionsComponent dimenstionsComponent = dimensionsComponentMapper.get(child);
					TintComponent tint = tintComponentMapper.get(child);
					//TODO parent alpha thing
					//tint.color.a *= parentAlpha;
					if (labelComponent.style.background != null) {
						batch.setColor(tint.color.r, tint.color.g, tint.color.b, tint.color.a);
						labelComponent.style.background.draw(batch, childTransformComponent.x, childTransformComponent.y, dimenstionsComponent.width, dimenstionsComponent.height);
						//System.out.println("LAbel BG");
					}
					//TODO we need tmp color here
					//if (labelComponent.style.fontColor != null) tint.color.mul(labelComponent.style.fontColor);
					//labelComponent.cache.tint(tint.color);
					
//					BitmapFont font = labelComponent.cache.getFont();
//					labelComponent.layout.setText(font, labelComponent.text, 0, labelComponent.text.length, Color.WHITE, dimenstionsComponent.width, Align.center, labelComponent.wrap, null);
//					labelComponent.cache.setText(labelComponent.layout, 0, 50);
					
					labelComponent.cache.tint(Color.WHITE);
					labelComponent.cache.setPosition(childTransformComponent.x, childTransformComponent.y);
					labelComponent.cache.draw(batch);
					
				}
				
				if(childNodeComponent !=null){
					drawRecursively(child);
					//currentComposite = rootEntity;
				}
			}
		} else {
			// No transform for this group, offset each child.

//			float offsetX = curTransform.x, offsetY = curTransform.y;
//			curTransform.x = 0;
//			curTransform.y = 0;
//			for (int i = 0, n = nodeComponent.children.size; i < n; i++) {
//				Entity child = children[i];
//				
//				TextureRegionComponent childTextureRegionComponent = textureRegionMapper.get(child);
//				TransformComponent childTransformComponent = transformMapper.get(child); 
//				NodeComponent childNodeComponent = nodeMapper.get(child);
//				
//				//TODO visibility and parent Alpha thing
//				//if (!child.isVisible()) continue;
//				//if (!child.isVisible()) continue;
//				
//				float cx = childTransformComponent.x, cy = childTransformComponent.y;
//				childTransformComponent.x = cx + offsetX;
//				childTransformComponent.y = cy + offsetY;
//				if(childTextureRegionComponent != null){
//					batch.draw(childTextureRegionComponent.region, childTransformComponent.x, childTransformComponent.y, 0, 0, childTextureRegionComponent.region.getRegionWidth(), childTextureRegionComponent.region.getRegionHeight(), childTransformComponent.scaleX, childTransformComponent.scaleY, childTransformComponent.rotation);
//				}
//				childTransformComponent.x = cx;
//				childTransformComponent.y = cy;
//				
//				
//				//TODO other things lights, particles, sprite spine
//				
//				if(childNodeComponent !=null){
//					drawRecursively(child);
//				}
//			}
//			curTransform.x = offsetX;
//			curTransform.y = offsetY;
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
		//TODO orogin thing
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

	/** Set the batch's transformation matrix, often with the result of {@link #computeTransform()}. Note this causes the batch to
	 * be flushed. {@link #resetTransform(Batch)} will restore the transform to what it was before this call. 
	 * @param rootEntity */
	protected void applyTransform (Entity rootEntity, Batch batch) {
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		curCompositeTransformComponent.oldTransform.set(batch.getTransformMatrix());
		batch.setTransformMatrix(curCompositeTransformComponent.computedTransform);
	}

	/** Restores the batch transform to what it was before {@link #applyTransform(Batch, Matrix4)}. Note this causes the batch to be
	 * flushed. 
	 * @param rootEntity */
	protected void resetTransform (Entity rootEntity, Batch batch) {
		CompositeTransformComponent curCompositeTransformComponent = compositeTransformMapper.get(rootEntity);
		batch.setTransformMatrix(curCompositeTransformComponent.oldTransform);
	}
	
}

