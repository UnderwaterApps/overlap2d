package com.uwsoft.editor.renderer.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;

/**
 * Created by azakhary on 9/28/2014.
 */
public class PhysicsBodyLoader {

    private static PhysicsBodyLoader instance;

    private float scale;

    private PhysicsBodyLoader() {

    }

    public static PhysicsBodyLoader getInstance() {
        if(instance == null) {
            instance = new PhysicsBodyLoader();
        }

        return instance;
    }

    public void setScaleFromPPWU(float pixelPerWU) {
        scale = 1f/(20f*pixelPerWU);
    }

    public static float getScale() {
        return getInstance().scale;
    }

    public Body createBody(World world, PhysicsBodyComponent pysicsComponent, Vector2[][] minPolygonData, Vector2 mulVec) {

        FixtureDef fixtureDef = new FixtureDef();

        if(pysicsComponent != null) {
            fixtureDef.density = pysicsComponent.density;
            fixtureDef.friction = pysicsComponent.friction;
            fixtureDef.restitution = pysicsComponent.restitution;
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0);

        if(pysicsComponent.bodyType == 0) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        } else if (pysicsComponent.bodyType == 1){
            bodyDef.type = BodyDef.BodyType.KinematicBody;
        } else {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();

        for(int i = 0; i < minPolygonData.length; i++) {
        	float[] verts = new float[minPolygonData[i].length * 2];
        	for(int j=0;j<verts.length;j+=2){
        		verts[j] = minPolygonData[i][j/2].x * mulVec.x * scale;
        		verts[j+1] = minPolygonData[i][j/2].y * mulVec.y * scale;
        	}
            polygonShape.set(verts);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        return body;
    }

}
