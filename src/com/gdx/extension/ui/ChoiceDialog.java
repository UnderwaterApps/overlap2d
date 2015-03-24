/** Copyright 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gdx.extension.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

/**
 * Simple modal dialog with accept and refuse buttons
 * 
 * @author Kyu
 */
public abstract class ChoiceDialog extends Dialog
{

    private ChoiceDialogStyle style;
    
    private TextButton refuseButton;
    private TextButton acceptButton;
    
    /**
     * Create a {@link ChoiceDialog} with default style
     * 
     * @param title the title of the dialog
     * @param message the message of the dialog
     * @param refuse the refuse button label
     * @param accept the accept button label
     * @param skin the skin to use
     */
    public ChoiceDialog(String title, String message, String refuse, String accept, Skin skin)
    {
	this(title, message, refuse, accept, skin, "default");
    }
    
    /**
     * Create a {@link ChoiceDialog} with specified style
     * 
     * @param title the title of the dialog
     * @param message the message of the dialog
     * @param refuse the refuse button label
     * @param accept the accept button label
     * @param skin the skin to use
     * @param styleName the name of the style to use
     */
    public ChoiceDialog(String title, String message, String refuse, String accept, Skin skin, String styleName)
    {
	super(title, skin.get(styleName, WindowStyle.class));
	
	this.setTitleAlignment(Align.center);
	padTop(Value.percentHeight(0.2f));
	
	this.style = skin.get(styleName, ChoiceDialogStyle.class);
	
	refuseButton = new TextButton(refuse, skin);
	refuseButton.setStyle(style.refuseButtonStyle);
	refuseButton.align(Align.center);
	setObject(refuseButton, new ChoiceDialogObject(false));

	acceptButton = new TextButton(accept, skin);
	acceptButton.setStyle(style.acceptButtonStyle);
	acceptButton.align(Align.center);
	setObject(acceptButton, new ChoiceDialogObject(true));
	
	Label _message = new Label(message, skin);
	_message.setStyle(style.messageStyle);
	
	getContentTable().add(_message).minWidth(300f).pad(10f);
	getButtonTable().defaults().minWidth(100f).pad(10f);
	getButtonTable().add(refuseButton);
	getButtonTable().add(acceptButton);
	setMovable(false);
	center();
    }
    
    /**
     * Get direct access to the refuse {@link TextButton button}
     * 
     * @return the refuse {@link TextButton button}
     */
    public TextButton getRefuseButton()
    {
	return refuseButton;
    }
    
    /**
     * Get direct access to the accept button
     * 
     * @return the accept {@link TextButton button}
     */
    public TextButton getAcceptButton()
    {
	return acceptButton;
    }
    
    /**
     * Fired when the refuse button is touched up
     */
    public abstract void onRefuse();
    
    /**
     * Fired when the accept button is touched up
     */
    public abstract void onAccept();
    
    /**
     * Show the {@link ChoiceDialog dialog} on the specified {@link Stage stage}
     * 
     * @param stage the stage where the {@link ChoiceDialog dialog} will be displayed
     * @return this {@link ChoiceDialog dialog}
     */
    @Override
    public ChoiceDialog show(Stage stage)
    {
	return (ChoiceDialog) super.show(stage);
    }
    
    /**
     * Call the correct event when a button is clicked
     */
    @Override
    protected void result(Object object)
    {
	if(object instanceof ChoiceDialogObject)
	{
	    if(((ChoiceDialogObject) object).isAccepted)
		onAccept();
	    else
		onRefuse();
	}
    }
    
    /**
     *	Used by {@link ChoiceDialog} to define choice when a button is clicked
     *
     * @author Kyu
     */
    public class ChoiceDialogObject {
	
	private boolean isAccepted;
	
	public ChoiceDialogObject(boolean isAccepted)
	{
	    this.isAccepted = isAccepted;
	}
	
    }
    
    /**
     *	Define the style of a {@link ChoiceDialog}
     *
     * @author Kyu
     */
    public static class ChoiceDialogStyle {
	
	public TextButtonStyle refuseButtonStyle, acceptButtonStyle;
	public LabelStyle messageStyle;
	
	public ChoiceDialogStyle()
	{
	}
	
    }

}
