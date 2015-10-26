package com.uwsoft.editor.renderer.scripts;

import com.uwsoft.editor.renderer.scene2d.CompositeActor;


public interface IActorScript {
    public void init(CompositeActor entity);

    public void act(float delta);

    public void dispose();
}
