package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.controlles.flow.FlowActionEnum;
import com.uwsoft.editor.controlles.flow.FlowManager;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.manager.SandboxResourceManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.mediators.ItemControlMediator;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.actor.ParticleItem;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by CyberJoe on 3/18/2015.
 */
public class Sandbox {

    public EditingMode editingMode;

    public SceneControlMediator sceneControl;
    public ItemControlMediator itemControl;

    private SandboxStage sandboxStage;
    private UIStage uiStage;

    private SandboxInputAdapter sandboxInputAdapter;
    private UserActionController uac;
    private ItemFactory itemFactory;
    private ItemSelector selector;

    private InputMultiplexer inputMultiplexer;

    public FlowManager flow;

	 public TransformationHandler transformationHandler;

    /**
     * this part is to be modified
     */
    public int currTransformType = -1;
    public IBaseItem currTransformHost;
    public boolean isResizing = false;
    public boolean isUsingSelectionTool = true;
    public boolean isItemTouched = false;
    public boolean dirty = false;
    public Vector3 copedItemCameraOffset;
    public IResourceRetriever rm;
    public  ArrayList<MainItemVO> tempClipboard;
    public String fakeClipboard;
    public String currentLoadedSceneFileName;
    public boolean cameraPanOn;

    public Sandbox() {

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
    }

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

    public SandboxInputAdapter getSandboxInputAdapter () {
        return sandboxInputAdapter;
    }

	 public EditingMode getCurrentMode() {
		  return editingMode;
	 }



    public void initData(String sceneName) {
        DataManager.getInstance().preloadSceneSpecificData(sceneControl.getEssentials().rm.getSceneVO(sceneName), DataManager.getInstance().curResolution);

        sceneControl.initScene(sceneName);

        flow = new FlowManager(sceneControl.getRootSceneVO());
    }


	 public void loadCurrentProject(String name) {
		  rm = new SandboxResourceManager();
		  sceneControl.getEssentials().rm = rm;
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

		  sandboxStage.initView();
		  uiStage.getCompositePanel().addScene(sceneControl.getRootSceneVO());
		  initSceneView(sceneControl.getRootSceneVO());
		  initEvents();

		  ProjectVO projectVO = DataManager.getInstance().getCurrentProjectVO();
		  projectVO.lastOpenScene = sceneName;
		  DataManager.getInstance().saveCurrentProject();
		  sandboxStage.getCamera().position.set(0, 0, 0);

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





	 public void enterIntoPrevComposite () {
		  sandboxStage.getCamera().position.set(0, 0, 0);
		  uiStage.getCompositePanel().stepUp();
		  uiStage.getItemsBox().initContent();
	 }



	 public void enterIntoComposite () {
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

	 public void enterIntoComposite (CompositeItemVO compositeItemVO) {
		  //rootSceneVO.update(new CompositeItemVO(currentSceneVo.composite));
		  sandboxStage.getCamera().position.set(0, 0, 0);
		  sandboxStage.disableAmbience(true);
		  uiStage.getLightBox().disableAmbiance.setChecked(true);
		  uiStage.getCompositePanel().addScene(compositeItemVO);
		  initSceneView(compositeItemVO);
		  uiStage.getItemsBox().initContent();
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


    public void setCurrentMode(EditingMode currentMode) {
        this.editingMode = currentMode;
        for (SelectionRectangle value : selector.getCurrentSelection().values()) {
            value.setMode(currentMode);
        }
    }


    public SceneVO sceneVoFromItems() {
        CompositeItemVO itemVo = sceneControl.getRootSceneVO();
        itemFactory.cleanComposite(itemVo.composite);
        sceneControl.getCurrentSceneVO().composite = itemVo.composite;
        return sceneControl.getCurrentSceneVO();
    }

    public void reconstructFromSceneVo(CompositeItemVO vo) {
        initSceneView(vo);
    }

    public void undo() {
        FlowActionEnum lastFlowAction = flow.getFlowLastAction();
        CompositeItemVO compositeItemVO = flow.undo();
        switch (lastFlowAction) {
            case GET_INTO_COMPOSITE:
                enterIntoPrevComposite();
                break;
            case GET_OUT_COMPOSITE:
                enterIntoComposite(compositeItemVO);
                break;
            default:
                reconstructFromSceneVo(compositeItemVO);
                break;
        }
        sceneControl.getCurrentScene().updateDataVO();
    }

    public void redo() {
        CompositeItemVO compositeItemVO = flow.redo();
        FlowActionEnum lastFlowAction = flow.getFlowLastAction();
        switch (lastFlowAction) {
            case GET_INTO_COMPOSITE:
                enterIntoComposite(compositeItemVO);
                break;
            case GET_OUT_COMPOSITE:
                enterIntoPrevComposite();
                break;
            default:
                reconstructFromSceneVo(compositeItemVO);
                break;
        }
        sceneControl.getCurrentScene().updateDataVO();
    }


    public void cutAction() {
        ArrayList<IBaseItem> items = selector.getSelectedItems();
        putItemsToClipboard(items);
        selector.removeCurrentSelectedItems();
    }

    public void copyAction() {
        sceneControl.getCurrentScene().updateDataVO();
        ArrayList<IBaseItem> items = selector.getSelectedItems();
        putItemsToClipboard(items);
    }

    public void pasteAction(float x, float y, boolean ignoreCameraPos) {
        CompositeVO tempHolder;
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        tempHolder = json.fromJson(CompositeVO.class, fakeClipboard);

        if (tempHolder == null) return;

        CompositeItemVO fakeVO = new CompositeItemVO();

        fakeVO.composite = tempHolder;
        CompositeItem fakeItem = new CompositeItem(fakeVO, sceneControl.getEssentials());

        ArrayList<IBaseItem> finalItems = new ArrayList<IBaseItem>();
        Actor firstItem = (Actor) fakeItem.getItems().get(0);
        float offsetX = firstItem.getX()*sceneControl.getCurrentScene().mulX;
        float offsetY = firstItem.getY()*sceneControl.getCurrentScene().mulY;
        for (int i = 1; i < fakeItem.getItems().size(); i++) {
            Actor item = (Actor) fakeItem.getItems().get(i);
            if (item.getX()*sceneControl.getCurrentScene().mulX < offsetX) {
                offsetX = item.getX()*sceneControl.getCurrentScene().mulX;
            }
            if (item.getY()*sceneControl.getCurrentScene().mulY < offsetY) {
                offsetY = item.getY()*sceneControl.getCurrentScene().mulY;
            }
        }
        Vector3 cameraPos = ignoreCameraPos ? new Vector3(0, 0, 0) : ((OrthographicCamera) sandboxStage.getCamera()).position;
        for (int i = 0; i < fakeItem.getItems().size(); i++) {
            IBaseItem itm = fakeItem.getItems().get(i);
            itm.getDataVO().layerName = uiStage.getCurrentSelectedLayer().layerName;
            sceneControl.getCurrentScene().addItem(itm);
            ((Actor) itm).setX(x + ((Actor) itm).getX() - offsetX + (cameraPos.x + copedItemCameraOffset.x));
            ((Actor) itm).setY(y + ((Actor) itm).getY() - offsetY + (cameraPos.y + copedItemCameraOffset.y));
            itm.updateDataVO();
            sandboxInputAdapter.initItemListeners(itm);
            finalItems.add(itm);
        }

        selector.setSelections(finalItems, true);
        uiStage.getItemsBox().initContent();
    }

    private void putItemsToClipboard(ArrayList<IBaseItem> items) {
        CompositeVO tempHolder = new CompositeVO();
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        Actor actor = (Actor) items.get(0);
        Vector3 cameraPos = ((OrthographicCamera) sandboxStage.getCamera()).position;
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
        if (TextureManager.getInstance().projectSkin == null) {
            return false;
        }

        return true;
    }


    public LayerItemVO getSelectedLayer() {
        return uiStage.getCurrentSelectedLayer();
    }

    public void initEvents() {
        sandboxInputAdapter.initSandboxEvents();
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
		  getSandboxStage().frontUI.showDropDownForSelection(x, y);

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
}
