package com.uwsoft.editor.view.ui.properties.panels;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.uwsoft.editor.event.SelectBoxChangeListener;
import com.uwsoft.editor.view.ui.properties.UIItemCollapsibleProperties;

/**
 * Created by azakhary on 8/2/2015.
 */
public class UIImageItemProperties extends UIItemCollapsibleProperties {

    private VisSelectBox renderModeBox;
    private VisSelectBox spriteTypeBox;

    public UIImageItemProperties() {
        super("Render Properties");

        renderModeBox = new VisSelectBox<>();
        spriteTypeBox = new VisSelectBox<>();

        renderModeBox.setItems("REPEAT");
        spriteTypeBox.setItems("SQUARE", "POLYGON");

        mainTable.add(createLabel("Render Mode:", Align.right)).padRight(5).width(90).left();
        mainTable.add(renderModeBox).left().width(90).padRight(5);
        mainTable.row().padTop(5);
        mainTable.add(createLabel("Sprite Type:", Align.right)).padRight(5).width(90).left();
        mainTable.add(spriteTypeBox).left().width(90).padRight(5);
        mainTable.row().padTop(5);

        collapse();

        renderModeBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));
        spriteTypeBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));
    }

    public void setRenderMode(String mode) {
        renderModeBox.setSelected(mode);
    }

    public String getRenderMode() {
        return renderModeBox.getSelected().toString();
    }

    public void setSpriteType(String mode) {
        spriteTypeBox.setSelected(mode);
    }

    public String getSpriteType() {
        return spriteTypeBox.getSelected().toString();
    }
}
