package com.uwsoft.editor.renderer.data;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.NinePatchComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;

public class SimpleImageVO extends MainItemVO {
	public String imageName = "";
	
	public SimpleImageVO() {
		super();
	}
	
	public SimpleImageVO(SimpleImageVO vo) {
		super(vo);
		imageName = new String(vo.imageName);
	}

	@Override
	public void loadFromEntity(Entity entity) {
		super.loadFromEntity(entity);

		TextureRegionComponent textureRegionComponent = entity.getComponent(TextureRegionComponent.class);
		if(textureRegionComponent != null) {
			imageName = textureRegionComponent.regionName;
		} else {
			NinePatchComponent ninePatchComponent = entity.getComponent(NinePatchComponent.class);
			imageName = ninePatchComponent.textureRegionName;
		}
	}
}
