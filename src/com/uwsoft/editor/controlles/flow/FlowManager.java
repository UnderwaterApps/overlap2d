package com.uwsoft.editor.controlles.flow;

import java.util.ArrayList;

import com.uwsoft.editor.renderer.data.CompositeItemVO;

/**
 * Keeps track of user actions for undo/redo to work
 */
public class FlowManager {

    private ArrayList<HistoryData> history = new ArrayList<>();
    private int historyPointer;
    private HistoryData pendingHistoryData;

    public FlowManager(CompositeItemVO dataVO) {
        historyPointer = -1;
        setPendingHistory(dataVO, FlowActionEnum.SIMPLE);
        applyPendingAction();
    }

    private void addHistory(HistoryData data) {
        ++historyPointer;
        if (history.size() == historyPointer) {
            history.add(data);
            //System.out.println("addHistory(), history.size : " + history.size() + " , historyPointer :" + historyPointer);
        } else {
            history.set(historyPointer, data);
            ArrayList<HistoryData> newHistory = new ArrayList<>();
            newHistory.addAll(history.subList(0, historyPointer));
            history = newHistory;
            historyPointer = history.size() - 1;
            //System.out.println("addHistory(), history.size : " + history.size() + " , historyPointer :" + historyPointer);
        }
    }


    public CompositeItemVO undo() {
        //System.out.println("undo(), history.size : " + history.size() + " , historyPointer :" + historyPointer);
        HistoryData historyData = history.get(historyPointer > 0 ? --historyPointer : 0);
        return new CompositeItemVO(historyData.compositeItemVO);
    }

    public CompositeItemVO redo() {
        HistoryData historyData = history.get(historyPointer < history.size() - 1 ? ++historyPointer : history.size() - 1);
        //System.out.println("redo(), history.size : " + history.size() + " , historyPointer :" + historyPointer);
        return new CompositeItemVO(historyData.compositeItemVO);
    }

    public FlowActionEnum getFlowLastAction() {
        HistoryData historyData = history.get(historyPointer);
        return historyData.action;
    }

    public void setPendingHistory(CompositeItemVO pendingAction) {
        setPendingHistory(pendingAction, FlowActionEnum.SIMPLE);
    }

    public void setPendingHistory(CompositeItemVO pendingAction, FlowActionEnum action) {
        pendingHistoryData = new HistoryData();
        pendingHistoryData.compositeItemVO = new CompositeItemVO(pendingAction);
        pendingHistoryData.action = action;
    }

    public void cancelPendingAction() {
        pendingHistoryData = null;
    }

    public void applyPendingAction() {
        addHistory(pendingHistoryData);
        pendingHistoryData = null;
    }
}
