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

package com.uwsoft.editor.controller.commands.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.commons.MsgAPI;
import com.uwsoft.editor.controller.commands.EntityModifyRevertableCommand;
import com.uwsoft.editor.renderer.components.label.LabelComponent;
import com.uwsoft.editor.renderer.factory.component.LabelComponentFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import com.uwsoft.editor.view.stage.Sandbox;

/**
 * Created by azakhary on 6/11/2015.
 *
 */
public class UpdateLabelDataCommand extends EntityModifyRevertableCommand {

    Integer entityId;

    String prevFontName;
    int prevFontSize;
    int prevLabelAlign;
    int prevLineAlign;
    String prevText;
    Label.LabelStyle prevStyle;

    @Override
    public void doAction() {
        Object[] payload = getNotification().getBody();
        Entity entity = (Entity) payload[0];
        entityId = EntityUtils.getEntityId(entity);

        LabelComponent labelComponent = ComponentRetriever.get(entity, LabelComponent.class);


        this.prevFontName = labelComponent.fontName;
        this.prevFontSize = labelComponent.fontSize;
        this.prevLabelAlign = labelComponent.labelAlign;
        this.prevLineAlign = labelComponent.lineAlign;
        this.prevStyle = labelComponent.getStyle();
        this.prevText = (String) payload[5];


        labelComponent.fontName = (String) payload[1];
        labelComponent.fontSize = (int) payload[2];
        labelComponent.setAlignment((Integer) payload[3]);
        labelComponent.setText((String) payload[4]);
        labelComponent.setStyle(getNewStyle(labelComponent.fontName, labelComponent.fontSize));

        facade.sendNotification(MsgAPI.ITEM_PROPERTY_DATA_FINISHED_MODIFYING, entity);
    }

    private Label.LabelStyle getNewStyle(String fontName, int fontSize) {

        IResourceRetriever rm = Sandbox.getInstance().getSceneControl().sceneLoader.getRm();
        final boolean hasBitmapFont = rm.getBitmapFont(fontName, fontSize) != null;

        if(!hasBitmapFont) {
            com.uwsoft.editor.proxy.ResourceManager resourceManager = facade.retrieveProxy(com.uwsoft.editor.proxy.ResourceManager.NAME);
            resourceManager.prepareEmbeddingFont(fontName, fontSize);
        }
        return LabelComponentFactory.generateStyle(rm, fontName, fontSize);
    }

    @Override
    public void undoAction() {

        final Entity entity = EntityUtils.getByUniqueId(entityId);
        final LabelComponent labelComponent = ComponentRetriever.get(entity, LabelComponent.class);

        labelComponent.fontName = prevFontName;
        labelComponent.fontSize = prevFontSize;
        labelComponent.setAlignment(prevLabelAlign, prevLineAlign);
        labelComponent.setText(prevText);
        labelComponent.setStyle(prevStyle);

    }
}
