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

package com.uwsoft.editor.mvc.view.ui.box.resourcespanel;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.uwsoft.editor.gdx.ui.thumbnailbox.ImageThumbnailBox;

/**
 * Created by azakhary on 4/17/2015.
 */
public class UIImagesTab extends Tab {

    private VisTable contentTable;
    private VisTable imagesTable;
    private VisScrollPane scrollPane;

//    private TextureManager textureManager;
//    private UIStage stage;

    public UIImagesTab() {
        super(false, false);

//        textureManager = Overlap2DFacade.getInstance().retrieveProxy(TextureManager.NAME);
//        stage = Sandbox.getInstance().getUIStage();

        contentTable = new VisTable();

        imagesTable = new VisTable();
        scrollPane = new VisScrollPane(imagesTable);
        contentTable.add(scrollPane).width(230).height(350);
        contentTable.row();
    }

    @Override
    public String getTabTitle() {
        return "Images";
    }

    @Override
    public Table getContentTable() {
        return contentTable;
    }

    public void setThumbnailBoxes(Array<ImageThumbnailBox> thumbnailBoxes) {
        imagesTable.clearChildren();
        for (int i = 0; i < thumbnailBoxes.size; i++) {
            imagesTable.add(thumbnailBoxes.get(i)).pad(3);
            if ((i - 7) % 4 == 0) {
                imagesTable.row();
            }
        }
    }
}
