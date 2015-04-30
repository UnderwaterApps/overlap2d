package com.uwsoft.editor.renderer.script;


public interface IScript {
    public void init(CompositeItem item);

    public void dispose();

    public void act(float delta);
}
