package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;

import java.util.HashMap;
import java.util.Map;

/**
 * DropDown element with list of clickable items
 * It requires listener being given from outside
 */
public class DropDown extends Group {

    private final Overlap2DFacade facade;
    private final DataManager dataManager;
    private Group container;
    private HashMap<Integer, String> listEntries = new HashMap<Integer, String>();
    private SelectionEvent eventListener;

    public DropDown(Group container) {
        this.container = container;
        container.addActor(this);
        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
    }

    public SelectionEvent getEventListener() {
        return eventListener;
    }

    public void setEventListener(SelectionEvent eventListener) {
        this.eventListener = eventListener;
    }

    public void clearItems() {
        listEntries.clear();
    }

    public void initView(float x, float y) {
        super.clear();
        setVisible(true);

        int iterator = 0;
        for (Map.Entry<Integer, String> entry : listEntries.entrySet()) {
            final Integer action = entry.getKey();
            String name = entry.getValue();

            final PixelRect rct = new PixelRect(130, 20);
            rct.setFillColor(new Color(0.32f, 0.32f, 0.32f, 1));
            rct.setBorderColor(new Color(0.22f, 0.22f, 0.22f, 1));

            rct.setY(-(iterator + 1) * rct.getHeight());

            addActor(rct);

            Label lbl = new Label(name, dataManager.textureManager.editorSkin);
            lbl.setX(3);
            lbl.setY(rct.getY() + 3);
            lbl.setColor(new Color(1, 1, 1, 0.65f));
            lbl.setTouchable(Touchable.disabled);
            addActor(lbl);

            rct.addListener(new ClickListener() {
                @Override
                public boolean mouseMoved(InputEvent event, float x, float y) {
                    rct.setFillColor(new Color(0.26f, 0.26f, 0.26f, 1));
                    return true;
                }

                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    rct.setFillColor(new Color(0.32f, 0.32f, 0.32f, 1));
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    if (eventListener != null) {
                        eventListener.doAction(action);
                    }
                    hide();
                }
            });

            iterator++;
        }

        Vector2 vector2 = container.stageToLocalCoordinates(new Vector2(x, y));
        setX(vector2.x);
        setY(container.getStage().getHeight() - vector2.y);
    }

    public void addItem(int action, String name) {
        listEntries.put(action, name);
    }

    public void hide() {
        setVisible(false);
    }

    public interface SelectionEvent {
        public void doAction(int action);

    }
}
