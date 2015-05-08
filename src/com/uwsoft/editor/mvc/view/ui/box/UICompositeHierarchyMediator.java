package com.uwsoft.editor.mvc.view.ui.box;

import java.util.Stack;

import com.badlogic.ashley.core.Entity;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.renderer.legacy.data.CompositeItemVO;

/**
 * Created by CyberJoe on 4/22/2015.
 */
public class UICompositeHierarchyMediator extends SimpleMediator<UICompositeHierarchy> {
    private static final String TAG = UICompositeHierarchyMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    // Stack of scenes in the current hierarchy
    private Stack<CompositeItemVO> scenes = new Stack<>();

    private Sandbox sandbox;

    public UICompositeHierarchyMediator() {
        super(NAME, new UICompositeHierarchy());
    }

    public String[] listNotificationInterests() {
        return new String[]{
                ProjectManager.PROJECT_OPENED,
                Overlap2D.OPENED_COMPOSITE,
                Overlap2D.OPENED_PREVIOUS_COMPOSITE,
                UICompositeHierarchy.SCENE_CHOOSEN
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        sandbox = Sandbox.getInstance();

        super.handleNotification(notification);

        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
                CompositeItemVO compositeItemVO = Sandbox.getInstance().sceneControl.getRootSceneVO();
                addScene("root", compositeItemVO);
                break;
            case Overlap2D.OPENED_COMPOSITE:
                compositeItemVO = notification.getBody();
                addScene("composite 1", compositeItemVO);
                break;
            case Overlap2D.OPENED_PREVIOUS_COMPOSITE:
                if(scenes.size() < 2) return;
                loadScene(scenes.get(scenes.size()-2));
                break;
            case UICompositeHierarchy.SCENE_CHOOSEN:
                compositeItemVO = notification.getBody();
                loadScene(compositeItemVO);
                break;
            default:
                break;
        }
    }

    private void addScene(String name, CompositeItemVO scene) {
        scenes.add(scene);
        viewComponent.addItem(name, scene);
    }

    private void loadScene(CompositeItemVO scene) {

        while(scenes.peek() != scene) {
        	//TODO fix and uncomment
            //updateOriginalItem(scenes.peek(), sandbox.getCurrentScene());
            viewComponent.removeLastItem();
            scenes.pop();
        }

        sandbox.getUIStage().loadScene(scene);
    }

    public void updateOriginalItem() {
    	//TODO fix and uncomment
        //updateOriginalItem(scenes.get(scenes.size() - 1), sandbox.sceneControl.getCurrentScene());
    }

    private void updateOriginalItem(CompositeItemVO updatableVo, Entity currItem) {
    	//TODO fix and uncomment
//        updatableVo.update(new CompositeItemVO(currItem.getDataVO().composite));
//
//        String libName = currItem.getDataVO().itemName;
//        CompositeItemVO libItem = sandbox.sceneControl.getCurrentSceneVO().libraryItems.get(libName);
//
//        if (libItem != null) {
//            libItem.update(currItem.getDataVO());
//
//
//            //TODO: update other items with same name
//            revursiveUpdateLibraryVO(libName, sandbox.sceneControl.getRootSceneVO(), currItem.getDataVO());
//        }
    }

    private void revursiveUpdateLibraryVO(String libName, CompositeItemVO initialVO, CompositeItemVO updatingWith) {
        for (int i = 0; i < initialVO.composite.sComposites.size(); i++) {
            if (initialVO.composite.sComposites.get(i).itemName.equals(libName)) {
                initialVO.composite.sComposites.get(i).update(updatingWith);
            } else {
                revursiveUpdateLibraryVO(libName, initialVO.composite.sComposites.get(i), updatingWith);
            }
        }
    }
}
