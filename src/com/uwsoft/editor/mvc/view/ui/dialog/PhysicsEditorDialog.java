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

package com.uwsoft.editor.mvc.view.ui.dialog;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.gdx.ui.components.ItemPhysicsEditor;
import com.uwsoft.editor.renderer.actor.IBaseItem;


/**
 * Created by azakhary on 4/28/2015.
 */
public class PhysicsEditorDialog extends O2DDialog {

    private VisTable mainTable;
    private VisTable controlsTable;
    private ItemPhysicsEditor itemPhysicsEditor;


    private VisSelectBox<String> bodyTypeBox;
    private VisValidableTextField massField;

    private VisValidableTextField centerOfMassXField;
    private VisValidableTextField centerOfMassYField;
    private VisValidableTextField rotationalIntertiaField;
    private VisValidableTextField dumpingField;
    private VisValidableTextField gravityScaleField;
    private VisValidableTextField densityField;
    private VisValidableTextField frictionField;
    private VisValidableTextField restitutionField;

    private VisSelectBox<String> poligonyzerBox;
    private VisValidableTextField hullToleranceField;
    private VisValidableTextField alphaToleranceField;
    private VisCheckBox multiPartDetectionBox;
    private VisCheckBox holeDetectionBox;
    private VisTextButton retraceButton;

    private VisTextButton clearMeshButton;
    private VisTextButton createFreshCopyButton;

    public PhysicsEditorDialog() {
        super("Physics Editor");
        setModal(true);
        addCloseButton();

        mainTable = new VisTable();
        controlsTable = new VisTable();
        itemPhysicsEditor = new ItemPhysicsEditor(500, 500);

        initSidePanel();

        mainTable.add(controlsTable).top();
        mainTable.add(itemPhysicsEditor).top();

        mainTable.row();

        add(mainTable);
        row();
    }

    private void initSidePanel() {

        initPropertiesUI();
        initMeshUI();
        initRetraceUI();

    }

    public void initPropertiesUI() {

        bodyTypeBox = new VisSelectBox<>();
        Array<String> types = new Array<>();
        types.add("STATIC");
        types.add("KINEMATIC");
        types.add("DYNAMIC");
        bodyTypeBox.setItems(types);

        Validators.FloatValidator floatValidator = new Validators.FloatValidator();

        massField = new VisValidableTextField(floatValidator);
        centerOfMassXField = new VisValidableTextField(floatValidator);
        centerOfMassYField = new VisValidableTextField(floatValidator);
        rotationalIntertiaField = new VisValidableTextField(floatValidator);
        dumpingField = new VisValidableTextField(floatValidator);
        gravityScaleField = new VisValidableTextField(floatValidator);
        densityField = new VisValidableTextField(floatValidator);
        frictionField = new VisValidableTextField(floatValidator);
        restitutionField = new VisValidableTextField(floatValidator);

        controlsTable.add(new VisLabel("Body type:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(bodyTypeBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Mass:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(massField).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Center of Mass:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(centerOfMassXField).width(60);
        controlsTable.add(centerOfMassYField).width(60);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Rotational Inertia:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(rotationalIntertiaField).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Dumping:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(dumpingField).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Gravity Scale:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(gravityScaleField).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Density:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(densityField).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Friction:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(frictionField).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Restitution:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(restitutionField).width(120).colspan(2).padBottom(20);
        controlsTable.row().padTop(5);
    }

    public void initRetraceUI() {
        poligonyzerBox = new VisSelectBox<>();
        Array<String> types = new Array<>();
        types.add("BAYAZIT");
        types.add("EWJORDAN");
        poligonyzerBox.setItems(types);

        Validators.FloatValidator floatValidator = new Validators.FloatValidator();

        hullToleranceField = new VisValidableTextField(floatValidator);
        alphaToleranceField = new VisValidableTextField(floatValidator);
        multiPartDetectionBox = new VisCheckBox(null);
        holeDetectionBox = new VisCheckBox(null);
        retraceButton = new VisTextButton("Retrace");

        controlsTable.row().padTop(20);
        controlsTable.add(new VisLabel("Polygonizer:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(poligonyzerBox).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Hull Tolerance:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(hullToleranceField).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(new VisLabel("Alpha Tolerance:", Align.right)).padRight(5).colspan(2).fillX();
        controlsTable.add(alphaToleranceField).width(120).colspan(2);
        controlsTable.row().padTop(5);

        controlsTable.add(multiPartDetectionBox).colspan(2).right();
        controlsTable.add(new VisLabel("Multi part detection: ", Align.left)).padLeft(5).colspan(2).fillX();
        controlsTable.row().padTop(5);

        controlsTable.add(holeDetectionBox).colspan(2).right();
        controlsTable.add(new VisLabel("Hole detection: ", Align.left)).padLeft(5).colspan(2).fillX();
        controlsTable.row().padTop(5);

        controlsTable.add(retraceButton).colspan(4).right().padBottom(20);
        controlsTable.row().padTop(5);
    }

    public void initMeshUI() {
        clearMeshButton = new VisTextButton("Clear Mesh");
        createFreshCopyButton = new VisTextButton("Create Fresh Copy");

        controlsTable.add(new VisLabel("Clearing mesh does not clone it:", Align.center)).colspan(4).fillX();
        controlsTable.row().padTop(5);
        controlsTable.add(clearMeshButton).padRight(5).colspan(2);
        controlsTable.add(createFreshCopyButton).padRight(5).colspan(2);
        controlsTable.row().padBottom(20);
    }

    public void setItem(IBaseItem item) {
        itemPhysicsEditor.editItem(item);
    }

    public VisSelectBox<String> getBodyTypeBox() {
        return bodyTypeBox;
    }

    public void setBodyTypeBox(VisSelectBox<String> bodyTypeBox) {
        this.bodyTypeBox = bodyTypeBox;
    }

    public VisValidableTextField getMassField() {
        return massField;
    }

    public void setMassField(VisValidableTextField massField) {
        this.massField = massField;
    }

    public VisValidableTextField getCenterOfMassXField() {
        return centerOfMassXField;
    }

    public void setCenterOfMassXField(VisValidableTextField centerOfMassXField) {
        this.centerOfMassXField = centerOfMassXField;
    }

    public VisValidableTextField getCenterOfMassYField() {
        return centerOfMassYField;
    }

    public void setCenterOfMassYField(VisValidableTextField centerOfMassYField) {
        this.centerOfMassYField = centerOfMassYField;
    }

    public VisValidableTextField getRotationalIntertiaField() {
        return rotationalIntertiaField;
    }

    public void setRotationalIntertiaField(VisValidableTextField rotationalIntertiaField) {
        this.rotationalIntertiaField = rotationalIntertiaField;
    }

    public VisValidableTextField getDumpingField() {
        return dumpingField;
    }

    public void setDumpingField(VisValidableTextField dumpingField) {
        this.dumpingField = dumpingField;
    }

    public VisValidableTextField getGravityScaleField() {
        return gravityScaleField;
    }

    public void setGravityScaleField(VisValidableTextField gravityScaleField) {
        this.gravityScaleField = gravityScaleField;
    }

    public VisValidableTextField getDensityField() {
        return densityField;
    }

    public void setDensityField(VisValidableTextField densityField) {
        this.densityField = densityField;
    }

    public VisValidableTextField getFrictionField() {
        return frictionField;
    }

    public void setFrictionField(VisValidableTextField frictionField) {
        this.frictionField = frictionField;
    }

    public VisValidableTextField getRestitutionField() {
        return restitutionField;
    }

    public void setRestitutionField(VisValidableTextField restitutionField) {
        this.restitutionField = restitutionField;
    }

    public VisSelectBox<String> getPoligonyzerBox() {
        return poligonyzerBox;
    }

    public void setPoligonyzerBox(VisSelectBox<String> poligonyzerBox) {
        this.poligonyzerBox = poligonyzerBox;
    }

    public VisValidableTextField getHullToleranceField() {
        return hullToleranceField;
    }

    public void setHullToleranceField(VisValidableTextField hullToleranceField) {
        this.hullToleranceField = hullToleranceField;
    }

    public VisValidableTextField getAlphaToleranceField() {
        return alphaToleranceField;
    }

    public void setAlphaToleranceField(VisValidableTextField alphaToleranceField) {
        this.alphaToleranceField = alphaToleranceField;
    }

    public VisCheckBox getMultiPartDetectionBox() {
        return multiPartDetectionBox;
    }

    public void setMultiPartDetectionBox(VisCheckBox multiPartDetectionBox) {
        this.multiPartDetectionBox = multiPartDetectionBox;
    }

    public VisCheckBox getHoleDetectionBox() {
        return holeDetectionBox;
    }

    public void setHoleDetectionBox(VisCheckBox holeDetectionBox) {
        this.holeDetectionBox = holeDetectionBox;
    }

    public VisTextButton getRetraceButton() {
        return retraceButton;
    }

    public void setRetraceButton(VisTextButton retraceButton) {
        this.retraceButton = retraceButton;
    }

    public VisTextButton getClearMeshButton() {
        return clearMeshButton;
    }

    public void setClearMeshButton(VisTextButton clearMeshButton) {
        this.clearMeshButton = clearMeshButton;
    }

    public VisTextButton getCreateFreshCopyButton() {
        return createFreshCopyButton;
    }

    public void setCreateFreshCopyButton(VisTextButton createFreshCopyButton) {
        this.createFreshCopyButton = createFreshCopyButton;
    }

    public ItemPhysicsEditor getItemPhysicsEditor() {
        return itemPhysicsEditor;
    }

    public void setItemPhysicsEditor(ItemPhysicsEditor itemPhysicsEditor) {
        this.itemPhysicsEditor = itemPhysicsEditor;
    }
}
