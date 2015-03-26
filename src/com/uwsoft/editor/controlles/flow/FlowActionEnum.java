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
