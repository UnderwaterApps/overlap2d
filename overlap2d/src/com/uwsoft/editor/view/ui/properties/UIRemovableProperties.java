package com.uwsoft.editor.view.ui.properties;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * Created by CyberJoe on 7/2/2015.
 */
public abstract class UIRemovableProperties extends UIItemCollapsibleProperties {


    public UIRemovableProperties(String title) {
        super(title);
    }

    @Override
    public Table crateHeaderTable() {
        VisTable header = new VisTable();
        header.setBackground(VisUI.getSkin().getDrawable("expandable-properties-active-bg"));
        VisImageButton collapseButton = new VisImageButton("expandable-properties-button");
        VisImageButton closeButton = new VisImageButton("close-panel");
        header.add(closeButton).left().padLeft(2);
        header.add(createLabel(title)).left().expandX().padLeft(6);
        header.add(collapseButton).right().padRight(3);
        collapseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                collapsibleWidget.setCollapsed(!collapsibleWidget.isCollapsed());
                header.setBackground(VisUI.getSkin().getDrawable("expandable-properties-" + (collapsibleWidget.isCollapsed() ? "inactive" : "active") + "-bg"));
            }
        });
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                onClose();
                remove();
            }
        });
        return header;
    }

    public abstract void onClose();
}
