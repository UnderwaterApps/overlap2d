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

package com.commons;

/**
 * Created by azakhary on 10/23/2015.
 */
public class MsgAPI {

    /**
     *  Previous Overlap2D class notifications
     */
    public static final String GLOBAL_PREFIX = "com.uwsoft.editor.Overlap2D";

    public static final String PAUSE = GLOBAL_PREFIX + ".PAUSE";
    public static final String RESUME = GLOBAL_PREFIX + ".RESUME";
    public static final String RENDER = GLOBAL_PREFIX + ".RENDER";
    public static final String RESIZE = GLOBAL_PREFIX + ".RESIZE";
    public static final String DISPOSE = GLOBAL_PREFIX + ".DISPOSE";
    public static final String CREATE = GLOBAL_PREFIX + ".CREATE_BTN_CLICKED";

    // tmp events
    public static final String ZOOM_CHANGED = GLOBAL_PREFIX + ".ZOOM_CHANGED";
    public static final String GRID_SIZE_CHANGED = GLOBAL_PREFIX + ".GRID_SIZE_CHANGED";
    public static final String ITEM_DATA_UPDATED = GLOBAL_PREFIX + ".ITEM_DATA_UPDATED";
    public static final String ITEM_PROPERTY_DATA_FINISHED_MODIFYING = GLOBAL_PREFIX + ".ITEM_PROPERTY_DATA_FINISHED_MODIFYING";

    // this should move
    public static final String HIDE_SELECTIONS = GLOBAL_PREFIX + ".HIDE_SELECTIONS";
    public static final String SHOW_SELECTIONS = GLOBAL_PREFIX + ".SHOW_SELECTIONS";
    public static final String ITEM_SELECTION_CHANGED = GLOBAL_PREFIX + ".ITEM_SELECTION_CHANGED";
    public static final String EMPTY_SPACE_CLICKED = GLOBAL_PREFIX + ".EMPTY_SPACE_CLICKED";

    public static final String SCENE_RIGHT_CLICK = GLOBAL_PREFIX + ".SCENE_RIGHT_CLICK";
    public static final String ITEM_RIGHT_CLICK = GLOBAL_PREFIX + ".ITEM_RIGHT_CLICK";

    public static final String LIBRARY_LIST_UPDATED = GLOBAL_PREFIX + ".LIBRARY_LIST_UPDATED";


    /**
     *  Previous Sandbox class notifications
     */
    public static final String SANDBOX_PREFIX = "com.uwsoft.editor.gdx.commands.Sandbox";

    public static final String ACTION_GROUP_ITEMS = SANDBOX_PREFIX + "ACTION_GROUP_ITEMS";
    public static final String ACTION_CAMERA_CHANGE_COMPOSITE = SANDBOX_PREFIX + "ACTION_CAMERA_CHANGE_COMPOSITE";
    public static final String ACTION_CONVERT_TO_BUTTON = SANDBOX_PREFIX + "ACTION_CONVERT_TO_BUTTON";
    public static final String ACTION_CUT = SANDBOX_PREFIX + "ACTION_CUT";
    public static final String ACTION_COPY = SANDBOX_PREFIX + "ACTION_COPY";
    public static final String ACTION_PASTE = SANDBOX_PREFIX + "ACTION_PASTE";
    public static final String ACTION_DELETE = SANDBOX_PREFIX + "ACTION_DELETE";
    public static final String ACTION_CREATE_ITEM = SANDBOX_PREFIX + "ACTION_CREATE_ITEM";

    public static final String ACTION_DELETE_LAYER = SANDBOX_PREFIX + "ACTION_DELETE_LAYER";
    public static final String ACTION_NEW_LAYER = SANDBOX_PREFIX + "ACTION_NEW_LAYER";
    public static final String ACTION_SWAP_LAYERS = SANDBOX_PREFIX + "ACTION_SWAP_LAYERS";
    public static final String ACTION_RENAME_LAYER = SANDBOX_PREFIX + "ACTION_RENAME_LAYER";

    public static final String ACTION_ADD_COMPONENT = SANDBOX_PREFIX + "ACTION_ADD_COMPONENT";
    public static final String ACTION_REMOVE_COMPONENT = SANDBOX_PREFIX + "ACTION_REMOVE_COMPONENT";
    public static final String CUSTOM_VARIABLE_MODIFY = SANDBOX_PREFIX + "CUSTOM_VARIABLE_MODIFY";

    public static final String SHOW_ADD_LIBRARY_DIALOG = SANDBOX_PREFIX + "SHOW_ADD_LIBRARY_DIALOG";
    public static final String ACTION_ADD_TO_LIBRARY = SANDBOX_PREFIX + "ACTION_ADD_TO_LIBRARY";
    public static final String ACTION_EDIT_PHYSICS = SANDBOX_PREFIX + "ACTION_EDIT_PHYSICS";
    public static final String ACTION_SET_GRID_SIZE_FROM_ITEM = SANDBOX_PREFIX + "ACTION_SET_GRID_SIZE_FROM_ITEM";
    public static final String ACTION_ITEMS_MOVE_TO = SANDBOX_PREFIX + "ACTION_ITEMS_MOVE_TO";
    public static final String ACTION_ITEM_TRANSFORM_TO = SANDBOX_PREFIX + "ACTION_ITEM_TRANSFORM_TO";

    public static final String ACTION_CREATE_PRIMITIVE = SANDBOX_PREFIX + "ACTION_CREATE_PRIMITIVE";

    public static final String ACTION_SET_SELECTION = SANDBOX_PREFIX + "ACTION_SET_SELECTION";
    public static final String ACTION_ADD_SELECTION = SANDBOX_PREFIX + "ACTION_ADD_SELECTION";
    public static final String ACTION_RELEASE_SELECTION = SANDBOX_PREFIX + "ACTION_RELEASE_SELECTION";

    public static final String ACTION_UPDATE_ITEM_DATA = SANDBOX_PREFIX + "ACTION_UPDATE_ITEM_DATA";
    public static final String ACTION_UPDATE_LABEL_DATA = SANDBOX_PREFIX + "ACTION_UPDATE_LABEL_DATA";
    public static final String ACTION_UPDATE_LIGHT_DATA = SANDBOX_PREFIX + "ACTION_UPDATE_LIGHT_DATA";
    public static final String ACTION_UPDATE_SPRITE_ANIMATION_DATA = SANDBOX_PREFIX + "ACTION_UPDATE_SPRITE_ANIMATION_DATA";
    public static final String ACTION_UPDATE_MESH_DATA = SANDBOX_PREFIX + "ACTION_UPDATE_MESH_DATA";

    public static final String ACTION_PLUGIN_PROXY_COMMAND = SANDBOX_PREFIX + "ACTION_PLUGIN_PROXY_COMMAND";

    public static final String ACTION_EXPORT_PROJECT = SANDBOX_PREFIX + "ACTION_EXPORT_PROJECT";
    public static final String SAVE_EXPORT_PATH = SANDBOX_PREFIX + "SAVE_EXPORT_SETTINGS";


    // drop down resources
    public static final String ACTION_DELETE_IMAGE_RESOURCE = SANDBOX_PREFIX + "ACTION_DELETE_IMAGE_RESOURCE";
    public static final String ACTION_DELETE_SPRITE_ANIMATION_RESOURCE = SANDBOX_PREFIX + "ACTION_DELETE_SPRITE_ANIMATION_RESOURCE";
    public static final String ACTION_DELETE_SPRITER_ANIMATION_RESOURCE = SANDBOX_PREFIX + "ACTION_DELETE_SPRITER_ANIMATION_RESOURCE";
    public static final String ACTION_DELETE_SPINE_ANIMATION_RESOURCE = SANDBOX_PREFIX + "ACTION_DELETE_SPINE_ANIMATION_RESOURCE";
    public static final String ACTION_DELETE_LIBRARY_ITEM = SANDBOX_PREFIX + "ACTION_DELETE_LIBRARY_ITEM";
    public static final String ACTION_DELETE_PARTICLE_EFFECT = SANDBOX_PREFIX + "ACTION_DELETE_PARTICLE_EFFECT";
    
    public static final String ACTION_UPDATE_RULER_POSITION = SANDBOX_PREFIX + "ACTION_UPDATE_RULER_POSITION";

    /**
     *  Other
     */
    public static final String ITEM_FACTORY_PREFIX = "com.uwsoft.editor.factory.ItemFactory";
    public static final String NEW_ITEM_ADDED = ITEM_FACTORY_PREFIX + ".NEW_ITEM_ADDED";
    public static final String SCENE_DATA_PREFIX = "com.uwsoft.editor.proxy.SceneDataManager";
    public static final String SCENE_LOADED = SCENE_DATA_PREFIX + ".SCENE_LOADED";
}
