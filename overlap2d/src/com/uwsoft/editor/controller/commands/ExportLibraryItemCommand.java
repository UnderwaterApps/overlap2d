package com.uwsoft.editor.controller.commands;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.renderer.components.*;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Sasun Poghosyan on 11/28/2016.
 */
public class ExportLibraryItemCommand extends NonRevertibleCommand {
    private final Json json = new Json(JsonWriter.OutputType.json);

    public ExportLibraryItemCommand() {
        super();
    }

    @Override
    public void execute(Notification notification) {
        super.execute(notification);
    }

    @Override
    public void doAction() {
        getJsonStringFromEntities(sandbox.getSelector().getSelectedItems());
    }

    private void getJsonStringFromEntities(Set<Entity> entities) {
        CompositeVO holderComposite = new CompositeVO();
        for (Entity entity : entities) {
            int entityType = ComponentRetriever.get(entity, MainItemComponent.class).entityType;
            if (entityType == EntityFactory.COMPOSITE_TYPE) {
                CompositeItemVO vo = new CompositeItemVO();
                vo.loadFromEntity(entity);
                if (vo.itemName.length() == 0) {
                    System.out.println("item is not a library item");
                    return;
                }
                holderComposite.sComposites.add(vo);
                vo.cleanIds();
            } else {
                System.out.println("item is not a composite");
                return;
            }
        }

        for (int i = 0; i < holderComposite.sComposites.size(); i++) {
            CompositeItemVO compositeItemVO = holderComposite.sComposites.get(i);
            String jsonString = json.toJson(compositeItemVO);
            String currentProjectPath = projectManager.getCurrentProjectPath();

            try {
                FileUtils.writeStringToFile(new File(currentProjectPath + "\\export\\libraryItems\\" + compositeItemVO.itemName + ".json"), jsonString, "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
