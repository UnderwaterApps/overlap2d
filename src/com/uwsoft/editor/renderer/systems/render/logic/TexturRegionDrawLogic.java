package com.uwsoft.editor.renderer.systems.render.logic;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.ShaderComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.TintComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;

public class TexturRegionDrawLogic implements Drawable {

	private ComponentMapper<TintComponent> tintComponentComponentMapper;
	private ComponentMapper<TextureRegionComponent> textureRegionMapper;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<DimensionsComponent> dimensionsComponentComponentMapper;
	private ComponentMapper<ShaderComponent> shaderComponentMapper;

	public TexturRegionDrawLogic() {
		tintComponentComponentMapper = ComponentMapper.getFor(TintComponent.class);
		textureRegionMapper = ComponentMapper.getFor(TextureRegionComponent.class);
		transformMapper = ComponentMapper.getFor(TransformComponent.class);
		dimensionsComponentComponentMapper = ComponentMapper.getFor(DimensionsComponent.class);
		shaderComponentMapper = ComponentMapper.getFor(ShaderComponent.class);
	}

	@Override
	public void draw(Batch batch, Entity entity) {
        TextureRegionComponent entityTextureRegionComponent = textureRegionMapper.get(entity);
        if(entityTextureRegionComponent.polygonSprite != null) {
            if(entityTextureRegionComponent.isRepeat) {
                drawPolygonSprite(batch, entity);
            } else {
                drawPolygonSprite(batch, entity);
            }
        } else {
            drawSprite(batch, entity);
        }
	}

    public void drawSprite(Batch batch, Entity entity) {
        TintComponent tintComponent = tintComponentComponentMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        TextureRegionComponent entityTextureRegionComponent = textureRegionMapper.get(entity);
        DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);
        batch.setColor(tintComponent.color);
        
        if(shaderComponentMapper.has(entity)){
			ShaderComponent shaderComponent = shaderComponentMapper.get(entity);
			batch.setShader(shaderComponent.shaderProgram);
			//shaderComponent.shaderProgram.setUniformf("rt_w", entityTextureRegionComponent.region.getRegionWidth());
			//shaderComponent.shaderProgram.setUniformf("rt_h", entityTextureRegionComponent.region.getRegionHeight());
			shaderComponent.shaderProgram.setUniformf("rt_w", 1f);
			shaderComponent.shaderProgram.setUniformf("rt_h", 1f);
			shaderComponent.shaderProgram.setUniformf("vx_offset", 1f);
			//System.out.println("SHADER SET");
		}
		
		
        
        batch.draw(entityTextureRegionComponent.region,
                entityTransformComponent.x, entityTransformComponent.y,
                entityTransformComponent.originX, entityTransformComponent.originY,
                dimensionsComponent.width, dimensionsComponent.height,
                entityTransformComponent.scaleX, entityTransformComponent.scaleY,
                entityTransformComponent.rotation);
        
        if(shaderComponentMapper.has(entity)){
			batch.setShader(null);
		}
    }

    public void drawPolygonSprite(Batch batch, Entity entity) {
        TintComponent tintComponent = tintComponentComponentMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        TextureRegionComponent entityTextureRegionComponent = textureRegionMapper.get(entity);

        DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);

        entityTextureRegionComponent.polygonSprite.setPosition(
                entityTransformComponent.x + entityTextureRegionComponent.polygonShift.x,
                entityTransformComponent.y + entityTextureRegionComponent.polygonShift.y);
        entityTextureRegionComponent.polygonSprite.setRotation(entityTransformComponent.rotation);
        entityTextureRegionComponent.polygonSprite.setOrigin(entityTransformComponent.originX, entityTransformComponent.originY);
        entityTextureRegionComponent.polygonSprite.setColor(tintComponent.color);
        entityTextureRegionComponent.polygonSprite.draw((PolygonSpriteBatch) batch);
    }

    public void drawTiledPolygonSprite(Batch batch, Entity entity) {
        // TODO: this should change to be working
        TintComponent tintComponent = tintComponentComponentMapper.get(entity);
        TransformComponent entityTransformComponent = transformMapper.get(entity);
        TextureRegionComponent entityTextureRegionComponent = textureRegionMapper.get(entity);
        DimensionsComponent dimensionsComponent = dimensionsComponentComponentMapper.get(entity);
        batch.setColor(tintComponent.color);
        entityTextureRegionComponent.polygonSprite.setPosition(
                entityTransformComponent.x+entityTextureRegionComponent.polygonShift.x,
                entityTransformComponent.y+entityTextureRegionComponent.polygonShift.y);
        entityTextureRegionComponent.polygonSprite.setRotation(entityTransformComponent.rotation);
        entityTextureRegionComponent.polygonSprite.draw((PolygonSpriteBatch) batch);
    }
}
