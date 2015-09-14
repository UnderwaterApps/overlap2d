/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.view.ui.widget.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Rectangle;
import com.brashmonkey.spriter.SCMLReader;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.LibGdxDrawer;
import com.uwsoft.editor.renderer.utils.LibGdxLoader;

import java.util.ArrayList;

/**
 * Created by hayk on 12/8/14.
 */
public class SpriterActor extends Actor {

    public boolean looping;
    protected boolean reverse = false;

    private int frameHeight;
    private int frameWidth;

    private String animationName = "";
     private String currentAnimationName = "";

    private LibGdxDrawer drawer;
    private Player player;
    private Data data;
    private ArrayList<String> animations = new ArrayList<String>();
    private ArrayList<String> entities = new ArrayList<String>();
    private int currentEntityIndex	=	0;
    private int currentAnimationIndex;

    private IResourceRetriever irr;

    public SpriterActor(String animationName, IResourceRetriever irr) {
        this.irr = irr;
        this.animationName = animationName;

        initSpriterAnimation();
    }


    private void initSpriterAnimation() {
        FileHandle handle 	=	irr.getSCMLFile(animationName);
        data 			= 	new SCMLReader(handle.read()).getData();
        LibGdxLoader loader = 	new LibGdxLoader(data);
        loader.load(handle.file());
        ShapeRenderer renderer	=	new ShapeRenderer();
        drawer = new LibGdxDrawer(loader, renderer);
        currentAnimationIndex	=	0;
        currentEntityIndex		=	0;
        initPlayer();
    }

    private void initPlayer() {
        player = new Player(data.getEntity(currentEntityIndex));
        player.setAnimation(currentAnimationIndex);
        player.setScale(1);
        setRectangle();
    }

    private void setRectangle() {
        player.update();
        Rectangle bbox = player.getBoundingRectangle(null);
        frameWidth = (int) bbox.size.width;
        frameHeight = (int) bbox.size.height;
        setWidth(frameWidth);
        setHeight(frameHeight);
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        player.setScale(scaleXY);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        player.update();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1, 1, 1, parentAlpha * getColor().a);
        super.draw(batch, parentAlpha);

        player.setPosition(getX(), getY());
        player.setPivot(getWidth()/2, getHeight()/2);
        player.rotate(getRotation()-player.getAngle());
        drawer.beforeDraw(player,batch);
    }


    public ArrayList<String> getAnimations() {
        animations	=	new ArrayList<String>();

        for (int i = 0; i < data.getEntity(currentEntityIndex).animations(); i++) {
            animations.add(data.getEntity(currentEntityIndex).getAnimation(i).name);
        }
        return animations;
    }
}