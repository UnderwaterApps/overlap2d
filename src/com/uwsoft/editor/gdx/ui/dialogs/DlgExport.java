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

package com.uwsoft.editor.gdx.ui.dialogs;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.uwsoft.editor.mvc.proxy.ResolutionManager;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.actor.TextButtonItem;

import java.io.File;
import java.util.HashMap;

/**
 * Created by sargis on 7/31/14.
 */
public class DlgExport extends CompositeDialog {

    private final TextBoxItem projectAssets;
    private final TextBoxItem packerWidth;
    private final TextBoxItem packerHeight;
    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private ProjectVO projectVO;
    private HashMap<String, File> paths = new HashMap<>();

    public DlgExport(UIStage s) {
        super(s, "exportSettingsDlg", 480, 230);

        setTitle("Export settings");

        facade = Overlap2DFacade.getInstance();
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        projectVO = projectManager.getCurrentProjectVO();
        packerWidth = ui.getTextBoxById("packerWidth");
        packerWidth.setText(projectVO.texturepackerWidth);
        packerHeight = ui.getTextBoxById("packerHeight");
        packerHeight.setText(projectVO.texturepackerHeight);
        projectAssets = ui.getTextBoxById("projectAssetsPath");
        projectAssets.setDisabled(true);
        projectAssets.setText(projectVO.projectMainExportPath);
        paths.put("global", new File(projectVO.projectMainExportPath));

        setPathProvider("global", projectAssets, ui.getTextButtonById("projectAssetsBtn"), "");

        ui.getTextButtonById("saveBtn").addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                saveNewExportPaths();
                remove();
            }
        });

        ui.getTextButtonById("saveAndExportBtn").addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                saveNewExportPaths();
                projectManager.exportProject();
                remove();
            }
        });

    }

    private void setMiniPaths() {
        paths.put("scene", paths.get("global"));
        paths.put("atlas", paths.get("global"));
        paths.put("effects", paths.get("global"));
        paths.put("spine", paths.get("global"));
        paths.put("spriter", paths.get("global"));
        paths.put("fonts", paths.get("global"));
    }

    private void saveNewExportPaths() {
        // save before importing
        int width = Integer.parseInt(packerWidth.getText());
        int height = Integer.parseInt(packerHeight.getText());
        if (!((width > 0) && ((width & (width - 1)) == 0) && (width > 0) && ((height & (height - 1)) == 0))) {
            stage.dialogs().showInfoDialogNavigateBack("Width and height must be power of 2");
            return;
        }
        if (width != Integer.parseInt(projectVO.texturepackerWidth) || height != Integer.parseInt(projectVO.texturepackerHeight)) {
            projectManager.setTexturePackerSizes(Integer.toString(width), Integer.toString(height));
            ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
            resolutionManager.rePackProjectImagesForAllResolutions();
        }
        projectManager.setExportPaths(paths.get("global"));
        projectManager.saveCurrentProject();

    }

    private void setPathProvider(final String type, final TextField textField, final TextButtonItem btn, final String defaultPath) {
        btn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                //chooser creation
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES);
                fileChooser.setMultiselectionEnabled(false);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(FileHandle file) {
                        textField.setText(file.path());
                        if (type.equals("global")) {
                            setMiniPaths();
                        }
                        paths.put(type, file.file());
                    }
                });
                stage.addActor(fileChooser.fadeIn());
            }
        });
    }
}
