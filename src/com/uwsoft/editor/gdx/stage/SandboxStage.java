package com.uwsoft.editor.gdx.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.controlles.flow.FlowManager;
import com.uwsoft.editor.data.TypeConstants;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.SandboxResourceManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.ui.SandboxUI;
import com.uwsoft.editor.gdx.ui.SelectionActions;
import com.uwsoft.editor.gdx.ui.SelectionActions.SelectionEvent;
import com.uwsoft.editor.gdx.ui.dialogs.ConfirmDialog;
import com.uwsoft.editor.gdx.ui.dialogs.InputDialog;
import com.uwsoft.editor.gdx.ui.dialogs.InputDialog.InputDialogListener;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.*;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SandboxStage extends BaseStage implements TypeConstants {
    public static SandboxStage instance;
    public static int MODE_SELECT = 0;
    private int currentMode = MODE_SELECT;
    public static int MODE_TRANSFORM = 1;
    public UIStage uiStage;
    public String fakeClipboard = "";
    public ArrayList<MainItemVO> tempClipboard = null;
    public SandboxUI ui;
    public Group frontUI;
    public boolean cameraPanOn;
    public FlowManager flow;
    public String currentLoadedSceneFileName = "MainScene";
    public Box2DDebugRenderer box2dDebugRenderer;
    SelectionActions dropDown;
    PixelRect selectionRec;
    HashMap<IBaseItem, SelectionRectangle> currentSelection = new HashMap<IBaseItem, SelectionRectangle>();
    private FPSLogger fpsLogger;
    private int currTransformType = -1;
    private IBaseItem currTransformHost;
    private boolean isResizing = false;
    private SceneVO currentSceneVo;
    private Group mainBox = new Group();
    //private CompositeItem sceneRoot;
    private CompositeItemVO rootSceneVO;
    private boolean isUsingSelectionTool = true;
    private CompositeItem currentScene;
    private boolean isItemTouched = false;
    private boolean dirty = false;
    private Vector3 copedItemCameraOffset;
    private IResourceRetriever rm;

    public SandboxStage() {
        super();

        instance = this;
        cameraPanOn = false;
        isUsingSelectionTool = false;
        fpsLogger = new FPSLogger();

        rm = essentials.rm;

        box2dDebugRenderer = new Box2DDebugRenderer();
        
        physiscStopped = true;
    }
    public CompositeItemVO getRootScene() {
        return rootSceneVO;
    }

    public CompositeItem getCurrentScene() {
        return currentScene;
    }

    public SceneVO getCurrentSceneVO() {
        return currentSceneVo;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public void setUIStage(UIStage uiStage) {
        this.uiStage = uiStage;
    }

    private void initData(String sceneName) {
        if (essentials.world != null) {
            Array<Body> bodies = new Array<Body>();
            essentials.world.getBodies(bodies);
            System.out.println(bodies.size);
            if (currentScene != null) currentScene.dispose();
        }

        //essentials.rayHandler.removeAll();
        essentials.physicsStopped = true;
        sceneLoader = new SceneLoader(essentials);
        sceneLoader.setResolution(DataManager.getInstance().curResolution);
        DataManager.getInstance().preloadSceneSpecificData(essentials.rm.getSceneVO(sceneName), DataManager.getInstance().curResolution);

        currentSceneVo = sceneLoader.loadScene(sceneName, false);
        essentials.world = new World(new Vector2(currentSceneVo.physicsPropertiesVO.gravityX, currentSceneVo.physicsPropertiesVO.gravityY), true);
        essentials.rayHandler.setWorld(essentials.world);
        //essentials.world = null; // no need for physics here

        //if(currentSceneVo.composite == null) currentSceneVo.composite = new CompositeVO();
        //rootSceneVO = sceneLoader.sceneActor.getDataVO();
        rootSceneVO = new CompositeItemVO(currentSceneVo.composite);

        flow = new FlowManager(rootSceneVO);
    }

    public void selectItemsByLayerName(String name) {
        ArrayList<IBaseItem> itemsArr = new ArrayList<IBaseItem>();
        for (int i = 0; i < currentScene.getItems().size(); i++) {
            if (currentScene.getItems().get(i).getDataVO().layerName.equals(name)) {
                itemsArr.add(currentScene.getItems().get(i));
            }
        }

        setSelections(itemsArr, true);
    }


    public void setSelection(IBaseItem item, boolean removeOthers) {
        if (currentSelection.get(item) != null) return;

        if (removeOthers) clearSelections();

        SelectionRectangle rect = new SelectionRectangle(this);
        rect.claim(item);
        rect.setMode(currentMode);
        currentSelection.put(item, rect);
        frontUI.addActor(rect);
        rect.show();
        uiStage.itemWasSelected(item);

        if (dropDown != null) {
            dropDown.remove();
            dropDown = null;
        }

        uiStage.getItemsBox().setSelected(currentSelection);

    }

    private void releaseSelection(IBaseItem item) {
        currentSelection.get(item).remove();
        currentSelection.remove(item);

        uiStage.getItemsBox().setSelected(currentSelection);
    }

    private void clearSelections() {
        for (SelectionRectangle value : currentSelection.values()) {
            value.remove();
        }

        currentSelection.clear();
        uiStage.getItemsBox().setSelected(currentSelection);
    }

    private void setSelections(ArrayList<IBaseItem> items, boolean alsoShow) {
        clearSelections();

        for (int i = 0; i < items.size(); i++) {
            setSelection(items.get(i), false);
            if (alsoShow) {
                currentSelection.get(items.get(i)).show();
            }
        }
    }

    private void itemZeIndexChange(boolean isUp) {
        for (SelectionRectangle value : currentSelection.values()) {
            currentScene.updateDataVO();
            int ammount = 1;
            if (!isUp) ammount = -1;

            int setting = value.getHostAsActor().getZIndex() + ammount;
            if (setting < 0) setting = 0;
            Group parent = value.getHostAsActor().getParent();
            parent.swapActor(value.getHostAsActor().getZIndex(), setting);
            currentScene.updateDataVO();
        }
    }

    private void moveSelectedItemsBy(float x, float y) {
        for (SelectionRectangle value : currentSelection.values()) {
            value.getHostAsActor().setX(value.getHostAsActor().getX() + x);
            value.getHostAsActor().setY(value.getHostAsActor().getY() + y);

            value.setX(value.getX() + x);
            value.setY(value.getY() + y);
        }
        saveSceneCurrentSceneData();
    }

    private void removeCurrentSelectedItems() {
        for (SelectionRectangle value : currentSelection.values()) {
            value.getHostAsActor().remove();
            currentScene.removeItem(value.getHost());
            value.remove();
            ((IBaseItem) (value.getHostAsActor())).dispose();
        }
        uiStage.getItemsBox().initContent();
        currentSelection.clear();
    }

    public void createParticleItem(String pName, float x, float y) {
        currentScene.updateDataVO();
        ParticleEffectVO vo = new ParticleEffectVO();
        vo.particleName = pName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;
        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        ParticleItem itm = new ParticleItem(vo, essentials, currentScene);
        currentScene.addItem(itm);
        itm.forceContinuous();
        itm.start();
        initItemListeners(itm);
        updateSceneTree();
        itm.setX(vo.x);
        itm.setY(vo.y);
    }

    public void createItemFromLibrary(String libraryName, float x, float y) {
        currentScene.updateDataVO();
        CompositeItemVO vo = currentSceneVo.libraryItems.get(libraryName).clone();

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;
        vo.itemName = libraryName;
        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        CompositeItem itm = new CompositeItem(vo, sceneLoader.essentials, currentScene);

        currentScene.addItem(itm);
        initItemListeners(itm);
        saveSceneCurrentSceneData();
        updateSceneTree();
        itm.setX(vo.x);
        itm.setY(vo.y);
    }

    public void createItem(MainItemVO itemVo) {
        //System.out.println(itemVo.getClass());
        /*
        BasicItem itm = new BasicItem(item)

		vo.x = ((OrthographicCamera)getCamera()).position.x - getWidth()/2;
		vo.y = ((OrthographicCamera)getCamera()).position.y - getHeight()/2;

		currentScene.addItem(item);
		initItemListeners(item);
		*/
    }

    public void createSpriteAnimation(String animName, float x, float y) {
        currentScene.updateDataVO();
        SpriteAnimationVO vo = new SpriteAnimationVO();
        vo.animationName = animName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        SpriteAnimation anim = new SpriteAnimation(vo, sceneLoader.essentials, currentScene);
        //anim.setAnimation(anim.skeletonData.getAnimations().get(0).getName());

        currentScene.addItem(anim);
        initItemListeners(anim);
        updateSceneTree();
        anim.setX(vo.x);
        anim.setY(vo.y);
        anim.start();
    }

    public void createSpineAnimation(String animName, float x, float y) {
        currentScene.updateDataVO();
        SpineVO vo = new SpineVO();
        vo.animationName = animName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        SpineActor anim = new SpineActor(vo, sceneLoader.essentials, currentScene);
        anim.setAnimation(anim.skeletonData.getAnimations().get(0).getName());

        currentScene.addItem(anim);
        initItemListeners(anim);
        updateSceneTree();
        anim.setX(vo.x);
        anim.setY(vo.y);
    }
    public void createSpriterAnimation(String animationsName, float x, float y) {
    	currentScene.updateDataVO();
        SpriterVO vo = new SpriterVO();
        vo.animationName = animationsName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        SpriterActor anim = new SpriterActor(vo, sceneLoader.essentials, currentScene);

        currentScene.addItem(anim);
        initItemListeners(anim);
        updateSceneTree();
        anim.setX(vo.x);
        anim.setY(vo.y);
		
	}
    public void createLight(LightVO vo) {
        currentScene.updateDataVO();
        vo.x += getCamera().position.x - getWidth() / 2;
        vo.y += getCamera().position.y - getHeight() / 2;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        LightActor light = new LightActor(vo, sceneLoader.essentials, currentScene);
        currentScene.addItem(light);
        initItemListeners(light);
        saveSceneCurrentSceneData();
        updateSceneTree();
        light.setX(vo.x);
        light.setY(vo.y);

    }

    public void createItem(String regionName, float x, float y) {
        currentScene.updateDataVO();

        SimpleImageVO vo = new SimpleImageVO();
        vo.imageName = regionName;

        if (dataManager.getCurrentProjectInfoVO().assetMeshMap.containsKey(regionName)) {
            vo.meshId = dataManager.getCurrentProjectInfoVO().assetMeshMap.get(regionName);
            PhysicsBodyDataVO data = dataManager.getCurrentProjectInfoVO().meshes.get(vo.meshId).initialProperties;
            if(data != null){
            	vo.physicsBodyData = new PhysicsBodyDataVO(data);
            }else{
            	vo.physicsBodyData = new PhysicsBodyDataVO();
            }
        }

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        ImageItem itm = new ImageItem(vo, essentials, currentScene);

        currentScene.addItem(itm);
        initItemListeners(itm);
        saveSceneCurrentSceneData();
        updateSceneTree();
        itm.setX(vo.x);
        itm.setY(vo.y);
    }

    public void create9patchItem(String regionName, float x, float y) {
        currentScene.updateDataVO();

        Image9patchVO vo = new Image9patchVO();
        vo.imageName = regionName;

        if (checkForNoLayersSelectedDialog()) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        vo.x = x + getCamera().position.x - getWidth() / 2;
        vo.y = y + getCamera().position.y - getHeight() / 2;

        Image9patchItem itm = new Image9patchItem(vo, essentials, currentScene);

        currentScene.addItem(itm);
        initItemListeners(itm);
        saveSceneCurrentSceneData();
        updateSceneTree();
        itm.setX(vo.x);
        itm.setY(vo.y);
    }

    public void createComponent(String type, float x, float y) {
        currentScene.updateDataVO();
        MainItemVO vo = new MainItemVO();

        if (checkForNoLayersSelectedDialog()) return;
        if (!type.equals("Label") && checkForNoSkinDialog(type, x, y)) return;

        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;
        IBaseItem itm = null;
        if (type.equals("Text Field")) {
            vo = new TextBoxVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            itm = new TextBoxItem((TextBoxVO) vo, essentials, currentScene);
            ((TextBoxItem) itm).setDisabled(true);
        }
        if (type.equals("Text Button")) {
            vo = new ButtonVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            itm = new TextButtonItem((ButtonVO) vo, essentials, currentScene);
            ((TextButtonItem) itm).setDisabled(true);
        }
        if (type.equals("Label")) {
            if (checkForNoLabelStyleDialog(type, x, y)) return;
            vo = new LabelVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            File folder = new File(DataManager.getInstance().getFreeTypeFontPath());
            String fontName = folder.listFiles()[0].getName();
            fontName = fontName.substring(0, fontName.lastIndexOf('.'));
            ((LabelVO) vo).style = fontName;
            itm = new LabelItem((LabelVO) vo, essentials, currentScene);
        }
        if (type.equals("CheckBox")) {
            vo = new CheckBoxVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            itm = new CheckBoxItem((CheckBoxVO) vo, essentials, currentScene);
        }
        if (type.equals("SelectBox")) {
            vo = new SelectBoxVO();
            vo.x = x + getCamera().position.x - getWidth() / 2;
            vo.y = y + getCamera().position.y - getHeight() / 2;
            itm = new SelectBoxItem((SelectBoxVO) vo, essentials, currentScene);
        }

        currentScene.addItem(itm);
        initItemListeners(itm);
        saveSceneCurrentSceneData();
        updateSceneTree();
        ((Actor) itm).setX(vo.x);
        ((Actor) itm).setY(vo.y);
    }

    private boolean checkForNoSkinDialog(final String type, final float x, final float y) {
        if (TextureManager.getInstance().projectSkin == null) {
            ConfirmDialog confirmDialog = uiStage.showConfirmDialog();

            confirmDialog.setDescription("There is no style imported yet. Do you want to add default style instead to make this work?");
            confirmDialog.setListener(new ConfirmDialog.ConfirmDialogListener() {
                @Override
                public void onConfirm() {
                    DataManager.getInstance().copyDefaultStyleIntoProject();
                    createComponent(type, x, y);
                }

                @Override
                public void onCancel() {

                }
            });
            return true;
        }
        return false;
    }

    private boolean checkForNoLabelStyleDialog(final String type, final float x, final float y) {
        if (!TextureManager.getInstance().checkFontExistence()) {
            ConfirmDialog confirmDialog = uiStage.showConfirmDialog();

            confirmDialog.setDescription("There is no fonts imported yet.Do you want to copy default font");
            confirmDialog.setListener(new ConfirmDialog.ConfirmDialogListener() {
                @Override
                public void onConfirm() {
                    DataManager.getInstance().copyDefaultFontIntoProject();
                    createComponent(type, x, y);
                }

                @Override
                public void onCancel() {

                }
            });
            return true;
        }
        return false;
    }

    public boolean checkForNoLayersSelectedDialog() {
        if (uiStage.getCurrentSelectedLayer() == null) {
            // TODO: alert that there is no layers availibly
            return true;
        }

        return false;
    }

    private void updateSceneTree() {
        uiStage.getItemsBox().initContent();
    }

    private void initItemListeners(final IBaseItem iterableItem) {
        ClickListener listener = new ClickListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                iterableItem.updateDataVO();
                if (cameraPanOn) {
                    return false;
                }

                if (currentSelection.get(iterableItem) != null && Gdx.input.isKeyPressed(59)) {
                    releaseSelection(iterableItem);
                } else {
                    if (iterableItem.isLockedByLayer()) {
                        clearSelections();
                    } else {
                        setSelection(iterableItem, !Gdx.input.isKeyPressed(59));
                    }
                }

                for (SelectionRectangle value : currentSelection.values()) {
                    value.setTouchDiff(event.getStageX() - value.getHostAsActor().getX(), event.getStageY() - value.getHostAsActor().getY());
                }
                isItemTouched = true;
                uiStage.updateCurrentItemState();
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                for (SelectionRectangle value : currentSelection.values()) {
                    IBaseItem item = ((IBaseItem) value.getHostAsActor());
                    item.updateDataVO();

                    // update physics objetcs
                    if (item.isComposite()) {
                        ((CompositeItem) item).positionPhysics();
                    } else if (item.getBody() != null) {
                        item.getBody().setTransform(item.getDataVO().x * currentScene.mulX * PhysicsBodyLoader.SCALE, item.getDataVO().y * currentScene.mulY * PhysicsBodyLoader.SCALE, (float) Math.toRadians(item.getDataVO().rotation));
                    }

                }

                if (cameraPanOn) {
                   // cameraPanOn = !(button == 2);
                    return;
                }
                if (button == 1) {
                    showDropDownForSelection(event.getStageX(), event.getStageY());
                }
                for (SelectionRectangle value : currentSelection.values()) {
                    value.show();
                }

                if (dirty) {
                    saveSceneCurrentSceneData();
                }
                isItemTouched = false;
                dirty = false;
                if (getTapCount() == 2) {
                    getIntoComposite();
                    flow.setPendingHistory(getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
                    flow.applyPendingAction();
                }
                uiStage.updateCurrentItemState();
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (isItemTouched && !isResizing && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    dirty = true;
                    for (SelectionRectangle value : currentSelection.values()) {
                        float[] diff = value.getTouchDiff();
                        value.getHostAsActor().setX(event.getStageX() - diff[0]);
                        value.getHostAsActor().setY(event.getStageY() - diff[1]);
                        value.hide();
                    }
                }
                uiStage.updateCurrentItemState();
            }
        };
        listener.setTapCountInterval(0.5f);
        ((Actor) iterableItem).addListener(listener);
    }

    private void initView() {
        if (mainBox != null) mainBox.clear();
        clear();
        getCamera().position.set(0, 0, 0);

        frontUI = new Group();

        ui = new SandboxUI(this);
        addActor(ui);

        selectionRec = new PixelRect(textureManager, 0, 0);
        selectionRec.setFillColor(new Color(1, 1, 1, 0.1f));
        selectionRec.setOpacity(0.0f);
        selectionRec.setTouchable(Touchable.disabled);
        frontUI.addActor(selectionRec);

        addActor(mainBox);

        addActor(frontUI);

        initEvents();

        initSceneView(rootSceneVO);
        uiStage.getCompositePanel().addScene(rootSceneVO);
    }

    public void initSceneView(CompositeItemVO compositeItemVO) {
        disableLights(false);
        //essentials.rayHandler.removeAll();

        if (currentScene != null) currentScene.dispose();

        CompositeItemVO itemVo = new CompositeItemVO();
        itemVo.composite = compositeItemVO.composite;
        itemVo.itemIdentifier = compositeItemVO.itemIdentifier;
        itemVo.itemName = compositeItemVO.itemName;
        CompositeItem composite = new CompositeItem(itemVo, sceneLoader.essentials);
        initSceneView(composite);
    }

    public void initSceneView(CompositeItem composite) {

        composite.applyResolution(DataManager.getInstance().curResolution);

        clearSelections();

        mainBox.clear();
        currentScene = composite;
        if (uiStage.getCompositePanel().isRootScene()) {
            rootSceneVO = currentScene.dataVO;
            uiStage.getCompositePanel().updateRootScene(rootSceneVO);
        }
        for (int i = 0; i < currentScene.getItems().size(); i++) {
            initItemListeners(currentScene.getItems().get(i));
        }
        mainBox.addActor(currentScene);
        currentScene.setX(0);
        currentScene.setY(0);

        uiStage.getLayerPanel().initContent();

        if (currentSceneVo.ambientColor == null) {
            currentSceneVo.ambientColor = new float[4];
            currentSceneVo.ambientColor[0] = 0.5f;
            currentSceneVo.ambientColor[1] = 0.5f;
            currentSceneVo.ambientColor[2] = 0.5f;
            currentSceneVo.ambientColor[3] = 1.0f;
        }

        forceContinuousParticles(composite);
        //updateSceneTree();
    }

    private void forceContinuousParticles(CompositeItem composite) {
        ArrayList<IBaseItem> asd = composite.getItems();
        for (int i = 0; i < asd.size(); i++) {
            IBaseItem item = asd.get(i);
            if (item instanceof ParticleItem) {
                ((ParticleItem) item).forceContinuous();
                continue;
            }
            if (item instanceof CompositeItem) {
                forceContinuousParticles((CompositeItem) item);
            }

        }
    }

    public void showDropDownForSelection(final float x, final float y) {
        showDropDownForSelection(x, y, null);
    }

    /**
     * TODO: this needs to be made right
     *
     * @param x
     * @param y
     * @param regionName
     */
    public void showDropDownForSelection(final float x, final float y, final String regionName) {
        if (dropDown != null) dropDown.remove();
        dropDown = new SelectionActions();

        if (regionName != null) {
            dropDown.addItem(SelectionActions.EDIT_ASSET_PHYSICS, "Edit Physics");
        } else {

            if (currentSelection.size() > 0) {
                dropDown.addItem(SelectionActions.GROUP_ITEMS, "Group into Composite");
            }

            if (currentSelection.size() == 1) {
                for (SelectionRectangle value : currentSelection.values()) {
                    if (value.getHost().isComposite()) {
                        dropDown.addItem(SelectionActions.ADD_TO_LIBRARY, "Add to Library");
                        dropDown.addItem(SelectionActions.EDIT_COMPOSITE, "Edit Composite");
                    }
                }
            }

            dropDown.addItem(SelectionActions.PASTE, "Paste");

            if (isItemTouched) {
                dropDown.addItem(SelectionActions.CONVERT_TO_BUTTON, "Convert to Button");
                dropDown.addItem(SelectionActions.EDIT_PHYSICS, "Edit Physics");
                dropDown.addItem(SelectionActions.CUT, "Cut");
                dropDown.addItem(SelectionActions.COPY, "Copy");
                dropDown.addItem(SelectionActions.DELETE, "Delete");
            }
        }

        dropDown.initView();
        Vector2 vector2 = uiStage.contextMenuContainer.stageToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        dropDown.setX(vector2.x);
        dropDown.setY(uiStage.getHeight() - vector2.y);
        uiStage.contextMenuContainer.addActor(dropDown);

        //
        dropDown.setEventListener(new SelectionEvent() {

            @Override
            public void doAction(int action) {
                switch (action) {
                    case SelectionActions.GROUP_ITEMS:
                        groupItemsIntoComposite();
                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.CONVERT_TO_BUTTON:
                        CompositeItem btn = groupItemsIntoComposite();
                        btn.getDataVO().composite.layers.add(new LayerItemVO("normal"));
                        btn.getDataVO().composite.layers.add(new LayerItemVO("pressed"));
                        btn.reAssembleLayers();

                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.EDIT_ASSET_PHYSICS:
                        uiStage.editPhysics(regionName);
                        break;
                    case SelectionActions.EDIT_PHYSICS:
                        if (currentSelection.size() == 1) {
                            for (SelectionRectangle value : currentSelection.values()) {
                                IBaseItem item = value.getHost();
                                uiStage.editPhysics(item);
                                break;
                            }
                        }

                        break;
                    case SelectionActions.ADD_TO_LIBRARY:
                        addCompositeToLibrary();
                        break;
                    case SelectionActions.EDIT_COMPOSITE:
                        getIntoComposite();
                        flow.setPendingHistory(getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
                        flow.applyPendingAction();
                        break;
                    case SelectionActions.COPY:
                        copyAction();
                        break;
                    case SelectionActions.CUT:
                        cutAction();
                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.PASTE:
                        pasteAction(x, y, true);
                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.DELETE:
                        removeCurrentSelectedItems();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void getIntoPrevComposite() {
        getCamera().position.set(0, 0, 0);
        uiStage.getCompositePanel().stepUp();
        uiStage.getItemsBox().initContent();
    }

    public void addCompositeToLibrary() {
        CompositeItem item = null;
        if (currentSelection.size() == 1) {
            for (SelectionRectangle value : currentSelection.values()) {
                if (value.getHost().isComposite()) {
                    item = (CompositeItem) value.getHost();
                }
            }
        }

        if (item == null) return;

        InputDialog dlg = uiStage.showInputDialog();

        dlg.setDescription("Please set unique name for your component");

        final CompositeItem itemToAdd = item;

        dlg.setListener(new InputDialogListener() {

            @Override
            public void onConfirm(String input) {
                currentSceneVo.libraryItems.put(input, itemToAdd.getDataVO());
                uiStage.reInitLibrary();
            }
        });


    }

    public void getIntoComposite() {
        CompositeItem item = null;
        getCurrentScene().updateDataVO();
        if (currentSelection.size() == 1) {
            for (SelectionRectangle value : currentSelection.values()) {
                if (value.getHost().isComposite()) {
                    item = (CompositeItem) value.getHost();
                }
            }
        }
        if (item == null) return;
        clearSelections();
        getIntoComposite(item.getDataVO());
    }

    public void getIntoComposite(CompositeItemVO compositeItemVO) {
        //rootSceneVO.update(new CompositeItemVO(currentSceneVo.composite));
        getCamera().position.set(0, 0, 0);
        disableAmbience(true);
        uiStage.getLightBox().disableAmbiance.setChecked(true);
        uiStage.getCompositePanel().addScene(compositeItemVO);
        initSceneView(compositeItemVO);
        uiStage.getItemsBox().initContent();
    }

    public void copyCurrentSelection() {
        ArrayList<MainItemVO> voList = new ArrayList<>();
        for (int i = 0; i < currentScene.getItems().size(); i++) {
            voList.add(currentScene.getItems().get(i).getDataVO());
        }

        //TODO: change this to real clipboard
        tempClipboard = voList;
    }

    public void pastClipBoard() {
        //TODO: duplicate item here
    }

    public CompositeItem groupItemsIntoComposite() {
        currentScene.updateDataVO();
        CompositeItemVO vo = new CompositeItemVO();

        // Calculating lower left and upper values
        float lowerX = 0, lowerY = 0, upperX = 0, upperY = 0;
        int iter = 0;
        for (SelectionRectangle value : currentSelection.values()) {
            if (iter++ == 0) {
                if (value.getScaleX() > 0 && value.getWidth() > 0) {
                    lowerX = value.getX();
                    upperX = value.getX() + value.getWidth();
                } else {
                    upperX = value.getX();
                    lowerX = value.getX() + value.getWidth();
                }

                if (value.getScaleY() > 0 && value.getHeight() > 0) {
                    lowerY = value.getY();
                    upperY = value.getY() + value.getHeight();
                } else {
                    upperY = value.getY();
                    lowerY = value.getY() + value.getHeight();
                }
            }
            if (value.getScaleX() > 0 && value.getWidth() > 0) {
                if (lowerX > value.getX()) lowerX = value.getX();
                if (upperX < value.getX() + value.getWidth()) upperX = value.getX() + value.getWidth();
            } else {
                if (upperX < value.getX()) upperX = value.getX();
                if (lowerX > value.getX() + value.getWidth()) lowerX = value.getX() + value.getWidth();
            }
            if (value.getScaleY() > 0 && value.getHeight() > 0) {
                if (lowerY > value.getY()) lowerY = value.getY();
                if (upperY < value.getY() + value.getHeight()) upperY = value.getY() + value.getHeight();
            } else {
                if (upperY < value.getY()) upperY = value.getY();
                if (lowerY > value.getY() + value.getHeight()) lowerY = value.getY() + value.getHeight();
            }
        }

        float width = upperX - lowerX;
        float height = upperY - lowerY;

        for (SelectionRectangle value : currentSelection.values()) {
            MainItemVO itemVo = value.getHost().getDataVO();
            //System.out.println("ASSSDDD " + itemVo.x + " BASDDD " + lowerX);
            itemVo.x = itemVo.x - lowerX;
            itemVo.y = itemVo.y - lowerY;
            //System.out.println("adddd " + itemVo.x );
            vo.composite.addItem(itemVo);
        }
        vo.x = lowerX;
        vo.y = lowerY;
        vo.layerName = uiStage.getCurrentSelectedLayer().layerName;

        CompositeItem item = sceneLoader.getCompositeElement(vo);

        item.setWidth(width);
        item.setHeight(height);
        //item.getItems().get(0).getDataVO();
        ///System.out.println("fddddd " + ((Actor)item.getItems().get(0)).getX());

        removeCurrentSelectedItems();

        currentScene.addItem(item);

        ///System.out.println("SSSSddddd " + ((Actor)item.getItems().get(0)).getX());
        initItemListeners(item);
        updateSceneTree();
        setSelection(item, true);

        return item;
    }

    private void initEvents() {
        ClickListener listener = new ClickListener() {

            private float lastX = 0
                    ,
                    lastY = 0;


            /** Called when the mouse wheel has been scrolled. When true is returned, the event is {@link Event#handle() handled}. */
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                if (amount == 0) return false;
                if (isItemTouched) {
                    for (SelectionRectangle value : currentSelection.values()) {
                        float degreeAmmount = 1;
                        if (amount < 0) degreeAmmount = -1;
                        if (Gdx.input.isKeyPressed(59)) {
                            degreeAmmount = degreeAmmount * 30;
                        }
                        value.getHostAsActor().rotateBy(degreeAmmount);
                        value.update();
                    }
                    dirty = true;
                }
                return false;
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
//                cameraPanOn = false;
//                if (event.getStage() == uiStage) {
//                    return false;
//                }
                lastX = Gdx.input.getX();
                lastY = Gdx.input.getY();
                uiStage.setKeyboardFocus();
                uiStage.setScrollFocus(mainBox);
                setKeyboardFocus();
                if (dropDown != null) {
                    dropDown.remove();
                    dropDown = null;
                }
                switch (button) {
                    case 2:
                        cameraPanOn = true;
                        clearSelections();
                        isItemTouched = false;
                        break;
                    case 0:
                        if (!Gdx.input.isKeyPressed(62)) {
                            selectionRec.setOpacity(0.6f);
                        }
                        selectionRec.setWidth(0);
                        selectionRec.setHeight(0);
                        selectionRec.setX(x);
                        selectionRec.setY(y);
                        break;
                }
                return !isItemTouched;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (button == 1 && (dropDown == null || dropDown.getParent() == null)) {
                    clearSelections();
                    showDropDownForSelection(x, y);
                    return;
                }
                if (cameraPanOn) {
                    cameraPanOn = Gdx.input.isKeyPressed(Input.Keys.SPACE);
                    return;
                }
                isUsingSelectionTool = false;
                selectionRec.setOpacity(0.0f);
                ArrayList<IBaseItem> curr = new ArrayList<IBaseItem>();
                Rectangle sR = selectionRec.getRect();
                for (int i = 0; i < currentScene.getItems().size(); i++) {
                    Actor asActor = (Actor) currentScene.getItems().get(i);
                    if (!currentScene.getItems().get(i).isLockedByLayer() && Intersector.overlaps(sR, new Rectangle(asActor.getX(), asActor.getY(), asActor.getWidth(), asActor.getHeight()))) {
                        curr.add(currentScene.getItems().get(i));
                    }
                }

                setSelections(curr, true);

                if (curr.size() == 0) {
                    uiStage.emptyClick();
                }
                if (getTapCount() == 2) {
                    getIntoPrevComposite();
                    flow.setPendingHistory(getCurrentScene().getDataVO(), FlowActionEnum.GET_OUT_COMPOSITE);
                    flow.applyPendingAction();
                }

            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (isResizing) return;

                if (cameraPanOn) {
                    float currX = getCamera().position.x + (lastX - Gdx.input.getX());
                    float currY = getCamera().position.y + (Gdx.input.getY() - lastY);
                    getCamera().position.set(currX, currY, 0);
                    lastX = Gdx.input.getX();
                    lastY = Gdx.input.getY();
                } else {
                    // else using selection tool
                    isUsingSelectionTool = true;
                    selectionRec.setWidth(x - selectionRec.getX());
                    selectionRec.setHeight(y - selectionRec.getY());
                }
            }

            public boolean keyDown(InputEvent event, int keycode) {
                float deltaMove = 1;

                if (Gdx.input.isKeyPressed(129) || Gdx.input.isKeyPressed(0)) {
                    if (keycode == 19) { // up
                        itemZeIndexChange(true);
                    }
                    if (keycode == 20) { // down
                        itemZeIndexChange(false);
                    }
                    if (keycode == 29) { // A
                        ArrayList<IBaseItem> curr = new ArrayList<IBaseItem>();
                        for (int i = 0; i < currentScene.getItems().size(); i++) {
                            curr.add(currentScene.getItems().get(i));
                        }

                        setSelections(curr, true);
                    }

                    if (keycode == Input.Keys.NUM_1 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        alignSelections(Align.top);
                    }
                    if (keycode == Input.Keys.NUM_2 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        alignSelections(Align.left);
                    }
                    if (keycode == Input.Keys.NUM_3 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        alignSelections(Align.bottom);
                    }
                    if (keycode == Input.Keys.NUM_4 && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                        alignSelections(Align.right);
                    }

                    return true;
                }

                if (Gdx.input.isKeyPressed(59)) deltaMove = 20;
                if (keycode == 19) {
                    moveSelectedItemsBy(0, deltaMove);
                }
                if (keycode == 20) {
                    moveSelectedItemsBy(0, -deltaMove);
                }
                if (keycode == 21) {
                    moveSelectedItemsBy(-deltaMove, 0);
                }
                if (keycode == 22) {
                    moveSelectedItemsBy(deltaMove, 0);
                }
                if (keycode == Input.Keys.SPACE && !isItemTouched && !isUsingSelectionTool) {
                    setCursor(Cursor.HAND_CURSOR);
                    cameraPanOn = true;
                }


                return true;
            }

            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 67) {
                    // delete
                    removeCurrentSelectedItems();
                }
                if (keycode == 62) {
                    setCursor(Cursor.DEFAULT_CURSOR);
                    cameraPanOn = false;
                }
                return true;
            }
        };

        this.addListener(listener);

        listener.setTapCountInterval(0.5f);


        ClickListener transformationListeners = new ClickListener() {

            private int currType;
            private Actor currHost;

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (currTransformHost != null && currTransformType >= 0) {
                    isResizing = true;
                    currType = currTransformType;
                    currHost = (Actor) currTransformHost;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (isResizing == true && currHost != null) {
                    ((IBaseItem) currHost).updateDataVO();
                }
                isResizing = false;
                currType = -1;
                currHost = null;
                currTransformHost = null;
                setCursor(Cursor.DEFAULT_CURSOR);
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!isResizing) {
                    return;
                }
                float pseudoWidth = currHost.getWidth() * (currHost instanceof LabelItem ? 1 : currHost.getScaleX());
                float pseudoHeight = currHost.getHeight() * (currHost instanceof LabelItem ? 1 : currHost.getScaleY());
                float currPseudoWidth = pseudoWidth;
                float currPseudoHeight = pseudoHeight;
                switch (currType) {
                    case SelectionRectangle.LB:
                        pseudoWidth = (currHost.getX() + currPseudoWidth) - event.getStageX();
                        pseudoHeight = (currHost.getY() + currPseudoHeight) - event.getStageY();
                        currHost.setX(currHost.getX() - (pseudoWidth - currPseudoWidth));
                        currHost.setY(currHost.getY() - (pseudoHeight - currPseudoHeight));
                        break;
                    case SelectionRectangle.L:
                        pseudoWidth = (currHost.getX() + currPseudoWidth) - event.getStageX();
                        currHost.setX(currHost.getX() - (pseudoWidth - currPseudoWidth));
                        break;
                    case SelectionRectangle.LT:
                        pseudoWidth = (currHost.getX() + currPseudoWidth) - event.getStageX();
                        pseudoHeight = event.getStageY() - currHost.getY();
                        currHost.setX(currHost.getX() - (pseudoWidth - currPseudoWidth));
                        break;
                    case SelectionRectangle.T:
                        pseudoHeight = event.getStageY() - currHost.getY();
                        break;
                    case SelectionRectangle.B:
                        pseudoHeight = (currHost.getY() + currPseudoHeight) - event.getStageY();
                        currHost.setY(currHost.getY() - (pseudoHeight - currPseudoHeight));
                        break;
                    case SelectionRectangle.RB:
                        pseudoWidth = event.getStageX() - currHost.getX();
                        pseudoHeight = (currHost.getY() + currPseudoHeight) - event.getStageY();
                        currHost.setY(currHost.getY() - (pseudoHeight - currPseudoHeight));
                        break;
                    case SelectionRectangle.R:
                        pseudoWidth = event.getStageX() - currHost.getX();
                        break;
                    case SelectionRectangle.RT:
                        pseudoWidth = event.getStageX() - currHost.getX();
                        pseudoHeight = event.getStageY() - currHost.getY();
                        break;
                    default:
                        return;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    float enclosingRectSize = Math.max(pseudoWidth, pseudoHeight);
                    if (currHost.getWidth() >= currHost.getHeight()) {
                        pseudoWidth = enclosingRectSize;
                        pseudoHeight = (pseudoWidth / currHost.getWidth()) * currHost.getHeight();
                    }
                    if (currHost.getHeight() > currHost.getWidth()) {
                        pseudoHeight = enclosingRectSize;
                        pseudoWidth = (pseudoHeight / currHost.getHeight()) * currHost.getWidth();
                    }

                }
                currHost.setScale(pseudoWidth / currHost.getWidth(), pseudoHeight / currHost.getHeight());
                uiStage.updateCurrentItemState();
                SelectionRectangle selectionRectangle = currentSelection.get(currHost);
                //TODO: sometimes it is null, find out why
                if (selectionRectangle != null) {
                    selectionRectangle.update();
                }
            }
        };

        this.addListener(transformationListeners);
    }

    public void alignSelectionsByY(float y, boolean ignoreSelfHeight) {
        int ratio = ignoreSelfHeight ? 0 : 1;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            //actor.setY(y - ratio * actor.getHeight());
            if (actor.getScaleY() < 0) {
                actor.setY(y - (ratio + actor.getScaleY()) * actor.getHeight());
            } else {
                actor.setY(y - ratio * actor.getHeight());
            }
            value.setY(actor.getY());
        }
    }

    public void alignSelectionsByX(float x, boolean ignoreSelfWidth) {
        int ratio = ignoreSelfWidth ? 0 : 1;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            //actor.setX(x - ratio * actor.getWidth());
            if (actor.getScaleX() < 0) {
                actor.setX(x - (ratio + actor.getScaleX()) * actor.getWidth());
            } else {
                actor.setX(x - ratio * actor.getWidth());
            }
            value.setX(actor.getX());
        }
    }

    public void loadCurrentProject(String name) {
        rm = new SandboxResourceManager();
        essentials.rm = rm;
        loadScene(name);
    }

    public void loadCurrentProject() {
        ProjectVO projectVO = DataManager.getInstance().getCurrentProjectVO();
        loadCurrentProject(projectVO.lastOpenScene.isEmpty() ? "MainScene" : projectVO.lastOpenScene);
    }

    public void loadScene(String sceneName) {
        currentLoadedSceneFileName = sceneName;
        uiStage.getCompositePanel().clearScenes();
        initData(sceneName);
        initView();
        ProjectVO projectVO = DataManager.getInstance().getCurrentProjectVO();
        projectVO.lastOpenScene = sceneName;
        DataManager.getInstance().saveCurrentProject();
        getCamera().position.set(0, 0, 0);

    }

    public void setKeyboardFocus() {
        setKeyboardFocus(mainBox);
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
        for (SelectionRectangle value : currentSelection.values()) {
            value.setMode(currentMode);
        }
    }

    public void setCurrentlyTransforming(IBaseItem item, int transformType) {
        if (item == null || item.getClass().getSimpleName().equals("LabelItem")) return;
        currTransformType = transformType;
        currTransformHost = item;
    }

    public SceneVO sceneVoFromItems() {
        CompositeItemVO itemVo = rootSceneVO;
        cleanComposite(itemVo.composite);
        currentSceneVo.composite = itemVo.composite;
        return currentSceneVo;
    }

    private void cleanComposite(CompositeVO compositeVO) {
        Iterator<CompositeItemVO> compositeItemVOIterator = compositeVO.sComposites.iterator();
        while (compositeItemVOIterator.hasNext()) {
            CompositeItemVO next = compositeItemVOIterator.next();
            if (isCompositeEmpty(next.composite)) {
                compositeItemVOIterator.remove();
            }
        }
    }

    private boolean isCompositeEmpty(CompositeVO composite) {
        if (composite.isEmpty()) {
            return true;
        }
        cleanComposite(composite);
        return composite.isEmpty();
    }

    public void reconstructFromSceneVo(CompositeItemVO vo) {
        initSceneView(vo);
    }

    public void undo() {
        FlowActionEnum lastFlowAction = flow.getFlowLastAction();
        CompositeItemVO compositeItemVO = flow.undo();
        switch (lastFlowAction) {
            case GET_INTO_COMPOSITE:
                getIntoPrevComposite();
                break;
            case GET_OUT_COMPOSITE:
                getIntoComposite(compositeItemVO);
                break;
            default:
                reconstructFromSceneVo(compositeItemVO);
                break;
        }
        currentScene.updateDataVO();
    }

    public void redo() {
        CompositeItemVO compositeItemVO = flow.redo();
        FlowActionEnum lastFlowAction = flow.getFlowLastAction();
        switch (lastFlowAction) {
            case GET_INTO_COMPOSITE:
                getIntoComposite(compositeItemVO);
                break;
            case GET_OUT_COMPOSITE:
                getIntoPrevComposite();
                break;
            default:
                reconstructFromSceneVo(compositeItemVO);
                break;
        }
        currentScene.updateDataVO();
    }

    public ArrayList<IBaseItem> getSelectedItems() {
        ArrayList<IBaseItem> items = new ArrayList<IBaseItem>();
        for (SelectionRectangle value : currentSelection.values()) {
            items.add(value.getHost());
        }
        return items;
    }

    public void cutAction() {
        ArrayList<IBaseItem> items = getSelectedItems();
        putItemsToClipboard(items);
        removeCurrentSelectedItems();
    }

    public void copyAction() {
        currentScene.updateDataVO();
        ArrayList<IBaseItem> items = getSelectedItems();
        putItemsToClipboard(items);
    }

    public void pasteAction(float x, float y, boolean ignoreCameraPos) {
        CompositeVO tempHolder;
        Json json = new Json();
        json.setOutputType(OutputType.json);
        tempHolder = json.fromJson(CompositeVO.class, fakeClipboard);

        if (tempHolder == null) return;

        CompositeItemVO fakeVO = new CompositeItemVO();

        fakeVO.composite = tempHolder;
        CompositeItem fakeItem = new CompositeItem(fakeVO, sceneLoader.essentials);

        ArrayList<IBaseItem> finalItems = new ArrayList<IBaseItem>();
        Actor firstItem = (Actor) fakeItem.getItems().get(0);
        float offsetX = firstItem.getX();
        float offsetY = firstItem.getY();
        for (int i = 1; i < fakeItem.getItems().size(); i++) {
            Actor item = (Actor) fakeItem.getItems().get(i);
            if (item.getX() < offsetX) {
                offsetX = item.getX();
            }
            if (item.getY() < offsetY) {
                offsetY = item.getY();
            }
        }
        Vector3 cameraPos = ignoreCameraPos ? new Vector3(0, 0, 0) : ((OrthographicCamera) getCamera()).position;
        for (int i = 0; i < fakeItem.getItems().size(); i++) {
            IBaseItem itm = fakeItem.getItems().get(i);
            itm.getDataVO().layerName = uiStage.getCurrentSelectedLayer().layerName;
            currentScene.addItem(itm);
            ((Actor) itm).setX(x + ((Actor) itm).getX() - offsetX + (cameraPos.x + copedItemCameraOffset.x));
            ((Actor) itm).setY(y + ((Actor) itm).getY() - offsetY + (cameraPos.y + copedItemCameraOffset.y));
            itm.updateDataVO();
            initItemListeners(itm);
            finalItems.add(itm);
        }

        setSelections(finalItems, true);
        updateSceneTree();
    }

    private void putItemsToClipboard(ArrayList<IBaseItem> items) {
        CompositeVO tempHolder = new CompositeVO();
        Json json = new Json();
        json.setOutputType(OutputType.json);
        Actor actor = (Actor) items.get(0);
        Vector3 cameraPos = ((OrthographicCamera) getCamera()).position;
        Vector3 vector3 = new Vector3(actor.getX() - cameraPos.x, actor.getY() - cameraPos.y, 0);
        for (IBaseItem item : items) {
            tempHolder.addItem(item.getDataVO());
            actor = (Actor) item;
            if (actor.getX() - cameraPos.x < vector3.x) {
                vector3.x = actor.getX() - cameraPos.x;
            }
            if (actor.getY() - cameraPos.y < vector3.y) {
                vector3.y = actor.getY() - cameraPos.y;
            }
        }
        fakeClipboard = json.toJson(tempHolder);
        copedItemCameraOffset = vector3;
    }

    public void saveSceneCurrentSceneData() {
        currentScene.updateDataVO();
        flow.setPendingHistory(getCurrentScene().dataVO);
        flow.applyPendingAction();
    }

    public void setSceneAmbientColor(Color color) {
        currentSceneVo.ambientColor[0] = color.r;
        currentSceneVo.ambientColor[1] = color.g;
        currentSceneVo.ambientColor[2] = color.b;
        currentSceneVo.ambientColor[3] = color.a;
        essentials.rayHandler.setAmbientLight(color);
    }

    public void updateSelections() {
        for (SelectionRectangle value : currentSelection.values()) {
            value.update();
        }
    }

    public void alignSelections(int align) {
        //ResolutionEntryVO resolutionEntryVO = dataManager.getCurrentProjectInfoVO().getResolution(dataManager.curResolution);
        switch (align) {
            case Align.top:
                alignSelectionsByY(getCurrentSelectionsHighestY(), false);
                break;
            case Align.left:
                alignSelectionsByX(getCurrentSelectionsLowestX(), true);
                break;
            case Align.bottom:
                alignSelectionsByY(getCurrentSelectionsLowestY(), true);
                break;
            case Align.right:
                alignSelectionsByX(getCurrentSelectionsHighestX(), false);
                break;
        }
    }


    public float getCurrentSelectionsHighestY() {
        float highestY = -Float.MAX_VALUE;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            float maxY = Math.max(actor.getY(), actor.getY() + actor.getHeight() * actor.getScaleY());
            if (maxY > highestY) {
                highestY = maxY;
            }
        }
        return highestY;
    }

    public float getCurrentSelectionsHighestX() {
        float highestX = -Float.MAX_VALUE;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            float maxX = Math.max(actor.getX(), actor.getX() + actor.getWidth() * actor.getScaleX());
            if (maxX > highestX) {
                highestX = maxX;
            }
        }
        return highestX;
    }

    public float getCurrentSelectionsLowestX() {
        float lowestX = Float.MAX_VALUE;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            float minX = Math.min(actor.getX(), actor.getX() + actor.getWidth() * actor.getScaleX());
            if (minX < lowestX) {
                lowestX = minX;
            }
        }
        return lowestX;
    }

    public float getCurrentSelectionsLowestY() {
        float lowestY = Float.MAX_VALUE;
        for (SelectionRectangle value : currentSelection.values()) {
            Actor actor = value.getHostAsActor();
            float minY = Math.min(actor.getY(), actor.getY() + actor.getHeight() * actor.getScaleY());
            if (minY < lowestY) {
                lowestY = minY;
            }
        }
        return lowestY;
    }

    @Override
    public void draw() {
        super.draw();

        //  TODO: make this in special mode
        if (essentials.world != null) {
            //box2dDebugRenderer.render(essentials.world, getCamera().combined.cpy().scl(1/PhysicsBodyLoader.SCALE));
        }
    }
	

}
