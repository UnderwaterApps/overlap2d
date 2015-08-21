package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.utils.PolygonUtils;

public class TextureRegionComponent implements Component {
	public String regionName = "";
	public TextureRegion region = null;
    public boolean isRepeat = false;
    public boolean isPolygon = false;

    // optional
    public PolygonSprite polygonSprite = null;

    public void setPolygonSprite(PolygonComponent polygonComponent, float pixelToWorld) {
        Vector2[] verticesArray = PolygonUtils.mergeTouchingPolygonsToOne(polygonComponent.vertices);
        float[] vertices = new float[verticesArray.length*2];
        for(int i  = 0; i < verticesArray.length; i++) {
            vertices[i*2] = verticesArray[i].x*pixelToWorld;
            vertices[i*2+1] = verticesArray[i].y*pixelToWorld;
        }
        
        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        short[] triangles = triangulator.computeTriangles(vertices).toArray();
        
        PolygonRegion polygonRegion = new PolygonRegion(region, vertices, triangles);
        polygonSprite = new PolygonSprite(polygonRegion);
    }
}
