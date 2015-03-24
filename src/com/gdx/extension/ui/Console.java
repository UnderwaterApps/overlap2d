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

import java.util.Arrays;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.StringBuilder;
import com.gdx.extension.ui.Console.Command.Parameter.Value;

/**
 * A console allow to execute command and display informations.<br />
 * Commands history is handled with arrow up and arrow down.<br />
 * Command execution is handled with enter.
 */
public class Console extends Table {

    private ConsoleStyle style;

    private ScrollPane consoleScroll;
    private Container<VerticalGroup> consoleContainer;
    private VerticalGroup body;
    private TextField textfield;

    private int maxEntries;
    private int currentHistory;

    private Array<String> commandsHistory;
    private ObjectMap<String, Command> commands;

    /**
     * Create a console with default style.
     * 
     * @param maxEntries the max lines to display in the console
     * @param skin the skin to use
     */
    public Console(int maxEntries, Skin skin) {
	this(maxEntries, skin, "default");
    }

    /**
     * Create a console with specified style.
     * 
     * @param maxEntries the max lines to display in the console
     * @param skin the skin to use
     * @param styleName the style name to use
     */
    public Console(int maxEntries, Skin skin, String styleName) {
	this(maxEntries, skin.get(styleName, ConsoleStyle.class));
    }

    /**
     * Create a console with specified style.
     * 
     * @param maxEntries the max lines to display in the console
     * @param style the style to use
     */
    public Console(int maxEntries, ConsoleStyle style) {
	this.maxEntries = maxEntries;
	this.style = style;

	commandsHistory = new Array<String>();
	commands = new ObjectMap<String, Command>();

	body = new VerticalGroup();
	body.left();

	consoleContainer = new Container<VerticalGroup>(body);
	consoleContainer.setBackground(style.consoleBackground);
	consoleContainer.left().top();
	consoleContainer.pad(10f);
	consoleScroll = new ScrollPane(consoleContainer);
	consoleScroll.setScrollingDisabled(true, false);
	consoleScroll.setFlickScroll(false);

	textfield = new TextField("", style.textfieldStyle);
	textfield.addCaptureListener(new InputListener() {

	    @Override
	    public boolean keyDown(InputEvent event, int keycode) {
		switch (keycode) {
		    case Keys.ENTER:
			String _inputs = textfield.getText();
			if (_inputs.length() > 0) {
			    textfield.setText("");
			    String[] _split = _inputs.split(" ");
			    if (_split.length == 0) {
				break;
			    }

			    if (commandsHistory.size == 0 || !(commandsHistory.peek().equals(_inputs))) {
				commandsHistory.add(_inputs);
			    }
			    currentHistory = commandsHistory.size;

			    Command _commandAction = commands.get(_split[0]);
			    if (_commandAction == null) {
				addEntry("Command " + _split[0] + " not found");
			    } else {
				String[] _args = null;
				if (_split.length > 1) {
				    _args = Arrays.copyOfRange(_split, 1, _split.length);
				}
				_commandAction.executeInternal(Console.this, _args);
			    }
			}

			break;

		    case Keys.UP:
			if (currentHistory > 0) {
			    currentHistory--;
			    String _historyCmd = commandsHistory.get(currentHistory);
			    textfield.setText(_historyCmd);
			    textfield.setCursorPosition(_historyCmd.length());
			}

			break;

		    case Keys.DOWN:
			if (currentHistory < commandsHistory.size - 1) {
			    currentHistory++;
			    String _historyCmd = commandsHistory.get(currentHistory);
			    textfield.setText(_historyCmd);
			    textfield.setCursorPosition(_historyCmd.length());
			}

			break;

		    default:
			return false;
		}
		return true;
	    }
	});

	add(consoleScroll).expand().fill().row();
	add(textfield).fillX();
    }

    /**
     * Register a command.
     * 
     * @param command the command to add
     */
    public void registerCommand(Command command) {
	commands.put(command.name, command);
    }

    /**
     * Unregister a command.
     * 
     * @param command the command name to remove
     */
    public void unregisterCommand(String command) {
	commands.remove(command);
    }

    /**
     * Unregister a command.
     * 
     * @param command the command to remove
     */
    public void unregisterCommand(Command command) {
	commands.remove(command.name);
    }

    /**
     * Add an entry in the console.
     * 
     * @param entry the entry to add
     */
    public synchronized void addEntry(String entry) {
	SnapshotArray<Actor> _children = body.getChildren();
	if (_children.size >= maxEntries) {
	    _children.first().remove();
	}

	Label _entryLabel = new Label(entry, style.entryStyle);
	_entryLabel.setAlignment(Align.left);
	_entryLabel.setWrap(true);
	body.addActor(_entryLabel);
    }

    /**
     * Set focus on the textfield.
     */
    public void setFocus() {
	getStage().setKeyboardFocus(textfield);
    }

    /**
     * @return the list of registered commands of this console
     */
    public ObjectMap<String, Command> getCommands() {
	return commands;
    }

    /**
     * @return the console scroll pane
     */
    public ScrollPane getConsoleScroll() {
	return consoleScroll;
    }

    /**
     * @return the style
     */
    public ConsoleStyle getStyle() {
	return style;
    }

    /**
     * Update the style of the console.
     * 
     * @param style the style to apply
     */
    public void setStyle(ConsoleStyle style) {
	consoleScroll.setStyle(style.scrollStyle);
	consoleContainer.setBackground(style.consoleBackground);
	SnapshotArray<Actor> _children = body.getChildren();
	for (int i = 0; i < _children.size; i++) {
	    ((Label) _children.get(i)).setStyle(style.entryStyle);
	}
	textfield.setStyle(style.textfieldStyle);
    }

    /**
     * An executable command managing its parameters.
     */
    static public abstract class Command {

	private String name;
	private Array<Parameter> parameters = new Array<Parameter>(4);
	private Array<Value> args = new Array<Value>(4);
	private int forcedParameters;

	/**
	 * Create a new command to add to a console.
	 * 
	 * @param name the name of the command used in the console (name parameters)
	 */
	public Command(String name) {
	    this.name = name;
	}

	/**
	 * Process the command before allowing the user to execute it.
	 * 
	 * @param console the console the command come from
	 * @param args the arguments of the command, null if any
	 */
	public void executeInternal(Console console, String[] args) {
	    this.args.clear();
	    if ((args == null && forcedParameters > 0)
		    || (args != null && args.length < forcedParameters)) {
		console.addEntry("Need more arguments, the command should match : ");
		console.addEntry(toString());
		return;
	    } else if (args != null && args.length > parameters.size) {
		console.addEntry("Too much arguments, the command should match : ");
		console.addEntry(toString());
		return;
	    }

	    if (args != null) {
		for (int i = 0; i < args.length; i++) {
		    Parameter _registeredParameter = parameters.get(i);
		    Class<?> _type = _registeredParameter.type;
		    Object _value = null;
		    if (_type == Integer.class) {
			try {
			    _value = Integer.valueOf(args[i]);
			} catch (NumberFormatException e) {
			    console.addEntry("Wrong type for parameter " + _registeredParameter + " should be an integer between " + Integer.MIN_VALUE + " and " + Integer.MAX_VALUE);
			    return;
			}
		    } else if (_type == Float.class) {
			try {
			    _value = Float.valueOf(args[i]);
			} catch (NumberFormatException e) {
			    console.addEntry("Wrong type for parameter " + _registeredParameter + " should be a float between " + Float.MIN_VALUE + " and " + Float.MAX_VALUE);
			    return;
			}
		    } else if (_type == Boolean.class) {
			String _arg = args[i];
			if (_arg.equalsIgnoreCase("false") || _arg.equals("0")) {
			    _value = false;
			} else if (_arg.equalsIgnoreCase("true") || _arg.equals("1")) {
			    _value = true;
			} else {
			    console.addEntry("Wrong type for parameter " + _registeredParameter + " should be a boolean (false, true, 0, 1)");
			    return;
			}
		    } else if (_type == String.class) {
			_value = args[i];
		    } else {
			console.addEntry("Unsuported type for parameter : " + _registeredParameter);
			return;
		    }
		    this.args.add(new Value(_registeredParameter, _value));
		}
	    }
	    execute(console, this.args);
	}

	/**
	 * Called when the command is executed.
	 * 
	 * @param console the console the command come from
	 * @param args the args of the command or null if any
	 */
	public abstract void execute(Console console, Array<Value> args);

	/**
	 * Add a parameter to this command.<br />
	 * The parameters should be added in the order you want to type them in the console.<br />
	 * The optional parameters must be added last.
	 * 
	 * @param param the param to add
	 */
	public void addParameter(Parameter param) {
	    parameters.add(param);
	    if (!param.isOptional) {
		forcedParameters++;
	    }
	}

	@Override
	public String toString() {
	    StringBuilder _sBuild = new StringBuilder();
	    _sBuild.append(name).append(' ');
	    for (int i = 0; i < parameters.size; i++) {
		Parameter _param = parameters.get(i);
		boolean _isOptional = _param.isOptional;
		if (_isOptional) {
		    _sBuild.append('[');
		}
		_sBuild.append(_param.name).append('(').append(_param.type.getSimpleName()).append(')');
		if (_isOptional) {
		    _sBuild.append(']');
		}

		if (i + 1 < parameters.size) {
		    _sBuild.append(' ');
		}
	    }
	    return _sBuild.toString();
	}

	/**
	 * Define command parameters.
	 */
	static public class Parameter {

	    private final String name;
	    private final Class<?> type;
	    private final boolean isOptional;

	    /**
	     * Create a new parameter.
	     * 
	     * @param name the name of the parameter (used for command help)
	     * @param type the type of the parameter (Integer, Boolean, String)
	     * @param isOptional true if the parameter is optional, false otherwise
	     */
	    public Parameter(String name, Class<?> type, boolean isOptional) {
		this.name = name;
		this.type = type;
		this.isOptional = isOptional;
	    }

	    /**
	     * @return the parameter name
	     */
	    public String getName() {
		return name;
	    }

	    /**
	     * @return the parameter type (Integer, Boolean, String)
	     */
	    public Class<?> getType() {
		return type;
	    }

	    /**
	     * @return true if the parameter is optional, false otherwise
	     */
	    public boolean isOptional() {
		return isOptional;
	    }

	    @Override
	    public String toString() {
		return name + " (" + type.getSimpleName() + ")";
	    }

	    /**
	     * Define the value of a parameter.
	     */
	    static public class Value {

		private Parameter parameter;
		private Object value;

		/**
		 * Bind a value to a parameter.
		 * 
		 * @param param the parameter to bind to
		 * @param value the value to bind (should be of the type of the parameter)
		 */
		public Value(Parameter param, Object value) {
		    this.parameter = param;
		    this.value = value;
		}

		/**
		 * @return the parameter
		 */
		public Parameter getParameter() {
		    return parameter;
		}

		/**
		 * @return the value
		 */
		public Object getValue() {
		    return value;
		}
	    }

	}

    }

    static public class ConsoleStyle {

	/** Optional */
	public Drawable consoleBackground;
	/** Optional */
	public ScrollPaneStyle scrollStyle;

	public LabelStyle entryStyle;
	public TextFieldStyle textfieldStyle;

	public ConsoleStyle() {
	}

	public ConsoleStyle(Drawable consoleBackground, ScrollPaneStyle scrollStyle, LabelStyle entryStyle, TextFieldStyle textfieldStyle) {
	    this.consoleBackground = consoleBackground;
	    this.scrollStyle = scrollStyle;
	    this.entryStyle = entryStyle;
	    this.textfieldStyle = textfieldStyle;
	}

	public ConsoleStyle(ConsoleStyle style) {
	    consoleBackground = style.consoleBackground;
	    scrollStyle = style.scrollStyle;
	    entryStyle = style.entryStyle;
	    textfieldStyle = style.textfieldStyle;
	}
    }

}
