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

package com.uwsoft.editor.gdx.ui.dialogs;

import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.building.OneRowTableBuilder;
import com.kotcrab.vis.ui.building.StandardTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.CellWidget;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.building.utilities.layouts.ActorLayout;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class CustomVariablesDialog extends VisWindow {
	
	private IBaseItem item;
	private CustomVariables vars;
	private VisList<String> varNames;
	
	private VisTextField varName;
	private VisTextField varVal;
	
	private VisTextButton changeName;
	private VisTextButton updateValue;
	private VisTextButton deleteVar;
	
	private VisTextField keyName;
	private VisTextField keyVal;
	private VisTextButton addVar;
	
	public CustomVariablesDialog(final IBaseItem item) {
		super("Custom Variables Dialog");
		
		setModal(true);
		addCloseButton();
		closeOnEscape();
		
		TableUtils.setSpacingDefaults(this);
		columnDefaults(0).left();
		this.item = item;
		vars = item.getCustomVariables();
		
		buildLayout();
		
		setupHooks();
		
		pack();
		centerWindow();
	}
	
	private void buildLayout() {
		// Left Side list of Variables
		varNames = new VisList<String>();
		renderNames();
		VisScrollPane pane = new VisScrollPane(varNames);
		pane.setFlickScroll(false);
		pane.setFadeScrollBars(false);
		varNames.setSelectedIndex(-1);
		
		// Used Labels in layout.
		VisLabel lbl1 = new VisLabel("Variable Name:");
		VisLabel lbl2 = new VisLabel("Variable Value:");
		VisLabel lbl3 = new VisLabel("New Variable:");
		VisLabel lbl4 = new VisLabel("=");
		
		// Text Fields in layout.
		varName = new VisTextField("");
		varName.setDisabled(true);
		varVal = new VisTextField("");
		keyName = new VisTextField("");
		keyVal = new VisTextField("");
		
		// Buttons in layout.
		changeName = new VisTextButton("Change Name");
		updateValue = new VisTextButton("Update Value");
		deleteVar = new VisTextButton("Delete Variable");
		addVar = new VisTextButton("Add Variable");
		
		// Layout everything
		VisTable form = new VisTable(true);
		form.add(lbl1);
		form.add(varName).expandX().fillX();
		form.add(changeName);
		form.row();
		
		form.add(lbl2);
		form.add(varVal).expandX().fillX();
		form.row();
		
		form.add(updateValue);
		form.add(deleteVar);
		
		VisTable newKVE = new VisTable(true);
		newKVE.add(lbl3);
		newKVE.add(keyName);
		newKVE.add(lbl4);
		newKVE.add(keyVal);
		newKVE.add(addVar);
		
		add(pane).expandX().expandY().fillX().fillY();
		add(new Separator()).padTop(10).fillY().expandY();
		add(form).row();
		
		add(newKVE).colspan(3).expandX().fillX().padBottom(3).padTop(3).row();
	}
	
	private void setupHooks() {
		varNames.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (varNames.getSelectedIndex() != -1) {
					String val = vars.getStringVariable(varNames.getSelected());
					varName.setText(varNames.getSelected());
					varVal.setText(val);
				} else {
					varName.setText("");
					varVal.setText("");
				}
			}
		});
		
		changeName.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				DialogUtils.showInputDialog(getStage(), "New Name for Variable", "Name:", new InputDialogAdapter() {
					@Override
					public void finished(String input) {
						// Change the Variable name here.
						vars.removeVariable(varName.getText());
						vars.setVariable(input, varVal.getText());
						item.updateDataVO();
						renderNames();
						varNames.setSelected(input);
						varName.setText(input);
					}
				});
			}
		});
		
		updateValue.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				vars.setVariable(varName.getText(),varVal.getText());
				item.updateDataVO();
			}
		});
		
		deleteVar.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				vars.removeVariable(varName.getText());
				item.updateDataVO();
				varName.setText("");
				varVal.setText("");
				renderNames();
				varNames.setSelectedIndex(-1);
			}
		});
		
		addVar.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				vars.setVariable(keyName.getText(), keyVal.getText());
				item.updateDataVO();
				keyName.setText("");
				keyVal.setText("");
				renderNames();
				varNames.setSelectedIndex(-1);
			}
		});
	}
	
	private void renderNames() {
		varNames.clearItems();
		Array<String> names = new Array<String>();
		
		for (Map.Entry<String, String> entry : vars.getHashMap().entrySet()) {
			final String key = entry.getKey();
			names.add(key);
		}
		varNames.setItems(names);
	}
}
