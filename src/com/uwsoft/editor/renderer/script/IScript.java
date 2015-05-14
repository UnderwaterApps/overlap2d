package com.uwsoft.editor.renderer.script;

import com.badlogic.ashley.core.Entity;


public interface IScript {
    public void init(Entity item);

    public void dispose();

    public void act(float delta);
}
