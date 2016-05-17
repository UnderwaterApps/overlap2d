package com.uwsoft.editor.controller.commands.resource;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.controller.commands.NonRevertibleCommand;
import com.uwsoft.editor.renderer.components.SpineDataComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.data.SpineVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Sasun Poghosyan on 5/10/2016.
 */
public class DeleteSpineAnimation extends NonRevertibleCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.resource.DeleteSpineAnimation";
    public static final String DONE = CLASS_NAME + "DONE";

    private final ArrayList<Entity> entityList = new ArrayList<>();
    private final ArrayList<SpineVO> tmpSpineAnimList = new ArrayList<>();

    @Override
    public void doAction() {
        String spineItemName = notification.getBody();
        if (projectManager.deleteSpineAnimation(spineItemName)) {
            deleteEntitiesWithParticleEffects(sandbox.getRootEntity(), spineItemName);
            deleteAllItemsSpineAnimations(spineItemName);
            projectManager.loadProjectData(projectManager.getCurrentProjectPath());
            sendNotification(DONE, spineItemName);
            SceneVO vo = sandbox.sceneVoFromItems();
            projectManager.saveCurrentProject(vo);
        } else {
            cancel();
        }
    }

    private void deleteAllItemsSpineAnimations(String spineAnimationName) {
        for (CompositeItemVO compositeItemVO : libraryItems.values()) {
            deleteAllSpineAnimationsOfItem(compositeItemVO, spineAnimationName);
        }
    }

    private void deleteAllSpineAnimationsOfItem(CompositeItemVO compositeItemVO, String spineAnimationName) {
        Consumer<CompositeItemVO> action = (rootItemVo) -> deleteCurrentItemSpineAnimations(rootItemVo, spineAnimationName);
        EntityUtils.applyActionRecursivelyOnLibraryItems(compositeItemVO, action);
    }

    private void deleteCurrentItemSpineAnimations(CompositeItemVO compositeItemVO, String spineAnimationName) {
        if (compositeItemVO.composite != null && compositeItemVO.composite.sSpineAnimations.size() != 0) {
            ArrayList<SpineVO> spineAnimations = compositeItemVO.composite.sSpineAnimations;
            tmpSpineAnimList.addAll(spineAnimations
                    .stream()
                    .filter(spineVO -> spineVO.animationName.equals(spineAnimationName))
                    .collect(Collectors.toList()));
            spineAnimations.removeAll(tmpSpineAnimList);
            tmpSpineAnimList.clear();
        }
    }

    private void deleteEntitiesWithParticleEffects(Entity rootEntity, String particleName) {
        entityList.clear();
        Consumer<Entity> action = (root) -> {
            SpineDataComponent spineDataComponent = ComponentRetriever.get(root, SpineDataComponent.class);
            if (spineDataComponent != null && spineDataComponent.animationName.equals(particleName)) {
                entityList.add(root);
            }
        };
        EntityUtils.applyActionRecursivelyOnEntities(rootEntity, action);
        EntityUtils.removeEntities(entityList);
    }
}


