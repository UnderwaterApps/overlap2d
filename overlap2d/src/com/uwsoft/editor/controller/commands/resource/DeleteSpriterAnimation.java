package com.uwsoft.editor.controller.commands.resource;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.controller.commands.NonRevertibleCommand;
import com.uwsoft.editor.renderer.components.spriter.SpriterComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.data.SpriterVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Sasun Poghosyan on 5/12/2016.
 */
public class DeleteSpriterAnimation extends NonRevertibleCommand {
    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.resource.DeleteSpriterAnimation";
    public static final String DONE = CLASS_NAME + "DONE";

    private final ArrayList<Entity> entityList = new ArrayList<>();
    private final ArrayList<SpriterVO> tmpSpriterAnimList = new ArrayList<>();

    @Override
    public void doAction() {
        String spriterAnimName = notification.getBody();
        if (projectManager.deleteSpriterAnimation(spriterAnimName)) {
            deleteEntitiesWithSpriterAnimation(sandbox.getRootEntity(), spriterAnimName);
            deleteAllItemsSpriterAnimations(spriterAnimName);
            projectManager.loadProjectData(projectManager.getCurrentProjectPath());
            facade.sendNotification(DONE, spriterAnimName);
            SceneVO vo = sandbox.sceneVoFromItems();
            projectManager.saveCurrentProject(vo);
        } else {
            cancel();
        }

    }

    private void deleteAllItemsSpriterAnimations(String spriterAnimationName) {
        for (CompositeItemVO compositeItemVO : libraryItems.values()) {
            deleteAllSpriterAnimationsOfItem(compositeItemVO, spriterAnimationName);
        }
    }

    private void deleteAllSpriterAnimationsOfItem(CompositeItemVO rootItemVo, String spriterAnimationName) {
        Consumer<CompositeItemVO> action = (currentItemVo) -> deleteCurrentItemSpriterAnimations(currentItemVo, spriterAnimationName);
        EntityUtils.applyActionRecursivelyOnLibraryItems(rootItemVo, action);
    }

    private void deleteCurrentItemSpriterAnimations(CompositeItemVO compositeItemVO, String spriterAnimationName) {
        if (compositeItemVO.composite != null && compositeItemVO.composite.sSpriterAnimations.size() != 0) {
            ArrayList<SpriterVO> spriterAnimations = compositeItemVO.composite.sSpriterAnimations;
            tmpSpriterAnimList.addAll(spriterAnimations
                    .stream()
                    .filter(spriterVO -> spriterVO.animationName.equals(spriterAnimationName))
                    .collect(Collectors.toList()));

            spriterAnimations.removeAll(tmpSpriterAnimList);
            tmpSpriterAnimList.clear();
        }
    }

    private void deleteEntitiesWithSpriterAnimation(Entity rootEntity, String spriterAnimationName) {
        entityList.clear();
        Consumer<Entity> action = (root) -> {
            SpriterComponent spriterAnimationComponent = ComponentRetriever.get(root, SpriterComponent.class);
            if (spriterAnimationComponent != null && spriterAnimationComponent.animationName.equals(spriterAnimationName)) {
                entityList.add(root);
            }
        };
        EntityUtils.applyActionRecursivelyOnEntities(rootEntity, action);
        EntityUtils.removeEntities(entityList);
    }
}
