package com.uwsoft.editor.mvc.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.uwsoft.editor.renderer.data.CompositeItemVO;

import java.util.Stack;

/**
 * Created by CyberJoe on 4/22/2015.
 */
public class UICompositeHierarchy extends UIBaseBox {

    private static final String PREFIX = "com.uwsoft.editor.mvc.view.ui.box.UICompositeHierarchy";

    public static final String SCENE_CHOOSEN = PREFIX + ".CREATE_NEW_RESOLUTION_BTN_CLICKED";

    private Stack<VisTextButton> buttons = new Stack<>();

    public UICompositeHierarchy() {
        clearItems();
    }

    public void addItem(String name, CompositeItemVO itemVo) {
        VisTextButton button = new VisTextButton(name);

        button.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                facade.sendNotification(SCENE_CHOOSEN, itemVo);
            }
        });

        add(button).left().expandX();
        buttons.add(button);
    }

    public void removeLastItem() {
        VisTextButton button = buttons.pop();
        button.remove();
        layout();
    }

    public void clearItems() {
        clear();
        buttons.clear();
    }

    @Override
    public void update() {

    }
}
