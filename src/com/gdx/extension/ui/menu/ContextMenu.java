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
package com.gdx.extension.ui.menu;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;


/**
 * A context menu containing {@link MenuItem menu items}.
 * 
 * @author Kyu
 *
 */
public class ContextMenu extends VerticalGroup
{
    
    /**
     * Store all open {@link ContextMenu context menus} of this {@link ContextMenu context menu}.(
     */
    private ContextMenu lastOpened;
    
    /**
     * Create a {@link ContextMenu context menu}
     */
    public ContextMenu()
    {
	left();
    }
    
    /**
     * Add a menu.
     * 
     * @param menu the menu to add
     */
    public void addMenu(MenuItem menu)
    {
	setVisible(false);
	
	super.addActor(menu);
    }
    
    /** 
     * Close recursively {@link ContextMenu context menus} tree.
     */
    public void closeMenu()
    {
	if(lastOpened == null)
	    return;
	
	lastOpened.closeMenu();
	lastOpened.setVisible(false);
	lastOpened = null;
    }
    
    /**
     * Used internally.
     */
    public void setLastOpened(ContextMenu menu)
    {
	lastOpened = menu;
    }

}
