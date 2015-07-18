package com.uwsoft.editor.view.ui;

import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.data.vo.SceneConfigVO;
import com.uwsoft.editor.proxy.ProjectManager;
import com.uwsoft.editor.proxy.SceneDataManager;

import com.uwsoft.editor.utils.Guide;

/**
 * Created by azakhary on 7/18/2015.
 */
public class RulersUIMediator extends SimpleMediator<RulersUI> {
    private static final String TAG = RulersUIMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    /**
     * Constructor.
     */
    public RulersUIMediator() {
        super(NAME, new RulersUI());
    }

    @Override
    public void onRegister() {
        facade = Overlap2DFacade.getInstance();
        viewComponent.setVisible(false);
    }


    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                SceneDataManager.SCENE_LOADED,
                RulersUI.ACTION_GUIDES_MODIFIED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        ProjectManager projectManager = Overlap2DFacade.getInstance().retrieveProxy(ProjectManager.NAME);
        SceneConfigVO sceneConfigVO = projectManager.getCurrentSceneConfigVO();

        switch (notification.getName()) {
            case SceneDataManager.SCENE_LOADED:
                Array<Guide> guides = new Array<>();
                for(int i  = 0; i < sceneConfigVO.verticalGuides.size(); i++) {
                    Guide tmp = new Guide(true);
                    tmp.pos = sceneConfigVO.verticalGuides.get(i);
                    guides.add(tmp);
                }
                for(int i  = 0; i < sceneConfigVO.horizontalGuides.size(); i++) {
                    Guide tmp = new Guide(false);
                    tmp.pos = sceneConfigVO.horizontalGuides.get(i);
                    guides.add(tmp);
                }

                viewComponent.setGuides(guides);

                viewComponent.setVisible(true);
                break;
            case RulersUI.ACTION_GUIDES_MODIFIED:
                guides = viewComponent.getGuides();
                sceneConfigVO.verticalGuides.clear();
                sceneConfigVO.horizontalGuides.clear();

                for(int i  = 0; i < guides.size; i++) {
                    if(guides.get(i).isVertical) {
                        sceneConfigVO.verticalGuides.add(guides.get(i).pos);
                    } else {
                        sceneConfigVO.horizontalGuides.add(guides.get(i).pos);
                    }
                }

                break;
        }
    }
}
