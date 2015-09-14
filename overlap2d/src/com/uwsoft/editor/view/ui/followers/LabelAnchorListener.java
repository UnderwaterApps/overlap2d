package com.uwsoft.editor.view.ui.followers;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * Created by CyberJoe on 6/15/2015.
 */
public class LabelAnchorListener extends AnchorListener {

    protected FollowerTransformationListener listenerTransform = new EmptyTransformationListener();
    protected FollowerTransformationListener listenerResize = new EmptyTransformationListener();

    public LabelAnchorListener(NormalSelectionFollower follower, FollowerTransformationListener listenerResize, int anchorId) {
        super(follower, anchorId);
        this.listenerResize = listenerResize;
    }

    public void setListenerTransform(FollowerTransformationListener listener) {
        listenerTransform = listenerTransform;
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.normal) {
            listenerResize.anchorDown(follower, anchorId, event.getStageX(), event.getStageY());
        }
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.transform) {
            if(listenerTransform != null) listenerTransform.anchorDown(follower, anchorId, event.getStageX(), event.getStageY());
        }
        return true;
    }
    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.normal) {
            listenerResize.anchorDragged(follower, anchorId, event.getStageX(), event.getStageY());
        }
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.transform) {
            if(listenerTransform != null) listenerTransform.anchorDragged(follower, anchorId, event.getStageX(), event.getStageY());
        }
    }
    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.normal) {
            listenerResize.anchorUp(follower, anchorId, event.getStageX(), event.getStageY());
        }
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.transform) {
            if(listenerTransform != null) listenerTransform.anchorUp(follower, anchorId, event.getStageX(), event.getStageY());
        }
    }
    @Override
    public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
        super.enter(event, x, y, pointer, fromActor);
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.normal) {
            listenerResize.anchorMouseEnter(follower, anchorId, event.getStageX(), event.getStageY());
        }
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.transform) {
            if(listenerTransform != null) listenerTransform.anchorMouseEnter(follower, anchorId, event.getStageX(), event.getStageY());
        }
    }
    @Override
    public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
        super.exit(event, x, y, pointer, toActor);
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.normal) {
            listenerResize.anchorMouseExit(follower, anchorId, event.getStageX(), event.getStageY());
        }
        if(follower.getMode() == NormalSelectionFollower.SelectionMode.transform) {
            if(listenerTransform != null) listenerTransform.anchorMouseExit(follower, anchorId, event.getStageX(), event.getStageY());
        }
    }
}
