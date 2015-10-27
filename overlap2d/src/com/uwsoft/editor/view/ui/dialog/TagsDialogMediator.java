package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.ashley.core.Entity;
import com.commons.MsgAPI;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.stage.UIStage;
import com.uwsoft.editor.view.ui.properties.panels.UIBasicItemProperties;

import java.util.Set;

/**
 * Created by azakhary on 8/1/2015.
 */
public class TagsDialogMediator extends SimpleMediator<TagsDialog> {
    private static final String TAG = TagsDialogMediator.class.getCanonicalName();
    private static final String NAME = TAG;

    private Entity observable = null;

    public TagsDialogMediator() {
        super(NAME, new TagsDialog());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
        viewComponent.setEmpty();
    }

    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                MsgAPI.ITEM_SELECTION_CHANGED,
                MsgAPI.EMPTY_SPACE_CLICKED,
                UIBasicItemProperties.TAGS_BUTTON_CLICKED,
                TagsDialog.LIST_CHANGED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        Sandbox sandbox = Sandbox.getInstance();
        UIStage uiStage = sandbox.getUIStage();

        switch (notification.getName()) {
            case UIBasicItemProperties.TAGS_BUTTON_CLICKED:
                viewComponent.show(uiStage);
                break;
            case MsgAPI.ITEM_SELECTION_CHANGED:
                Set<Entity> selection = notification.getBody();
                if(selection.size() == 1) {
                    setObservable(selection.iterator().next());
                }
                break;
            case MsgAPI.EMPTY_SPACE_CLICKED:
                setObservable(null);
                break;
            case TagsDialog.LIST_CHANGED:
                viewComponent.updateView();
                MainItemComponent mainItemComponent = observable.getComponent(MainItemComponent.class);
                mainItemComponent.tags = viewComponent.getTags();
                break;
        }
    }

    private void setObservable(Entity item) {
        observable = item;
        updateView();
    }

    private void updateView() {
        if(observable == null) {
            viewComponent.setEmpty();
        } else {
            MainItemComponent mainItemComponent = ComponentRetriever.get(observable, MainItemComponent.class);
            viewComponent.setTags(mainItemComponent.tags);
            viewComponent.updateView();
        }
    }
}
