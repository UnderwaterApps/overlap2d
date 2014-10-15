package com.uwsoft.editor.gdx.ui.dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.ProgressHandler;

public class CreateNewResolutionDialog extends CompositeDialog implements ProgressHandler {

    private TextField resName;
    private TextField resWidth;
    private TextField resHeight;

    private ProgressHandler progressHandler;

    private ProgressBar progressBar;
    private Label progressLbl;


    public CreateNewResolutionDialog(UIStage s) {
        super(s, "createNewResolutionDlg", 270, 230);

        progressHandler = this;

        setTitle("create new resolution");

        resName = ui.getTextBoxById("resName");
        resWidth = ui.getTextBoxById("resWidth");
        resHeight = ui.getTextBoxById("resHeight");

        final SelectBox<String> selectBox = ui.getSelectBoxById("resolutionBase");
        selectBox.setWidth(75);
        String[] strArray = {"width", "height"};
        selectBox.setItems(strArray);
        selectBox.setSelectedIndex(0);
        resWidth.setText(800 + "");
        resHeight.setText(480 + "");

        ui.getTextButtonById("createBtn").addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if(resName.getText().length() == 0) return;

                DataManager.getInstance().createNewResolution(resName.getText(), Integer.parseInt(resWidth.getText()), Integer.parseInt(resHeight.getText()), (String) selectBox.getSelected(), progressHandler);
            }
        });

        // adding progress bar

        progressBar = new ProgressBar(0, 100, 1, false, TextureManager.getInstance().editorSkin);
        progressBar.setWidth(getWidth() - 60);
        progressBar.setX(10);
        progressBar.setY(55);
        mainLayer.addActor(progressBar);

        progressLbl = new Label("0%", TextureManager.getInstance().editorSkin);
        progressLbl.setX(progressBar.getX() + progressBar.getWidth() + 4);
        progressLbl.setY(58);
        mainLayer.addActor(progressLbl);

    }

    @Override
    public void progressStarted() {
        // TODO Auto-generated method stub

    }

    @Override
    public void progressChanged(float value) {
        value = Math.round(value);
        progressBar.setValue(value);
        progressLbl.setText(value + "%");

    }

    @Override
    public void progressComplete() {

        stage.getCompositePanel().initResolutionBox();

        this.remove();
    }

}
