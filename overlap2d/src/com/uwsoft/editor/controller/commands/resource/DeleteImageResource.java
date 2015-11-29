package com.uwsoft.editor.controller.commands.resource;

import com.badlogic.gdx.files.FileHandle;
import com.uwsoft.editor.controller.commands.NonRevertableCommand;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.proxy.ResolutionManager;
import com.uwsoft.editor.proxy.ResourceManager;

/**
 * Created by azakhary on 11/29/2015.
 */
public class DeleteImageResource extends NonRevertableCommand {


    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.resource.DeleteImageResource";
    public static final String DONE = CLASS_NAME + "DONE";

    @Override
    public void doAction() {
        String imageName = notification.getBody();
        ProjectManager projectManager = facade.retrieveProxy(ProjectManager.NAME);

        if(projectManager.deleteImage(imageName)) {
            ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
            resolutionManager.rePackProjectImagesForAllResolutions();
            String sceneName = sandbox.sceneControl.getCurrentSceneVO().sceneName;
            sandbox.loadCurrentProject(sceneName);

            projectManager.reLoadProjectAssets();

            sendNotification(DONE, imageName);
        } else {
            cancel();
        }
    }
}
