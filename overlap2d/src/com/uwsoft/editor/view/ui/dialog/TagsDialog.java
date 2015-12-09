package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.commons.UIDraggablePanel;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.utils.StandardWidgetsFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by azakhary on 8/1/2015.
 */
public class TagsDialog extends UIDraggablePanel {

    public static final String prefix = "com.uwsoft.editor.view.ui.dialog.panels.TagsDialog";
    public static final String LIST_CHANGED = prefix + ".LIST_CHANGED";

    private Overlap2DFacade facade;

    private VisTable mainTable;
    private VisTable tagTable;
    private TagItem.TagItemListener tagItemListener;

    private Set<String> tags = new HashSet<>();

    public TagsDialog() {
        super("Tags");
        addCloseButton();

        facade = Overlap2DFacade.getInstance();

        mainTable = new VisTable();

        add(mainTable).padBottom(4);

        tagItemListener = new TagItem.TagItemListener() {
            @Override
            public void removed(String tag) {
                tags.remove(tag);
                facade.sendNotification(LIST_CHANGED);
            }
        };
    }

    public void setEmpty() {
        mainTable.clear();
        VisLabel label = StandardWidgetsFactory.createLabel("No item selected");
        label.setAlignment(Align.center);
        mainTable.add(label).pad(10).width(278).center();
        invalidateHeight();
    }

    private void addTag(String tag) {
        tags.add(tag);
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void updateView() {
        mainTable.clear();

        tagTable = new VisTable();
        VisTable inputTable = new VisTable();

        for(String tag: tags) {
            tagTable.add(new TagItem(tag, tagItemListener)).pad(5).left().expandX().fillX();
            tagTable.row();
        }

        VisTextField newTagField = StandardWidgetsFactory.createTextField();
        VisTextButton createTagBtn = new VisTextButton("add");
        inputTable.add(newTagField).width(200);
        inputTable.add(createTagBtn).padLeft(5);

        createTagBtn.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                String tag = newTagField.getText();
                if(!tagExists(tag)) {
                    newTagField.setText("");
                    addTag(tag);
                    facade.sendNotification(LIST_CHANGED);
                }
            }
        });

        mainTable.add(tagTable).expandX().fillX();
        mainTable.row();
        mainTable.add(inputTable);

        invalidateHeight();
    }

    public Set<String> getTags() {
        return tags;
    }

    private boolean tagExists(String tag) {
        return tags.contains(tag);
    }
}
