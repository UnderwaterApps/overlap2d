package com.uwsoft.editor.gdx.stage;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.uwsoft.editor.controlles.flow.FlowManager;
import com.uwsoft.editor.data.TypeConstants;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.sandbox.SandboxFrontUI;
import com.uwsoft.editor.gdx.ui.SandboxUI;
import com.uwsoft.editor.gdx.ui.SelectionActions;
import com.uwsoft.editor.gdx.ui.dialogs.ConfirmDialog;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.actor.ParticleItem;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

import java.util.ArrayList;

public class SandboxStage extends BaseStage implements TypeConstants {
    public static SandboxStage instance;

    public UIStage uiStage;
    public SandboxUI ui;
    public SandboxFrontUI frontUI;

    public FlowManager flow;

    public PixelRect selectionRec;
    private FPSLogger fpsLogger;


    public Group mainBox = new Group();


    public Sandbox sandbox;

    public SandboxStage() {
        super();

        instance = this;

        // remove this
        rm = essentials.rm;

        physiscStopped = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void setUIStage(UIStage uiStage) {
        this.uiStage = uiStage;
    }

    private void initData(String sceneName) {
        sandbox.initData(sceneName);
        flow = new FlowManager(sandbox.sceneControl.getRootSceneVO());
    }

    private void initView() {
        if (mainBox != null) mainBox.clear();
        clear();
        getCamera().position.set(0, 0, 0);

        frontUI = new SandboxFrontUI();

        ui = new SandboxUI(this);
        addActor(ui);

        selectionRec = new PixelRect(textureManager, 0, 0);
        selectionRec.setFillColor(new Color(1, 1, 1, 0.1f));
        selectionRec.setOpacity(0.0f);
        selectionRec.setTouchable(Touchable.disabled);
        frontUI.addActor(selectionRec);

        addActor(mainBox);

        addActor(frontUI);

        sandbox.initSceneView(rootSceneVO);
        uiStage.getCompositePanel().addScene(rootSceneVO);

        sandbox.initEvents();
    }


    public void setKeyboardFocus() {
        setKeyboardFocus(mainBox);
    }

    public void setCurrentlyTransforming(IBaseItem item, int transformType) {
        if (item == null || item.getClass().getSimpleName().equals("LabelItem")) return;
        currTransformType = transformType;
        currTransformHost = item;
    }

}
