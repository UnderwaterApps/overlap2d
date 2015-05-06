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

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.thumbnail;

import com.badlogic.gdx.graphics.Color;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.mvc.view.ui.box.resourcespanel.draggable.DraggableResource;

/**
 * Created by sargis on 5/6/15.
 */
public abstract class ThumbnailBoxResource extends DraggableResource {
    protected float thumbnailSize = 50;
    protected PixelRect rc;

    public ThumbnailBoxResource() {
        rc = new PixelRect(thumbnailSize, thumbnailSize);
        rc.setFillColor(new Color(1, 1, 1, 0.2f));
        rc.setBorderColor(new Color(1, 1, 1, 0.4f));
        addActor(rc);
        setWidth(thumbnailSize);
        setHeight(thumbnailSize);
    }
}
