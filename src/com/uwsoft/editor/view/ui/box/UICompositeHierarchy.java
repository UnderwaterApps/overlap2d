package com.uwsoft.editor.view.ui.box;

import java.util.Stack;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImageTextButton;

/**
 * Created by CyberJoe on 4/22/2015.
 */
public class UICompositeHierarchy extends UIBaseBox {

    private static final String PREFIX = "com.uwsoft.editor.view.ui.box.UICompositeHierarchy";

    public static final String SWITCH_VIEW_COMPOSITE_CLICKED = PREFIX + ".SWITCH_VIEW_COMPOSITE_CLICKED";

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

    public void addItem(String name, Integer id) {
        String classType = "hierarchy-item";
        if(name.equals("root")) classType+="-root";

        VisImageTextButton button = new VisImageTextButton(name, classType);
        button.getLabelCell().padLeft(3);


        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facade.sendNotification(SWITCH_VIEW_COMPOSITE_CLICKED, id);
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
