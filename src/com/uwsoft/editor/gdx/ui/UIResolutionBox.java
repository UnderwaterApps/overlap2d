package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.dialogs.ConfirmDialog;
import com.uwsoft.editor.gdx.ui.dialogs.ConfirmDialog.ConfirmDialogListener;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;

public class UIResolutionBox extends Group {

    private final String curResolution;

    private UIStage stage;

    private SelectBox<String> dropdown;

    private ProjectInfoVO projectInfoVO;

    public UIResolutionBox(UIStage s, ProjectInfoVO prjVo, String curResolution) {

        this.stage = s;

        this.projectInfoVO = prjVo;

        this.curResolution = curResolution;

        int padding = 5;

        String[] arr = new String[projectInfoVO.resolutions.size() + 1];

        arr[0] = projectInfoVO.originalResolution.toString();
        int selectedIndex = 0;
        for (int i = 0; i < projectInfoVO.resolutions.size(); i++) {
            ResolutionEntryVO resolution = projectInfoVO.resolutions.get(i);
            String resolutionString = projectInfoVO.resolutions.get(i).toString();
            arr[i + 1] = resolutionString;
            if (resolution.name.equals(curResolution)) {
                selectedIndex = i + 1;
            }
        }

        dropdown = new SelectBox(stage.textureManager.editorSkin);
        dropdown.setItems(arr);
        dropdown.setSelectedIndex(selectedIndex);
        dropdown.setWidth(150);
        addActor(dropdown);

        dropdown.setX(0);
        dropdown.setY(6);

        dropdown.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadCurrentResolution();

            }
        });

        TextButton delBtn = new TextButton("Delete", stage.textureManager.editorSkin);
        delBtn.setX(dropdown.getX() + dropdown.getWidth() + padding);
        delBtn.setY(8);
        addActor(delBtn);

        TextButton createBtn = new TextButton("Create New", stage.textureManager.editorSkin);
        createBtn.setX(delBtn.getX() + delBtn.getWidth() + padding);
        createBtn.setY(8);
        addActor(createBtn);

        TextButton repackBtn = new TextButton("Repack", stage.textureManager.editorSkin);
        repackBtn.setX(createBtn.getX() + createBtn.getWidth() + padding);
        repackBtn.setY(8);
        addActor(repackBtn);

//        openBtn.addListener(new ClickListener() {
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//
//                String res = "orig";
//
//                final int index = dropdown.getSelectedIndex();
//                if (index > 0) {
//                    res = projectInfoVO.resolutions.get(index - 1).name;
//                }
//                String name = stage.sandboxStage.getCurrentSceneVO().sceneName;
//                DataManager.getInstance().openProjectAndLoadAllData(DataManager.getInstance().getCurrentProjectVO().projectName, res);
//                stage.sandboxStage.loadCurrentProject(name);
//                stage.loadCurrentProject();
//
//            }
//        });

        createBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                stage.showCreateNewResolutionDialog();

            }
        });

        delBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                final int index = dropdown.getSelectedIndex();
                if (index == 0) {
                    return;
                }

                ResolutionEntryVO resEntry = projectInfoVO.resolutions.get(index - 1);

                ConfirmDialog dlg = stage.showConfirmDialog();
                dlg.setDescription("Are you sure you want to delete resolution: " + resEntry.toString() + " ?");

                dlg.setListener(new ConfirmDialogListener() {

                    @Override
                    public void onConfirm() {
                        DataManager.getInstance().deleteResolution(index - 1);
                        stage.getCompositePanel().initResolutionBox();
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });

        repackBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                DataManager.getInstance().rePackProjectImagesForAllResolutions();
                loadCurrentResolution();
            }
        });

        setWidth(340);
    }

    private void loadCurrentResolution() {
        String res = "orig";
        final int index = dropdown.getSelectedIndex();
        if (index > 0) {
            res = projectInfoVO.resolutions.get(index - 1).name;
        }
        String name = stage.sandboxStage.getCurrentSceneVO().sceneName;
        DataManager.getInstance().openProjectAndLoadAllData(DataManager.getInstance().getCurrentProjectVO().projectName, res);
        stage.sandboxStage.loadCurrentProject(name);
        stage.loadCurrentProject();
    }
}
