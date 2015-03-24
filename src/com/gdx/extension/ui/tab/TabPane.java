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
package com.gdx.extension.ui.tab;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.gdx.extension.exception.OptimizationException;

/**
 * 
 * @author Kyu
 * 
 */
public class TabPane extends Table {

    private WidgetGroup tabGroup;
    private Container<TabContainer> container;

    private ButtonGroup buttonGroup;

    private Array<Tab> tabs;

    private Tab currentTab;

    private TabPaneStyle style;

    /**
     * Create a {@link TabPane} with {@link Tab tabs} at top and using default style.
     * 
     * @param skin the skin to use for style
     */
    public TabPane(Skin skin) {
	this(TabPosition.TOP, skin);
    }

    /**
     * Create a {@link TabPane} with default style.
     * 
     * @param tabPos the position of the tabs
     * @param skin the skin to use for style
     */
    public TabPane(TabPosition tabPos, Skin skin) {
	this(tabPos, skin, "default");
    }

    /**
     * Create a {@link TabPane} with the specified style.
     * 
     * @param isHorizontal set tabs
     * @param skin the skin to use for style
     * @param styleName the name of the style to use
     */
    public TabPane(TabPosition tabPos, Skin skin, String styleName) {
	super(skin);

	tabs = new Array<Tab>();

	setStyle(skin.get(styleName, TabPaneStyle.class));

	buttonGroup = new ButtonGroup();
	if (tabPos == TabPosition.TOP || tabPos == TabPosition.BOTTOM) {
	    tabGroup = new HorizontalGroup();
	} else {
	    tabGroup = new VerticalGroup().fill();
	}

	container = new Container<TabContainer>();
	container.fill();
	this.left().top();

	if (tabPos == TabPosition.TOP) {
	    addTabs(tabPos);
	    this.row();
	    addBody();
	} else if (tabPos == TabPosition.BOTTOM) {
	    addBody();
	    this.row();
	    addTabs(tabPos);
	} else if (tabPos == TabPosition.LEFT) {
	    addTabs(tabPos);
	    addBody();
	} else if (tabPos == TabPosition.RIGHT) {
	    addBody();
	    addTabs(tabPos);
	}
    }

    private void addTabs(TabPosition tabPos) {
	Cell<WidgetGroup> _cell = super.add(tabGroup).left();
	if (tabPos == TabPosition.TOP || tabPos == TabPosition.BOTTOM) {
	    _cell.expandX().fillX();
	} else {
	    _cell.expandY().fillY();
	}

    }

    private void addBody() {
	super.add(container).expand().fill();
    }

    /**
     * Apply a {@link TabPaneStyle style}.
     * 
     * @param style
     */
    public void setStyle(TabPaneStyle style) {
	this.style = style;

	setBackground(style.background);
    }

    /**
     * @return the current {@link TabPaneStyle style}
     */
    public TabPaneStyle getStyle() {
	return style;
    }

    @Deprecated
    @Override
    public Cell<Actor> add() {
	throw new OptimizationException("Use addTab(Tab) instead.");
    };

    @Deprecated
    @Override
    public Cell<Actor> add(Actor actor) {
	if (actor instanceof HorizontalGroup || actor instanceof VerticalGroup || actor instanceof Container)
	    return super.add(actor);

	throw new OptimizationException("Use addTab(Tab) instead.");
    }

    @Deprecated
    @Override
    public Cell<Label> add(String text) {
	throw new OptimizationException("Use addTab(Tab) instead.");
    }

    @Deprecated
    @Override
    public Cell<Label> add(String text, String fontName, Color color) {
	throw new OptimizationException("Use addTab(Tab) instead.");
    }

    @Deprecated
    @Override
    public Cell<Label> add(String text, String fontName, String colorName) {
	throw new OptimizationException("Use addTab(Tab) instead.");
    }

    @Deprecated
    @Override
    public Cell<Label> add(String text, String labelStyleName) {
	throw new OptimizationException("Use addTab(Tab) instead.");
    }

    @Deprecated
    @Override
    public void addActor(Actor actor)
    {
	if (actor instanceof HorizontalGroup || actor instanceof VerticalGroup || actor instanceof Container)
	    super.addActor(actor);
	else
	    throw new OptimizationException("Use addTab(Tab) instead.");
    }

    @Deprecated
    @Override
    public void addActorAfter(Actor actorAfter, Actor actor) {
	throw new OptimizationException("Use addTab(Tab) instead.");
    }

    @Deprecated
    @Override
    public void addActorAt(int index, Actor actor) {
	throw new OptimizationException("Use addTab(Tab) instead.");
    }

    @Deprecated
    @Override
    public void addActorBefore(Actor actorBefore, Actor actor) {
	throw new OptimizationException("Use addTab(Tab) instead.");
    }

    /**
     * Add the specified {@link Tab}.
     * 
     * @param tab the {@link Tab} to add
     */
    public void addTab(Tab tab) {
	tabs.add(tab);
	buttonGroup.add(tab);
	tabGroup.addActor(tab);
	tab.setFrom(this);
	setCurrentTab(tab);
    }

    /**
     * Remove the specified {@link Tab}.
     * 
     * @param tab the {@ink Tab} to remove
     */
    public void removeTab(Tab tab) {
	Tab _tab = null;
	int _index = tabs.indexOf(tab, true);
	if (_index + 1 < tabs.size)
	    _tab = tabs.get(_index + 1);
	else if (_index - 1 >= 0)
	    _tab = tabs.get(_index - 1);

	buttonGroup.remove(tab);
	tabs.removeIndex(_index).remove();
	tab.remove();

	setCurrentTab(_tab);
    }

    @Deprecated
    @Override
    public boolean removeActor(Actor actor) {
	throw new OptimizationException("Use removeTab(Tab) instead.");
    }

    /**
     * Get all {@link Tab}s of this {@link TabPane}.
     * 
     * @return all {@link Tab}
     */
    public Array<Tab> getTabs() {
	return tabs;
    }

    /**
     * Get the current {@link Tab}.
     * 
     * @return the current {@link Tab}
     */
    public Tab getCurrentTab() {
	return currentTab;
    }

    /**
     * Set the selected {@link Tab}.
     * 
     * @param currentTab the {@link Tab} to set selected
     */
    public void setCurrentTab(Tab currentTab) {
	if (currentTab == null)
	    return;

	Tab _lastTab = this.currentTab;
	this.currentTab = currentTab;

	if (_lastTab != null) {
	    _lastTab.getContainer().remove();
	}

	container.setActor(currentTab.getContainer());
	currentTab.setChecked(true);
    }

    /**
     * Set the selected {@ink Tab}.
     * 
     * @param index the index of the {@link Tab}
     */
    public void setCurrentTab(int index) {
	setCurrentTab(tabs.get(index));
    }

    /**
     * Get the {@link Tab} at the specified index.
     * Start from 0.
     * 
     * @param index position of the {@link Tab}
     * @return the {@link Tab} at the specified index
     */
    public Tab getTab(int index) {
	return tabs.get(index);
    }

    /**
     * Get the first {@link Tab} of the specified type.
     * 
     * @param clazz the type of the searched {@link Tab}
     * @return the first found {@link Tab} of the searched type or null if nothing is found
     */
    @SuppressWarnings("unchecked")
    public <T extends Tab> T getTab(Class<T> clazz) {
	for (int i = 0; i < tabs.size; i++) {
	    Tab _tab = tabs.get(i);
	    if (ClassReflection.isInstance(clazz, _tab))
		return (T) _tab;
	}

	return null;
    }

    /**
     * Define the style of a {@link TabPane tab pane}.
     */
    public static class TabPaneStyle {

	/** Optional */
	public Drawable background;

	public TabPaneStyle() {
	}

	public TabPaneStyle(Drawable background) {
	    this.background = background;
	}

	public TabPaneStyle(TabPaneStyle style) {
	    this.background = style.background;
	}

    }

    public enum TabPosition {
	TOP, BOTTOM, LEFT, RIGHT;
    }

}
