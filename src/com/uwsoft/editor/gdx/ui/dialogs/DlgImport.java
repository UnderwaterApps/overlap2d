package com.uwsoft.editor.gdx.ui.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.controlles.FileChooserHandler;
import com.uwsoft.editor.controlles.NameConstants;
import com.uwsoft.editor.controlles.UIController;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.ProgressHandler;
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.actor.TextButtonItem;
import com.uwsoft.editor.renderer.data.SceneVO;

public class DlgImport extends CompositeDialog implements ProgressHandler {

    private final ProgressBar progressBar;
    private final Label progressLbl;
    private final TextBoxItem spriteAnimationPath;
    private TextField imagesPath;
    private TextField particlePath;
    private TextField stylePath;
    private TextField fontPath;
    private TextField spinePath;
    private ProgressHandler progressHandler;

    private HashMap<String, ArrayList<File>> paths = new HashMap<String, ArrayList<File>>();

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


        ui.getTextButtonById("startBtn").addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (paths.get("images") != null) {
                    DataManager.getInstance().importExternalImagesIntoProject(paths.get("images"), progressHandler);
                }
                if (paths.get("particles") != null) {
                    DataManager.getInstance().importExternalParticlesIntoProject(paths.get("particles"), progressHandler);
                }
                if (paths.get("styles") != null) {
                	DataManager.getInstance().importExternalStyleIntoProject(paths.get("styles").get(0), progressHandler);
                }
                if (paths.get("font") != null) {
                    DataManager.getInstance().importExternalFontIntoProject(paths.get("font"), progressHandler);
                }

                if (paths.get("spine") != null) {
                    DataManager.getInstance().importExternalSpineAnimationsIntoProject(paths.get("spine"), progressHandler);
                }

                if (paths.get("spriteAnimation") != null) {
                    DataManager.getInstance().importExternalSpriteAnimationsIntoProject(paths.get("spriteAnimation"), progressHandler);
                }


                // save before importing
                SceneVO vo = stage.sandboxStage.sceneVoFromItems();
                stage.getCompositePanel().updateOriginalItem();
                DataManager.getInstance().saveScene(vo);
                DataManager.getInstance().saveCurrentProject();
            }
        });


        // adding progress bar

        progressBar = new ProgressBar(0, 100, 1, false, TextureManager.getInstance().editorSkin);
        progressBar.setWidth(getWidth() - 60);
        progressBar.setX(10);
        progressBar.setY(33);
        mainLayer.addActor(progressBar);

        progressLbl = new Label("0%", TextureManager.getInstance().editorSkin);
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

                FileChooserHandler chooseHandler = new FileChooserHandler() {
                    @Override
                    public void FileChoosen(JFileChooser jfc) {
                        File[] resultHolderCopy = jfc.getSelectedFiles();
                        ArrayList<File> result = new ArrayList<>();

                        String fileNames = "";
                        for (int i = 0; i < resultHolderCopy.length; i++) {
                            if (i > 0) fileNames = fileNames + ", ";
                            fileNames = fileNames + resultHolderCopy[i].getName();
                            result.add(resultHolderCopy[i]);
                        }

                        textField.setText(fileNames);

                        paths.put(type, result);
                    }

                    @Override
                    public boolean isMultiple() {
                        return true;
                    }

                    @Override
                    public String getDefaultPath() {
                        return defaultPath;
                    }

                    @Override
                    public int getFileSelectionMode() {
                        return JFileChooser.FILES_ONLY;
                    }
                };

                UIController.instance.sendNotification(NameConstants.SHOW_FILE_CHOOSER, chooseHandler);
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
                DataManager.getInstance().openProjectAndLoadAllData(DataManager.getInstance().getCurrentProjectVO().projectName);
                stage.getCompositePanel().initResolutionBox();
                remove();
                stage.loadCurrentProject();
                stage.sandboxStage.loadCurrentProject(stage.sandboxStage.currentLoadedSceneFileName);
            }
        });
    }
}
