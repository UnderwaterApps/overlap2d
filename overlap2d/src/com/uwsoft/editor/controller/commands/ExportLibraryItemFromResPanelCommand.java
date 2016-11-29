package com.uwsoft.editor.controller.commands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Sasun Poghosyan on 11/28/2016.
 */
public class ExportLibraryItemFromResPanelCommand extends NonRevertibleCommand {
    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.resource.ExportLibraryItemFromResPanelCommand";
    public static final String DONE = CLASS_NAME + "DONE";
    private final Json json = new Json(JsonWriter.OutputType.json);

    public ExportLibraryItemFromResPanelCommand() {
        super();
    }

    @Override
    public void execute(Notification notification) {
        super.execute(notification);
        String libraryItemName = notification.getBody();
        String currentProjectPath = projectManager.getCurrentProjectPath();

        ProjectManager projectManager = Overlap2DFacade.getInstance().retrieveProxy(ProjectManager.NAME);
        HashMap<String, CompositeItemVO> libraryItems = projectManager.currentProjectInfoVO.libraryItems;
        CompositeItemVO compositeItemVO = libraryItems.get(libraryItemName);

        String jsonString = json.toJson(compositeItemVO);
        json.prettyPrint(jsonString);

        try {
            FileUtils.writeStringToFile(new File(currentProjectPath + "\\export\\libraryItems\\" + libraryItemName + ".json"), jsonString, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doAction() {

    }

    @Override
    public void callDoAction() {
        super.callDoAction();
    }

    @Override
    public void cancel() {
        super.cancel();
    }
}
