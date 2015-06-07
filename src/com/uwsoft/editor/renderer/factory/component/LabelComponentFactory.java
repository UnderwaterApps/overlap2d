package com.uwsoft.editor.renderer.factory.component;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.components.particle.ParticleComponent;
import com.uwsoft.editor.renderer.legacy.data.LabelVO;
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;
import com.uwsoft.editor.renderer.legacy.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

public class LabelComponentFactory extends ComponentFactory{

	public LabelComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
		super(rayHandler, world, rm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createComponents(Entity root, Entity entity, MainItemVO vo) {
		 createCommonComponents(entity, vo);
		 createParentNodeComponent(root, entity);
		 createNodeComponent(root, entity);
		 createPhysicsComponents(entity, vo);
		 createLabelCompononet(entity, (LabelVO) vo);
	}

	 @Override
	    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
	        DimensionsComponent component = new DimensionsComponent();
	        component.height = 100;
	        component.width = 100;

	        entity.add(component);
	        return component;
	    }

	    protected LabelComponent createLabelCompononet(Entity entity, LabelVO vo) {
	        //TODO
	    	LabelComponent component = new LabelComponent();
	        entity.add(component);
	        return component;
	    }

}
