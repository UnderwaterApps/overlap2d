package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.utils.PolygonUtils;

public class DimensionsComponent implements Component {
	public float width = 0;
	public float height = 0;

	public Rectangle boundBox;
    public Polygon polygon;

	public boolean hit(float x, float y){
        if(polygon != null) {
            return polygon.contains(x, y);
        } else if(boundBox != null) {
            return (x >= boundBox.x && x < boundBox.x+boundBox.width && y >= boundBox.y && y < boundBox.y+boundBox.height);
        } else {
            return (x >= 0 && x < width && y >= 0 && y < height);
        }
	}

    public void setPolygon(PolygonComponent polygonComponent) {
        Vector2[] verticesArray = PolygonUtils.mergeTouchingPolygonsToOne(polygonComponent.vertices);
        float[] vertices = new float[verticesArray.length*2];
        for(int i  = 0; i < verticesArray.length; i++) {
            vertices[i*2] = (verticesArray[i].x);
            vertices[i*2+1] = (verticesArray[i].y);
        }
        polygon = new Polygon(vertices);
    }
}
