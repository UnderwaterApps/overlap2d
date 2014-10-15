package com.uwsoft.editor.renderer.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.uwsoft.editor.renderer.data.ButtonVO;
import com.uwsoft.editor.renderer.data.CheckBoxVO;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.Image9patchVO;
import com.uwsoft.editor.renderer.data.LabelVO;
import com.uwsoft.editor.renderer.data.LayerItemVO;
import com.uwsoft.editor.renderer.data.LightVO;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.data.PhysicsBodyDataVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.data.SelectBoxVO;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.data.SpineVO;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.data.TextBoxVO;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.script.IScript;
import com.uwsoft.editor.renderer.utils.CustomVariables;

/**
 * ContainerActor that can have other children inside
 * Also used as the root actor for every scene
 * Sort of a Group with extended capabilities for searching items by id specified from editor
 * attaching IScripts to items and much more
 * You are not encouraged to create instances of this, it is solely tob e instantiated by Overlap2DRuntime inner workings
 *
 * @author Avetis Zakharyan | http://www.overlap2d.com
 */
public class CompositeItem extends Group implements IBaseItem {

    public CompositeItemVO dataVO;
    public float mulX = 1f;
    public float mulY = 1f;
    public Essentials essentials;
    protected int layerIndex = 0; //this is for internal use only, for sorting
    private ArrayList<IBaseItem> items = new ArrayList<IBaseItem>();

    private HashMap<String, IBaseItem> itemIdMap = new HashMap<String, IBaseItem>();
    private HashMap<String, LayerItemVO> layerMap = new HashMap<String, LayerItemVO>();
    private HashMap<String, ArrayList<IBaseItem>> itemLayerMap = new HashMap<String, ArrayList<IBaseItem>>();

    private boolean isLockedByLayer = false;
    private CompositeItem parentItem = null;

    private CustomVariables customVariables = new CustomVariables();

    private Comparator<IBaseItem> ZIndexComparator = new Comparator<IBaseItem>() {

        @Override
        public int compare(IBaseItem obj1, IBaseItem obj2) {
            return obj1.getLayerIndex() == obj2.getLayerIndex() ? obj1.getDataVO().zIndex - obj2.getDataVO().zIndex : obj1.getLayerIndex() - obj2.getLayerIndex();
            //return obj1.getLayerIndex() == obj2.getLayerIndex()? ((Actor)obj1).getZIndex()-((Actor)obj2).getZIndex() : obj1.getLayerIndex() - obj2.getLayerIndex();
        }
    };

    private ArrayList<IScript> scripts = new ArrayList<IScript>(3);
    private Rectangle scissorBounds;

    private Body body;

    public CompositeItem(CompositeItemVO vo, Essentials essentials, CompositeItem parent) {
        this(vo, essentials);
        setParentItem(parent);
    }

    public CompositeItem(CompositeItemVO vo, Essentials essentials) {
        this.essentials = essentials;
        dataVO = vo;
        setX(dataVO.x);
        setY(dataVO.y);
        setScaleX(dataVO.scaleX);
        setScaleY(dataVO.scaleY);
        customVariables.loadFromString(dataVO.customVars);
        this.setRotation(dataVO.rotation);

        if (dataVO.zIndex < 0) dataVO.zIndex = 0;

        if (dataVO.tint == null) {
            setTint(new Color(1, 1, 1, 1));
        } else {
            setTint(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
        }

        dataVO = vo;
        reAssemble();
        //this is for testing don't delete
//		if(dataVO.itemIdentifier.equals("asd")){
//			setScissors(50, 50, 160, 160);
//		}
    }

    /**
     * Sets tint color for composite and it's children
     * @param tint
     */
    public void setTint(Color tint) {
        float[] clr = new float[4];
        clr[0] = tint.r;
        clr[1] = tint.g;
        clr[2] = tint.b;
        clr[3] = tint.a;
        this.getDataVO().tint = clr;
        this.setColor(tint);
    }

    /**
     *
     * @return CompositeItem parent of this Composite
     * If this is a root item in scene, returns null
     */
    public CompositeItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(CompositeItem parentItem) {
        this.parentItem = parentItem;
    }

    /**
     * Attaches iScript instance to this CompositeItem
     * @param iScript - data and logic
     */
    public void addScript(IScript iScript) {
        scripts.add(iScript);
        iScript.init(this);
    }

    /**
     *  Attaches iScript instance to this CompositeItem
     * @param iScripts - ArrayList<IScript>
     */
    public void addScript(ArrayList<IScript> iScripts) {
        scripts.addAll(iScripts);
        for (IScript iScript : iScripts) {
            iScript.init(this);
        }
    }

    /**
     * Ads array of scripts to all children composite items that are
     * all instances of same prefab library name
     *
     * @param name - String library name
     * @param iScripts
     */
    public void addScriptTo(String name, ArrayList<IScript> iScripts) {
        for (IBaseItem itm : items) {
            if (itm instanceof CompositeItem) {
                if (itm.getDataVO().itemName.equals(name)) {
                    ((CompositeItem) itm).addScript(iScripts);
                }
                ((CompositeItem) itm).addScriptTo(name, iScripts);
            }
        }
    }

    /**
     * Ads iScript instance to all children composite items that are
     * all instances of same prefab library name
     *
     * @param name - String library name
     * @param iScript
     */
    public void addScriptTo(String name, IScript iScript) {
        for (IBaseItem itm : items) {
            if (itm instanceof CompositeItem) {
                if (itm.getDataVO().itemName.equals(name)) {
                    ((CompositeItem) itm).addScript(iScript);
                }
                ((CompositeItem) itm).addScriptTo(name, iScript);
            }
        }
    }

    /**
     * Creates instances of provided iScript class for each item
     * that is found in children composite items with the name
     * equal to the library name they have been all instantiated from as a prefab
     * uses libGDX Reflection
     *
     * @param name - String library name
     * @param iScript - Class object
     */
    public void addScriptTo(String name, Class<? extends IScript> iScript) {
        for (IBaseItem itm : items) {
            if (itm instanceof CompositeItem) {
                if (itm.getDataVO().itemName.equals(name)) {
                    IScript instance = null;
                    try {
                        instance = ClassReflection.newInstance(iScript);
                    } catch (ReflectionException e) {
                        e.printStackTrace();
                    }
                    if (instance != null) {
                        ((CompositeItem) itm).addScript(instance);
                    }

                }
                ((CompositeItem) itm).addScriptTo(name, iScript);
            }

        }
    }

    /**
     * removes iscript from this item
     * @param iScript
     */
    public void removeScript(IScript iScript) {
        scripts.remove(iScript);
        iScript.dispose();
    }

    /**
     * removes provided iScripts from this item
     * @param iScripts
     */
    public void removeScript(ArrayList<IScript> iScripts) {
        scripts.removeAll(iScripts);
        for (IScript iScript : iScripts) {
            iScript.dispose();
        }
    }

    /**
     * Removes provided iScripts from all the children items with the name
     * equal to the library name this items were instantiated from
     * @param name
     * @param iScripts
     */
    public void removeScript(String name, ArrayList<IScript> iScripts) {
        for (IBaseItem itm : items) {
            if (itm instanceof CompositeItem) {
                if (itm.getDataVO().itemName.equals(name)) {
                    ((CompositeItem) itm).removeScript(iScripts);
                }
                ((CompositeItem) itm).removeScript(name, iScripts);
            }
        }
    }

    private void reAssemble() {
        clear();
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                items.get(i).dispose();
            }
        }
        items.clear();
        itemIdMap.clear();
        itemLayerMap.clear();

        for (int i = 0; i < dataVO.composite.sImages.size(); i++) {
            SimpleImageVO tmpVo = dataVO.composite.sImages.get(i);
            ImageItem itm = new ImageItem(tmpVo, essentials, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(tmpVo.zIndex);
        }

        for (int i = 0; i < dataVO.composite.sImage9patchs.size(); i++) {
            Image9patchVO tmpVo = dataVO.composite.sImage9patchs.get(i);
            Image9patchItem itm = new Image9patchItem(tmpVo, essentials, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(tmpVo.zIndex);
        }

        for (int i = 0; i < dataVO.composite.sTextBox.size(); i++) {
            TextBoxVO tmpVo = dataVO.composite.sTextBox.get(i);
            TextBoxItem itm = new TextBoxItem(tmpVo, essentials, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }
        for (int i = 0; i < dataVO.composite.sButtons.size(); i++) {
            ButtonVO tmpVo = dataVO.composite.sButtons.get(i);
            TextButtonItem itm = new TextButtonItem(tmpVo, essentials, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }
        for (int i = 0; i < dataVO.composite.sLabels.size(); i++) {
            LabelVO tmpVo = dataVO.composite.sLabels.get(i);
            LabelItem itm = new LabelItem(tmpVo, essentials, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }
        for (int i = 0; i < dataVO.composite.sCheckBoxes.size(); i++) {
            CheckBoxVO tmpVo = dataVO.composite.sCheckBoxes.get(i);
            CheckBoxItem itm = new CheckBoxItem(tmpVo, essentials, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }
        for (int i = 0; i < dataVO.composite.sSelectBoxes.size(); i++) {
            SelectBoxVO tmpVo = dataVO.composite.sSelectBoxes.get(i);
            SelectBoxItem itm = new SelectBoxItem(tmpVo, essentials, this);    //TODO need to fix this
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }

        for (int i = 0; i < dataVO.composite.sComposites.size(); i++) {
            CompositeItemVO tmpVo = dataVO.composite.sComposites.get(i);
            CompositeItem itm = new CompositeItem(tmpVo, essentials, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }


        for (int i = 0; i < dataVO.composite.sParticleEffects.size(); i++) {
            ParticleEffectVO tmpVo = dataVO.composite.sParticleEffects.get(i);
            ParticleItem itm = new ParticleItem(tmpVo, essentials, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }

        if (essentials.rayHandler != null) {
            for (int i = 0; i < dataVO.composite.sLights.size(); i++) {
                LightVO tmpVo = dataVO.composite.sLights.get(i);
                LightActor itm = new LightActor(tmpVo, essentials, this);
                inventorize(itm);
                addActor(itm);
            }
        }

        if (essentials.spineReflectionHelper != null) {
            for (int i = 0; i < dataVO.composite.sSpineAnimations.size(); i++) {
                SpineVO tmpVo = dataVO.composite.sSpineAnimations.get(i);
                SpineActor itm = new SpineActor(tmpVo, essentials, this);
                inventorize(itm);
                addActor(itm);
                itm.setZIndex(itm.dataVO.zIndex);
            }
        }

        for (int i = 0; i < dataVO.composite.sSpriteAnimations.size(); i++) {
            SpriteAnimationVO tmpVo = dataVO.composite.sSpriteAnimations.get(i);
            SpriteAnimation itm = new SpriteAnimation(tmpVo, essentials, this);
            inventorize(itm);
            itm.start();
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }

        if (dataVO.composite.layers.size() == 0) {
            LayerItemVO layerVO = new LayerItemVO();
            layerVO.layerName = "Default";
            dataVO.composite.layers.add(layerVO);
        }

        recalculateSize();
        sortZindexes();
        reAssembleLayers();
    }
    
    

    @Override
	protected void setStage(Stage stage) {
		super.setStage(stage);
        if(stage != null)
		    assemblePhysics();
	}

	private void inventorize(IBaseItem itm) {
        String id = itm.getDataVO().itemIdentifier;
        String layerName = itm.getDataVO().layerName;

        if (id.length() > 0) {
            itemIdMap.put(id, itm);
        }
        if (layerName.length() > 0) {
            if (!itemLayerMap.containsKey(layerName)) {
                itemLayerMap.put(layerName, new ArrayList<IBaseItem>());
            }
            itemLayerMap.get(layerName).add(itm);
        }
        //all items
        items.add(itm);
    }

    /**
     * Finds all children items by name, goes deep recursively
     *
     * @note name is not the identifier
     * @param itemName - String name of library item that searchable children have been instance of
     * @return
     */
    public ArrayList<CompositeItem> findItemsByName(String itemName) {
        ArrayList<CompositeItem> initialArray = new ArrayList<CompositeItem>();

        return findItemsByName(itemName, this, initialArray);
    }

    private ArrayList<CompositeItem> findItemsByName(String itemName, CompositeItem itm, ArrayList<CompositeItem> recursiveArray) {
        for (int i = 0; i < itm.items.size(); i++) {
            if (itm.items.get(i).getClass().getSimpleName().equals("CompositeItem")) {
                CompositeItem tmp = (CompositeItem) itm.items.get(i);
                if (tmp.dataVO.itemName.equals(itemName)) {
                    recursiveArray.add(tmp);
                }
                findItemsByName(itemName, tmp, recursiveArray);
            }
        }

        return recursiveArray;
    }

    public CheckBoxItem getCheckBoxById(String itemId) {
        return (CheckBoxItem) itemIdMap.get(itemId);
    }

    public CompositeItem getCompositeById(String itemId) {
        return (CompositeItem) itemIdMap.get(itemId);
    }

    public IBaseItem getItemById(String itemId) {
        return itemIdMap.get(itemId);
    }

    public ImageItem getImageById(String itemId) {
        return (ImageItem) itemIdMap.get(itemId);
    }

    public LabelItem getLabelById(String itemId) {
        return (LabelItem) itemIdMap.get(itemId);
    }

    public ParticleItem getParticleById(String itemId) {
        return (ParticleItem) itemIdMap.get(itemId);
    }

    public SelectBoxItem getSelectBoxById(String itemId) {
        return (SelectBoxItem) itemIdMap.get(itemId);
    }

    public TextBoxItem getTextBoxById(String itemId) {
        return (TextBoxItem) itemIdMap.get(itemId);
    }

    public TextButtonItem getTextButtonById(String itemId) {
        return (TextButtonItem) itemIdMap.get(itemId);
    }

    public LightActor getLightActorById(String itemId) {
        return (LightActor) itemIdMap.get(itemId);
    }

    public SpriteAnimation getSpriteAnimationById(String itemId) {
        return (SpriteAnimation) itemIdMap.get(itemId);
    }

    public SpineActor getSpineActorById(String itemId) {
        return (SpineActor) itemIdMap.get(itemId);
    }

    public Image9patchItem getNinePatchById(String itemId) {
        return (Image9patchItem) itemIdMap.get(itemId);
    }

    /**
    * Ads item inside the composite and reassebmles everything
    * It is not reccommended for general use, it is better to have items already there from editor
    * But if you need dynamic one, then it has to be IBaseItem
    **/
    public void addItem(IBaseItem item) {
        dataVO.composite.addItem(item.getDataVO());
        item.setParentItem(this);
        inventorize(item);

        addActor((Actor) item);
        
        item.updateDataVO();
        item.applyResolution(mulX, mulY);
        recalculateSize();
        sortZindexes();
        reAssembleLayers();

        //apply physics
        if(item.getDataVO().physicsBodyData != null && item.getDataVO().meshId >= 0) {
        	Vector2 toStageVec = new Vector2();
    		toStageVec.set(item.getDataVO().x * this.mulX, item.getDataVO().y * this.mulY);
            localToStageCoordinates(toStageVec);
            toStageVec.scl(PhysicsBodyLoader.SCALE);

            if(item.getBody() != null){
                essentials.world.destroyBody(item.getBody());
                item.setBody(null);
            }
            
            item.setBody(PhysicsBodyLoader.createBody(essentials.world, item.getDataVO().physicsBodyData, essentials.rm.getProjectVO().meshes.get(item.getDataVO().meshId), new Vector2(mulX, mulY)));
            item.getBody().setTransform(toStageVec.x, toStageVec.y, (float) Math.toRadians(item.getDataVO().rotation));
        }
    }

    /**
     * Loads CompositeItem data from provided data class
     * @param vo
     */
    public void loadFromVO(CompositeItemVO vo) {
        dataVO = vo;
        reAssemble();
        recalculateSize();
        sortZindexes();
        reAssembleLayers();
    }

    /**
     *
     * @return all children items of composite
     */
    public ArrayList<IBaseItem> getItems() {
        return items;
    }

    /**
     * If changes are made in layer ordering
     * then this method call will reassemble all zIndexes to take actual effect
     */
    public void reAssembleLayers() {
        LayerItemVO layer = null;

        layerMap.clear();

        for (int i = 0; i < dataVO.composite.layers.size(); i++) {
            layer = dataVO.composite.layers.get(i);
            setLayerChildrenVisibilty(layer.layerName, layer.isVisible);
            setLayerChildrenLock(layer.layerName, layer.isLocked || !layer.isVisible);

            layerMap.put(layer.layerName, layer);
        }
    }

    private void assemblePhysics() {
        if(essentials.world == null) return;
        Vector2 mulVec = new Vector2(mulX, mulY);
        for(IBaseItem item: items) {
        	if(item.getBody() != null){
        		essentials.world.destroyBody(item.getBody());
                item.setBody(null);
        	}
            MainItemVO itemVO = item.getDataVO();
            PhysicsBodyDataVO bodyData = itemVO.physicsBodyData;

            if( itemVO.meshId < 0 || bodyData == null) continue;
            
            item.setBody(PhysicsBodyLoader.createBody(essentials.world, bodyData, essentials.rm.getProjectVO().meshes.get(itemVO.meshId+""), mulVec));
            item.getBody().setUserData(item);
        }
        positionPhysics();
    }
    
    public void positionPhysics() {
    	Vector2 toStageVec = new Vector2();
    	for(IBaseItem item: items) {
    		if(item.getBody() == null){
    			continue;
    		}
    		MainItemVO itemVO = item.getDataVO();
    		toStageVec.set(itemVO.x * this.mulX, itemVO.y * this.mulY);
            localToStageCoordinates(toStageVec);
            toStageVec.scl(PhysicsBodyLoader.SCALE);
            item.getBody().setTransform(toStageVec.x,toStageVec.y, (float) Math.toRadians(item.getDataVO().rotation));
    	}
	}

    private void recalculateSize() {
        float lowerX = 0, lowerY = 0, upperX = 0, upperY = 0;
        for (int i = 0; i < items.size(); i++) {
            Actor value = (Actor) items.get(i);
            if (i == 0) {
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

        setWidth(upperX - 0);
        setHeight(upperY - 0);
    }

    /**
     * If changes are made in layer ordering
     * then this method call will reassemble all zIndexes to take actual effect
     */
    public void sortZindexes() {
        if (items == null) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setLayerIndex(getlayerIndexByName(items.get(i).getDataVO().layerName));
        }
        Collections.sort(items, ZIndexComparator);
        for (int i = 0; i < items.size(); i++) {
            IBaseItem value = items.get(i);
            if (value.getDataVO().zIndex < 0) {
                value.getDataVO().zIndex = 0;
            }
            ((Actor) value).setZIndex(i);
            value.getDataVO().zIndex = i;
        }
    }

    private int getlayerIndexByName(String layerName) {
        ArrayList<LayerItemVO> layers = this.dataVO.composite.layers;
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).layerName.equals(layerName)) {
                return i;
            }
        }
        return 0;
    }

    /**
     *
     * @return data of current composite item
     */
    public CompositeItemVO getDataVO() {
        //updateDataVO();
        return dataVO;
    }

    /**
     * Removes particular child
     *
     * @param item
     */
    public void removeItem(IBaseItem item) {
        items.remove(item);
        dataVO.composite.removeItem(item.getDataVO());
        item.dispose();
    }

    /**
     * duh...
     *
     * @return true
     */
    public boolean isComposite() {
        return true;
    }

    /**
     * Applies new resolution to this item and it's children recursively
     * Everything will chaneg in size, coordinates will be recalculated accordingly
     *
     * @param resolutionName - String name
     */
    public void applyResolution(String resolutionName) {
        if (resolutionName != null && !resolutionName.isEmpty()) {
            ProjectInfoVO projectVO = essentials.rm.getProjectVO();
            ResolutionEntryVO curResolution = projectVO.getResolution(resolutionName);
            if (curResolution != null) {
                float mulX = (float) curResolution.width / (float) essentials.rm.getProjectVO().originalResolution.width;
                applyResolution(mulX, mulX);
            }

        }
    }

    /**
     * @param mulX
     * @param mulY
     */
    public void applyResolution(float mulX, float mulY) {
    	this.mulX = mulX;
        this.mulY = mulY;
        for (int i = 0; i < items.size(); i++) {
            items.get(i).applyResolution(mulX, mulY);
            if(items.get(i).getBody() != null) {
                essentials.world.destroyBody(items.get(i).getBody());
                items.get(i).setBody(null);
            }
        }
        if(getBody() != null) essentials.world.destroyBody(getBody());
        setBody(null);
        
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        updateDataVO();
        recalculateSize();
        assemblePhysics();
    }

    @Override
    public void act(float delta) {
        for (int i = 0; i < scripts.size(); i++) {
            scripts.get(i).act(delta);
        }
        // physics is enabled for this body and it is not static body
        if(essentials.world != null && body != null && dataVO.physicsBodyData != null && dataVO.physicsBodyData.bodyType > 0 && !essentials.physicsStopped) {
            setX(body.getPosition().x/PhysicsBodyLoader.SCALE);
            setY(body.getPosition().y/PhysicsBodyLoader.SCALE);
            setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        }

        super.act(delta);
    }

    /**
     * Sets scissor bounding rectangle in order to mask composite item
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void setScissors(float x, float y, float w, float h) {
        dataVO.scissorX = x / mulX;
        dataVO.scissorY = y / mulY;
        dataVO.scissorWidth = w / mulX;
        dataVO.scissorHeight = h / mulY;
        scissorBounds = new Rectangle(dataVO.scissorX * mulX, dataVO.scissorY * mulY, dataVO.scissorWidth * mulX, dataVO.scissorHeight * mulY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (dataVO.scissorWidth > 0 && dataVO.scissorHeight > 0) {
            if (isTransform()) applyTransform(batch, computeTransform());
            Rectangle calculatedScissorBounds = Pools.obtain(Rectangle.class);
            getStage().calculateScissors(scissorBounds, calculatedScissorBounds);
            // Enable scissors.
            if (ScissorStack.pushScissors(calculatedScissorBounds)) {
                drawChildren(batch, parentAlpha);
                ScissorStack.popScissors();
                if (isTransform()) resetTransform(batch);
                Pools.free(calculatedScissorBounds);
            }
            return;
        }
        super.draw(batch, parentAlpha);
    }

    /**
     * Checks if layer with particlar name exists
     *
     * @param layerName
     * @return
     */
    public boolean layerExists(String layerName) {
        return layerMap.containsKey(layerName);
    }

    /**
     * Sets layer visibility by name
     *
     * @param layerName
     * @param visible
     */
    public void setLayerVisibilty(String layerName, boolean visible) {
        LayerItemVO layer = layerMap.get(layerName);
        if (layer == null) return;

        layer.isVisible = visible;
        setLayerChildrenVisibilty(layerName, visible);
    }

    // TODO: should be made private
    private void setLayerChildrenVisibilty(String layerName, boolean visible) {
        ArrayList<IBaseItem> items = itemLayerMap.get(layerName);

        if (items == null) return;

        for (int i = 0; i < items.size(); i++) {
            ((Actor) (items.get(i))).setVisible(visible);
        }
    }

    /**
     * turns on/off touch for a layer
     * @param layerName
     * @param isLocked
     */
    public void setLayerLock(String layerName, boolean isLocked) {
        for (int i = 0; i < dataVO.composite.layers.size(); i++) {
            if (dataVO.composite.layers.get(i).layerName.equals(layerName)) {
                dataVO.composite.layers.get(i).isLocked = isLocked;
                setLayerChildrenLock(layerName, isLocked);
            }
        }
    }

    // TODO: should be made private
    public void setLayerChildrenLock(String layerName, boolean isLocked) {
        IBaseItem item = null;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            if (item.getDataVO().layerName.equals(layerName)) {
                item.setLockByLayer(isLocked);
                ((Actor) item).setTouchable(isLocked ? Touchable.disabled : Touchable.enabled);
            }

        }
    }

    @Override
    public void renew() {
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        setScaleX(dataVO.scaleX);
        setScaleY(dataVO.scaleY);
        setRotation(dataVO.rotation);
        setColor(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]);
        customVariables.loadFromString(dataVO.customVars);
        scissorBounds = new Rectangle(dataVO.scissorX * mulX, dataVO.scissorY * mulY, dataVO.scissorWidth * mulX, dataVO.scissorHeight * mulY);
    }

    @Override
    public void updateDataVO() {
        dataVO.x = getX() / this.mulX;
        dataVO.y = getY() / this.mulY;
        dataVO.rotation = getRotation();
        if (getZIndex() >= 0) {
            dataVO.zIndex = getZIndex();
        }

        if (dataVO.layerName == null || dataVO.layerName.equals("")) {
            dataVO.layerName = "Default";
        }

        for (int i = 0; i < items.size(); i++) {
            items.get(i).updateDataVO();
        }
        //if(getParentItem() != null){
        sortZindexes();
        //}
        dataVO.scaleX = getScaleX();
        dataVO.scaleY = getScaleY();
        dataVO.customVars = customVariables.saveAsString();
    }

    @Override
    public boolean isLockedByLayer() {
        return isLockedByLayer;
    }

    @Override
    public void setLockByLayer(boolean isLocked) {
        isLockedByLayer = isLocked;
    }

    @Override
    public int getLayerIndex() {
        return layerIndex;
    }

    @Override
    public void setLayerIndex(int index) {
        layerIndex = index;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    @Override
    public void dispose() {
        if(essentials.world != null && body != null) essentials.world.destroyBody(getBody());
        setBody(null);

        for (int i = 0; i < items.size(); i++) {
            items.get(i).dispose();
        }
    }

    /**
     *
     * @return CustomVariables attached to this item from editor
     */
    public CustomVariables getCustomVariables() {
        return customVariables;
    }

}
