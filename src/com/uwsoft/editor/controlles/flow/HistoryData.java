package com.uwsoft.editor.controlles.flow;

import com.uwsoft.editor.renderer.data.CompositeItemVO;

/**
 * This is a history data point created on each user action.
 *
 * Created by sargis on 7/23/14.
 */
class HistoryData {
	 /**
	  * The snapshot of scene, stored as a VO data object of root element of the scene.
	  */
    public CompositeItemVO compositeItemVO;

	 /**
	  * The type of user action
	  */
    public FlowActionEnum action;
}
