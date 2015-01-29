package com.uwsoft.editor.gdx.ui.dialogs;

import java.io.File;
import java.util.HashMap;

import javax.swing.JFileChooser;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.controlles.FileChooserHandler;
import com.uwsoft.editor.controlles.NameConstants;
import com.uwsoft.editor.controlles.UIController;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.renderer.actor.TextBoxItem;
import com.uwsoft.editor.renderer.actor.TextButtonItem;

/**
 * Created by sargis on 7/31/14.
 */
public class DlgExport extends CompositeDialog {

    private final TextBoxItem projectAssets;

    private HashMap<String, File> paths = new HashMap<>();

    public DlgExport(UIStage s) {
        super(s, "exportSettingsDlg", 480, 160);

        setTitle("Export settings");

        ProjectVO projectVO = DataManager.getInstance().getCurrentProjectVO();

        projectAssets = ui.getTextBoxById("projectAssetsPath");
        projectAssets.setDisabled(true);
        projectAssets.setText(projectVO.projectMainExportPath);
        paths.put("global", new File(projectVO.projectMainExportPath));

        /*
        sceneFilesPath = ui.getTextBoxById("sceneFilesPath");
        sceneFilesPath.setDisabled(true);
        sceneFilesPath.setText(projectVO.projectSceneExportPath);

        atlasPath = ui.getTextBoxById("atlasPath");
        atlasPath.setDisabled(true);
        atlasPath.setText(projectVO.projectAtlasExportPath);

        effectsPath = ui.getTextBoxById("effectsPath");
        effectsPath.setDisabled(true);
        effectsPath.setText(projectVO.projectEffectsExportPath);

        spineAnimationsPath = ui.getTextBoxById("spineAnimationsPath");
        spineAnimationsPath.setDisabled(true);
        spineAnimationsPath.setText(projectVO.projectSpineExportPath);
        
        fontsPath = ui.getTextBoxById("fontsPath");
        fontsPath.setDisabled(true);
        fontsPath.setText(projectVO.projectFontsExportPath);



        setPathProvider("scene", sceneFilesPath, ui.getTextButtonById("sceneFilesBtn"), "");
        setPathProvider("atlas", atlasPath, ui.getTextButtonById("atlasBtn"), "");
        setPathProvider("effects", effectsPath, ui.getTextButtonById("effectsBtn"), "");
        setPathProvider("spine", spineAnimationsPath, ui.getTextButtonById("spineAnimationsBtn"), "");
        setPathProvider("fonts", fontsPath, ui.getTextButtonById("fontsBtn"), "");
        */
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
                DataManager.getInstance().buildProject();
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
        DataManager.getInstance().setExportPaths(paths.get("global"));
        DataManager.getInstance().saveCurrentProject();

    }

    private void setPathProvider(final String type, final TextField textField, final TextButtonItem btn, final String defaultPath) {
        btn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                FileChooserHandler chooseHandler = new FileChooserHandler() {
                    @Override
                    public void FileChoosen(JFileChooser jfc) {
                        File selectedFile = jfc.getSelectedFile();
                        if (selectedFile == null || !selectedFile.isDirectory()) {
                            return;
                        }

                        textField.setText(selectedFile.getPath());
                        if(type.equals("global")) {
                            setMiniPaths();
                        }
                        paths.put(type, selectedFile);
                    }

                    @Override
                    public boolean isMultiple() {
                        return false;
                    }

                    @Override
                    public String getDefaultPath() {
                        return defaultPath;
                    }

                    @Override
                    public int getFileSelectionMode() {
                        return JFileChooser.DIRECTORIES_ONLY;
                    }
                };

                UIController.instance.sendNotification(NameConstants.SHOW_FILE_CHOOSER, chooseHandler);
            }
        });
    }
}
