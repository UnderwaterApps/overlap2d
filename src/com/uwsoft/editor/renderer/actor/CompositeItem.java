package com.uwsoft.editor.renderer.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
import com.uwsoft.editor.renderer.data.ParticleEffectVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.data.SelectBoxVO;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.data.SpineVO;
import com.uwsoft.editor.renderer.data.SpriteAnimationVO;
import com.uwsoft.editor.renderer.data.TextBoxVO;
import com.uwsoft.editor.renderer.script.IScript;
import com.uwsoft.editor.renderer.utils.CustomVariables;

public class CompositeItem extends Group implements IBaseItem {

    public CompositeItemVO dataVO;
    public float mulX = 1f;
    public float mulY = 1f;
    public Essentials essentials;
    protected int layerIndex = 0; //this is for internal use only, for sorting
    private ArrayList<IBaseItem> items = new ArrayList<IBaseItem>();
    private HashMap<String, IBaseItem> itemIdMap = new HashMap<String, IBaseItem>();
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

    public void setTint(Color tint) {
        float[] clr = new float[4];
        clr[0] = tint.r;
        clr[1] = tint.g;
        clr[2] = tint.b;
        clr[3] = tint.a;
        this.getDataVO().tint = clr;
        this.setColor(tint);
    }

    public CompositeItem getParentItem() {
        return parentItem;
    }

    public void setParentItem(CompositeItem parentItem) {
        this.parentItem = parentItem;
    }

    public void addScript(IScript iScript) {
        scripts.add(iScript);
        iScript.init(this);
    }

    public void addScript(ArrayList<IScript> iScripts) {
        scripts.addAll(iScripts);
        for (int i = 0; i < iScripts.size(); i++) {
            iScripts.get(i).init(this);
        }
    }

    public void addScriptTo(String name, ArrayList<IScript> iScripts) {
        for (int i = 0; i < items.size(); i++) {
            IBaseItem itm = items.get(i);
            if (itm instanceof CompositeItem) {
                if (itm.getDataVO().itemName.equals(name)) {
                    ((CompositeItem) itm).addScript(iScripts);
                }
                ((CompositeItem) itm).addScriptTo(name, iScripts);
            }
        }
    }

    public void addScriptTo(String name, IScript iScript) {
        for (int i = 0; i < items.size(); i++) {
            IBaseItem itm = items.get(i);
            if (itm instanceof CompositeItem) {
                if (itm.getDataVO().itemName.equals(name)) {
                    ((CompositeItem) itm).addScript(iScript);
                }
                ((CompositeItem) itm).addScriptTo(name, iScript);
            }

        }
    }

    public void addScriptTo(String name, Class<? extends IScript> iScript) {
        for (int i = 0; i < items.size(); i++) {
            IBaseItem itm = items.get(i);
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

    private void reAssemble() {
        clear();
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                items.get(i).dispose();
            }
        }
        items.clear();

        for (int i = 0; i < dataVO.composite.sImages.size(); i++) {
            SimpleImageVO tmpVo = dataVO.composite.sImages.get(i);
            ImageItem itm = new ImageItem(tmpVo, essentials.rm, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(tmpVo.zIndex);
        }

        for (int i = 0; i < dataVO.composite.sImage9patchs.size(); i++) {
            Image9patchVO tmpVo = dataVO.composite.sImage9patchs.get(i);
            Image9patchItem itm = new Image9patchItem(tmpVo, essentials.rm, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(tmpVo.zIndex);
        }

        for (int i = 0; i < dataVO.composite.sTextBox.size(); i++) {
            TextBoxVO tmpVo = dataVO.composite.sTextBox.get(i);
            TextBoxItem itm = new TextBoxItem(tmpVo, essentials.rm, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }
        for (int i = 0; i < dataVO.composite.sButtons.size(); i++) {
            ButtonVO tmpVo = dataVO.composite.sButtons.get(i);
            TextButtonItem itm = new TextButtonItem(tmpVo, essentials.rm, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }
        for (int i = 0; i < dataVO.composite.sLabels.size(); i++) {
            LabelVO tmpVo = dataVO.composite.sLabels.get(i);
            LabelItem itm = new LabelItem(tmpVo, essentials.rm, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }
        for (int i = 0; i < dataVO.composite.sCheckBoxes.size(); i++) {
            CheckBoxVO tmpVo = dataVO.composite.sCheckBoxes.get(i);
            CheckBoxItem itm = new CheckBoxItem(tmpVo, essentials.rm, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }
        for (int i = 0; i < dataVO.composite.sSelectBoxes.size(); i++) {
            SelectBoxVO tmpVo = dataVO.composite.sSelectBoxes.get(i);
            SelectBoxItem itm = new SelectBoxItem(tmpVo, essentials.rm, this);    //TODO need to fix this
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
            ParticleItem itm = new ParticleItem(tmpVo, essentials.rm, this);
            inventorize(itm);
            addActor(itm);
            itm.setZIndex(itm.dataVO.zIndex);
        }

        if (essentials.rayHandler != null) {
            for (int i = 0; i < dataVO.composite.slights.size(); i++) {
                LightVO tmpVo = dataVO.composite.slights.get(i);
                LightActor itm = new LightActor(tmpVo, essentials, this);
                inventorize(itm);
                addActor(itm);
            }
        }
        
        if(essentials.spineReflectionHelper != null){
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

    private void inventorize(IBaseItem itm) {
        String id = itm.getDataVO().itemIdentifier;

        if (id.length() > 0) {
            itemIdMap.put(id, itm);
        }
        //all items
        items.add(itm);
    }

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
        return (SelectBoxItem) itemIdMap.get(itemId); //TODO
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

    public void addItem(IBaseItem item) {
        dataVO.composite.addItem(item.getDataVO());
        item.setParentItem(this);
        inventorize(item);
        addActor((Actor) item);
        item.updateDataVO();
        applyResolution(mulX, mulY);
        recalculateSize();
        sortZindexes();
        reAssembleLayers();
    }

    public void loadFromVO(CompositeItemVO vo) {
        dataVO = vo;
        reAssemble();
        recalculateSize();
        sortZindexes();
        reAssembleLayers();
    }

    public ArrayList<IBaseItem> getItems() {
        return items;
    }

    public void reAssembleLayers() {
        LayerItemVO layer = null;
        for (int i = 0; i < dataVO.composite.layers.size(); i++) {
            layer = dataVO.composite.layers.get(i);
            setLayerChildrenVisibilty(layer.layerName, layer.isVisible);
            setLayerChildrenLock(layer.layerName, layer.isLocked);
        }
    }

    private void recalculateSize() {
        float lowerX = 0, lowerY = 0, upperX = 0, upperY = 0;
        for (int i = 0; i < items.size(); i++) {
            Actor value = (Actor) items.get(i);
            if (i == 0) {
                lowerX = value.getX();
                lowerY = value.getY();
                upperX = value.getX() + value.getWidth() * value.getScaleX();
                upperY = value.getY() + value.getHeight() * value.getScaleY();
            }

            if (lowerX > value.getX()) lowerX = value.getX();
            if (lowerY > value.getY()) lowerY = value.getY();
            if (upperX < value.getX() + value.getWidth() * value.getScaleX())
                upperX = value.getX() + value.getWidth() * value.getScaleX();
            if (upperY < value.getY() + value.getHeight() * value.getScaleY())
                upperY = value.getY() + value.getHeight() * value.getScaleY();

        }

        setWidth(upperX - 0);
        setHeight(upperY - 0);
    }

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

    public CompositeItemVO getDataVO() {
        //updateDataVO();
        return dataVO;
    }

    public void removeItem(IBaseItem item) {
        items.remove(item);
        dataVO.composite.removeItem(item.getDataVO());
        item.dispose();
    }

    public boolean isComposite() {
        return true;
    }

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

    public void applyResolution(float mulX, float mulY) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).applyResolution(mulX, mulY);
        }
        this.mulX = mulX;
        this.mulY = mulY;
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        updateDataVO();
        recalculateSize();
    }

    @Override
    public void act(float delta) {
        for (int i = 0; i < scripts.size(); i++) {
            scripts.get(i).act(delta);
        }
        super.act(delta);
    }

    //in local coordinates
    public void setScissors(float x, float y, float w, float h) {
        dataVO.scissorX = x / mulX;
        dataVO.scissorY = y / mulY;
        dataVO.scissorWidth = w / mulX;
        dataVO.scissorHeight = h / mulY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (dataVO.scissorWidth > 0 && dataVO.scissorHeight > 0) {
            boolean t = clipBegin(getX() + dataVO.scissorX * mulX, getY() + dataVO.scissorY * mulY, dataVO.scissorWidth * mulX, dataVO.scissorHeight * mulY);
            if (t) {
                super.draw(batch, parentAlpha);
                batch.flush();
                clipEnd();
                return;
            }
        }
        super.draw(batch, parentAlpha);
    }

    public void setLayerVisibilty(String layerName, boolean visible) {
        for (int i = 0; i < dataVO.composite.layers.size(); i++) {
            if (dataVO.composite.layers.get(i).layerName.equals(layerName)) {
                dataVO.composite.layers.get(i).isVisible = visible;
                setLayerChildrenVisibilty(layerName, visible);
            }
        }

    }

    private void setLayerChildrenVisibilty(String layerName, boolean visible) {
        IBaseItem item = null;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            if (item.getDataVO().layerName.equals(layerName)) {
                ((Actor) item).setVisible(visible);
            }

        }
    }

    public void setLayerLock(String layerName, boolean isLocked) {
        for (int i = 0; i < dataVO.composite.layers.size(); i++) {
            if (dataVO.composite.layers.get(i).layerName.equals(layerName)) {
                dataVO.composite.layers.get(i).isLocked = isLocked;
                setLayerChildrenLock(layerName, isLocked);
            }
        }
    }

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
        setScaleX(dataVO.scaleX * this.mulX);
        setScaleY(dataVO.scaleY * this.mulY);
        setRotation(dataVO.rotation);
        setColor(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]);
        customVariables.loadFromString(dataVO.customVars);
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

    @Override
    public void dispose() {
        removeLights();
    }

    public void removeLights() {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof CompositeItem) {
                ((CompositeItem) items.get(i)).removeLights();
                continue;
            }
            if (items.get(i) instanceof LightActor) {
                ((LightActor) items.get(i)).removeLights();
            }
        }
    }


    public CustomVariables getCustomVariables() {
        return customVariables;
    }
}
