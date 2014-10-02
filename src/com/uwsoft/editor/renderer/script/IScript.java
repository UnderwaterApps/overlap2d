package com.uwsoft.editor.renderer.script;

import com.uwsoft.editor.renderer.actor.CompositeItem;

public interface IScript {
    public void init(CompositeItem item);

    public void dispose();

    public void act(float delta);
}
