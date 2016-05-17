package com.uwsoft.editor.controller.commands.resource;

import com.badlogic.ashley.core.Entity;
import com.uwsoft.editor.controller.commands.NonRevertibleCommand;
import com.uwsoft.editor.renderer.components.particle.ParticleComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by Sasun Poghosyan on 5/10/2016.
 */
public class DeleteParticleEffect extends NonRevertibleCommand {

    private static final String CLASS_NAME = "com.uwsoft.editor.controller.commands.resource.DeleteParticleEffect";
    public static final String DONE = CLASS_NAME + "DONE";

    private final ArrayList<Entity> entityList = new ArrayList<>();
    private final ArrayList<ParticleEffectVO> tmpParticleEffectList = new ArrayList<>();

    @Override
    public void doAction() {
        String particleName = notification.getBody();
        if (projectManager.deleteParticle(particleName)) {
            deleteEntitiesWithParticleEffects(sandbox.getRootEntity(), particleName); // delete entities from scene
            deleteAllItemsWithParticleName(particleName);
            projectManager.loadProjectData(projectManager.getCurrentProjectPath());
            sendNotification(DONE, particleName);
            SceneVO vo = sandbox.sceneVoFromItems();
            projectManager.saveCurrentProject(vo);
        } else {
            cancel();
        }
    }

    private void deleteAllItemsWithParticleName(String name) {
        for (CompositeItemVO compositeItemVO : libraryItems.values()) {
            deleteAllParticles(compositeItemVO, name);
        }
    }

    private void deleteAllParticles(CompositeItemVO compositeItemVO, String name) {
        Consumer<CompositeItemVO> action = (rootItemVo) -> getParticles(rootItemVo, name);
        EntityUtils.applyActionRecursivelyOnLibraryItems(compositeItemVO, action);
    }

    private void getParticles(CompositeItemVO compositeItemVO, String name) {
        if (compositeItemVO.composite != null && compositeItemVO.composite.sParticleEffects.size() != 0) {
            ArrayList<ParticleEffectVO> particleEffectList = compositeItemVO.composite.sParticleEffects;
            for (ParticleEffectVO particleEffectVO : particleEffectList) {
                if (particleEffectVO.particleName.equals(name)) {
                    tmpParticleEffectList.add(particleEffectVO);
                }
            }
            particleEffectList.removeAll(tmpParticleEffectList);
            tmpParticleEffectList.clear();
        }
    }

    private void deleteEntitiesWithParticleEffects(Entity rootEntity, String particleName) {
        entityList.clear();
        Consumer<Entity> action = (root) -> {
            ParticleComponent particleComponent = ComponentRetriever.get(root, ParticleComponent.class);
            if (particleComponent != null && particleComponent.particleName.equals(particleName)) {
                entityList.add(root);
            }
        };
        EntityUtils.applyActionRecursivelyOnEntities(rootEntity, action);
        EntityUtils.removeEntities(entityList);
    }
}
