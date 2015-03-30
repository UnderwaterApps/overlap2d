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

import java.util.ArrayList;

import com.uwsoft.editor.renderer.data.CompositeItemVO;

/**
 * Keeps track of user actions that can be one of {@link FlowActionEnum} types
 * Allow functionality such as undo redo and adding a new action to history
 *
 * Current model is done in a simple "loophole"-ish way, so instead of making a huge list of
 * each possible user action, we have just 3 types, the biggest of which is simple action,
 * and on each action this class will kind of copy of entire scene snapshot, in order to revert back.
 *
 * TODO: ideally this should be changed to standard COMMAND pattern, but will require a shitload of work
 *
 * @author azakhary
 */
public class FlowManager {

	 /**
	  * List of {@link HistoryData} items, that are basically data points of user action/scene-snapshot
	  */
    private ArrayList<HistoryData> history = new ArrayList<>();

	 /**
	  * index of where we stand in history of actions at this moment
	  */
    private int historyPointer;

	 /**
	  * Keeps a scheduled data to be added as a history data point
	  */
    private HistoryData pendingHistoryData;

    public FlowManager(CompositeItemVO dataVO) {
        historyPointer = -1;

		  /**
			* When this is just created a first data point is created from existing scene, as a simple action of doing nothing
			*/
        setPendingHistory(dataVO, FlowActionEnum.SIMPLE);
        applyPendingAction();
    }

	 /**
	  * Adds fixed history point to the end of chain if pointer is at the end of action chain
	  * If pointer is not in the end of action chain, then all other data points should be removed, and this one added.
	  * Should be private and used internally to permanently add a point
	  *
	  * @param data point to add
	  */
    private void addHistory(HistoryData data) {
        ++historyPointer;
        if (history.size() == historyPointer) {
            history.add(data);
        } else {
            history.set(historyPointer, data);
            ArrayList<HistoryData> newHistory = new ArrayList<>();
            newHistory.addAll(history.subList(0, historyPointer));
            history = newHistory;
            historyPointer = history.size() - 1;
        }
    }

	 /**
	  * Undo action reverts history to a point back from current point,
	  *
	  * @return snapshot data of new current scene composite item
	  */
    public CompositeItemVO undo() {
        HistoryData historyData = history.get(historyPointer > 0 ? --historyPointer : 0);
        return new CompositeItemVO(historyData.compositeItemVO);
    }

	 /**
	  * Redo action reverts history to a point previously undone if there is any
	  *
	  * @return snapshot data of new current scene composite item
	  */
    public CompositeItemVO redo() {
        HistoryData historyData = history.get(historyPointer < history.size() - 1 ? ++historyPointer : history.size() - 1);
        return new CompositeItemVO(historyData.compositeItemVO);
    }

	 /**
	  * @return latest history data point
	  */
    public FlowActionEnum getFlowLastAction() {
        HistoryData historyData = history.get(historyPointer);
        return historyData.action;
    }

	 /**
	  * Adds a simple snapshot of scene as a simple action to history
	  *
	  * @param pendingAction snapshot data of scene
	  */
    public void setPendingHistory(CompositeItemVO pendingAction) {
        setPendingHistory(pendingAction, FlowActionEnum.SIMPLE);
    }

	 /**
	  * Adds snapshot of current root composite with specified user action type
	  *
	  * @param composite snapshot of root composite to be remembered
	  * @param action action type
	  */
    public void setPendingHistory(CompositeItemVO composite, FlowActionEnum action) {
        pendingHistoryData = new HistoryData();
        pendingHistoryData.compositeItemVO = new CompositeItemVO(composite);
        pendingHistoryData.action = action;
    }

	 /**
	  * cancels a pending action so it will not be added to history
	  */
    public void cancelPendingAction() {
        pendingHistoryData = null;
    }

	 /**
	  * Submits a pending action, by adding it to history
	  */
    public void applyPendingAction() {
		  if(pendingHistoryData != null) {
				addHistory(pendingHistoryData);
		  }

        pendingHistoryData = null;
    }
}
