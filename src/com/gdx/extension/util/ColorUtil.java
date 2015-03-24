/** Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdx.extension.util;

public class ColorUtil {

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
	int _r = 0, _g = 0, _b = 0;
	if (saturation == 0) {
	    _r = _g = _b = (int) (brightness * 255.0f + 0.5f);
	} else {
	    float _h = (hue - (float) Math.floor(hue)) * 6.0f;
	    float _f = _h - (float) Math.floor(_h);
	    float _p = brightness * (1.0f - saturation);
	    float _q = brightness * (1.0f - saturation * _f);
	    float _t = brightness * (1.0f - (saturation * (1.0f - _f)));
	    switch ((int) _h) {
		case 0:
		    _r = (int) (brightness * 255.0f + 0.5f);
		    _g = (int) (_t * 255.0f + 0.5f);
		    _b = (int) (_p * 255.0f + 0.5f);
		    break;
		case 1:
		    _r = (int) (_q * 255.0f + 0.5f);
		    _g = (int) (brightness * 255.0f + 0.5f);
		    _b = (int) (_p * 255.0f + 0.5f);
		    break;
		case 2:
		    _r = (int) (_p * 255.0f + 0.5f);
		    _g = (int) (brightness * 255.0f + 0.5f);
		    _b = (int) (_t * 255.0f + 0.5f);
		    break;
		case 3:
		    _r = (int) (_p * 255.0f + 0.5f);
		    _g = (int) (_q * 255.0f + 0.5f);
		    _b = (int) (brightness * 255.0f + 0.5f);
		    break;
		case 4:
		    _r = (int) (_t * 255.0f + 0.5f);
		    _g = (int) (_p * 255.0f + 0.5f);
		    _b = (int) (brightness * 255.0f + 0.5f);
		    break;
		case 5:
		    _r = (int) (brightness * 255.0f + 0.5f);
		    _g = (int) (_p * 255.0f + 0.5f);
		    _b = (int) (_q * 255.0f + 0.5f);
		    break;
	    }
	}
	return 0xff000000 | (_r << 16) | (_g << 8) | (_b << 0);
    }

    public static Color getHSBColor(float h, float s, float b) {
	return new Color(HSBtoRGB(h, s, b));
    }

    public static class Color {

	private int value;

	public Color(int rgb) {
	    value = 0xff000000 | rgb;
	}

	public int getValue() {
	    return value;
	}

	public int getRed() {
	    return (value >> 16) & 0xFF;
	}

	public int getGreen() {
	    return (value >> 8) & 0xFF;
	}

	public int getBlue() {
	    return (value >> 0) & 0xFF;
	}

	public int getAlpha() {
	    return (value >> 24) & 0xff;
	}
    }

}
