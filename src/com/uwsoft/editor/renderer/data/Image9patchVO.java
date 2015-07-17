package com.uwsoft.editor.renderer.data;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.NinePatchComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;

public class Image9patchVO extends MainItemVO {

    public String imageName = "";
    public float width = 0;
    public float height = 0;

    public Image9patchVO() {
        super();
    }

    public Image9patchVO(Image9patchVO vo) {
        super(vo);
        imageName = new String(vo.imageName);
        width = vo.width;
        height = vo.height;
    }

    @Override
    public void loadFromEntity(Entity entity) {
        super.loadFromEntity(entity);

        NinePatchComponent ninePatchComponent = entity.getComponent(NinePatchComponent.class);
        DimensionsComponent dimensionsComponent = entity.getComponent(DimensionsComponent.class);
        imageName = ninePatchComponent.textureRegionName;

        width = dimensionsComponent.width;
        height = dimensionsComponent.height;
    }
}
