package com.uwsoft.editor.view.ui.properties.panels;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.*;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.event.CheckBoxChangeListener;
import com.uwsoft.editor.event.KeyboardListener;
import com.uwsoft.editor.event.SelectBoxChangeListener;
import com.uwsoft.editor.view.ui.properties.UIRemovableProperties;

import java.util.HashMap;

/**
 * Created by CyberJoe on 7/5/2015.
 */
public class UIPhysicsProperties extends UIRemovableProperties {

    public static final String prefix = "com.uwsoft.editor.view.ui.properties.panels.UIPhysicsProperties";
    public static final String CLOSE_CLICKED = prefix + ".CLOSE_CLICKED";

    private HashMap<Integer, String> bodyTypes = new HashMap<>();

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
    private VisCheckBox allowSleepBox;
    private VisCheckBox awakeBox;
    private VisCheckBox bulletBox;

    public UIPhysicsProperties() {
        super("Physics Component");

        bodyTypes.put(0, "STATIC");
        bodyTypes.put(1, "KINEMATIC");
        bodyTypes.put(2, "DYNAMIC");

        initView();
        initListeners();
    }

    public void initView() {
        bodyTypeBox = new VisSelectBox<>("white");
        Array<String> types = new Array<>();
        bodyTypes.values().forEach(types::add);
        bodyTypeBox.setItems(types);

        Validators.FloatValidator floatValidator = new Validators.FloatValidator();

        massField = createValidableTextField(floatValidator);
        centerOfMassXField = createValidableTextField(floatValidator);
        centerOfMassYField = createValidableTextField(floatValidator);
        rotationalIntertiaField = createValidableTextField(floatValidator);
        dumpingField = createValidableTextField(floatValidator);
        gravityScaleField = createValidableTextField(floatValidator);
        densityField = createValidableTextField(floatValidator);
        frictionField = createValidableTextField(floatValidator);
        restitutionField = createValidableTextField(floatValidator);
        allowSleepBox = new VisCheckBox("Allow Sleep");
        awakeBox = new VisCheckBox("Awake");
        bulletBox = new VisCheckBox("Bullet");

        mainTable.add(new VisLabel("Body type:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(bodyTypeBox).width(100).colspan(2);
        mainTable.row().padTop(5);

        mainTable.add(new VisLabel("Mass:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(massField).width(100).colspan(2);
        mainTable.row().padTop(5);

        mainTable.add(new VisLabel("Center of Mass:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(centerOfMassXField).width(50);
        mainTable.add(centerOfMassYField).width(50);
        mainTable.row().padTop(5);

        mainTable.add(new VisLabel("Rotational Inertia:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(rotationalIntertiaField).width(100).colspan(2);
        mainTable.row().padTop(5);

        mainTable.add(new VisLabel("Dumping:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(dumpingField).width(100).colspan(2);
        mainTable.row().padTop(5);

        mainTable.add(new VisLabel("Gravity Scale:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(gravityScaleField).width(100).colspan(2);
        mainTable.row().padTop(5);

        mainTable.add(new VisLabel("Density:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(densityField).width(100).colspan(2);
        mainTable.row().padTop(5);

        mainTable.add(new VisLabel("Friction:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(frictionField).width(100).colspan(2);
        mainTable.row().padTop(5);

        mainTable.add(new VisLabel("Restitution:", Align.right)).padRight(5).colspan(2).fillX();
        mainTable.add(restitutionField).width(100).colspan(2);
        mainTable.row().padTop(5);

        VisTable bottomTable = new VisTable();
        bottomTable.add(allowSleepBox);
        bottomTable.add(awakeBox);
        bottomTable.add(bulletBox);
        mainTable.add(bottomTable).padBottom(5).colspan(4);
        mainTable.row().padTop(5);
    }

    private void initListeners() {
        bodyTypeBox.addListener(new SelectBoxChangeListener(getUpdateEventName()));

        massField.addListener(new KeyboardListener(getUpdateEventName()));
        centerOfMassXField.addListener(new KeyboardListener(getUpdateEventName()));
        centerOfMassYField.addListener(new KeyboardListener(getUpdateEventName()));
        rotationalIntertiaField.addListener(new KeyboardListener(getUpdateEventName()));
        dumpingField.addListener(new KeyboardListener(getUpdateEventName()));
        gravityScaleField.addListener(new KeyboardListener(getUpdateEventName()));
        densityField.addListener(new KeyboardListener(getUpdateEventName()));
        frictionField.addListener(new KeyboardListener(getUpdateEventName()));
        restitutionField.addListener(new KeyboardListener(getUpdateEventName()));

        allowSleepBox.addListener(new CheckBoxChangeListener(getUpdateEventName()));
        awakeBox.addListener(new CheckBoxChangeListener(getUpdateEventName()));
        bulletBox.addListener(new CheckBoxChangeListener(getUpdateEventName()));
    }

    public int getBodyType() {
        for(Integer key: bodyTypes.keySet()) {
            if(bodyTypes.get(key).equals(bodyTypeBox.getSelected())) {
                return key;
            }
        }

        return 0;
    }

    public void setBodyType(int type) {
        bodyTypeBox.setSelected(bodyTypes.get(type));
    }

    public VisValidableTextField getMassField() {
        return massField;
    }

    public VisValidableTextField getCenterOfMassXField() {
        return centerOfMassXField;
    }

    public VisValidableTextField getCenterOfMassYField() {
        return centerOfMassYField;
    }

    public VisValidableTextField getRotationalIntertiaField() {
        return rotationalIntertiaField;
    }

    public VisValidableTextField getDumpingField() {
        return dumpingField;
    }

    public VisValidableTextField getGravityScaleField() {
        return gravityScaleField;
    }

    public VisValidableTextField getDensityField() {
        return densityField;
    }

    public VisValidableTextField getFrictionField() {
        return frictionField;
    }

    public VisValidableTextField getRestitutionField() {
        return restitutionField;
    }

    public VisCheckBox getAllowSleepBox() {
        return allowSleepBox;
    }

    public VisCheckBox getAwakeBox() {
        return awakeBox;
    }

    public VisCheckBox getBulletBox() {
        return bulletBox;
    }

    @Override
    public void onClose() {
        Overlap2DFacade.getInstance().sendNotification(CLOSE_CLICKED);
    }
}
