package com.uwsoft.editor.renderer.factory.component;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.components.particle.ParticleComponent;
import com.uwsoft.editor.renderer.data.LabelVO;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

public class LabelComponentFactory extends ComponentFactory{
	
	private static int labelDefaultSize = 12;

	public LabelComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
		super(rayHandler, world, rm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createComponents(Entity root, Entity entity, MainItemVO vo) {
		 createCommonComponents(entity, vo, EntityFactory.LABEL_TYPE);
		 createParentNodeComponent(root, entity);
		 createNodeComponent(root, entity);
		 createLabelComponent(entity, (LabelVO) vo);
	}

	@Override
	protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();
        component.height = ((LabelVO) vo).height;
        component.width = ((LabelVO) vo).width;

        entity.add(component);
        return component;
    }

    protected LabelComponent createLabelComponent(Entity entity, LabelVO vo) {
    	LabelComponent component = new LabelComponent(vo.text, generateStyle(rm, vo.style, vo.size));
        component.fontName = vo.style;
        component.fontSize = vo.size;
        component.setAlignment(vo.align);

        ProjectInfoVO projectInfoVO = rm.getProjectVO();
        component.setFontScale(1f/projectInfoVO.pixelToWorld);

        entity.add(component);
        return component;
    }
    
    
    public static LabelStyle generateStyle(IResourceRetriever rManager, String fontName, int size) {

        if (size == 0) {
            size = labelDefaultSize;
        }
        return new LabelStyle(rManager.getBitmapFont(fontName, size), null);
    }

}
