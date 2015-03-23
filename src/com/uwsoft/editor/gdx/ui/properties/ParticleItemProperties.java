package com.uwsoft.editor.gdx.ui.properties;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.ParticleActor;
import com.uwsoft.editor.renderer.actor.ParticleItem;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

/**
 * Created by azakhary on 7/2/2014.
 */
public class ParticleItemProperties extends Group implements IPropertyBox<ParticleItem> {

    private Group mainGroup;

    private IResourceRetriever rm;

    public ParticleItemProperties(SceneLoader scene) {
        rm = scene.getRm();
        initView();
    }

    @Override
    public void initView() {
        clear();
        Image bgImg = new Image(DataManager.getInstance().textureManager.getEditorAsset("pixel"));
        bgImg.setColor(0, 0, 0, 1.0f);
        bgImg.setScale(230, 100);
        addActor(bgImg);
        setWidth(230);
        setHeight(100);

        mainGroup = new Group();
        addActor(mainGroup);
    }


    @Override
    public void updateView() {

    }

    @Override
    public void setObject(ParticleItem object) {
        mainGroup.clear();
        ParticleActor particle = new ParticleActor(rm.getParticleEffect(((ParticleEffectVO)object.getDataVO()).particleName));
        Array<ParticleEmitter> emitters = particle.getParticleEffect().getEmitters();
        for(int i = 0; i < emitters.size; i++) {
            emitters.get(i).setContinuous(true);
        }
        particle.start();
        mainGroup.addActor(particle);
        particle.setX(getWidth()/2);
        particle.setY(getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        boolean t = clipBegin(getX(),getY(), getWidth(), getHeight());
        if(t){
            super.draw(batch, parentAlpha);
            batch.flush();
            clipEnd();
            return;
        }
        super.draw(batch, parentAlpha);
    }
}
