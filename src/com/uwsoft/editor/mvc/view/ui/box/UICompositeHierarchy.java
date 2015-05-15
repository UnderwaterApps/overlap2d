package com.uwsoft.editor.mvc.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImageTextButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.uwsoft.editor.renderer.data.CompositeItemVO;

import java.util.Stack;

/**
 * Created by CyberJoe on 4/22/2015.
 */
public class UICompositeHierarchy extends UIBaseBox {

    private static final String PREFIX = "com.uwsoft.editor.mvc.view.ui.box.UICompositeHierarchy";

    public static final String SCENE_CHOOSEN = PREFIX + ".CREATE_NEW_RESOLUTION_BTN_CLICKED";

    private Stack<VisImageTextButton> buttons = new Stack<>();

    private HorizontalGroup mainGroup;

    public UICompositeHierarchy() {
        super();

        mainGroup = new HorizontalGroup();
        clearItems();

        add(mainGroup).left().fill();

        add().fill().expand();
        row();
    }

    public void addItem(String name, CompositeItemVO itemVo) {
        String classType = "hierarchy-item";
        if(name.equals("root")) classType+="-root";

        VisImageTextButton button = new VisImageTextButton(name, classType);
        button.getLabelCell().padLeft(3);


        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facade.sendNotification(SCENE_CHOOSEN, itemVo);
            }
        });

        button.padLeft(5).padRight(3);
        mainGroup.addActor(button);
        buttons.add(button);
    }

    public void removeLastItem() {
        VisImageTextButton button = buttons.pop();
        button.remove();
    }

    public void clearItems() {
        mainGroup.clear();
        buttons.clear();
    }

    @Override
    public void update() {

    }
}
