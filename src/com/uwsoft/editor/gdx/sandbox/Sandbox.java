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

import java.util.ArrayList;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.mediators.ItemControlMediator;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.gdx.ui.SandboxUI;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.ResolutionManager;
import com.uwsoft.editor.mvc.proxy.ResourceManager;
import com.uwsoft.editor.mvc.proxy.SceneDataManager;
import com.uwsoft.editor.mvc.view.stage.SandboxMediator;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.mvc.view.stage.UIStageMediator;
import com.uwsoft.editor.mvc.view.stage.input.InputListener;
import com.uwsoft.editor.renderer.Overlap2dRenderer;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.legacy.data.CompositeItemVO;
import com.uwsoft.editor.renderer.legacy.data.LayerItemVO;
import com.uwsoft.editor.renderer.legacy.data.MainItemVO;
import com.uwsoft.editor.renderer.legacy.data.SceneVO;

/**
 * Sandbox is a complex hierarchy of managing classes that is supposed to be a main hub for the "sandbox" the part of editor where
 * user drops all panels, moves them around, and composes the scene. sandbox is responsible for using runtime to render the visual scene,
 * it is responsible to listen for all the events, item resizing, selecting, aligning, removing and things like that.
 *
 * @author azakhary
 */
public class Sandbox {

    private static Sandbox instance = null;

    private static final String CLASS_NAME = "com.uwsoft.editor.gdx.sandbox.Sandbox";

    public static final String ACTION_GROUP_ITEMS = CLASS_NAME + "ACTION_GROUP_ITEMS";
    public static final String ACTION_COMPOSITE_HIERARCHY_UP = CLASS_NAME + "ACTION_COMPOSITE_HIERARCHY_UP";
    public static final String ACTION_EDIT_COMPOSITE = CLASS_NAME + "ACTION_EDIT_COMPOSITE";
    public static final String ACTION_CONVERT_TO_BUTTON = CLASS_NAME + "ACTION_CONVERT_TO_BUTTON";
    public static final String ACTION_CUT = CLASS_NAME + "ACTION_CUT";
    public static final String ACTION_COPY = CLASS_NAME + "ACTION_COPY";
    public static final String ACTION_PASTE = CLASS_NAME + "ACTION_PASTE";
    public static final String ACTION_DELETE = CLASS_NAME + "ACTION_DELETE";
    public static final String SHOW_ADD_LIBRARY_DIALOG = CLASS_NAME + "SHOW_ADD_LIBRARY_DIALOG";
    public static final String ACTION_ADD_TO_LIBRARY = CLASS_NAME + "ACTION_ADD_TO_LIBRARY";
    public static final String ACTION_EDIT_PHYSICS = CLASS_NAME + "ACTION_EDIT_PHYSICS";
    public static final String ACTION_SET_GRID_SIZE_FROM_ITEM = CLASS_NAME + "ACTION_SET_GRID_SIZE_FROM_ITEM";
    public static final String ACTION_UPDATE_ITEM_DATA = CLASS_NAME + "ACTION_UPDATE_ITEM_DATA";
    public static final String ACTION_ITEMS_MOVE_TO = CLASS_NAME + "ACTION_ITEMS_MOVE_TO";

    public static final String ACTION_SET_SELECTION = CLASS_NAME + "ACTION_SET_SELECTION";
    public static final String ACTION_ADD_SELECTION = CLASS_NAME + "ACTION_ADD_SELECTION";
    public static final String ACTION_RELEASE_SELECTION = CLASS_NAME + "ACTION_RELEASE_SELECTION";

    public SceneControlMediator sceneControl;
    public ItemControlMediator itemControl;
    /**
     * this part contains legacy params that need to be removed one by one
     */
    public int currTransformType = -1;
    public Entity currTransformHost;
    public boolean isResizing = false;
    public boolean dirty = false;
    public Vector3 copedItemCameraOffset;
    public ArrayList<MainItemVO> tempClipboard;
    public String fakeClipboard;
    public String currentLoadedSceneFileName;
    private int gridSize = 1; // pixels
    private float zoomPercent = 100;
    private UIStage uiStage;
    private UserActionController uac;
    private ItemFactory itemFactory;
    private ItemSelector selector;
    private Overlap2DFacade facade;

    private ProjectManager projectManager;
    private ResourceManager resourceManager;
    
    
    public PixelRect selectionRec;
    //public Group mainBox = new Group();
    //public SandboxUI ui;
    //public Group frontUI;

	private Engine engine;
	private Array<InputListener> listeners = new Array<InputListener>(1);


    /**
     * end of shitty part
     */


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
    	 projectManager = facade.retrieveProxy(ProjectManager.NAME);
         resourceManager = facade.retrieveProxy(ResourceManager.NAME);
    	
        
        Overlap2D overlap2D = facade.retrieveProxy(Overlap2D.NAME);
        SandboxMediator sandboxStageMediator = facade.retrieveMediator(SandboxMediator.NAME);
        UIStageMediator uiStageMediator = facade.retrieveMediator(UIStageMediator.NAME);
        uiStage = uiStageMediator.getViewComponent();
        //setUIStage(uiStage);

        engine = new Engine();
        //TODO rendering system must be added in scene loader or idk where
        Overlap2dRenderer renderer = new Overlap2dRenderer(new SpriteBatch());
        engine.addSystem(renderer);
		SceneLoader sceneLoader = new SceneLoader(engine);
		sceneLoader.setResourceManager(resourceManager);
        sceneControl = new SceneControlMediator(sceneLoader);
        itemControl = new ItemControlMediator(sceneControl);

        uac = new UserActionController(this);
        selector = new ItemSelector(this);
        itemFactory = new ItemFactory(this);

       
    }
    
    public void initView() {
        //TODO fix and uncomment
//        getCamera().position.set(0, 0, 0);

        selectionRec = new PixelRect(0, 0);
        selectionRec.setFillColor(new Color(1, 1, 1, 0.1f));
        selectionRec.setOpacity(0.0f);
        selectionRec.setTouchable(Touchable.disabled);
        uiStage.sandBoxUIGroup.addActor(selectionRec);

    }
    
    public void setCursor(int cursor) {
        //UIController.instance.sendNotification(NameConstants.SET_CURSOR, cursor);
    }

    public void setKeyboardFocus() {
        uiStage.setKeyboardFocus(uiStage.sandBoxUIGroup);
    }
    

    /**
     * Getters *
     */

    public UserActionController getUac() {
        return uac;
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
    
    public Engine getEngine() {
        return engine;
    }


    /**
     * TODO: loading fonts this way is a bit outdated and needs to change
     *
     * @param sceneName
     */
    public void initData(String sceneName) {
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
      //TODO fix and uncomment
        //sceneDataManager.loadScene(sceneControl.getEssentials().rm.getSceneVO(sceneName), resolutionManager.currentResolutionName);

        sceneControl.initScene(sceneName);
    }

    public void loadCurrentProject(String name) {
    	//TODO fix and uncomment
        //sceneControl.getEssentials().rm = resourceManager;
        loadScene(name);
    }

    public void loadCurrentProject() {
        ProjectVO projectVO = projectManager.getCurrentProjectVO();
        loadCurrentProject(projectVO.lastOpenScene.isEmpty() ? "MainScene" : projectVO.lastOpenScene);
    }

    public void loadScene(String sceneName) {
        currentLoadedSceneFileName = sceneName;
//        uiStage.getCompositePanel().clearScenes();
        initData(sceneName);

        initView();
//        uiStage.getCompositePanel().addScene(sceneControl.getRootSceneVO());
        initSceneView(sceneControl.getRootSceneVO());
//        sandboxInputAdapter.initSandboxEvents();

        ProjectVO projectVO = projectManager.getCurrentProjectVO();
        projectVO.lastOpenScene = sceneName;
        projectManager.saveCurrentProject();
      //TODO fix and uncomment
        //sandboxStage.getCamera().position.set(0, 0, 0);

        facade.sendNotification(Overlap2D.LIBRARY_LIST_UPDATED);

        //TODO: move this into SceneDataManager!
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        sceneDataManager.sendNotification(SceneDataManager.SCENE_LOADED);
    }

    public void initSceneView(CompositeItemVO compositeItemVO) {
    	//TODO fix and uncomment
        //initSceneView(sceneControl.initSceneView(compositeItemVO));
    }

    public void initSceneView(Entity composite) {
    	//TODO fix and uncomment
//        selector.clearSelections();
//        sandboxStage.mainBox.clear();
//        sceneControl.initSceneView(composite, true/*uiStage.getCompositePanel().isRootScene()*/);
////        if (uiStage.getCompositePanel().isRootScene()) {
////            uiStage.getCompositePanel().updateRootScene(sceneControl.getRootSceneVO());
////        }
//
//        sandboxStage.mainBox.addActor(sceneControl.getCurrentScene());
//        sceneControl.getCurrentScene().setX(0);
//        sceneControl.getCurrentScene().setY(0);
//
//        //uiStage.getLayerPanel().initContent();
//        forceContinuousParticles(composite);
    }

    /**
     * if user is currently viewing a composite item, this will go one step up to previous composite in hierarchy
     */
    public void enterIntoPrevComposite() {
		//TODO fix and uncomment
        //sandboxStage.getCamera().position.set(0, 0, 0);
        facade.sendNotification(Overlap2D.OPENED_PREVIOUS_COMPOSITE);
    }

    /**
     * If current selection contains only one item that is a composite item,
     * it will call enterIntoComposite with that time as parameter
     */
    public void enterIntoComposite() {
    	//TODO fix and uncomment
//        CompositeItem item = null;
//        sceneControl.getCurrentScene().updateDataVO();
//        if (selector.getCurrentSelection().size() == 1) {
//            for (SelectionRectangle value : selector.getCurrentSelection().values()) {
//                if (value.getHost().isComposite()) {
//                    item = (CompositeItem) value.getHost();
//                }
//            }
//        }
//        if (item == null) return;
//        selector.clearSelections();
//        enterIntoComposite(item.getDataVO());
    }

    /**
     * Opens up provided composite item, sets it as a main view, and focuses editing on it's contents.
     *
     * @param compositeItemVO tools object of composite to enter into
     */
    public void enterIntoComposite(CompositeItemVO compositeItemVO) {
        //rootSceneVO.update(new CompositeItemVO(currentSceneVo.composite));
    	//TODO fix and uncomment
        //sandboxStage.getCamera().position.set(0, 0, 0);
        getSceneControl().disableAmbience(true);

        facade.sendNotification(Overlap2D.OPENED_COMPOSITE, compositeItemVO);
        //uiStage.getCompositePanel().addScene(compositeItemVO);

        initSceneView(compositeItemVO);
//        uiStage.getItemsBox().init();
    }

    /**
     * Some particle panels might not be continuous, so they will stop after first iteration, which is ok
     * This method will make sure they look continuous while in editor, so user will find and see them easily.
     *
     * @param composite composite on screen with particles to be forced to be continuous
     */
  //TODO fix and uncomment
//    private void forceContinuousParticles(CompositeItem composite) {
//        ArrayList<IBaseItem> asd = composite.getItems();
//        for (int i = 0; i < asd.size(); i++) {
//            IBaseItem item = asd.get(i);
//            if (item instanceof ParticleItem) {
//                ((ParticleItem) item).forceContinuous();
//                continue;
//            }
//            if (item instanceof CompositeItem) {
//                forceContinuousParticles((CompositeItem) item);
//            }
//
//        }
//    }

    /**
     * Well... that's a bummer, I cannot remember why this was for. but the name speaks for itself sort of.
     * TODO: figure this out
     *
     * @return SceneVO
     */
    public SceneVO sceneVoFromItems() {
        CompositeItemVO itemVo = sceneControl.getRootSceneVO();
      //TODO fix and uncomment
        //itemFactory.cleanComposite(itemVo.composite);
        sceneControl.getCurrentSceneVO().composite = itemVo.composite;
        return sceneControl.getCurrentSceneVO();
    }

    /**
     * Initializes current scene on screen from a tools object.
     *
     * @param vo CompositeItemVO tools
     */
    public void reconstructFromSceneVo(CompositeItemVO vo) {
        initSceneView(vo);
    }

    /**
     * TODO: what does this do? seems to be saving as checkpoint of Flow? it so it should be renamed
     */
    public void saveSceneCurrentSceneData() {
		//TODO fix and uncomment
        //sceneControl.getCurrentScene().updateDataVO();
    }

    public void setSceneAmbientColor(Color color, boolean showChange) {
        sceneControl.getCurrentSceneVO().ambientColor[0] = color.r;
        sceneControl.getCurrentSceneVO().ambientColor[1] = color.g;
        sceneControl.getCurrentSceneVO().ambientColor[2] = color.b;
        sceneControl.getCurrentSceneVO().ambientColor[3] = color.a;
      //TODO fix and uncomment
        //if (showChange) sceneControl.getEssentials().rayHandler.setAmbientLight(color);
    }

    public ItemSelector getSelector() {
        return selector;
    }

    /**
     * @deprecated
     * @return
     */
    public boolean isComponentSkinAvailable() {
        return true;
    }

    public LayerItemVO getSelectedLayer() {
        return uiStage.getCurrentSelectedLayer();
    }

    public void setCurrentlyTransforming(Entity item, int transformType) {
        if (item == null || item.getClass().getSimpleName().equals("LabelItem")) return;
        currTransformType = transformType;
        currTransformHost = item;
    }

    public Entity getCurrentScene() {
        return sceneControl.getCurrentScene();
    }

    public void enablePan() {
        //cameraPanOn = true;
        //selector.clearSelections();
        //isItemTouched = false;
    }

    public void prepareSelectionRectangle(float x, float y, boolean setOpacity) {
        // space is panning, so if we are not, then prepare the selection rectangle
        if (setOpacity) {
            selectionRec.setOpacity(0.6f);
        }
        selectionRec.setWidth(0);
        selectionRec.setHeight(0);
        selectionRec.setX(x);
        selectionRec.setY(y);
    }


    public int getZoomPercent() {
        return (int)zoomPercent;
    }

    public void setZoomPercent(float percent) {
    	//TODO fix and uncomment
//        zoomPercent = percent;
//        OrthographicCamera camera = (OrthographicCamera) (sandboxStage.getCamera());
//        camera.zoom = 1f / (zoomPercent / 100f);
    }

    public void zoomBy(float amount) {
        zoomPercent += -amount * 15f;

        if (zoomPercent < 20) zoomPercent = 20;
        if (zoomPercent > 1000) zoomPercent = 1000;

        setZoomPercent(zoomPercent);
        facade.sendNotification(Overlap2D.ZOOM_CHANGED);
    }

    public void zoomDevideBy(float amount) {

        zoomPercent /= amount;
        if (zoomPercent < 20) zoomPercent = 20;
        if (zoomPercent > 1000) zoomPercent = 1000;

        setZoomPercent(zoomPercent);
        facade.sendNotification(Overlap2D.ZOOM_CHANGED);
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
        facade.sendNotification(Overlap2D.GRID_SIZE_CHANGED, gridSize);
    }


    /**
     * TODO: put this in different place, not sandbox specific
     * @param items
     */
    public void putItemsToClipboard(ArrayList<Entity> items) {
    	//TODO fix and uncomment
//        CompositeVO tempHolder = new CompositeVO();
//        Json json = new Json();
//        json.setOutputType(JsonWriter.OutputType.json);
//        Actor actor = (Actor) items.get(0);
//        Vector3 cameraPos = ((OrthographicCamera) getSandboxStage().getCamera()).position;
//        Vector3 vector3 = new Vector3(actor.getX() - cameraPos.x, actor.getY() - cameraPos.y, 0);
//        for (IBaseItem item : items) {
//            tempHolder.addItem(item.getDataVO());
//            actor = (Actor) item;
//            if (actor.getX() - cameraPos.x < vector3.x) {
//                vector3.x = actor.getX() - cameraPos.x;
//            }
//            if (actor.getY() - cameraPos.y < vector3.y) {
//                vector3.y = actor.getY() - cameraPos.y;
//            }
//        }
//        fakeClipboard = json.toJson(tempHolder);
//        copedItemCameraOffset = vector3;
    }
    
    public Entity getRootEntity(){
    	return sceneControl.getRootEntity();
    }
    
    
    //Global Listeners part
    
    public void addListener(InputListener listener){
		if (!listeners.contains(listener, true)) {
			listeners.add(listener);
		}
		
	}
	
	public void removeListener(InputListener listener){
		listeners.removeValue(listener, true);
	}
	
	public void removeAllListener(){
		listeners.clear();
	}
	
	public Array<InputListener> getAllListeners(){
		listeners.shrink();
		return listeners;
	}
}
