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

package com.uwsoft.editor.mvc.view.dialog;

import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTable;
import com.uwsoft.editor.ui.widget.InputFileWidget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AssetsImportDialog extends VisDialog {

//    private final ProgressBar progressBar;
//    private final Label progressLbl;
//    private final TextBoxItem spriteAnimationPath;
//    private final Overlap2DFacade facade;
//    private final ProjectManager projectManager;
//    private TextField imagesPath;
//    private TextField particlePath;
//    private TextField stylePath;
//    private TextField fontPath;
//    private TextField spinePath;
//    private ProgressHandler progressHandler;

    private HashMap<String, ArrayList<File>> paths = new HashMap<>();

    AssetsImportDialog() {
        super("Import new Assets");
        addCloseButton();
        VisTable mainTable = new VisTable();
        mainTable.add(new InputFileWidget()).padRight(50);
        mainTable.add(new InputFileWidget());
        mainTable.pad(15);

        add(mainTable);
//        imagesPath = ui.getTextBoxById("imagesPath");
//        imagesPath.setDisabled(true);
//
//        particlePath = ui.getTextBoxById("particlePath");
//        particlePath.setDisabled(true);
//
//        stylePath = ui.getTextBoxById("stylePath");
//        stylePath.setDisabled(true);
//
//        fontPath = ui.getTextBoxById("fontPath");
//        fontPath.setDisabled(true);
//
//        spinePath = ui.getTextBoxById("spinePath");
//        spinePath.setDisabled(true);
//
//        spriteAnimationPath = ui.getTextBoxById("spriteAnimationPath");
//        spriteAnimationPath.setDisabled(true);
//
//        setPathProvider("images", imagesPath, ui.getTextButtonById("imagesBtn"), "");
//        setPathProvider("particles", particlePath, ui.getTextButtonById("particleBtn"), "");
//        setPathProvider("styles", stylePath, ui.getTextButtonById("styleBtn"), "");
//        setPathProvider("font", fontPath, ui.getTextButtonById("fontBtn"), "");
//        setPathProvider("spine", spinePath, ui.getTextButtonById("spineBtn"), "");
//        setPathProvider("spriteAnimation", spriteAnimationPath, ui.getTextButtonById("spriteAnimationBtn"), "");
//
//        facade = Overlap2DFacade.getInstance();
//        projectManager = facade.retrieveProxy(ProjectManager.NAME);
//        ui.getTextButtonById("startBtn").addListener(new ClickListener() {
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//
//                if (paths.get("images") != null) {
//                    projectManager.importExternalImagesIntoProject(paths.get("images"), progressHandler);
//                }
//                if (paths.get("particles") != null) {
//                    projectManager.importExternalParticlesIntoProject(paths.get("particles"), progressHandler);
//                }
//                if (paths.get("styles") != null) {
//                    projectManager.importExternalStyleIntoProject(paths.get("styles").get(0), progressHandler);
//                }
//                if (paths.get("font") != null) {
//                    projectManager.importExternalFontIntoProject(paths.get("font"), progressHandler);
//                }
//
//                if (paths.get("spine") != null) {
//                    projectManager.importExternalSpineAnimationsIntoProject(paths.get("spine"), progressHandler);
//                }
//
//                if (paths.get("spriteAnimation") != null) {
//                    projectManager.importExternalSpriteAnimationsIntoProject(paths.get("spriteAnimation"), progressHandler);
//                }
//
//
//                // save before importing
//                SceneVO vo = stage.getSandbox().sceneVoFromItems();
//                stage.getCompositePanel().updateOriginalItem();
//                SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
//                sceneDataManager.saveScene(vo);
//                projectManager.saveCurrentProject();
//            }
//        });
//
//
//        // adding progress bar
//        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
//        progressBar = new ProgressBar(0, 100, 1, false, textureManager.editorSkin);
//        progressBar.setWidth(getWidth() - 60);
//        progressBar.setX(10);
//        progressBar.setY(33);
//        mainLayer.addActor(progressBar);
//
//        progressLbl = new Label("0%", textureManager.editorSkin);
//        progressLbl.setX(progressBar.getX() + progressBar.getWidth() + 4);
//        progressLbl.setY(38);
//        mainLayer.addActor(progressLbl);
    }

//    @Override
//    public void show() {
//        super.show();
//        setBounds(Math.round(getX()), Math.round(getY()), Math.round(getWidth()), Math.round(getHeight()));
//    }

//    public void closeDialog() {
//        remove();
//    }
//
//
//    private void setPathProvider(final String type, final TextField textField, final TextButtonItem btn, final String defaultPath) {
//        btn.addListener(new ClickListener() {
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//
//                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
//                fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
//                fileChooser.setMultiselectionEnabled(true);
//                fileChooser.setListener(new FileChooserAdapter() {
//                    @Override
//                    public void selected(Array<FileHandle> result) {
//                        ArrayList<File> files = new ArrayList<>();
//                        String fileNames = "";
//                        for (int i = 0; i < result.size; i++) {
//                            if (i > 0) fileNames = fileNames + ", ";
//                            fileNames = fileNames + result.get(i).name();
//                            files.add(result.get(i).file());
//                        }
//
//                        textField.setText(fileNames);
//
//                        paths.put(type, files);
//                    }
//
//
//                });
//                stage.addActor(fileChooser.fadeIn());
//            }
//        });
//    }

//    @Override
//    public void progressStarted() {
//
//    }
//
//    @Override
//    public void progressChanged(float value) {
//        value = Math.round(value);
//        progressBar.setValue(value);
//        progressLbl.setText(value + "%");
//    }

//    @Override
//    public void progressComplete() {
//        closeDialog();
//        Gdx.app.postRunnable(() -> {
//            projectManager.openProjectAndLoadAllData(projectManager.getCurrentProjectVO().projectName);
//            stage.getCompositePanel().initResolutionBox();
//            remove();
//            stage.loadCurrentProject();
//            stage.getSandbox().loadCurrentProject(stage.getSandbox().currentLoadedSceneFileName);
//        });
//    }
}
