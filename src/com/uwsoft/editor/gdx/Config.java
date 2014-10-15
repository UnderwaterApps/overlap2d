package com.uwsoft.editor.gdx;

import com.badlogic.gdx.Gdx;

public class Config {
	public static final int[][]	SUPPORTED_SIZES							= { { 320, 480 }, { 480, 800 } };
	public static int [] SIZE  	= { 320, 480 };
	public static boolean isSoundOn = true;
	public static float mulX = 1.0f;
	public static float mulY = 1.0f;
	public static float fakeMulX = 1.0f;
	public static float fakeMulY = 1.0f;
	
	
	public static final void computeWindowedSize() {
		final int screenWidth;
		final int screenHeight;
			screenWidth = Gdx.graphics.getWidth();
			screenHeight = Gdx.graphics.getHeight();
		for (int i = 0; i < SUPPORTED_SIZES.length; i++) { 
			int[] curSize = SUPPORTED_SIZES[i];
			int curW = curSize[1];
			int curH = curSize[0];
			if (curW == screenWidth && curH == screenHeight) {
				
				mulX = (float) curSize[1]/SIZE[1];
				mulY = (float) curSize[0]/SIZE[0];			
				SIZE = curSize;
				if(curSize[0] == 320){
					fakeMulX = 1.0f;
					fakeMulY = 1.0f;
				}else{
					fakeMulX = 1.5f;
					fakeMulY = 1.5f;
				}
				return;
			}
		}
		int[] bestSize = SUPPORTED_SIZES[0];
		for (int i = 0; i < SUPPORTED_SIZES.length; i++) {
			int[] curSize = SUPPORTED_SIZES[i];
			int curH = curSize[0];
			if (curH == screenHeight) { 
				bestSize = curSize;
				break;
			} else if (curH < screenHeight) {
				bestSize = curSize;
			} else if (curH > screenHeight) {
				break;
			}
		}
		mulX = (float) bestSize[1]/SIZE[1];
		
		mulY = (float) bestSize[0]/SIZE[0];
		if(bestSize[0] == 320){
			fakeMulX = 1.0f;
			fakeMulY = 1.0f;
		}else{
			fakeMulX = 1.5f;
			fakeMulY = 1.5f;
		}		
		SIZE = bestSize;
	}
}
