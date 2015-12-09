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

package com.uwsoft.editor.utils.poly.earclipping.ewjordan;

/**
 *
 * @author EwJordan (http://www.ewjordan.com/earClip/)
 */
public class Triangle {
	public float[] x;
	public float[] y;

	public Triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		x = new float[3];
		y = new float[3];
		float dx1 = x2 - x1;
		float dx2 = x3 - x1;
		float dy1 = y2 - y1;
		float dy2 = y3 - y1;
		float cross = dx1 * dy2 - dx2 * dy1;
		boolean ccw = (cross > 0);
		if (ccw) {
			x[0] = x1;
			x[1] = x2;
			x[2] = x3;
			y[0] = y1;
			y[1] = y2;
			y[2] = y3;
		} else {
			x[0] = x1;
			x[1] = x3;
			x[2] = x2;
			y[0] = y1;
			y[1] = y3;
			y[2] = y2;
		}
	}

	public boolean isInside(float _x, float _y) {
		float vx2 = _x - x[0];
		float vy2 = _y - y[0];
		float vx1 = x[1] - x[0];
		float vy1 = y[1] - y[0];
		float vx0 = x[2] - x[0];
		float vy0 = y[2] - y[0];

		float dot00 = vx0 * vx0 + vy0 * vy0;
		float dot01 = vx0 * vx1 + vy0 * vy1;
		float dot02 = vx0 * vx2 + vy0 * vy2;
		float dot11 = vx1 * vx1 + vy1 * vy1;
		float dot12 = vx1 * vx2 + vy1 * vy2;
		float invDenom = 1f / (dot00 * dot11 - dot01 * dot01);
		float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

		return ((u > 0) && (v > 0) && (u + v < 1));
	}
}
