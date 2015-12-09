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
import com.badlogic.gdx.math.Vector2;
import com.commons.MsgAPI;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.EntityModifyRevertableCommand;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

/**
 * Created by azakhary on 7/3/2015.
 */
public class UpdatePolygonComponentCommand extends EntityModifyRevertableCommand {

    private Integer entityId;
    private Vector2[][] dataFrom;
    private Vector2[][] dataTo;

    private void collectData() {
        Object[] payload = getNotification().getBody();
        entityId = EntityUtils.getEntityId((Entity) payload[0]);
        dataFrom = (Vector2[][]) payload[1];
        dataTo = (Vector2[][]) payload[2];
        dataFrom = dataFrom.clone();
        dataTo = dataTo.clone();
    }

    @Override
    public void doAction() {
        collectData();

        Entity entity = EntityUtils.getByUniqueId(entityId);

        PolygonComponent polygonComponent = ComponentRetriever.get(entity, PolygonComponent.class);
        polygonComponent.vertices = dataTo;

        // if it's image update polygon sprite data
        TextureRegionComponent textureRegionComponent = ComponentRetriever.get(entity, TextureRegionComponent.class);
        if(textureRegionComponent != null && textureRegionComponent.isPolygon) {
            DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
            float ppwu = dimensionsComponent.width/textureRegionComponent.region.getRegionWidth();
            dimensionsComponent.setPolygon(polygonComponent);
            textureRegionComponent.setPolygonSprite(polygonComponent,1f/ppwu);
        }

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);

    }

    @Override
    public void undoAction() {
        Entity entity = EntityUtils.getByUniqueId(entityId);

        PolygonComponent polygonComponent = ComponentRetriever.get(entity, PolygonComponent.class);
        polygonComponent.vertices = dataFrom;

        // if it's image update polygon sprite data
        TextureRegionComponent textureRegionComponent = ComponentRetriever.get(entity, TextureRegionComponent.class);
        if(textureRegionComponent != null && textureRegionComponent.isPolygon) {
            DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
            float ppwu = dimensionsComponent.width/textureRegionComponent.region.getRegionWidth();
            dimensionsComponent.setPolygon(polygonComponent);
            textureRegionComponent.setPolygonSprite(polygonComponent, 1f/ppwu);
        }

        Overlap2DFacade.getInstance().sendNotification(MsgAPI.ITEM_DATA_UPDATED, entity);
    }

    public static Object[] payloadInitialState(Entity entity) {
        PolygonComponent polygonComponent = ComponentRetriever.get(entity, PolygonComponent.class);
        Object[] payload = new Object[3];
        payload[0] = entity;
        payload[1] = cloneData(polygonComponent.vertices);

        return payload;
    }

    public static Object[] payload(Object[] payload, Vector2[][] vertices) {
        payload[2] = cloneData(vertices);

        return payload;
    }

    private static Vector2[][] cloneData(Vector2[][] data) {
        Vector2[][] newData = new Vector2[data.length][];
        for(int i = 0; i < data.length; i++) {
            newData[i] = new Vector2[data[i].length];
            for(int j = 0; j < data[i].length; j++) {
                newData[i][j] = data[i][j].cpy();
            }
        }

        return newData;
    }
}
