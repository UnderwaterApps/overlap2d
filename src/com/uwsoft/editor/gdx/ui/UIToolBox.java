package com.uwsoft.editor.gdx.ui;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.gdx.stage.UIStage;

public class UIToolBox extends UIBox {

    private ButtonGroup btnGroup;

    private int maxRows = 1;
    private int maxCols = 6;

    private int currRow = 0;
    private int currCol = 0;

    public UIToolBox(UIStage s) {
        super(s, 160, 50);

        btnGroup = new ButtonGroup();
        btnGroup.setMaxCheckCount(1);
        btnGroup.setMinCheckCount(1);

        Button mainIcon = addButton("mainIcon", true);
        Button resizeIcon = addButton("resizeIcon", true);
        //
        Button topIcon = addButton("alignIconL", false);
        topIcon.setTransform(true);
        topIcon.setRotation(-90);
        Button leftIcon = addButton("alignIconL", false);
        leftIcon.setTransform(true);
        Button bottomIcon = addButton("alignIconL", false);
        bottomIcon.setTransform(true);
        bottomIcon.setRotation(90);
        Button rightIcon = addButton("alignIconL", false);
        rightIcon.setTransform(true);
        rightIcon.setRotation(180);

        topIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.sandboxStage.alignSelections(Align.top);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        leftIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.sandboxStage.alignSelections(Align.left);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        bottomIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.sandboxStage.alignSelections(Align.bottom);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        rightIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.sandboxStage.alignSelections(Align.right);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        mainIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.sandboxStage.setCurrentMode(SandboxStage.MODE_SELECT);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        resizeIcon.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.sandboxStage.setCurrentMode(SandboxStage.MODE_TRANSFORM);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private Button addButton(String img, boolean isCheckButton) {
        ButtonStyle btnStl = new ButtonStyle();
        btnStl.up = new TextureRegionDrawable(stage.textureManager.getEditorAsset(img));
        btnStl.down = new TextureRegionDrawable(stage.textureManager.getEditorAsset(img + "Checked"));


        Button btn = new Button(btnStl);

        btn.setX(10 + currCol * (btn.getWidth() + 5));
        btn.setY(getHeight() - (currRow + 1) * (btn.getHeight() + 3) - 17);
        btn.setOrigin(btn.getWidth() / 2, btn.getHeight() / 2);

        currCol++;
        if (currCol > maxCols) {
            currCol = 0;
            currRow++;
        }

        if (currRow > maxRows) return null;
        if (isCheckButton) {
            btnStl.checked = new TextureRegionDrawable(stage.textureManager.getEditorAsset(img + "Checked"));
            btnGroup.add(btn);
        }
        addActor(btn);

        return btn;
    }

}
