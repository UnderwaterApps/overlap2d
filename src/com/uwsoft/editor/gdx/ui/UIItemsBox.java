package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.gdx.Config;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.utils.MySkin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UIItemsBox extends ExpandableUIBox {

    private Tree tree;
    private MySkin skin;
    private IBaseItem selectedItem;

    public UIItemsBox(UIStage s) {
        super(s, 160, 300);
    }

    public void initContent() {
        for (int i = 0; i < this.getChildren().size; i++) {
            if (this.getChildren().get(i).getName() != null && this.getChildren().get(i).getName().equals("treeTable")) {
                this.getChildren().get(i).remove();
                break;
            }
        }
        skin = stage.textureManager.editorSkin;
        tree = new Tree(skin);


        CompositeItem sceneItems = stage.sandboxStage.getCurrentScene();
        Node root = addTree(sceneItems, stage.getCompositePanel().isRootScene());


        for (int i = 0; i < sceneItems.getItems().size(); i++) {
            if (sceneItems.getItems().get(i).isComposite()) {
                innerComposite(sceneItems.getItems().get(i), root);
            } else {
                addTree(sceneItems.getItems().get(i), root);
            }
        }
        //expand root
        if (root != null && root.getChildren() != null && root.getChildren().size > 0 && root.getChildren().get(0) != null) {
            root.getChildren().get(0).expandTo();
        }

        Table scrolltable = new Table();
        scrolltable.padBottom(20 * Config.mulY);
        scrolltable.add(tree).fill().expand();
        final ScrollPane scroller = new ScrollPane(scrolltable, skin);
        scroller.setFlickScroll(false);
        final Table table = new Table();
        table.setFillParent(true);
        table.padTop(10 * Config.mulY);
        table.add(scroller).fill().expand();
        table.setName("treeTable");
        addActor(table);
        tree.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Array<Node> selectedNodes = tree.getSelection().toArray();
                for (int i = 0; i < selectedNodes.size; i++) {
                    System.out.println();
                    IBaseItem baseItem = (IBaseItem) selectedNodes.get(i).getObject();
                    IBaseItem item = getCurrentSceneItem(stage.sandboxStage.getCurrentScene(), baseItem);
                    if(item != null) {
                        addSelectionAction(item);
                    }
                }

            }
        });
    }

    private IBaseItem getCurrentSceneItem(CompositeItem currentScene, IBaseItem baseItem) {
        IBaseItem currentSceneItem = baseItem;
        while (currentScene != currentSceneItem.getParentItem()) {
            currentSceneItem = currentSceneItem.getParentItem();
            if(currentSceneItem == null) break;
        }
        return currentSceneItem;
    }


    private void innerComposite(IBaseItem iBaseItem, Node node) {
        CompositeItem compoiteItem = (CompositeItem) iBaseItem;
        node = getNode(iBaseItem, node);

        for (int i = 0; i < compoiteItem.getItems().size(); i++) {
            if (compoiteItem.getItems().get(i).isComposite()) {
                innerComposite(compoiteItem.getItems().get(i), node);
            } else {
                addTree(compoiteItem.getItems().get(i), node);
            }
        }
    }


    private String getItemName(IBaseItem iBaseItem) {
        if (iBaseItem.getDataVO().itemIdentifier != null && !iBaseItem.getDataVO().itemIdentifier.equals("")) {
            return iBaseItem.getDataVO().itemIdentifier;
        } else {
            String className = iBaseItem.getDataVO().getClass().getSimpleName().toString();

            if (className.equals("SimpleImageVO")) {
                return "Image";
            } else if (className.equals("Image9patchVO")) {
                return "9PatchImage";
            } else if (className.equals("TextBoxVO")) {
                return "TextBbox";
            } else if (className.equals("ButtonVO")) {
                return "Button";
            } else if (className.equals("LabelVO")) {
                return "Label";
            } else if (className.equals("CompositeItemVO")) {
                return "CompositeItem";
            } else if (className.equals("CheckBoxVO")) {
                return "CheckBox";
            } else if (className.equals("SelectBoxVO")) {
                return "SelectBox";
            } else if (className.equals("ParticleEffectVO")) {
                return "ParticleEffect";
            } else if (className.equals("LightVO")) {
                return "Light";
            } else if (className.equals("SpineVO")) {
                return "Spine";
            } else if (className.equals("SpriteAnimationVO")) {
                return "SpriteAnimation";
            } else {
                return "unknown";
            }
        }
    }


    private Node addTree(CompositeItem sceneItems, boolean isRoot) {
        Node moo = new Node(new Label((isRoot) ? "root" : getItemName(sceneItems), skin));
        moo.setObject(sceneItems);
        tree.add(moo);
        //addClickListener(moo,sceneItems);
        return moo;
    }

    private void addTree(IBaseItem iBaseItem, Node node) {
        Node moo = new Node(new Label(getItemName(iBaseItem), skin));
        moo.setObject(iBaseItem);
        node.add(moo);
        //addClickListener(moo,iBaseItem);
        //checkExpand(iBaseItem,moo);
    }

    private Node getNode(IBaseItem iBaseItem, Node node) {
        Node moo = new Node(new Label(getItemName(iBaseItem), skin));
        moo.setObject(iBaseItem);
        node.add(moo);
        //addClickListener(moo,iBaseItem);
        //checkExpand(iBaseItem,moo);
        return moo;
    }

    private void checkExpand(IBaseItem iBaseItem, Node moo) {

        if (selectedItem != null && iBaseItem.equals(selectedItem)) {
            moo.expandTo();
        }
    }

    public void updateContent(IBaseItem item) {
        selectedItem = item;
        initContent();
    }

    private void addClickListener(Node moo, final IBaseItem iBaseItem) {
        moo.getActor().addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("CLICKED");
                if (getTapCount() == 2) {
                    if (!iBaseItem.isComposite()) {
                        if (iBaseItem.getParentItem() != null && !iBaseItem.getParentItem().equals(stage.sandboxStage.getCurrentScene()))
                            stage.sandboxStage.getIntoComposite(iBaseItem.getParentItem().getDataVO());
                    } else {
                        if (!iBaseItem.equals(stage.sandboxStage.getCurrentScene())) {
                            stage.sandboxStage.getIntoComposite(((CompositeItem) iBaseItem).getDataVO());
                        }
                    }
                } else {
                    stage.sandboxStage.setSelection(iBaseItem, true);
                }
            }

        });
    }

    private void addSelectionAction(IBaseItem iBaseItem) {
        stage.sandboxStage.setSelection(iBaseItem, true);
    }

    public void setSelected(HashMap<IBaseItem, SelectionRectangle> currentSelection) {
        if (tree == null) return;
        tree.getSelection().clear();
        Array<Node> allSceneRootNodes = tree.getNodes().get(0).getChildren();
        Iterator it = currentSelection.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            for (int i = 0; i < allSceneRootNodes.size; i++) {
                if (((IBaseItem) allSceneRootNodes.get(i).getObject()).equals(pairs.getKey())) {
                    tree.getSelection().add(allSceneRootNodes.get(i));
                }
            }
            //it.remove();
        }


    }

    @Override
    protected void expand() {
        setHeight(expandedHeight);
        if (mainLayer != null) {
            mainLayer.setVisible(true);
        }
        if (tree != null) {
            tree.setVisible(true);
        }
    }

    @Override
    protected void collapse() {
        setHeight(topImg.getHeight());
        if (mainLayer != null) {
            mainLayer.setVisible(false);
        }
        if (tree != null) {
            tree.setVisible(false);
        }
    }

//	 private ArrayList<MainItemVO> buildItemsVoList(CompositeVO composite) {
//		 	ArrayList<MainItemVO> returnList	=	new ArrayList<MainItemVO>();
//		 	for (int i = 0; i < composite.sComposites.size(); i++) {
//		 		returnList.add(composite.sComposites.get(i));
//		 	}
//		 	for (int i = 0; i < composite.sButtons.size(); i++) {
//		 		returnList.add(composite.sButtons.get(i));
//		 	}
//		 	for (int i = 0; i < composite.sCheckBoxes.size(); i++) {
//		 		returnList.add(composite.sCheckBoxes.get(i));
//		 	}		 	
//		 	for (int i = 0; i < composite.sImage9patchs.size(); i++) {
//		 		returnList.add(composite.sImage9patchs.get(i));
//		 	}
//		 	for (int i = 0; i < composite.sImages.size(); i++) {
//		 		returnList.add(composite.sImages.get(i));
//		 	}
//		 	for (int i = 0; i < composite.sLabels.size(); i++) {
//		 		returnList.add(composite.sLabels.get(i));
//		 	}
//		 	for (int i = 0; i < composite.sParticleEffects.size(); i++) {
//		 		returnList.add(composite.sParticleEffects.get(i));
//		 	}
//		 	for (int i = 0; i < composite.sSelectBoxes.size(); i++) {
//		 		returnList.add(composite.sSelectBoxes.get(i));
//		 	}
//		 	for (int i = 0; i < composite.sSpineAnimations.size(); i++) {
//		 		returnList.add(composite.sSpineAnimations.get(i));
//		 	}
//		 	for (int i = 0; i < composite.sSpriteAnimations.size(); i++) {
//		 		returnList.add(composite.sSpriteAnimations.get(i));
//		 	}
//		 	for (int i = 0; i < composite.sTextBox.size(); i++) {
//		 		returnList.add(composite.sSpriteAnimations.get(i));
//		 	}			
//			return returnList;
//		}


}
