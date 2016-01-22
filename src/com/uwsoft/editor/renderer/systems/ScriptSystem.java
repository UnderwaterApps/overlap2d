package com.uwsoft.editor.renderer.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.uwsoft.editor.renderer.components.ScriptComponent;
import com.uwsoft.editor.renderer.scripts.IScript;

/**
 * Created by azakhary on 6/19/2015.
 */
public class ScriptSystem extends IteratingSystem {

    private ComponentMapper<ScriptComponent> scriptComponentComponentMapper = ComponentMapper.getFor(ScriptComponent.class);

    public ScriptSystem() {
        super(Family.all(ScriptComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        for(IScript script: scriptComponentComponentMapper.get(entity).scripts) {
            script.act(deltaTime);
        }
    }
}
