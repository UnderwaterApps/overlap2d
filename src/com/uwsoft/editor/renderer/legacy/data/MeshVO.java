package com.uwsoft.editor.renderer.data;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MeshVO {
	public Vector2 [][] minPolygonData;
	public PhysicsBodyDataVO initialProperties;


    public MeshVO clone() {
        MeshVO newVo = new MeshVO();
        Vector2 [][] target = new Vector2[minPolygonData.length][];

        for (int i = 0; i < minPolygonData.length; i++) {
            target[i] = new Vector2[minPolygonData[i].length];
            for(int j=0;j<minPolygonData[i].length;j++){
                target[i][j] = minPolygonData[i][j].cpy();
            }
        }
        newVo.minPolygonData = target;

        if(initialProperties != null) {
            newVo.initialProperties = new PhysicsBodyDataVO(initialProperties);
        }

        return newVo;
    }
}
