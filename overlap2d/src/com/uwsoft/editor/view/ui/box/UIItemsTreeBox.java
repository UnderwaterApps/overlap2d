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

package com.uwsoft.editor.view.ui.box;

import java.util.Set;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTree;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.components.MainItemComponent;
import com.uwsoft.editor.renderer.components.NodeComponent;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.uwsoft.editor.utils.runtime.EntityUtils;

public class UIItemsTreeBox extends UICollapsibleBox {
    public static final String ITEMS_SELECTED = "com.uwsoft.editor.view.ui.box.UIItemsTreeBox." + ".ITEMS_SELECTED";
    private final Overlap2DFacade facade;
    private final VisTable treeTable;
    private VisTree tree;
    
    private ComponentMapper<MainItemComponent> mainItemMapper;
    private MainItemComponent mainItemComponent;

    private Node rootNode;

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
        mainItemComponent = ComponentRetriever.get(entity, MainItemComponent.class);
        if (mainItemComponent.itemIdentifier != null && !mainItemComponent.itemIdentifier.isEmpty()) {
            return mainItemComponent.itemIdentifier;
        } else {
            int type = EntityUtils.getType(entity);
            switch (type) {
                case EntityFactory.IMAGE_TYPE:
                    return "Image";
                case EntityFactory.NINE_PATCH:
                    return "9PatchImage";
                case EntityFactory.LABEL_TYPE:
                    return "Label";
                case EntityFactory.COMPOSITE_TYPE:
                    return "CompositeItem";
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
                case EntityFactory.COLOR_PRIMITIVE:
                    return "Primitive";
                default:
                    return "unknown";
            }
        }
    }

    private Node addTreeRoot(Entity entity, Node parentNode) {  // was like this addTreeRoot(CompositeItem compoiteItem, Node parentNode)
        Node node = addTreeNode(entity, parentNode);
        if (parentNode == null) rootNode = node;

        NodeComponent nodeComponent = ComponentRetriever.get(entity, NodeComponent.class);

        if(nodeComponent != null) {
            for (Entity item : nodeComponent.children) {
                if (EntityUtils.getType(entity) == EntityFactory.COMPOSITE_TYPE) {
                    addTreeRoot(item, node);
                } else {
                    addTreeNode(item, node);
                }
            }
        }
        return node;
    }

    private Node addTreeNode(Entity item, Node parentNode) {
        Node node = new Node(new VisLabel(parentNode == null ? "root" : getItemName(item)));
        MainItemComponent mainItemComponent = ComponentRetriever.get(item, MainItemComponent.class);
        node.setObject(mainItemComponent.uniqueId);
        if (parentNode != null) {
            parentNode.add(node);
        } else {
            tree.add(node);
        }
        return node;
    }

    public void setSelection(Set<Entity> selection) {

        if (tree == null) return;
        tree.getSelection().clear();
        if (selection == null) return;
        addToSelection(selection);
    }

    public void addToSelection(Set<Entity> selection) {

        if (tree == null) return;
        Array<Node> allSceneRootNodes = tree.getNodes().get(0).getChildren();

        for (int entityId : EntityUtils.getEntityId(selection)) {
            for (Node n : allSceneRootNodes) {
                if(n.getObject().equals(entityId)) {
                    tree.getSelection().add(n);
                    break;
                }
            }
        }
    }

    public void removeFromSelection(Set<Entity> selection) {

        if (tree == null) return;
        Array<Node> allSceneRootNodes = tree.getNodes().get(0).getChildren();

        for (int entityId : EntityUtils.getEntityId(selection)) {
            for (Node n : allSceneRootNodes) {
                if(n.getObject().equals(entityId)) {
                    tree.getSelection().remove(n);
                    break;
                }
            }
        }
    }


    private class TreeChangeListener extends ClickListener {
        public void clicked (InputEvent event, float x, float y) {
            Selection<Node> selection = tree.getSelection();
            selection.remove(rootNode);
            facade.sendNotification(ITEMS_SELECTED, selection);
        }
    }
}
