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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.mediators.ItemControlMediator;
import com.uwsoft.editor.gdx.mediators.SceneControlMediator;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.gdx.ui.dialogs.InputDialog;
import com.uwsoft.editor.renderer.actor.*;
import com.uwsoft.editor.renderer.data.*;

import java.io.File;
import java.util.Iterator;

/**
 * Provides methods to create items of different types based on provided data, and adds them to the scene.
 */
public class ItemFactory {

    private Sandbox sandbox;
    private SandboxStage sandboxStage;
    private SceneControlMediator sceneControl;
    private ItemControlMediator itemControl;

    public ItemFactory(Sandbox sandbox) {
        this.sandbox = sandbox;
        sandboxStage = sandbox.getSandboxStage();
        sceneControl = sandbox.getSceneControl();
    }

    private void prepareVO(MainItemVO vo, String layerName, float x, float y) {
        vo.layerName = layerName;
        vo.x = x + sandboxStage.getCamera().position.x - sandboxStage.getWidth() / 2;
        vo.y = y + sandboxStage.getCamera().position.y - sandboxStage.getHeight() / 2;
    }

    private void addItem(IBaseItem item, MainItemVO vo) {
        Actor itemActor = (Actor) item;
        sceneControl.getCurrentScene().addItem(item);
        sandbox.getSandboxInputAdapter().initItemListeners(item);
        sandbox.saveSceneCurrentSceneData();
        sandbox.getUIStage().getItemsBox().initContent();
        itemActor.setX(vo.x);
        itemActor.setY(vo.y);
    }

    private void initSpritePhysicsData(SimpleImageVO vo) {
        String regionName = vo.imageName;
        if (DataManager.getInstance().getCurrentProjectInfoVO().assetMeshMap.containsKey(regionName)) {
            vo.meshId = DataManager.getInstance().getCurrentProjectInfoVO().assetMeshMap.get(regionName);
            PhysicsBodyDataVO data = DataManager.getInstance().getCurrentProjectInfoVO().meshes.get(vo.meshId).initialProperties;
            if(data != null){
                vo.physicsBodyData = new PhysicsBodyDataVO(data);
            }else{
                vo.physicsBodyData = new PhysicsBodyDataVO();
            }
        }
    }

    public void createImageItem(LayerItemVO layer, String regionName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        SimpleImageVO vo = new SimpleImageVO();
        vo.imageName = regionName;

        initSpritePhysicsData(vo);
        prepareVO(vo, layer.layerName, x, y);

        ImageItem itm = new ImageItem(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);
    }

    public void create9patchItem(LayerItemVO layer, String regionName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        Image9patchVO vo = new Image9patchVO();
        vo.imageName = regionName;

        prepareVO(vo, layer.layerName, x, y);

        Image9patchItem itm = new Image9patchItem(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);
    }

    public void createParticleItem(LayerItemVO layer, String particleName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        ParticleEffectVO vo = new ParticleEffectVO();
        vo.particleName = particleName;

        prepareVO(vo, layer.layerName, x, y);

        ParticleItem itm = new ParticleItem(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);

        itm.forceContinuous();
        itm.start();
    }

    public void createSpriteAnimation(LayerItemVO layer, String animName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        SpriteAnimationVO vo = new SpriteAnimationVO();
        vo.animationName = animName;

        prepareVO(vo, layer.layerName, x, y);

        SpriteAnimation itm = new SpriteAnimation(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);
    }

    public void createSpriterAnimation(LayerItemVO layer, String animationsName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        SpriterVO vo = new SpriterVO();
        vo.animationName = animationsName;

        prepareVO(vo, layer.layerName, x, y);

        SpriterActor itm = new SpriterActor(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);

    }

    public void createSpineAnimation(LayerItemVO layer, String animName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        SpineVO vo = new SpineVO();
        vo.animationName = animName;

        prepareVO(vo, layer.layerName, x, y);

        SpineActor itm = new SpineActor(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
        itm.setAnimation(itm.skeletonData.getAnimations().get(0).getName());

        addItem(itm, vo);
    }

    public void createLight(LayerItemVO layer, LightVO vo, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        prepareVO(vo, layer.layerName, x, y);

        LightActor itm = new LightActor(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);

    }

    public void createItemFromLibrary(LayerItemVO layer, String libraryName, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        CompositeItemVO vo = sceneControl.getCurrentSceneVO().libraryItems.get(libraryName).clone();

        prepareVO(vo, layer.layerName, x, y);
        vo.itemName = libraryName;

        CompositeItem itm = new CompositeItem(vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());

        addItem(itm, vo);
    }


    public void createComponent(LayerItemVO layer, String type, float x, float y) {
        sceneControl.getCurrentScene().updateDataVO();

        MainItemVO vo = new MainItemVO();

        prepareVO(vo, layer.layerName, x, y);

        IBaseItem itm = null;
        if (type.equals("Text Field")) {
            vo = new TextBoxVO();
            itm = new TextBoxItem((TextBoxVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
            ((TextBoxItem) itm).setDisabled(true);
        }
        if (type.equals("Text Button")) {
            vo = new ButtonVO();
            itm = new TextButtonItem((ButtonVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
            ((TextButtonItem) itm).setDisabled(true);
        }
        if (type.equals("Label")) {
            vo = new LabelVO();
            File folder = new File(DataManager.getInstance().getFreeTypeFontPath());
            String fontName = folder.listFiles()[0].getName();
            fontName = fontName.substring(0, fontName.lastIndexOf('.'));
            ((LabelVO) vo).style = fontName;
            itm = new LabelItem((LabelVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
        }
        if (type.equals("CheckBox")) {
            vo = new CheckBoxVO();
            itm = new CheckBoxItem((CheckBoxVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
        }
        if (type.equals("SelectBox")) {
            vo = new SelectBoxVO();
            itm = new SelectBoxItem((SelectBoxVO) vo, sceneControl.getEssentials(), sceneControl.getCurrentScene());
        }

        addItem(itm, vo);
    }

	 public void addCompositeToLibrary() {
		  CompositeItem item = null;
		  if (sandbox.getSelector().getCurrentSelection().size() == 1) {
				for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
					 if (value.getHost().isComposite()) {
						  item = (CompositeItem) value.getHost();
					 }
				}
		  }

		  if (item == null) return;

		  InputDialog dlg = sandbox.getUIStage().dialogs().showInputDialog();

		  dlg.setDescription("Please set unique name for your component");

		  final CompositeItem itemToAdd = item;

		  dlg.setListener(new InputDialog.InputDialogListener() {

				@Override
				public void onConfirm(String input) {
					 sceneControl.getCurrentSceneVO().libraryItems.put(input, itemToAdd.getDataVO());
					 sandbox.getUIStage().reInitLibrary();
				}
		  });


	 }

	 public CompositeItem groupItemsIntoComposite() {
		  sceneControl.getCurrentScene().updateDataVO();
		  CompositeItemVO vo = new CompositeItemVO();

		  // Calculating lower left and upper values
		  float lowerX = 0, lowerY = 0, upperX = 0, upperY = 0;
		  int iter = 0;
		  for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
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

		  for (SelectionRectangle value : sandbox.getSelector().getCurrentSelection().values()) {
				MainItemVO itemVo = value.getHost().getDataVO();
				//System.out.println("ASSSDDD " + itemVo.x + " BASDDD " + lowerX);
				itemVo.x = itemVo.x - lowerX;
				itemVo.y = itemVo.y - lowerY;
				//System.out.println("adddd " + itemVo.x );
				vo.composite.addItem(itemVo);
		  }
		  vo.x = lowerX;
		  vo.y = lowerY;
		  vo.layerName = sandbox.getUIStage().getCurrentSelectedLayer().layerName;

		  CompositeItem item = sceneControl.getCompositeElement(vo);

		  item.setWidth(width);
		  item.setHeight(height);

		  sandbox.getSelector().removeCurrentSelectedItems();

		  sceneControl.getCurrentScene().addItem(item);

		  sandbox.getSandboxInputAdapter().initItemListeners(item);
		  sandbox.getUIStage().getItemsBox().initContent();
		  sandbox.getSelector().setSelection(item, true);

		  return item;
	 }


	 public void cleanComposite(CompositeVO compositeVO) {
		  Iterator<CompositeItemVO> compositeItemVOIterator = compositeVO.sComposites.iterator();
		  while (compositeItemVOIterator.hasNext()) {
				CompositeItemVO next = compositeItemVOIterator.next();
				if (isCompositeEmpty(next.composite)) {
					 compositeItemVOIterator.remove();
				}
		  }
	 }


	 public boolean isCompositeEmpty(CompositeVO composite) {
		  if (composite.isEmpty()) {
				return true;
		  }
		  cleanComposite(composite);
		  return composite.isEmpty();
	 }
}
