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

package com.uwsoft.editor.controller;

import com.commons.MsgAPI;
import com.puremvc.patterns.command.SimpleCommand;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.*;
import com.uwsoft.editor.controller.commands.component.UpdateLabelDataCommand;
import com.uwsoft.editor.controller.commands.component.UpdateLightDataCommand;
import com.uwsoft.editor.controller.commands.component.UpdatePolygonComponentCommand;
import com.uwsoft.editor.controller.commands.component.UpdateSpriteAnimationDataCommand;
import com.uwsoft.editor.controller.commands.resource.*;

/**
 * Created by azakhary on 4/28/2015.
 */
public class BootstrapCommand extends SimpleCommand {

    public void execute(Notification notification) {
        super.execute(notification);
        facade = Overlap2DFacade.getInstance();
        facade.registerCommand(MsgAPI.ACTION_CUT, CutItemsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_COPY, CopyItemsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_PASTE, PasteItemsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE, DeleteItemsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CREATE_ITEM, CreateItemCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CAMERA_CHANGE_COMPOSITE, CompositeCameraChangeCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CREATE_PRIMITIVE, CreatePrimitiveCommand.class);

        facade.registerCommand(MsgAPI.ACTION_DELETE_LAYER, DeleteLayerCommand.class);
        facade.registerCommand(MsgAPI.ACTION_NEW_LAYER, NewLayerCommand.class);
        facade.registerCommand(MsgAPI.ACTION_SWAP_LAYERS, LayerSwapCommand.class);
        facade.registerCommand(MsgAPI.ACTION_RENAME_LAYER, RenameLayerCommand.class);

        facade.registerCommand(MsgAPI.ACTION_ADD_COMPONENT, AddComponentToItemCommand.class);
        facade.registerCommand(MsgAPI.ACTION_REMOVE_COMPONENT, RemoveComponentFromItemCommand.class);
        facade.registerCommand(MsgAPI.CUSTOM_VARIABLE_MODIFY, CustomVariableModifyCommand.class);

        facade.registerCommand(MsgAPI.ACTION_ITEMS_MOVE_TO, ItemsMoveCommand.class);

        facade.registerCommand(MsgAPI.ACTION_ITEM_AND_CHILDREN_TO, ItemChildrenTransformCommand.class);

        facade.registerCommand(MsgAPI.ACTION_ITEM_TRANSFORM_TO, ItemTransformCommand.class);
        facade.registerCommand(MsgAPI.ACTION_ADD_TO_LIBRARY, AddToLibraryCommand.class);
        facade.registerCommand(MsgAPI.ACTION_CONVERT_TO_BUTTON, ConvertToButtonCommand.class);
        facade.registerCommand(MsgAPI.ACTION_GROUP_ITEMS, ConvertToCompositeCommand.class);
        facade.registerCommand(MsgAPI.ACTION_SET_GRID_SIZE_FROM_ITEM, SetGridSizeFromItemCommand.class);

        facade.registerCommand(MsgAPI.ACTION_SET_SELECTION, SetSelectionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_ADD_SELECTION, AddSelectionCommand.class);
        facade.registerCommand(MsgAPI.ACTION_RELEASE_SELECTION, ReleaseSelectionCommand.class);

        facade.registerCommand(MsgAPI.ACTION_UPDATE_RULER_POSITION, ChangeRulerPositionCommand.class);
        // DATA MODIFY by components
        facade.registerCommand(MsgAPI.ACTION_UPDATE_ITEM_DATA, UpdateEntityComponentsCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_LABEL_DATA, UpdateLabelDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_LIGHT_DATA, UpdateLightDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_SPRITE_ANIMATION_DATA, UpdateSpriteAnimationDataCommand.class);
        facade.registerCommand(MsgAPI.ACTION_UPDATE_MESH_DATA, UpdatePolygonComponentCommand.class);

        facade.registerCommand(MsgAPI.ACTION_UPDATE_MESH_DATA, UpdatePolygonComponentCommand.class);

        facade.registerCommand(MsgAPI.ACTION_EXPORT_PROJECT, ExportProjectCommand.class);
        facade.registerCommand(MsgAPI.SAVE_EXPORT_PATH, SaveExportPathCommand.class);

        facade.registerCommand(MsgAPI.ACTION_PLUGIN_PROXY_COMMAND, PluginItemCommand.class);

        // Resources
        facade.registerCommand(MsgAPI.ACTION_DELETE_IMAGE_RESOURCE, DeleteImageResource.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_LIBRARY_ITEM, DeleteLibraryItem.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_PARTICLE_EFFECT, DeleteParticleEffect.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_SPINE_ANIMATION_RESOURCE, DeleteSpineAnimation.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_SPRITE_ANIMATION_RESOURCE, DeleteSpriteAnimation.class);
        facade.registerCommand(MsgAPI.ACTION_DELETE_SPRITER_ANIMATION_RESOURCE, DeleteSpriterAnimation.class);
    }
}
