package com.uwsoft.editor.renderer.actor;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.uwsoft.editor.renderer.IResource;
import com.uwsoft.editor.renderer.data.SimpleImageVO;

public class Image9patchItem extends ImageItem implements IBaseItem {


    public Image9patchItem(SimpleImageVO vo, IResource rm) {
        super(vo, rm, new NinePatch());
    }

    public Image9patchItem(SimpleImageVO vo, IResource rm, CompositeItem parent) {
        this(vo, rm);
        setParent(parent);
    }
}
