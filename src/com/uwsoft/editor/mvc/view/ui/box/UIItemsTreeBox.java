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

package com.uwsoft.editor.mvc.view.ui.box;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTree;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.uwsoft.editor.gdx.actors.SelectionRectangle;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.utils.MySkin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UIItemsTreeBox extends VisWindow {

    private final Overlap2DFacade facade;
    private final TextureManager textureManager;
    private VisTree tree;
    private MySkin skin;
    private IBaseItem selectedItem;
    private VisTable mainTable;

    public UIItemsTreeBox() {
        super("Items Tree");
        setMovable(false);
        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(TextureManager.NAME);
        mainTable = new VisTable();
        mainTable.addSeparator().padBottom(10);
        add(mainTable).expandX().fillX();
    }

    public void init(CompositeItem rootScene) {

        tree = new VisTree();
        VisScrollPane scroller = new VisScrollPane(tree);
        scroller.setFlickScroll(false);
        mainTable.add(mainTable);
        Node rootNode = addTreeNode(rootScene, true);
        for (int i = 0; i < rootScene.getItems().size(); i++) {
            if (rootScene.getItems().get(i).isComposite()) {
                innerComposite(rootScene.getItems().get(i), rootNode);
            } else {
                addTreeNode(rootScene.getItems().get(i), rootNode);
            }
        }

//        skin = textureManager.editorSkin;
//        tree = new VisTree();
//
//
//        CompositeItem rootScene = stage.getSandbox().getCurrentScene();
//        Node rootNode = addTreeNode(rootScene, true /*stage.getCompositePanel().isRootScene()*/);
//
//

//        //expand rootNode
//        if (rootNode != null && rootNode.getChildren() != null && rootNode.getChildren().size > 0 && rootNode.getChildren().get(0) != null) {
//            rootNode.getChildren().get(0).expandTo();
//        }
//
//        Table scrolltable = new Table();
//        scrolltable.padBottom(20);
//        scrolltable.add(tree).fill().expand();
//        final ScrollPane scroller = new ScrollPane(scrolltable, skin);
//        scroller.setFlickScroll(false);
//        final Table table = new Table();
//        table.setFillParent(true);
//        table.padTop(10);
//        table.add(scroller).fill().expand();
//        table.setName("treeTable");
//        addActor(table);
//        tree.addListener(new ChangeListener() {
//
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Array<Node> selectedNodes = tree.getSelection().toArray();
//                for (int i = 0; i < selectedNodes.size; i++) {
//                    System.out.println();
//                    IBaseItem baseItem = (IBaseItem) selectedNodes.get(i).getObject();
//                    IBaseItem item = getCurrentSceneItem(stage.getSandbox().getCurrentScene(), baseItem);
//                    if (item != null) {
//                        addSelectionAction(item);
//                    }
//                }
//
//            }
//        });
    }

    private IBaseItem getCurrentSceneItem(CompositeItem currentScene, IBaseItem baseItem) {
        IBaseItem currentSceneItem = baseItem;
        while (currentScene != currentSceneItem.getParentItem()) {
            currentSceneItem = currentSceneItem.getParentItem();
            if (currentSceneItem == null) break;
        }
        return currentSceneItem;
    }


    private void innerComposite(IBaseItem iBaseItem, Node node) {
        CompositeItem compoiteItem = (CompositeItem) iBaseItem;
        node = getNode(iBaseItem, node);

        for (int i = 0; i < compoiteItem.getItems().size(); i++) {
            if (compoiteItem.getItems().get(i).isComposite()) {
//                innerComposite(compoiteItem.getItems().get(i), node);
            } else {
                addTreeNode(compoiteItem.getItems().get(i), node);
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


    private Node addTreeNode(CompositeItem sceneItems, boolean isRoot) {
        Node moo = new Node(new Label((isRoot) ? "root" : getItemName(sceneItems), skin));
        moo.setObject(sceneItems);
        tree.add(moo);
        //addClickListener(moo,sceneItems);
        return moo;
    }

    private void addTreeNode(IBaseItem iBaseItem, Node node) {
        Node moo = new Node(new Label(getItemName(iBaseItem), skin));
        moo.setObject(iBaseItem);
        node.add(moo);
    }

    private Node getNode(IBaseItem iBaseItem, Node node) {
        Node moo = new Node(new Label(getItemName(iBaseItem), skin));
        moo.setObject(iBaseItem);
        node.add(moo);
        return moo;
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
}
