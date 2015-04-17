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

package com.uwsoft.editor.gdx.sandbox;

/**
 * List of editing modes in sandbox
 *
 * Created by azakhary on 3/18/2015.
 */
public enum EditingMode {

	 /**
	  * user can only select boxes
	  */
    SELECTION,

	 /**
	  * User can scale and rotate boxes
	  */
    TRANSFORM,

	 /**
	  * User is in panning mode so dragging just moves the scene
	  */
    PAN
}
