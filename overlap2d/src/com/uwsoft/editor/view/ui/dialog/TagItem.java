package com.uwsoft.editor.view.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;

/**
 * Created by CyberJoe on 8/1/2015.
 */
public class TagItem extends VisTable {

    private VisTextButton tagLbl;

    private TagItemListener listener;

    public interface TagItemListener {
        public void removed(String tag);
    }

    public TagItem(String tag, TagItemListener listener) {
        this.listener = listener;
        tagLbl = new VisTextButton(tag, "tagBtn");
        VisImageButton closeBtn = new VisImageButton("close-panel");
        add(tagLbl).width(180);
        add(closeBtn).padLeft(5);

        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                TagItem.this.remove();
                listener.removed(tagLbl.getText().toString());
            }

        });
    }

    public String getTagName() {
        return tagLbl.getText().toString();
    }

}
