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

package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.utils.ImportUtils;
import com.uwsoft.editor.view.frame.FileDropListener;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.widget.ProgressHandler;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.view.menu.Overlap2DMenuBar;
import com.uwsoft.editor.view.stage.UIStage;
import com.uwsoft.editor.renderer.data.SceneVO;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

/**
 * Created by sargis on 4/3/15.
 */
public class ImportDialogMediator extends SimpleMediator<ImportDialog> {
    private static final String TAG = ImportDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;
    private AssetsImportProgressHandler progressHandler;

    private int importType;
    private String[] paths;

    public ImportDialogMediator() {
        super(NAME, new ImportDialog());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
        progressHandler = new AssetsImportProgressHandler();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                Overlap2DMenuBar.IMPORT_TO_LIBRARY,
                ImportDialog.BROWSE_BTN_CLICKED,
                ImportDialog.CANCEL_BTN_CLICKED,
                ImportDialog.IMPORT_BTN_CLICKED,
                FileDropListener.ACTION_DRAG_ENTER,
                FileDropListener.ACTION_DRAG_OVER,
                FileDropListener.ACTION_DRAG_EXIT,
                FileDropListener.ACTION_DROP,
        };
    }

    public Vector2 getLocationFromDtde(DropTargetDragEvent dtde) {
        Vector2 pos = new Vector2((float)(dtde).getLocation().getX(),(float)(dtde).getLocation().getY());

        return pos;
    }

    public Vector2 getLocationFromDropEvent(DropTargetDropEvent dtde) {
        Vector2 pos = new Vector2((float)(dtde).getLocation().getX(),(float)(dtde).getLocation().getY());

        return pos;
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);
        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();
        switch (notification.getName()) {
            case Overlap2DMenuBar.IMPORT_TO_LIBRARY:
                viewComponent.show(uiStage);
                break;
            case ImportDialog.BROWSE_BTN_CLICKED:
                showFileChoose();
                break;
            case FileDropListener.ACTION_DRAG_ENTER:
                Vector2 dropPos = getLocationFromDtde(notification.getBody());
                if(viewComponent.checkDropRegionHit(dropPos)) {
                    viewComponent.dragOver();
                }
                break;
            case FileDropListener.ACTION_DRAG_OVER:
                dropPos = getLocationFromDtde(notification.getBody());
                if(viewComponent.checkDropRegionHit(dropPos)) {
                    viewComponent.dragOver();
                }
                break;
            case FileDropListener.ACTION_DRAG_EXIT:
                dropPos = getLocationFromDtde(notification.getBody());
                if(viewComponent.checkDropRegionHit(dropPos)) {
                    viewComponent.dragExit();
                }
                break;
            case FileDropListener.ACTION_DROP:
                dropPos = getLocationFromDropEvent(notification.getBody());
                if(viewComponent.checkDropRegionHit(dropPos)) {
                    DropTargetDropEvent dtde = notification.getBody();
                    String[] paths = catchFiles(dtde);
                    postPathObtainAction(paths);
                }
                break;
            case ImportDialog.CANCEL_BTN_CLICKED:
                viewComponent.setDroppingView();
                break;
            case ImportDialog.IMPORT_BTN_CLICKED:
                startImport();
                break;
        }
    }

    private void postPathObtainAction(String[] paths) {
        int type = ImportUtils.getImportType(paths);

        if (type <= 0) {
            // error
            viewComponent.showError(type);
        } else {
            boolean isMultiple = false;
            if (paths.length > 1) isMultiple = true;
            if (type == ImportUtils.TYPE_ANIMATION_PNG_SEQUENCE) isMultiple = false;
            viewComponent.setImportingView(type, isMultiple);

            this.paths = paths;
            this.importType = type;

            startImport();
        }
    }

    private void showFileChoose() {
         Sandbox sandbox = Sandbox.getInstance();
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);

        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                String paths[] = new String[files.size];
                for(int i = 0; i < files.size; i++) {
                    paths[i] = files.get(i).path();
                }
                if(paths.length > 0) {
                    postPathObtainAction(paths);
                }
            }
        });
        sandbox.getUIStage().addActor(fileChooser.fadeIn());
    }

    private void startImport() {
        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);

        Array<FileHandle> files = getFilesFromPaths(this.paths);

        switch (importType) {
            case ImportUtils.TYPE_IMAGE:
                projectManager.importImagesIntoProject(files, progressHandler);
                break;
            case ImportUtils.TYPE_TEXTURE_ATLAS:
                projectManager.importAtlasesIntoProject(files, progressHandler);
                break;
            case ImportUtils.TYPE_PARTICLE_EFFECT:
                projectManager.importParticlesIntoProject(files, progressHandler);
                break;
            case ImportUtils.TYPE_SPRITER_ANIMATION:
                projectManager.importSpineAnimationsIntoProject(files, progressHandler);
                break;
            case ImportUtils.TYPE_SPINE_ANIMATION:
                projectManager.importSpineAnimationsIntoProject(files, progressHandler);
                break;
            case ImportUtils.TYPE_SPRITE_ANIMATION_ATLAS:
                projectManager.importSpriteAnimationsIntoProject(files, progressHandler);
                break;
            case ImportUtils.TYPE_ANIMATION_PNG_SEQUENCE:
                projectManager.importSpriteAnimationsIntoProject(files, progressHandler);
                break;
            case ImportUtils.TYPE_SHADER:
                projectManager.importShaderIntoProject(files, progressHandler);
                break;
        }

        // save before importing
        SceneVO vo = Sandbox.getInstance().sceneVoFromItems();
        projectManager.saveCurrentProject(vo);
    }

    private  Array<FileHandle> getFilesFromPaths(String[] paths) {
        Array<FileHandle> files = new Array<>();
        for(int i = 0; i < paths.length;i++) {
            files.add(new FileHandle(new File(paths[i])));
        }

        return files;
    }

    public String[] catchFiles(DropTargetDropEvent dtde) {
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

        Transferable t= dtde.getTransferable();
        if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            try {
                List<File> list = (List<File>)dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                String[] paths = new String[list.size()];
                for(int i = 0; i < list.size(); i++) {
                    paths[i] = list.get(i).getAbsolutePath();
                }
                return paths;
            }
            catch (Exception ufe) {
            }
        }

        return null;
    }

    private class AssetsImportProgressHandler implements ProgressHandler {

        @Override
        public void progressStarted() {

        }

        @Override
        public void progressChanged(float value) {

        }

        @Override
        public void progressComplete() {
            Gdx.app.postRunnable(() -> {
                Sandbox sandbox = Sandbox.getInstance();
                ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);
                projectManager.openProjectAndLoadAllData(projectManager.getCurrentProjectPath());
                sandbox.loadCurrentProject();
                ImportDialogMediator.this.viewComponent.setDroppingView();
                facade.sendNotification(ProjectManager.PROJECT_DATA_UPDATED);
            });
        }
    }
}
