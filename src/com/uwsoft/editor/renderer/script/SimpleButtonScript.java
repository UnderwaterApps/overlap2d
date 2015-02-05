package com.uwsoft.editor.renderer.script;

/**
 * Created by azakhary on 9/9/2014.
 */

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.uwsoft.editor.renderer.actor.CompositeItem;

public class SimpleButtonScript implements IScript {

    public static final int TEXT_EFFECT_NONE = 0;
    protected int textEffect = TEXT_EFFECT_NONE;
    public static final int TEXT_EFFECT_PUSH = 1;
    public static final int TEXT_EFFECT_DOWN = 2;
    protected final DelayedRemovalArray<ClickListener> listeners = new DelayedRemovalArray(0);
    protected CompositeItem buttonHolder;
    protected boolean isDown = false;
    protected boolean isToggled = false;
    protected float origTextY;
    protected float origTextScaleX;
    protected float origTextScaleY;
    
    public static SimpleButtonScript selfInit(CompositeItem item) {
        SimpleButtonScript script = new SimpleButtonScript();
        item.addScript(script);

        return script;
    }

    public int getTextEffect() {
        return textEffect;
    }

    public void setTextEffect(int textEffect) {
        this.textEffect = textEffect;
    }

    public boolean isDown() {
        return isDown;
    }

    public boolean isToggled() {
        return isToggled;
    }

    @Override
    public void init(CompositeItem item) {
        this.buttonHolder = item;

        String text = item.getCustomVariables().getStringVariable("text");
        if (item.getLabelById("text") != null) {
        	if(text != null) {
        		item.getLabelById("text").setText(text);
        	}
        	
            item.getLabelById("text").setAlignment(Align.center);
            origTextY = item.getLabelById("text").getY();
            origTextScaleX = item.getLabelById("text").getScaleX();
            origTextScaleY = item.getLabelById("text").getScaleY();
        }


        buttonHolder.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isDown = true;

                for (int i = 0; i < listeners.size; i++) listeners.get(i).touchDown(event, x, y, pointer, button);

                return super.touchDown(event, x, y, pointer, button);
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDown = false;
                isToggled = !isToggled;

                for (int i = 0; i < listeners.size; i++) listeners.get(i).touchUp(event, x, y, pointer, button);

                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void dispose() {
        clearListeners();
    }

    @Override
    public void act(float delta) {
        if (isDown) {
            buttonHolder.setLayerVisibilty("checked", false);
            buttonHolder.setLayerVisibilty("normal", false);
            buttonHolder.setLayerVisibilty("pressed", true);
        } else {
            if (isToggled && buttonHolder.layerExists("checked")) {
                buttonHolder.setLayerVisibilty("checked", true);
                buttonHolder.setLayerVisibilty("normal", false);
            } else {
                buttonHolder.setLayerVisibilty("normal", true);
                buttonHolder.setLayerVisibilty("checked", false);
            }

            buttonHolder.setLayerVisibilty("pressed", false);
        }

        if (buttonHolder.getLabelById("text") != null) {
            if (textEffect == TEXT_EFFECT_DOWN) {
                if (isDown) {
                    buttonHolder.getLabelById("text").setY(origTextY - 5 * buttonHolder.mulY);
                } else {
                    buttonHolder.getLabelById("text").setY(origTextY);
                }
            } else if (textEffect == TEXT_EFFECT_PUSH) {
                if (isDown) {
                    buttonHolder.getLabelById("text").setScale(0.9f);
                } else {
                    buttonHolder.getLabelById("text").setScaleX(origTextScaleX);
                    buttonHolder.getLabelById("text").setScaleY(origTextScaleY);
                }
            }

        }

    }

    public void setToggle(boolean toggle) {
        isToggled = toggle;
    }

    /**
     * Add a listener to receive events like click
     *
     * @see InputListener
     * @see ClickListener
     */
    public boolean addListener(ClickListener listener) {
        if (!listeners.contains(listener, true)) {
            listeners.add(listener);
            return true;
        }
        return false;
    }

    public boolean removeListener(ClickListener listener) {
        return listeners.removeValue(listener, true);
    }

    public Array<ClickListener> getListeners() {
        return listeners;
    }

    public void clearListeners() {
        listeners.clear();
    }

    public CompositeItem getItem() {
    	return buttonHolder;
    }
    
}
