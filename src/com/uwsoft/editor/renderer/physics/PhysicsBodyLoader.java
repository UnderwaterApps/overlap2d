package com.uwsoft.editor.renderer.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.data.MeshVO;
import com.uwsoft.editor.renderer.data.PhysicsBodyDataVO;

/**
 * Created by azakhary on 9/28/2014.
 */
public class PhysicsBodyLoader {
	public static final float SCALE = 1/10f;
    // Mesh Data
    private final World world;

    public PhysicsBodyLoader(World world) {
        this.world = world;
    }

    public Body createBody(PhysicsBodyDataVO data, MeshVO mesh,Vector2 mulVec) {
        return PhysicsBodyLoader.createBody(world, data, mesh, mulVec);
    }

    public static Body createBody(World world, PhysicsBodyDataVO data, MeshVO mesh, Vector2 mulVec) {
        FixtureDef fixtureDef = new FixtureDef();

        if(data != null) {
            fixtureDef.density = data.density;
            fixtureDef.friction = data.friction;
            fixtureDef.restitution = data.restitution;
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0, 0);

        if(data.bodyType == 0) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        } else if (data.bodyType == 1){
            bodyDef.type = BodyDef.BodyType.KinematicBody;
        } else {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();

        for(int i = 0; i < mesh.minPolygonData.length; i++) {
        	float[] verts = new float[mesh.minPolygonData[i].length * 2];
        	for(int j=0;j<verts.length;j+=2){
        		verts[j] = mesh.minPolygonData[i][j/2].x * mulVec.x * SCALE;
        		verts[j+1] = mesh.minPolygonData[i][j/2].y * mulVec.y * SCALE;
        	}
            polygonShape.set(verts);
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
        }

        return body;
    }

}
