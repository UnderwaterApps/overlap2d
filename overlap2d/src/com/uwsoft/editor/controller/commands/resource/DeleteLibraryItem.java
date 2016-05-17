package com.uwsoft.editor.controller.commands.resource;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.controller.commands.NonRevertibleCommand;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

import java.util.HashMap;

/**
 * Created by azakhary on 11/29/2015.
 */
public class DeleteLibraryItem extends NonRevertibleCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.resource.DeleteLibraryItem";
    public static final String DONE = CLASS_NAME + "DONE";

    @Override
    public void doAction() {
        String libraryItemName = notification.getBody();

        ProjectManager projectManager = Overlap2DFacade.getInstance().retrieveProxy(ProjectManager.NAME);
        HashMap<String, CompositeItemVO> libraryItems = projectManager.currentProjectInfoVO.libraryItems;

        libraryItems.remove(libraryItemName);

        Array<Entity> linkedEntities = EntityUtils.getByLibraryLink(libraryItemName);
        for (Entity entity : linkedEntities) {
            MainItemComponent mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
            mainItemComponent.libraryLink = "";
        }
        facade.sendNotification(DONE, libraryItemName);
    }
}
