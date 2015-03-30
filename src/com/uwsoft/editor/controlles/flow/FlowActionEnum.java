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

package com.uwsoft.editor.controlles.flow;

/**
 * Simple list of possible types of user actions that are "Undoable"
 *
 * Created by sargis on 7/23/14.
 */
public enum FlowActionEnum {
	 /**
	  * SIMPLE is any simple user action that does not include getting in or out of composites
	  * TODO: this is supposed to be devided by more types in future, instead of keeping it generic and simple
	  */
    SIMPLE,

	 /**
	  * GET_INTO_COMPOSITE - user does no modification but enters into composite item
	  */
    GET_INTO_COMPOSITE,

	 /**
	  * GET_OUT_COMPOSITE - user does no modification but exists currently opened composite item
	  */
    GET_OUT_COMPOSITE
}
