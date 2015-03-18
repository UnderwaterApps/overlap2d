package com.uwsoft.editor.gdx.sandbox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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
import com.uwsoft.editor.gdx.ui.dialogs.InputDialog;
import com.uwsoft.editor.gdx.ui.layer.LayerItem;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.actor.LightActor;
import com.uwsoft.editor.renderer.actor.ParticleItem;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;

import java.util.ArrayList;
import java.util.HashMap;
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

    private InputHandler inputHandler;
    private UserActionController uac;
    private ItemFactory itemFactory;

    private HashMap<IBaseItem, SelectionRectangle> currentSelection = new HashMap<IBaseItem, SelectionRectangle>();

    private InputMultiplexer inputMultiplexer;

    public FlowManager flow;

    /**
     * this part is to be modified
     */
    private int currTransformType = -1;
    private IBaseItem currTransformHost;
    private boolean isResizing = false;
    private boolean isUsingSelectionTool = true;
    private boolean isItemTouched = false;
    private boolean dirty = false;
    private Vector3 copedItemCameraOffset;
    private IResourceRetriever rm;
    private  ArrayList<MainItemVO> tempClipboard;
    private String fakeClipboard;
    private String currentLoadedSceneFileName;

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
        inputHandler = new InputHandler();
        uac = new UserActionController(this);
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

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public void initData(String sceneName) {
        DataManager.getInstance().preloadSceneSpecificData(sceneControl.getEssentials().rm.getSceneVO(sceneName), DataManager.getInstance().curResolution);

        sceneControl.initScene(sceneName);

        flow = new FlowManager(sceneControl.getRootSceneVO());
    }

    public void initSceneView(CompositeItemVO compositeItemVO) {
        initSceneView(sceneControl.initSceneView(compositeItemVO));
    }

    public void initSceneView(CompositeItem composite) {
        clearSelections();
        sandboxStage.mainBox.clear();
        sceneControl.initSceneView(composite, uiStage.getCompositePanel().isRootScene());
        if (uiStage.getCompositePanel().isRootScene()) {
            uiStage.getCompositePanel().updateRootScene(sceneControl.getRootSceneVO());
        }
        for (int i = 0; i < sceneControl.getCurrentScene().getItems().size(); i++) {
            inputHandler.initItemListeners(sceneControl.getCurrentScene().getItems().get(i));
        }
        sandboxStage.mainBox.addActor(sceneControl.getCurrentScene());
        sceneControl.getCurrentScene().setX(0);
        sceneControl.getCurrentScene().setY(0);

        uiStage.getLayerPanel().initContent();
        forceContinuousParticles(composite);
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

    public void selectItemsByLayerName(String name) {
        ArrayList<IBaseItem> itemsArr = new ArrayList<IBaseItem>();
        for (int i = 0; i < sceneControl.getCurrentScene().getItems().size(); i++) {
            if (sceneControl.getCurrentScene().getItems().get(i).getDataVO().layerName.equals(name)) {
                itemsArr.add(sceneControl.getCurrentScene().getItems().get(i));
            }
        }

        setSelections(itemsArr, true);
    }


    public void setSelection(IBaseItem item, boolean removeOthers) {
        if (currentSelection.get(item) != null) return;

        if (removeOthers) clearSelections();

        SelectionRectangle rect = new SelectionRectangle(sandboxStage);
        rect.claim(item);
        rect.setMode(editingMode);
        currentSelection.put(item, rect);
        sandboxStage.frontUI.addActor(rect);
        rect.show();
        sandboxStage.uiStage.itemWasSelected(item);

        if (sandboxStage.frontUI.dropDown != null) {
            sandboxStage.frontUI.dropDown.remove();
            sandboxStage.frontUI.dropDown = null;
        }

        sandboxStage.uiStage.getItemsBox().setSelected(currentSelection);

    }

    private void releaseSelection(IBaseItem item) {
        currentSelection.get(item).remove();
        currentSelection.remove(item);

        sandboxStage.uiStage.getItemsBox().setSelected(currentSelection);
    }

    private void clearSelections() {
        for (SelectionRectangle value : currentSelection.values()) {
            value.remove();
        }

        currentSelection.clear();
        sandboxStage.uiStage.getItemsBox().setSelected(currentSelection);
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

    private void moveSelectedItemsBy(float x, float y) {
        for (SelectionRectangle selectionRect : currentSelection.values()) {
            itemControl.moveItemBy(selectionRect.getHostAsActor(), x, y);

            selectionRect.setX(selectionRect.getX() + x);
            selectionRect.setY(selectionRect.getY() + y);
        }

        saveSceneCurrentSceneData();
    }

    private void removeCurrentSelectedItems() {
        for (SelectionRectangle selectionRect : currentSelection.values()) {
            itemControl.removeItem(selectionRect.getHostAsActor());
            selectionRect.remove();
        }
        sandboxStage.uiStage.getItemsBox().initContent();
        currentSelection.clear();
    }



    public void getIntoPrevComposite() {
        sandboxStage.getCamera().position.set(0, 0, 0);
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

        InputDialog dlg = uiStage.dialogs().showInputDialog();

        dlg.setDescription("Please set unique name for your component");

        final CompositeItem itemToAdd = item;

        dlg.setListener(new InputDialog.InputDialogListener() {

            @Override
            public void onConfirm(String input) {
                sceneControl.getCurrentSceneVO().libraryItems.put(input, itemToAdd.getDataVO());
                uiStage.reInitLibrary();
            }
        });


    }

    public void getIntoComposite() {
        CompositeItem item = null;
        sceneControl.getCurrentScene().updateDataVO();
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
        sandboxStage.getCamera().position.set(0, 0, 0);
        sandboxStage.disableAmbience(true);
        uiStage.getLightBox().disableAmbiance.setChecked(true);
        uiStage.getCompositePanel().addScene(compositeItemVO);
        initSceneView(compositeItemVO);
        uiStage.getItemsBox().initContent();
    }

    public void copyCurrentSelection() {
        ArrayList<MainItemVO> voList = new ArrayList<>();
        for (int i = 0; i < sceneControl.getCurrentScene().getItems().size(); i++) {
            voList.add(sceneControl.getCurrentScene().getItems().get(i).getDataVO());
        }

        //TODO: change this to real clipboard
        tempClipboard = voList;
    }

    public void pastClipBoard() {
        //TODO: duplicate item here
    }

    public CompositeItem groupItemsIntoComposite() {
        sceneControl.getCurrentScene().updateDataVO();
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

        CompositeItem item = sceneControl.getCompositeElement(vo);

        item.setWidth(width);
        item.setHeight(height);

        removeCurrentSelectedItems();

        sceneControl.getCurrentScene().addItem(item);

        inputHandler.initItemListeners(item);
        uiStage.getItemsBox().initContent();
        setSelection(item, true);

        return item;
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


    public EditingMode getCurrentMode() {
        return editingMode;
    }

    public void setCurrentMode(EditingMode currentMode) {
        this.editingMode = currentMode;
        for (SelectionRectangle value : currentSelection.values()) {
            value.setMode(currentMode);
        }
    }


    public SceneVO sceneVoFromItems() {
        CompositeItemVO itemVo = sceneControl.getRootSceneVO();
        cleanComposite(itemVo.composite);
        sceneControl.getCurrentSceneVO().composite = itemVo.composite;
        return sceneControl.getCurrentSceneVO();
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
        sceneControl.getCurrentScene().updateDataVO();
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
        sceneControl.getCurrentScene().updateDataVO();
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
        sceneControl.getCurrentScene().updateDataVO();
        ArrayList<IBaseItem> items = getSelectedItems();
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
            inputHandler.initItemListeners(itm);
            finalItems.add(itm);
        }

        setSelections(finalItems, true);
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
        inputHandler.initSandboxEvents();
    }


    public void setCurrentlyTransforming(IBaseItem item, int transformType) {
        if (item == null || item.getClass().getSimpleName().equals("LabelItem")) return;
        currTransformType = transformType;
        currTransformHost = item;
    }
}
