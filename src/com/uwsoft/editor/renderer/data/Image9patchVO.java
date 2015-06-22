package com.uwsoft.editor.renderer.data;

public class Image9patchVO extends SimpleImageVO {
    public float width = 0;
    public float height = 0;

    public Image9patchVO() {
        super();
    }

    public Image9patchVO(Image9patchVO vo) {
        super(vo);
        width = vo.width;
        height = vo.height;
    }
}
