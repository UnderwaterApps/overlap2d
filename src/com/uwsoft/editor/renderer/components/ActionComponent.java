package com.uwsoft.editor.renderer.components;

import com.badlogic.ashley.core.Component;
import com.uwsoft.editor.renderer.systems.action.data.ActionData;
import com.uwsoft.editor.renderer.systems.action.logic.ActionLogic;

import java.util.ArrayList;

/**
 * Created by Eduard on 10/13/2015.
 */
public class ActionComponent implements Component {
    public ArrayList<ActionData> dataArray = new ArrayList<ActionData>();
    public ActionLogic action;
}
