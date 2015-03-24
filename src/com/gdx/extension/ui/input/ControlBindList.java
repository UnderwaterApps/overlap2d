package com.gdx.extension.ui.input;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.gdx.extension.ui.input.InputCatcher.InputArray;
import com.gdx.extension.ui.input.InputCatcher.InputCatcherListener;
import com.gdx.extension.ui.input.InputCatcher.InputCatcherStyle;

/**
 * Create a control list allowing to catch inputs for each entries.
 */
public class ControlBindList extends Table {

    private ControlBindListStyle style;

    private String catchingLabel;
    private ObjectMap<String, InputCatcher> controls;
    private Array<InputCatcherListener> listeners;

    /**
     * Create a new {@link ControlBindList} with default style.
     * 
     * @param catchingLabel the label to display in the {@link InputCatcher} when catching inputs
     * @param skin the skin to use
     */
    public ControlBindList(String catchingLabel, Skin skin) {
	this(catchingLabel, skin, "default");
    }

    /**
     * Create a new {@link ControlBindList} with defined style.
     * 
     * @param skin the skin to use
     * @param styleName the style to use
     */
    public ControlBindList(String catchingLabel, Skin skin, String styleName) {
	this(catchingLabel, skin.get(styleName, ControlBindListStyle.class));
    }

    /**
     * Create a new {@link ControlBindList} with defined style.
     * 
     * @param style the style to use
     */
    public ControlBindList(String catchingLabel, ControlBindListStyle style) {
	this.catchingLabel = catchingLabel;

	controls = new ObjectMap<String, InputCatcher>();
	listeners = new Array<InputCatcher.InputCatcherListener>();
	setStyle(style);
    }

    /**
     * Add an {@link InputCatcher input catcher} entry to the list.
     * 
     * @param tag the tag corresponding to this entry (eg. the key of the entry in a {@link Preferences preference} file)
     * @param name the name to display before the {@link InputCatcher input catcher}
     * @param inputs the default inputs (eg. load it from preferences)
     */
    public void addInputCatchRow(final String tag, final String name, final InputArray inputs) {
	add(new Label(name, style.nameLabelStyle)).expandX().fill();
	final InputCatcher _inputCatcher = new InputCatcher(inputs, catchingLabel, style.inputCatcherStyle);
	_inputCatcher.addListener(new ChangeListener() {

	    @Override
	    public void changed(ChangeEvent event, Actor actor) {
		InputArray _inputs = _inputCatcher.getInputs();
		for (int i = 0; i < listeners.size; i++) {
		    InputCatcherListener _listener = listeners.get(i);
		    _listener.onControlChanged(tag, name, _inputs);
		}
	    }
	});
	add(_inputCatcher).expandX().fill().row();
	controls.put(tag, _inputCatcher);
    }

    /**
     * Get the input catcher for the specified control tag.
     * 
     * @param tag the control tag
     * @return the input catcher for the specified tag or null if no one found
     */
    public InputCatcher getInputCatcher(String tag) {
	return controls.get(tag);
    }

    /**
     * Set the inputs of the specified control.
     * 
     * @param tag the control tag to set
     * @param inputs the inputs to set
     */
    public void setControlInputs(String tag, InputArray inputs) {
	InputCatcher _inputCatcher = controls.get(tag);
	_inputCatcher.setInputs(inputs);
    }

    /**
     * Get the tag corresponding to the inputs.
     * 
     * @param inputs the inputs to check
     * @return the tag or null if not found or given inputs are null or empty
     */
    public String getTag(InputArray inputs) {
	if (inputs == null || inputs.size == 0) {
	    return null;
	}

	Entries<String, InputCatcher> _inputCatchers = controls.entries();
	while (_inputCatchers.hasNext) {
	    Entry<String, InputCatcher> _entry = _inputCatchers.next();
	    if (_entry.value.getInputs().equals(inputs)) {
		return _entry.key;
	    }
	}

	return null;
    }

    /**
     * Add a {@link InputCatcherListener control listener}.
     * 
     * @param listener the listener to add
     */
    public void addControlListener(InputCatcherListener listener) {
	listeners.add(listener);
    }

    /**
     * Remove a {@link InputCatcherListener control listener}.
     * 
     * @param listener the listener to remove
     * @return true if value was found and removed, false otherwise
     */
    public boolean removeControlListener(InputCatcherListener listener) {
	return listeners.removeValue(listener, true);
    }

    /**
     * Remove all {@link InputCatcherListener control listeners}.
     */
    public void clearListeners() {
	listeners.clear();
    }

    /**
     * @return all controls of this list
     */
    public ObjectMap<String, InputCatcher> getControls() {
	return controls;
    }

    /**
     * @return the style
     */
    public ControlBindListStyle getStyle() {
	return style;
    }

    /**
     * Set the style.
     * 
     * @param style the style to use
     */
    public void setStyle(ControlBindListStyle style) {
	this.style = style;
    }

    static public class ControlBindListStyle {

	public Drawable cellBackground;
	public LabelStyle nameLabelStyle;
	public InputCatcherStyle inputCatcherStyle;

	public ControlBindListStyle() {
	}

	public ControlBindListStyle(Drawable cellBackground, LabelStyle nameLabelStyle, InputCatcherStyle inputCatcherStyle) {
	    this.cellBackground = cellBackground;
	    this.nameLabelStyle = nameLabelStyle;
	    this.inputCatcherStyle = inputCatcherStyle;
	}

	public ControlBindListStyle(ControlBindListStyle style) {
	    this.cellBackground = style.cellBackground;
	    this.nameLabelStyle = style.nameLabelStyle;
	    this.inputCatcherStyle = style.inputCatcherStyle;
	}
    }

}
