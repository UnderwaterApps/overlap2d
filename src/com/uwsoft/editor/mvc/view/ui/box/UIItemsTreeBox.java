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

import java.util.HashMap;
import java.util.Map;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTree;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.ui.followers.NormalSelectionFollower;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.components.MainItemComponent;

public class UIItemsTreeBox extends UICollapsibleBox {
    public static final String ITEMS_SELECTED = "com.uwsoft.editor.mvc.view.ui.box.UIItemsTreeBox." + ".ITEMS_SELECTED";
    private final Overlap2DFacade facade;
    private final VisTable treeTable;
    private VisTree tree;
    
    private ComponentMapper<MainItemComponent> mainItemMapper;
    private MainItemComponent mainITemComponent;

    public UIItemsTreeBox() {
        super("Items Tree", 166);
        setMovable(false);
        facade = Overlap2DFacade.getInstance();
        treeTable = new VisTable();
        treeTable.left();
        createCollapsibleWidget(treeTable);
    }


    public void init(Entity rootScene) {
        treeTable.clear();
        tree = new VisTree();
        VisScrollPane scroller = new VisScrollPane(tree);
        scroller.setFlickScroll(false);
        treeTable.add(scroller).width(166).maxHeight(570);
        //
        Node root = addTreeRoot(rootScene, null);
        root.setExpanded(true);
        tree.addListener(new TreeChangeListener());
    }

    private String getItemName(Entity entity) {
    	mainITemComponent = mainItemMapper.get(entity);
        if (mainITemComponent.itemIdentifier != null && !mainITemComponent.itemIdentifier.isEmpty()) {
            return mainITemComponent.itemIdentifier;
        } else {
            int type = entity.flags;
            switch (type) {
                case EntityFactory.IMAGE_TYPE:
                    return "Image";
                case EntityFactory.NINE_PATCH:
                    return "9PatchImage";
//                case "TextBoxVO":
//                    return "TextBox";
//                case "ButtonVO":
//                    return "Button";
                case EntityFactory.LABEL_TYPE:
                    return "Label";
                case EntityFactory.COMPOSITE_TYPE:
                    return "CompositeItem";
//                case "CheckBoxVO":
//                    return "CheckBox";
//                case "SelectBoxVO":
//                    return "SelectBox";
                case EntityFactory.PARTICLE_TYPE:
                    return "ParticleEffect";
                case EntityFactory.LIGHT_TYPE:
                    return "Light";
                case EntityFactory.SPINE_TYPE:
                    return "Spine";
                case EntityFactory.SPRITE_TYPE:
                    return "SpriteAnimation";
                case EntityFactory.SPRITER_TYPE:
                    return "SpriterAnimation";
                default:
                    return "unknown";
            }
        }
    }

    private Node addTreeRoot(Entity compoiteItem, Node parentNode) {  // was like this addTreeRoot(CompositeItem compoiteItem, Node parentNode)
        Node node = addTreeNode(compoiteItem, parentNode);
      //TODO fix and uncomment
//        ArrayList<Entity> items = compoiteItem.getItems();
//        for (Entity item : items) {
//        	
//            if (item.isComposite()) {
//                addTreeRoot((CompositeItem) item, node);
//            } else {
//                addTreeNode(item, node);
//            }
//        }
        return node;
    }

    private Node addTreeNode(Entity baseItem, Node parentNode) {
        Node node = new Node(new VisLabel(parentNode == null ? "root" : getItemName(baseItem)));
        node.setObject(baseItem);
        if (parentNode != null) {
            parentNode.add(node);
        } else {
            tree.add(node);
        }
        return node;
    }


    public void setSelected(HashMap<Entity, NormalSelectionFollower> currentSelection) {
        if (tree == null) return;
        tree.getSelection().clear();
        Array<Node> allSceneRootNodes = tree.getNodes().get(0).getChildren();
        for (Object o : currentSelection.entrySet()) {
            Map.Entry pairs = (Map.Entry) o;
            for (int i = 0; i < allSceneRootNodes.size; i++) {
                if ((allSceneRootNodes.get(i).getObject()).equals(pairs.getKey())) {
                    tree.getSelection().add(allSceneRootNodes.get(i));
                }
            }
            //it.remove();
        }
    }


    private class TreeChangeListener extends ChangeListener {

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            facade.sendNotification(ITEMS_SELECTED, tree.getSelection());
        }
    }
}
