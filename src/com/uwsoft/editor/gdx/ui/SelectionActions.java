package com.uwsoft.editor.gdx.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;

public class SelectionActions extends Group {

    public static final int GROUP_ITEMS = 1;
    public static final int EDIT_COMPOSITE = 2;
    public static final int CONVERT_TO_BUTTON = 3;
    public static final int CUT = 4;
    public static final int COPY = 5;
    public static final int PASTE = 6;
    public static final int DELETE = 7;
    public static final int ADD_TO_LIBRARY = 8;
    public static final int EDIT_PHYSICS = 9;
    public static final int EDIT_ASSET_PHYSICS = 10;
    public static final int DO_NOTHING = 99;

}
