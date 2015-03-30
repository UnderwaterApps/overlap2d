package com.uwsoft.editor.gdx.sandbox;

/**
 * List of editing modes in sandbox
 *
 * Created by azakhary on 3/18/2015.
 */
public enum EditingMode {

	 /**
	  * user can only select items
	  */
    SELECTION,

	 /**
	  * User can scale and rotate items
	  */
    TRANSFORM,

	 /**
	  * User is in panning mode so dragging just moves the scene
	  */
    PAN
}
