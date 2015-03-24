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
package com.gdx.extension.ui.slide;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class SlideShow extends Table
{

    private SlideShowStyle style;

    private ScrollPane contentScroll;
    private WidgetGroup content;

    private ImageButton previousScrollButton;
    private ImageButton nextScrollButton;

    private Cell<ImageButton> previousScrollButtonCell;
    private Cell<ImageButton> nextScrollButtonCell;

    private boolean isVertical;
    private boolean showButtons = true;

    public SlideShow(Skin skin)
    {
	this(false, skin);
    }

    public SlideShow(boolean isVertical, Skin skin)
    {
	this(isVertical, skin, (isVertical) ? "default-vertical" : "default-horizontal");
    }

    public SlideShow(boolean isVertical, Skin skin, String styleName)
    {
	super(skin);

	setStyle(skin.get(styleName, SlideShowStyle.class));

	this.isVertical = isVertical;

	content = (isVertical) ? new VerticalGroup() : new HorizontalGroup();

	ScrollPaneStyle _scrollStyle = new ScrollPaneStyle();
	contentScroll = new ScrollPane(content, _scrollStyle);
	contentScroll.setScrollingDisabled(isVertical, !isVertical);
	contentScroll.setOverscroll(false, false);

	setBackground(style.background);

	previousScrollButton = new ImageButton(getStyle().previousScroll);
	previousScrollButtonCell = add(previousScrollButton);
	if (isVertical)
	    previousScrollButtonCell.row();
	Cell<ScrollPane> _contentCell = add(contentScroll);
	if (isVertical)
	    _contentCell.row();
	nextScrollButton = new ImageButton(getStyle().nextScroll);
	nextScrollButtonCell = add(nextScrollButton);

	previousScrollButton.addListener(new ClickListener() {

	    @Override
	    public void clicked(InputEvent event, float x, float y)
	    {
		if (SlideShow.this.isVertical)
		    contentScroll.setScrollPercentY(contentScroll.getScrollPercentY() - 0.1f);
		else
		    contentScroll.setScrollPercentX(contentScroll.getScrollPercentX() - 0.1f);
	    }

	});
	nextScrollButton.addListener(new ClickListener() {

	    @Override
	    public void clicked(InputEvent event, float x, float y)
	    {
		if (SlideShow.this.isVertical)
		    contentScroll.setScrollPercentY(contentScroll.getScrollPercentY() + 0.1f);
		else
		    contentScroll.setScrollPercentX(contentScroll.getScrollPercentX() + 0.1f);
	    }

	});
    }

    public void addItem(Actor actor)
    {
	content.addActor(actor);
    }

    public void setStyle(SlideShowStyle style)
    {
	this.style = style;
    }

    @Override
    public void validate()
    {
	super.validate();

	if (isVertical)
	    contentScroll.setWidth(content.getWidth());
	else
	    contentScroll.setHeight(content.getHeight());

	if (showButtons)
	{
	    float _scrollPercent;
	    if (isVertical)
		_scrollPercent = contentScroll.getScrollPercentY();
	    else
		_scrollPercent = contentScroll.getScrollPercentX();

	    if (_scrollPercent > 0f)
		previousScrollButtonCell.setActor(previousScrollButton);
	    else
		previousScrollButtonCell.setActor(null);
	    if (_scrollPercent < 1f)
		nextScrollButtonCell.setActor(nextScrollButton);
	    else
		nextScrollButtonCell.setActor(null);
	}
    }

    public void setShowButtons(boolean show)
    {
	this.showButtons = show;

	if (!show)
	{
	    previousScrollButtonCell.setActor(null);
	    nextScrollButtonCell.setActor(null);
	}
    }

    public boolean getShowButtons()
    {
	return showButtons;
    }

    public boolean isVertical()
    {
	return isVertical;
    }

    public SlideShowStyle getStyle()
    {
	return style;
    }

    public static class SlideShowStyle
    {

	/** Optional */
	public Drawable background;

	public ImageButtonStyle previousScroll, nextScroll;
    }

}
