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
package com.gdx.extension.ui.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;

/**
 * Catch mouse buttons and keyboard keys.</br >
 * Allow keys and buttons combinations.
 */
public class InputCatcher extends Container<Label> {

    private InputCatcherStyle style;
    private Label inputsLabel;
    private Label catchingLabel;

    private boolean isCatching;
    private InputArray currentInputs;
    private InputArray capturedInputs;
    private InputProcessor oldInputProcessor;
    private InputAdapter mouseListener;

    /**
     * Create an input catcher with default style.
     * 
     * @param defaultInputs default inputs or null if any
     * @param catchingMessage the message displayed in catching mode
     * @param skin the skin to use
     */
    public InputCatcher(InputArray defaultInputs, String catchingMessage, Skin skin) {
	this(defaultInputs, catchingMessage, skin, "default");
    }

    /**
     * Create an input catcher with defined style.
     * 
     * @param defaultInputs default inputs or null if any
     * @param catchingMessage the message displayed in catching mode
     * @param skin the skin to use
     * @param styleName the style to use
     */
    public InputCatcher(InputArray defaultInputs, String catchingMessage, Skin skin, String styleName) {
	this(defaultInputs, catchingMessage, skin.get(styleName, InputCatcherStyle.class));
    }

    /**
     * Create an input catcher with defined style.
     * 
     * @param defaultInputs default inputs or null if any
     * @param catchingMessage the message displayed in catching mode
     * @param style the style to use
     */
    public InputCatcher(InputArray defaultInputs, String catchingMessage, InputCatcherStyle style) {
	setTouchable(Touchable.enabled);

	capturedInputs = new InputArray();
	mouseListener = new InputAdapter();

	currentInputs = new InputArray();
	if (defaultInputs != null) {
	    currentInputs.addAll(defaultInputs);
	}

	inputsLabel = new Label((defaultInputs != null) ? defaultInputs.toString() : "", style.desactivatedLabelStyle);
	catchingLabel = new Label(catchingMessage, style.activatedLabelStyle);

	setStyle(style);
	if (style.desactivatedBackground != null) {
	    setBackground(style.desactivatedBackground);
	}
	setActor(inputsLabel);

	mouseListener = new InputAdapter() {

	    @Override
	    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (isCatching) {
		    capturedInputs.add(new InputHolder(InputType.Mouse, button));
		    return true;
		}
		return false;
	    }

	    @Override
	    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (isCatching && capturedInputs.size > 0) {
		    onInternalStopCatching();
		    return true;
		}
		return false;
	    }

	    @Override
	    public boolean keyDown(int keycode) {
		if (isCatching) {
		    if (keycode == Keys.ESCAPE) {
			onInternalStopCatching();
		    } else {
			capturedInputs.add(new InputHolder(InputType.Keyboard, keycode));
		    }
		    return true;
		}
		return false;
	    }

	    @Override
	    public boolean keyUp(int keycode) {
		if (isCatching) {
		    onInternalStopCatching();
		    return true;
		}
		return false;
	    }

	    @Override
	    public boolean scrolled(int amount) {
		if (isCatching) {
		    int _scrollInput = 0;
		    if (amount < 0) {
			_scrollInput = Buttons.WHEEL_UP;
		    } else {
			_scrollInput = Buttons.WHEEL_DOWN;
		    }
		    capturedInputs.add(new InputHolder(InputType.Mouse, _scrollInput));
		    onInternalStopCatching();
		    return true;
		}
		return false;
	    }
	};
	addListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		if (!isCatching && button == Buttons.LEFT) {
		    onInternalStartCatching();
		}
		return true;
	    }

	    @Override
	    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		if (isCatching)
		    return;

		Drawable _overBackground = InputCatcher.this.style.overBackground;
		if (_overBackground != null) {
		    setBackground(_overBackground);
		}
	    }

	    @Override
	    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
		if (isCatching)
		    return;

		Drawable _desactivatedBackground = InputCatcher.this.style.desactivatedBackground;
		if (_desactivatedBackground != null) {
		    setBackground(_desactivatedBackground);
		}
	    }

	});
    }

    private void onInternalStartCatching() {
	isCatching = true;
	if (style.activatedBackground != null) {
	    setBackground(style.activatedBackground);
	}
	setActor(catchingLabel);
	Gdx.input.setCursorCatched(true);
	oldInputProcessor = Gdx.input.getInputProcessor();
	Gdx.input.setInputProcessor(mouseListener);

	onStartCatching();
    }

    private void onInternalStopCatching() {
	isCatching = false;
	if (style.desactivatedBackground != null) {
	    setBackground(style.desactivatedBackground);
	}

	if (capturedInputs.size > 0) {
	    currentInputs.clear();
	    currentInputs.addAll(capturedInputs);
	    inputsLabel.setText(capturedInputs.toString());

	    ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
	    fire(changeEvent);
	    Pools.free(changeEvent);
	}

	setActor(inputsLabel);
	Gdx.input.setCursorCatched(false);
	Gdx.input.setInputProcessor(oldInputProcessor);

	capturedInputs.clear();

	onStopCatching();
    }

    /**
     * Override it if you want to know when the input catcher enter in cacthing mode.
     */
    public void onStartCatching() {
    }

    /**
     * Override it if you want to know when the input catcher exit the catching mode.
     */
    public void onStopCatching() {
    }

    /**
     * Get the inputs of this catcher.
     * 
     * @return the inputs
     */
    public InputArray getInputs() {
	return currentInputs;
    }

    /**
     * Set the inputs of this catcher.<br />
     * Useful when you want to initialize default inputs elsewhere than the constructor.
     * 
     * @param inputs inputs to set
     */
    public void setInputs(InputArray inputs) {
	currentInputs = inputs;
	inputsLabel.setText(inputs.toString());
    }

    @Override
    public float getPrefWidth() {
	return 150f;
    }

    @Override
    public float getPrefHeight() {
	if (style.desactivatedLabelStyle != null && style.desactivatedBackground != null) {
	    return Math.max(style.desactivatedLabelStyle.font.getCapHeight() +
		    style.desactivatedBackground.getBottomHeight() +
		    style.desactivatedBackground.getTopHeight(),
		    style.desactivatedBackground.getMinHeight());
	}

	return 25f;
    }

    /**
     * @return the style of this actor
     */
    public InputCatcherStyle getStyle() {
	return style;
    }

    /**
     * Set the style of this actor.
     * 
     * @param style style to use
     */
    public void setStyle(InputCatcherStyle style) {
	this.style = style;
    }

    static public enum InputType {
	Mouse, Keyboard;
    }

    /**
     * Define mouse buttons.
     */
    static public class Buttons extends com.badlogic.gdx.Input.Buttons {

	public static final int WHEEL_UP = 5;
	public static final int WHEEL_DOWN = 6;

	/**
	 * Return the literal of a button.
	 * 
	 * @param button the buttonCode
	 * @return the button literal
	 */
	public static String toString(int buttonCode) {
	    if (buttonCode < 0) {
		throw new IllegalArgumentException("buttoncode cannot be negative, buttoncode : " + buttonCode);
	    }
	    if (buttonCode > 6) {
		throw new IllegalArgumentException("buttonCode cannot be greater than 6, buttoncode : " + buttonCode);
	    }

	    switch (buttonCode) {
		case LEFT:
		    return "Left Mouse Button";
		case RIGHT:
		    return "Right Mouse Button";
		case MIDDLE:
		    return "Middle Mouse Button";
		case BACK:
		    return "Back Mouse Button";
		case FORWARD:
		    return "Forward Mouse Button";
		case WHEEL_UP:
		    return "Wheel Up";
		case WHEEL_DOWN:
		    return "Wheel Down";
		default:
		    return null;
	    }
	}

	private static ObjectIntMap<String> buttonNames;

	/**
	 * @param buttonName the button name returned by the {@link Buttons#toString(int)} method
	 * @return the int buttonCode or -1 if not found
	 */
	public static int valueOf(String buttonName) {
	    if (buttonNames == null)
		initializeButtonNames();
	    return buttonNames.get(buttonName, -1);
	}

	/**
	 * lazily intialized in {@link Buttons#valueOf(String)}
	 */
	private static void initializeButtonNames() {
	    buttonNames = new ObjectIntMap<String>();
	    for (int i = 0; i < 7; i++) {
		String name = toString(i);
		if (name != null)
		    buttonNames.put(name, i);
	    }
	}

    }

    /**
     * An array of {@link InputHolder inputs}.
     */
    static public class InputArray extends Array<InputHolder> implements Poolable {

	/**
	 * Create an empty array of inputs.
	 */
	public InputArray() {
	}

	/**
	 * Create an array of inputs with one input.
	 * 
	 * @param input the input to add
	 */
	public InputArray(InputHolder input) {
	    add(input);
	}

	/**
	 * Create an array with several inputs.
	 * 
	 * @param inputs the inputs to add
	 */
	public InputArray(InputHolder... inputs) {
	    addAll(inputs);
	}

	/**
	 * Create an array of inputs and fill it from inputs literals
	 * 
	 * @param literals the inputs literals to parse and add
	 */
	public InputArray(String literals) {
	    parse(literals);
	}

	/**
	 * Get the literal of this input array.
	 * 
	 * @return the literal or null if the array is empty
	 */
	@Override
	public String toString() {
	    if (size <= 0) {
		return null;
	    }

	    StringBuilder _sBuild = new StringBuilder();
	    for (int i = 0; i < size; i++) {
		InputHolder _input = get(i);
		if (_input.type == InputType.Mouse) {
		    _sBuild.append(Buttons.toString(_input.input));
		} else if (_input.type == InputType.Keyboard) {
		    _sBuild.append(Keys.toString(_input.input));
		}
		if (i + 1 < size) {
		    _sBuild.append(" + ");
		}
	    }

	    return _sBuild.toString();
	}

	@Override
	public int hashCode() {
	    int _hash = 0;
	    for (int i = size; i-- > 0;) {
		_hash += items[i].hashCode();
	    }

	    return _hash;
	}

	/**
	 * Convert literals into inputs.
	 * 
	 * @param inputString the literal to parse
	 * @throws NullPointerException if unable to parse
	 */
	public void parse(String inputString) {
	    String[] _splitInputs = inputString.split(" \\+ "); // Problem here
	    for (int i = 0; i < _splitInputs.length; i++) {
		String _inputString = _splitInputs[i];
		InputHolder _input = InputHolder.fromString(_inputString);
		if (_input == null) {
		    throw new NullPointerException("Unable to parse the inputs string.");
		}
		add(_input);
	    }
	}

	@Override
	public void reset() {
	    clear();
	}
    }

    /**
     * Store one input and his type.
     */
    static public class InputHolder implements Poolable {

	private InputType type;
	private int input = -1;

	public InputHolder() {
	}

	/**
	 * Create an {@link InputHolder} from a string given by {@link InputHolder#toString()}.
	 * 
	 * @param inputString the string to parse
	 * @deprecated <br />
	 * Use {@link #fromString(String)} instead
	 */
	@Deprecated
	public InputHolder(String inputString) {
	    if (inputString == null || inputString.isEmpty()) {
		return;
	    }

	    input = Buttons.valueOf(inputString);
	    if (input != -1) {
		type = InputType.Mouse;
		return;
	    }

	    input = Keys.valueOf(inputString);
	    if (input != -1) {
		type = InputType.Keyboard;
		return;
	    }

	    throw new IllegalStateException("Unable to parse the input");
	}

	/**
	 * Create an {@link InputHolder} from the specified input.
	 * 
	 * @param type the type of the input
	 * @param input the input
	 */
	public InputHolder(InputType type, int input) {
	    this.type = type;
	    this.input = input;
	}

	/**
	 * Get the input type.
	 * 
	 * @return the type
	 */
	public InputType getType() {
	    return type;
	}

	/**
	 * Set the type of the input.
	 * 
	 * @param type the type of the input
	 */
	public void setType(InputType type) {
	    this.type = type;
	}

	/**
	 * Get the input.
	 * 
	 * @return the input
	 */
	public int getInput() {
	    return input;
	}

	/**
	 * Set the input.
	 * 
	 * @param input the input
	 */
	public void setInput(int input) {
	    this.input = input;
	}

	@Override
	public void reset() {
	    type = null;
	    input = -1;
	}

	@Override
	public int hashCode() {
	    final int _prime = 31;
	    int _result = 1;
	    _result = _prime * _result + type.ordinal();
	    _result = _prime * _result + input;

	    return _result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
		return true;
	    if (obj == null)
		return false;
	    if (getClass() != obj.getClass())
		return false;

	    InputHolder _other = (InputHolder) obj;
	    if (type != _other.type)
		return false;
	    if (input != _other.input)
		return false;

	    return true;
	}

	/**
	 * Get the input literal. Can be resolved by new InputHolder(String}.
	 * 
	 * @return the input literal
	 */
	@Override
	public String toString() {
	    if (type == InputType.Mouse) {
		return Buttons.toString(input);
	    } else if (type == InputType.Keyboard) {
		return Keys.toString(input);
	    }
	    return "";
	}

	/**
	 * Create an {@link InputHolder} from the specified input.
	 * 
	 * @param type the type of the input
	 * @param input the input
	 * 
	 * @return the input holder
	 */
	public static InputHolder fromString(String inputLiteral) {
	    if (inputLiteral == null || inputLiteral.isEmpty()) {
		return null;
	    }

	    int _input;
	    InputType _type = null;
	    _input = Buttons.valueOf(inputLiteral);
	    if (_input != -1) {
		_type = InputType.Mouse;
	    } else {
		_input = Keys.valueOf(inputLiteral);
		if (_input != -1) {
		    _type = InputType.Keyboard;
		} else {
		    return null;
		}
	    }

	    return new InputHolder(_type, _input);
	}
    }

    /**
     * Listener that notify when a control is changed in an {@link InputCatcher}.
     */
    static public interface InputCatcherListener {

	/**
	 * Notify what control was changed with the new inputs.
	 * 
	 * @param tag the tag of the control
	 * @param name the name of the control
	 * @param inputs the new inputs
	 */
	public void onControlChanged(String tag, String name, InputArray inputs);
    }

    static public class InputCatcherStyle {

	public Drawable desactivatedBackground;
	public Drawable activatedBackground;
	public Drawable overBackground;
	public LabelStyle desactivatedLabelStyle;
	public LabelStyle activatedLabelStyle;

	public InputCatcherStyle() {
	}

	public InputCatcherStyle(InputCatcherStyle style) {
	    this(style.desactivatedBackground, style.activatedBackground, style.overBackground, style.desactivatedLabelStyle, style.activatedLabelStyle);
	}

	public InputCatcherStyle(Drawable desactivatedBackground, Drawable activatedBackground, Drawable overBackground, LabelStyle desactivatedLabelStyle, LabelStyle activatedLabelStyle) {
	    this.desactivatedBackground = desactivatedBackground;
	    this.activatedBackground = activatedBackground;
	    this.overBackground = overBackground;
	    this.desactivatedLabelStyle = desactivatedLabelStyle;
	    this.activatedLabelStyle = activatedLabelStyle;
	}

    }

}
