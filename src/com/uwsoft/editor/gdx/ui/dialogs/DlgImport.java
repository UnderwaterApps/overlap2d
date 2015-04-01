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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.ProgressHandler;
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.actor.TextButtonItem;
import com.uwsoft.editor.renderer.data.SceneVO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DlgImport extends CompositeDialog implements ProgressHandler {

    private final ProgressBar progressBar;
    private final Label progressLbl;
    private final TextBoxItem spriteAnimationPath;
    private final Overlap2DFacade facade;
    private final DataManager dataManager;
    private TextField imagesPath;
    private TextField particlePath;
    private TextField stylePath;
    private TextField fontPath;
    private TextField spinePath;
    private ProgressHandler progressHandler;

    private HashMap<String, ArrayList<File>> paths = new HashMap<>();

    public DlgImport(UIStage s) {
        super(s, "ImportDlg", 540, 250);
        progressHandler = this;
        setTitle("Import new Assets");

        imagesPath = ui.getTextBoxById("imagesPath");
        imagesPath.setDisabled(true);

        particlePath = ui.getTextBoxById("particlePath");
        particlePath.setDisabled(true);

        stylePath = ui.getTextBoxById("stylePath");
        stylePath.setDisabled(true);

        fontPath = ui.getTextBoxById("fontPath");
        fontPath.setDisabled(true);

        spinePath = ui.getTextBoxById("spinePath");
        spinePath.setDisabled(true);

        spriteAnimationPath = ui.getTextBoxById("spriteAnimationPath");
        spriteAnimationPath.setDisabled(true);

        setPathProvider("images", imagesPath, ui.getTextButtonById("imagesBtn"), "");
        setPathProvider("particles", particlePath, ui.getTextButtonById("particleBtn"), "");
        setPathProvider("styles", stylePath, ui.getTextButtonById("styleBtn"), "");
        setPathProvider("font", fontPath, ui.getTextButtonById("fontBtn"), "");
        setPathProvider("spine", spinePath, ui.getTextButtonById("spineBtn"), "");
        setPathProvider("spriteAnimation", spriteAnimationPath, ui.getTextButtonById("spriteAnimationBtn"), "");

        facade = Overlap2DFacade.getInstance();
        dataManager = facade.retrieveProxy(DataManager.NAME);
        ui.getTextButtonById("startBtn").addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (paths.get("images") != null) {
                    dataManager.importExternalImagesIntoProject(paths.get("images"), progressHandler);
                }
                if (paths.get("particles") != null) {
                    dataManager.importExternalParticlesIntoProject(paths.get("particles"), progressHandler);
                }
                if (paths.get("styles") != null) {
                    dataManager.importExternalStyleIntoProject(paths.get("styles").get(0), progressHandler);
                }
                if (paths.get("font") != null) {
                    dataManager.importExternalFontIntoProject(paths.get("font"), progressHandler);
                }

                if (paths.get("spine") != null) {
                    dataManager.importExternalSpineAnimationsIntoProject(paths.get("spine"), progressHandler);
                }

                if (paths.get("spriteAnimation") != null) {
                    dataManager.importExternalSpriteAnimationsIntoProject(paths.get("spriteAnimation"), progressHandler);
                }


                // save before importing
                SceneVO vo = stage.getSandbox().sceneVoFromItems();
                stage.getCompositePanel().updateOriginalItem();
                dataManager.sceneDataManager.saveScene(vo);
                dataManager.saveCurrentProject();
            }
        });


        // adding progress bar

        progressBar = new ProgressBar(0, 100, 1, false, dataManager.textureManager.editorSkin);
        progressBar.setWidth(getWidth() - 60);
        progressBar.setX(10);
        progressBar.setY(33);
        mainLayer.addActor(progressBar);

        progressLbl = new Label("0%", dataManager.textureManager.editorSkin);
        progressLbl.setX(progressBar.getX() + progressBar.getWidth() + 4);
        progressLbl.setY(38);
        mainLayer.addActor(progressLbl);
    }

    @Override
    public void show() {
        super.show();
        setBounds(Math.round(getX()), Math.round(getY()), Math.round(getWidth()), Math.round(getHeight()));
    }

    public void closeDialog() {
        remove();
    }


    private void setPathProvider(final String type, final TextField textField, final TextButtonItem btn, final String defaultPath) {
        btn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                fileChooser.setMultiselectionEnabled(true);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> result) {
                        ArrayList<File> files = new ArrayList<>();
                        String fileNames = "";
                        for (int i = 0; i < result.size; i++) {
                            if (i > 0) fileNames = fileNames + ", ";
                            fileNames = fileNames + result.get(i).name();
                            files.add(result.get(i).file());
                        }

                        textField.setText(fileNames);

                        paths.put(type, files);
                    }


                });
                stage.addActor(fileChooser.fadeIn());
            }
        });
    }

    @Override
    public void progressStarted() {

    }

    @Override
    public void progressChanged(float value) {
        value = Math.round(value);
        progressBar.setValue(value);
        progressLbl.setText(value + "%");
    }

    @Override
    public void progressComplete() {
        closeDialog();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                dataManager.openProjectAndLoadAllData(dataManager.getCurrentProjectVO().projectName);
                stage.getCompositePanel().initResolutionBox();
                remove();
                stage.loadCurrentProject();
                stage.getSandbox().loadCurrentProject(stage.getSandbox().currentLoadedSceneFileName);
            }
        });
    }
}
