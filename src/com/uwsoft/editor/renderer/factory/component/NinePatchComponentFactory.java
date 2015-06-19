package com.uwsoft.editor.renderer.factory.component;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NinePatchComponnent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.legacy.data.Image9patchVO;
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

public class NinePatchComponentFactory extends ComponentFactory {

	public NinePatchComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
		super(rayHandler, world, rm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createComponents(Entity root, Entity entity, MainItemVO vo) {
		createCommonComponents(entity, vo, EntityFactory.NINE_PATCH);
		createParentNodeComponent(root, entity);
		createNodeComponent(root, entity);
		createPhysicsComponents(entity, vo);
		createNinePatchComponent(entity, (Image9patchVO) vo);
	}

	@Override
	protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
		DimensionsComponent component = new DimensionsComponent();
		component.height = ((Image9patchVO) vo).height;
		component.width = ((Image9patchVO) vo).width;

		entity.add(component);
		return component;
	}

	private void createNinePatchComponent(Entity entity, Image9patchVO vo) {
		NinePatchComponnent ninePatchComponent = new NinePatchComponnent();
		AtlasRegion atlasRegion = (TextureAtlas.AtlasRegion) rm.getTextureRegion(vo.imageName);
		ninePatchComponent.ninePatch = new NinePatch(atlasRegion, atlasRegion.splits[0], atlasRegion.splits[1], atlasRegion.splits[2], atlasRegion.splits[3]);
		entity.add(ninePatchComponent);
	}

}
