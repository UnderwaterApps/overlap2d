package com.uwsoft.editor.renderer.scripts;

import com.badlogic.ashley.core.Entity;


public interface IScript {
    public void init(Entity entity);

    public void act(float delta);

    public void dispose();
}
