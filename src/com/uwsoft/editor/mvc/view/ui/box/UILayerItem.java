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

package com.uwsoft.editor.mvc.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.renderer.data.LayerItemVO;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UILayerItem extends VisTable {

    private Overlap2DFacade facade;
    private TextureManager textureManager;

    private Image lock;
    private Image eye;
    private String name;

    public UILayerItem(LayerItemVO layerData) {
        super();

        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(TextureManager.NAME);

        lock = new Image(textureManager.getEditorAsset("lock"));
        add(lock).left().padRight(10);

        eye = new Image(textureManager.getEditorAsset("eye"));
        add(eye).left().padRight(10);

        VisLabel lbl = new VisLabel(layerData.layerName);
        add(lbl).fillX();

        name = layerData.layerName;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setSelected(boolean selected) {
        // TODO: visual selecting
    }
}
