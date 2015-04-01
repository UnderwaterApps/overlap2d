/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.controlles.flow.FlowManager;
import com.uwsoft.editor.data.manager.SandboxResourceManager;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.mediators.ItemControlMediator;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.DropDown;
import com.uwsoft.editor.gdx.ui.SelectionActions;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.DataManager;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.actor.ParticleItem;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

import java.util.ArrayList;

/**
 * Sandbox is a complex hierarchy of managing classes that is supposed to be a main hub for the "sandbox" the part of editor where
 * user drops all items, moves them around, and composes the scene. sandbox is responsible for using runtime to render the visual scene,
 * it is responsible to listen for all the events, item resizing, selecting, aligning, removing and things like that.
 *
 * @author azakhary
 */
public class Sandbox {

    private static Sandbox instance = null;
    /**
     * end of shitty part
     */

    public EditingMode editingMode;
    public SceneControlMediator sceneControl;
    public ItemControlMediator itemControl;
    public FlowManager flow;
    public TransformationHandler transformationHandler;
    /**
     * this part contains legacy params that need to be removed one by one
     */
    public int currTransformType = -1;
    public IBaseItem currTransformHost;
    public boolean isResizing = false;
    public boolean isUsingSelectionTool = true;
    public boolean isItemTouched = false;
    public boolean dirty = false;
    public Vector3 copedItemCameraOffset;
    public IResourceRetriever rm;
    public ArrayList<MainItemVO> tempClipboard;
    public String fakeClipboard;
    public String currentLoadedSceneFileName;
    public boolean cameraPanOn;
    private SandboxStage sandboxStage;
    private UIStage uiStage;
    private SandboxInputAdapter sandboxInputAdapter;
    private UserActionController uac;
    private ItemFactory itemFactory;
    private ItemSelector selector;
    private InputMultiplexer inputMultiplexer;
    private Overlap2DFacade facade;
    private DataManager dataManager;


    private Sandbox() {
    }

    public synchronized static Sandbox getInstance() {
        /*
         * The instance gets created only when it is called for first time.
		 * Lazy-loading
		 */
        if (instance == null) {
            instance = new Sandbox();
            instance.init();
        }

        return instance;
    }

    private void init() {
        facade = Overlap2DFacade.getInstance();
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
        sandboxStage = new SandboxStage();
        uiStage = new UIStage(sandboxStage);
        sandboxStage.setUIStage(uiStage);

        inputMultiplexer.addProcessor(uiStage);
        inputMultiplexer.addProcessor(sandboxStage);

        editingMode = EditingMode.SELECTION;

        sceneControl = new SceneControlMediator(sandboxStage.sceneLoader, sandboxStage.essentials);
        itemControl = new ItemControlMediator(sceneControl);

        transformationHandler = new TransformationHandler();
        sandboxInputAdapter = new SandboxInputAdapter(this);
        uac = new UserActionController(this);
        selector = new ItemSelector(this);
        itemFactory = new ItemFactory(this);

        dataManager = facade.retrieveProxy(DataManager.NAME);
    }

    /**
     * Getters *
     */

    public UserActionController getUac() {
        return uac;
    }

    public SandboxStage getSandboxStage() {
        return sandboxStage;
    }

    public UIStage getUIStage() {
        return uiStage;
    }

    public ItemFactory getItemFactory() {
        return itemFactory;
    }

    public SceneControlMediator getSceneControl() {
        return sceneControl;
    }

    public SandboxInputAdapter getSandboxInputAdapter() {
        return sandboxInputAdapter;
    }


    /**
     * Initializers *
     */

    public EditingMode getCurrentMode() {
        return editingMode;
    }

    /**
     * sets current editing mode, and messages all selection rectangles about it.
     *
     * @param currentMode
     */
    public void setCurrentMode(EditingMode currentMode) {
        this.editingMode = currentMode;
        for (SelectionRectangle value : selector.getCurrentSelection().values()) {
            value.setMode(currentMode);
        }
    }

    /**
     * TODO: loading fonts this way is a bit outdated and needs to change
     *
     * @param sceneName
     */
    public void initData(String sceneName) {
        dataManager.sceneDataManager.preloadSceneSpecificData(sceneControl.getEssentials().rm.getSceneVO(sceneName), dataManager.resolutionManager.curResolution);

        sceneControl.initScene(sceneName);

        flow = new FlowManager(sceneControl.getRootSceneVO());
    }

    public void loadCurrentProject(String name) {
        rm = new SandboxResourceManager();
        sceneControl.getEssentials().rm = rm;
        loadScene(name);
    }

    public void loadCurrentProject() {
        ProjectVO projectVO = dataManager.getCurrentProjectVO();
        loadCurrentProject(projectVO.lastOpenScene.isEmpty() ? "MainScene" : projectVO.lastOpenScene);
        uiStage.loadCurrentProject();
    }

    public void loadScene(String sceneName) {
        currentLoadedSceneFileName = sceneName;
        uiStage.getCompositePanel().clearScenes();
        initData(sceneName);

        sandboxStage.initView();
        uiStage.getCompositePanel().addScene(sceneControl.getRootSceneVO());
        initSceneView(sceneControl.getRootSceneVO());
        sandboxInputAdapter.initSandboxEvents();

        ProjectVO projectVO = dataManager.getCurrentProjectVO();
        projectVO.lastOpenScene = sceneName;
        dataManager.saveCurrentProject();
        sandboxStage.getCamera().position.set(0, 0, 0);
        uiStage.reInitLibrary();
    }

    public void initSceneView(CompositeItemVO compositeItemVO) {
        initSceneView(sceneControl.initSceneView(compositeItemVO));
    }

    public void initSceneView(CompositeItem composite) {
        selector.clearSelections();
        sandboxStage.mainBox.clear();
        sceneControl.initSceneView(composite, uiStage.getCompositePanel().isRootScene());
        if (uiStage.getCompositePanel().isRootScene()) {
            uiStage.getCompositePanel().updateRootScene(sceneControl.getRootSceneVO());
        }
        for (int i = 0; i < sceneControl.getCurrentScene().getItems().size(); i++) {
            sandboxInputAdapter.initItemListeners(sceneControl.getCurrentScene().getItems().get(i));
        }
        sandboxStage.mainBox.addActor(sceneControl.getCurrentScene());
        sceneControl.getCurrentScene().setX(0);
        sceneControl.getCurrentScene().setY(0);

        uiStage.getLayerPanel().initContent();
        forceContinuousParticles(composite);
    }

    /**
     * if user is currently viewing a composite item, this will go one step up to previous composite in hierarchy
     */
    public void enterIntoPrevComposite() {
        sandboxStage.getCamera().position.set(0, 0, 0);
        uiStage.getCompositePanel().stepUp();
        uiStage.getItemsBox().initContent();
    }

    /**
     * If current selection contains only one item that is a composite item,
     * it will call enterIntoComposite with that time as parameter
     */
    public void enterIntoComposite() {
        CompositeItem item = null;
        sceneControl.getCurrentScene().updateDataVO();
        if (selector.getCurrentSelection().size() == 1) {
            for (SelectionRectangle value : selector.getCurrentSelection().values()) {
                if (value.getHost().isComposite()) {
                    item = (CompositeItem) value.getHost();
                }
            }
        }
        if (item == null) return;
        selector.clearSelections();
        enterIntoComposite(item.getDataVO());
    }

    /**
     * Opens up provided composite item, sets it as a main view, and focuses editing on it's contents.
     *
     * @param compositeItemVO data object of composite to enter into
     */
    public void enterIntoComposite(CompositeItemVO compositeItemVO) {
        //rootSceneVO.update(new CompositeItemVO(currentSceneVo.composite));
        sandboxStage.getCamera().position.set(0, 0, 0);
        getSceneControl().disableAmbience(true);
        uiStage.getLightBox().disableAmbiance.setChecked(true);
        uiStage.getCompositePanel().addScene(compositeItemVO);
        initSceneView(compositeItemVO);
        uiStage.getItemsBox().initContent();
    }

    /**
     * Some particle items might not be continuous, so they will stop after first iteration, which is ok
     * This method will make sure they look continuous while in editor, so user will find and see them easily.
     *
     * @param composite composite on screen with particles to be forced to be continuous
     */
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

    /**
     * Well... that's a bummer, I cannot remember why this was for. but the name speaks for itself sort of.
     * TODO: figure this out
     *
     * @return SceneVO
     */
    public SceneVO sceneVoFromItems() {
        CompositeItemVO itemVo = sceneControl.getRootSceneVO();
        itemFactory.cleanComposite(itemVo.composite);
        sceneControl.getCurrentSceneVO().composite = itemVo.composite;
        return sceneControl.getCurrentSceneVO();
    }

    /**
     * Initializes current scene on screen from a data object.
     *
     * @param vo CompositeItemVO data
     */
    public void reconstructFromSceneVo(CompositeItemVO vo) {
        initSceneView(vo);
    }

    /**
     * TODO: what does this do? seems to be saving as checkpoint of Flow? it so it should be renamed
     */
    public void saveSceneCurrentSceneData() {
        sceneControl.getCurrentScene().updateDataVO();
        flow.setPendingHistory(sceneControl.getCurrentScene().dataVO);
        flow.applyPendingAction();
    }

    public void setSceneAmbientColor(Color color) {
        sceneControl.getCurrentSceneVO().ambientColor[0] = color.r;
        sceneControl.getCurrentSceneVO().ambientColor[1] = color.g;
        sceneControl.getCurrentSceneVO().ambientColor[2] = color.b;
        sceneControl.getCurrentSceneVO().ambientColor[3] = color.a;
        sceneControl.getEssentials().rayHandler.setAmbientLight(color);
    }

    public ItemSelector getSelector() {
        return selector;
    }

    public boolean isComponentSkinAvailable() {
        return dataManager.textureManager.projectSkin != null;

    }

    public LayerItemVO getSelectedLayer() {
        return uiStage.getCurrentSelectedLayer();
    }

    public void setCurrentlyTransforming(IBaseItem item, int transformType) {
        if (item == null || item.getClass().getSimpleName().equals("LabelItem")) return;
        currTransformType = transformType;
        currTransformHost = item;
    }

    public CompositeItem getCurrentScene() {
        return sceneControl.getCurrentScene();
    }

    public void enablePan() {
        cameraPanOn = true;
        selector.clearSelections();
        isItemTouched = false;
    }

    public void prepareSelectionRectangle(float x, float y, boolean setOpacity) {
        // space is panning, so if we are not, then prepare the selection rectangle
        if (setOpacity) {
            getSandboxStage().selectionRec.setOpacity(0.6f);
        }
        getSandboxStage().selectionRec.setWidth(0);
        getSandboxStage().selectionRec.setHeight(0);
        getSandboxStage().selectionRec.setX(x);
        getSandboxStage().selectionRec.setY(y);
    }

    public boolean showDropDown(float x, float y) {
        showDropDownForSelection(x, y);

        return true;
    }

    public void selectionComplete() {
        // when touch is up, selection process stops, and if any items got "caught" in they should be selected.
        isUsingSelectionTool = false;
        // hiding selection rectangle
        getSandboxStage().selectionRec.setOpacity(0.0f);
        ArrayList<IBaseItem> curr = new ArrayList<IBaseItem>();
        Rectangle sR = getSandboxStage().selectionRec.getRect();
        for (int i = 0; i < getCurrentScene().getItems().size(); i++) {
            Actor asActor = (Actor) getCurrentScene().getItems().get(i);
            if (!getCurrentScene().getItems().get(i).isLockedByLayer() && Intersector
                    .overlaps(sR, new Rectangle(asActor.getX(), asActor.getY(), asActor.getWidth(), asActor.getHeight()))) {
                curr.add(getCurrentScene().getItems().get(i));
            }
        }

        selector.setSelections(curr, true);

        if (curr.size() == 0) {
            getUIStage().emptyClick();
        }
    }

    /**
     * Configures and shows drop down for currently selected items list
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void showDropDownForSelection(final float x, final float y) {
        DropDown dropDown = uiStage.mainDropDown;
        dropDown.clearItems();

        if (getSelector().getCurrentSelection().size() > 0) {
            dropDown.addItem(SelectionActions.GROUP_ITEMS, "Group into Composite");
        }

        if (getSelector().getCurrentSelection().size() == 1) {
            for (SelectionRectangle value : getSelector().getCurrentSelection().values()) {
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

        dropDown.initView(Gdx.input.getX(), Gdx.input.getY());

        dropDown.setEventListener(new DropDown.SelectionEvent() {

            @Override
            public void doAction(int action) {
                switch (action) {
                    case SelectionActions.GROUP_ITEMS:
                        getItemFactory().groupItemsIntoComposite();
                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.CONVERT_TO_BUTTON:
                        // TODO: this should go to UAC
                        CompositeItem btn = getItemFactory().groupItemsIntoComposite();
                        btn.getDataVO().composite.layers.add(new LayerItemVO("normal"));
                        btn.getDataVO().composite.layers.add(new LayerItemVO("pressed"));
                        btn.reAssembleLayers();

                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.EDIT_PHYSICS:
                        if (getSelector().getCurrentSelection().size() == 1) {
                            for (SelectionRectangle value : getSelector().getCurrentSelection().values()) {
                                IBaseItem item = value.getHost();
                                getUIStage().editPhysics(item);
                                break;
                            }
                        }
                        break;
                    case SelectionActions.ADD_TO_LIBRARY:
                        getItemFactory().addCompositeToLibrary();
                        break;
                    case SelectionActions.EDIT_COMPOSITE:
                        enterIntoComposite();
                        flow.setPendingHistory(getCurrentScene().getDataVO(), FlowActionEnum.GET_INTO_COMPOSITE);
                        flow.applyPendingAction();
                        break;
                    case SelectionActions.COPY:
                        getUac().copyAction();
                        break;
                    case SelectionActions.CUT:
                        getUac().cutAction();
                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.PASTE:
                        getUac().pasteAction(x, y, true);
                        saveSceneCurrentSceneData();
                        break;
                    case SelectionActions.DELETE:
                        getSelector().removeCurrentSelectedItems();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
