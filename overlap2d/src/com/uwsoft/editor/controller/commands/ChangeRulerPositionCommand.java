package com.uwsoft.editor.controller.commands;

import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.uwsoft.editor.utils.Guide;
import com.uwsoft.editor.view.ui.RulersUI;

/**
 * Created by billzo on 12/22/2015.
 */
public class ChangeRulerPositionCommand extends RevertableCommand {

	private float previousPosition;
	private String direction;
	
	@Override
    public void doAction() {
		previousPosition = RulersUI.getPreviousGuide().pos;
		direction =  RulersUI.getPreviousGuide().isVertical ? "Y" : "X";
		
		Dialogs.showInputDialog(sandbox.getUIStage(), "Manual " + direction + " Position", direction + " : ", new MyInputValidator(), new InputDialogListener() {
            @Override
            public void finished(String input) {
            	RulersUI.updateGuideManually(Float.parseFloat(input));
            }

            @Override
            public void canceled() {

            }
        });
	}

	@Override
	public void undoAction() {
		System.out.println(previousPosition);
		RulersUI.updateGuideManually(previousPosition);
	}
	
	private class MyInputValidator implements InputValidator {
		@Override
		public boolean validateInput(String input) {
		    try
		    {
		    	Float.parseFloat(input);
		        return true;
		    } catch (NumberFormatException ex)
		    {
		        return false;
		    }
		}
		
	}
	
}
