package com.uwsoft.editor.renderer.factory.component;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NinePatchComponent;
import com.uwsoft.editor.renderer.data.Image9patchVO;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

public class NinePatchComponentFactory extends ComponentFactory {

	NinePatchComponent ninePatchComponent;

	public NinePatchComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
		super(rayHandler, world, rm);
	}

	@Override
	public void createComponents(Entity root, Entity entity, MainItemVO vo) {
		ninePatchComponent = createNinePatchComponent(entity, (Image9patchVO) vo);
		createCommonComponents(entity, vo, EntityFactory.NINE_PATCH);
		createParentNodeComponent(root, entity);
		createNodeComponent(root, entity);
	}

	@Override
	protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
		DimensionsComponent component = new DimensionsComponent();
		component.height = ((Image9patchVO) vo).height;
		component.width = ((Image9patchVO) vo).width;
		if(component.width == 0) {
			component.width = ninePatchComponent.ninePatch.getTotalWidth();
		}

		if(component.height == 0) {
			component.height = ninePatchComponent.ninePatch.getTotalHeight();
		}

		entity.add(component);
		return component;
	}

	private NinePatchComponent createNinePatchComponent(Entity entity, Image9patchVO vo) {
		NinePatchComponent ninePatchComponent = new NinePatchComponent();
		AtlasRegion atlasRegion = (TextureAtlas.AtlasRegion) rm.getTextureRegion(vo.imageName);
		ninePatchComponent.ninePatch = new NinePatch(atlasRegion, atlasRegion.splits[0], atlasRegion.splits[1], atlasRegion.splits[2], atlasRegion.splits[3]);

		ResolutionEntryVO resolutionEntryVO = rm.getLoadedResolution();
		ProjectInfoVO projectInfoVO = rm.getProjectVO();
		float multiplier = resolutionEntryVO.getMultiplier(rm.getProjectVO().originalResolution);

		ninePatchComponent.ninePatch.scale(multiplier/projectInfoVO.pixelToWorld, multiplier/projectInfoVO.pixelToWorld);
		ninePatchComponent.ninePatch.setMiddleWidth(ninePatchComponent.ninePatch.getMiddleWidth()*multiplier/projectInfoVO.pixelToWorld);
		ninePatchComponent.ninePatch.setMiddleHeight(ninePatchComponent.ninePatch.getMiddleHeight()*multiplier/projectInfoVO.pixelToWorld);

		ninePatchComponent.textureRegionName = vo.imageName;
		entity.add(ninePatchComponent);

		return ninePatchComponent;
	}

}
