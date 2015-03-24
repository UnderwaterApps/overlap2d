package com.uwsoft.editor.gdx.ui.properties;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.uwsoft.editor.controlles.ColorPickerHandler;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.gdx.ui.components.ColorPicker;
import com.uwsoft.editor.gdx.ui.dialogs.CustomVariablesDialog;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.*;
import com.uwsoft.editor.renderer.data.LabelVO;
import com.uwsoft.editor.renderer.data.SelectBoxVO;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.data.TextBoxVO;

public class BasicItemProperties extends PropertyBox implements IPropertyBox<IBaseItem> {

    private final Sandbox sandbox;
    private TextField idBox;

    private TextField xVal;
    private TextField yVal;

    private TextField widthVal;
    private TextField heightVal;

    private TextField scalexVal;
    private TextField scaleyVal;

    private CheckBox flipV;
    private CheckBox flipH;

    private TextButton customVarsBtn;

    private ColorPicker tintColorComponent;

    private Actor itemActor;
    private TextBoxItem rotationVal;

    public BasicItemProperties(Sandbox sandbox, SceneLoader scene) {
        super(scene, "BasicItemProperties");
        this.sandbox = sandbox;
        tintColorComponent = new ColorPicker();
        LabelItem tintLbl = ui.getLabelById("tintLbl");
        tintColorComponent.setX(tintLbl.getX() + tintLbl.getWidth() + 5);
        tintColorComponent.setY(tintLbl.getY() - 3);
        addActor(tintColorComponent);
    }

    @Override
    public void setObject(IBaseItem object) {
        item = object;
        itemActor = (Actor) object;

        idBox = ui.getTextBoxById("itemIdTxt");
        idBox.setText(item.getDataVO().itemIdentifier);

        xVal = ui.getTextBoxById("xVal");
        yVal = ui.getTextBoxById("yVal");

        widthVal = ui.getTextBoxById("widthVal");
        heightVal = ui.getTextBoxById("heightVal");
        rotationVal = ui.getTextBoxById("rotationVal");

        xVal.setText(roundTwoDecimals(itemActor.getX()) + "");
        yVal.setText(roundTwoDecimals(itemActor.getY()) + "");

        final String className = item.getClass().getSimpleName();

        if (className.equals("LabelItem")) {
            widthVal.setText(roundTwoDecimals(itemActor.getWidth()) + "");
            heightVal.setText(roundTwoDecimals(itemActor.getHeight()) + "");
        } else {
            widthVal.setText(roundTwoDecimals(itemActor.getWidth() * itemActor.getScaleX()) + "");
            heightVal.setText(roundTwoDecimals(itemActor.getHeight() * itemActor.getScaleY()) + "");
        }

        rotationVal.setText(String.valueOf(itemActor.getRotation()));

        scalexVal = ui.getTextBoxById("scalexVal");
        scaleyVal = ui.getTextBoxById("scaleyVal");

        customVarsBtn = ui.getTextButtonById("customVarsBtn");

        scalexVal.setText(roundTwoDecimals(((Actor) item).getScaleX()) + "");
        scaleyVal.setText(roundTwoDecimals(((Actor) item).getScaleY()) + "");

        flipV = ui.getCheckBoxById("flipV");
        flipH = ui.getCheckBoxById("flipH");
        if (((Actor) item).getScaleY() < 0) {
            flipV.setChecked(true);
        }
        if (((Actor) item).getScaleX() < 0) {
            flipH.setChecked(true);
        }

        tintColorComponent.setColorValue(((Actor) item).getColor());


        setListeners();
    }

    @Override
    public void updateView() {
        xVal.setText(roundTwoDecimals(((Actor) item).getX()) + "");
        yVal.setText(roundTwoDecimals(((Actor) item).getY()) + "");


        final String className = item.getClass().getSimpleName();

        if (className.equals("LabelItem")) {
            widthVal.setText(roundTwoDecimals(itemActor.getWidth()) + "");
            heightVal.setText(roundTwoDecimals(itemActor.getHeight()) + "");
        } else {
            widthVal.setText(roundTwoDecimals(itemActor.getWidth() * itemActor.getScaleX()) + "");
            heightVal.setText(roundTwoDecimals(itemActor.getHeight() * itemActor.getScaleY()) + "");
        }


        rotationVal.setText(String.valueOf(itemActor.getRotation()));

        scalexVal.setText(roundTwoDecimals(((Actor) item).getScaleX()) + "");
        scaleyVal.setText(roundTwoDecimals(((Actor) item).getScaleY()) + "");

        if (((Actor) item).getScaleY() < 0) {
            flipV.setChecked(true);
        }
        if (((Actor) item).getScaleX() < 0) {
            flipH.setChecked(true);
        }

        tintColorComponent.setColorValue(((Actor) item).getColor());
    }

    private void setListeners() {
        final String className = item.getClass().getSimpleName();
        widthVal.setDisabled(true);
        heightVal.setDisabled(true);

        if (className.equals("TextBoxItem") || className.equals("SelectBoxItem") || className.equals("Image9patchItem") || className.equals("ImageItem") || className.equals("LabelItem")) {
            widthVal.setDisabled(false);
            heightVal.setDisabled(false);
        }

        customVarsBtn.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                CustomVariablesDialog dlg = new CustomVariablesDialog(sandbox.getUIStage(), item);
                sandbox.getUIStage().addActor(dlg);
            }
        });

        idBox.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    setItemId(idBox.getText());
                }
            }
        });

        idBox.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    setItemId(idBox.getText());
                }
                return true;
            }
        });


        widthVal.addListener(new FocusListener() {

            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    setItemWidth(getFloatValue(widthVal.getText(), ((Actor) item).getWidth()), className);
                }
            }
        });

        widthVal.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    setItemWidth(getFloatValue(widthVal.getText(), ((Actor) item).getWidth()), className);
                }
                return true;
            }
        });

        heightVal.addListener(new FocusListener() {

            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    setItemHeight(getFloatValue(heightVal.getText(), ((Actor) item).getHeight()), className);
                }
            }
        });

        heightVal.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    setItemHeight(getFloatValue(heightVal.getText(), ((Actor) item).getHeight()), className);
                }
                return true;
            }
        });

        xVal.addListener(new FocusListener() {

            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    setItemX(getFloatValue(xVal.getText(), ((Actor) item).getX()));
                }
            }

        });

        xVal.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    setItemX(getFloatValue(xVal.getText(), ((Actor) item).getX()));
                }
                return true;
            }
        });

        yVal.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    setItemY(getFloatValue(yVal.getText(), ((Actor) item).getY()));
                }
            }
        });


        yVal.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    setItemY(getFloatValue(yVal.getText(), ((Actor) item).getY()));
                }
                return true;
            }
        });

        scalexVal.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    setItemScaleX(getFloatValue(scalexVal.getText(), ((Actor) item).getScaleX()));
                }
            }
        });

        scalexVal.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    setItemScaleX(getFloatValue(scalexVal.getText(), ((Actor) item).getScaleX()));
                }
                return true;
            }
        });

        scaleyVal.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    setItemScaleY(getFloatValue(scaleyVal.getText(), ((Actor) item).getScaleY()));
                }
            }

        });

        scaleyVal.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    setItemScaleY(getFloatValue(scaleyVal.getText(), ((Actor) item).getScaleY()));
                }
                return true;
            }
        });


        flipV.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                item.getDataVO().scaleY *= -1;
                item.renew();
                super.touchUp(event, x, y, pointer, button);
                sandbox.getSelector().updateSelections();
                sandbox.saveSceneCurrentSceneData();
            }
        });
        flipH.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                item.getDataVO().scaleX *= -1;
                item.renew();
                super.touchUp(event, x, y, pointer, button);
                sandbox.getSelector().updateSelections();
                sandbox.saveSceneCurrentSceneData();
            }
        });

        tintColorComponent.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                ColorPickerHandler chooseHandler = new ColorPickerHandler() {
                    @Override
                    public void ColorChoosen(java.awt.Color javaColor) {
                        Color color = new Color(javaColor.getRed() / 255f, javaColor.getGreen() / 255f, javaColor.getBlue() / 255f, javaColor.getAlpha() / 255f);
                        tintColorComponent.setColorValue(color);
                        item.getDataVO().tint[0] = javaColor.getRed() / 255f;
                        item.getDataVO().tint[1] = javaColor.getGreen() / 255f;
                        item.getDataVO().tint[2] = javaColor.getBlue() / 255f;
                        item.getDataVO().tint[3] = javaColor.getAlpha() / 255f;
                        item.renew();
                        sandbox.saveSceneCurrentSceneData();
                    }
                };

                //UIController.instance.sendNotification(NameConstants.SHOW_COLOR_PICKER, chooseHandler);
            }
        });

        rotationVal.addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event,
                                             Actor actor,
                                             boolean focused) {
                if (!focused) {
                    setItemRotation(getFloatValue(rotationVal.getText(), ((Actor) item).getRotation()));
                }
            }

        });

        rotationVal.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == 66) {
                    setItemRotation(getFloatValue(rotationVal.getText(), ((Actor) item).getRotation()));
                }
                return true;
            }
        });
    }

    private void setItemRotation(float rotation) {
        if (item.getDataVO().rotation != rotation) {
            item.getDataVO().rotation = rotation;
            item.renew();
            sandbox.getSelector().updateSelections();
            sandbox.saveSceneCurrentSceneData();
        }
    }

    private void setItemScaleY(float scaleY) {
        if (item.getDataVO().scaleY != scaleY) {
            item.getDataVO().scaleY = scaleY;
            item.renew();
            sandbox.getSelector().updateSelections();
            sandbox.saveSceneCurrentSceneData();
        }
    }

    private void setItemY(float y) {
        if (item.getDataVO().y != y) {
            item.getDataVO().y = y;
            item.renew();
            sandbox.saveSceneCurrentSceneData();
        }
    }

    private void setItemX(float x) {
        if (item.getDataVO().x != x) {
            item.getDataVO().x = x;
            item.renew();
            sandbox.saveSceneCurrentSceneData();
        }
    }

    private void setItemHeight(float newHeight, String className) {
        boolean dirty = false;
        switch (className) {
            case "TextBoxItem":
                TextBoxVO textBoxVO = ((TextBoxItem) item).getDataVO();
                if (textBoxVO.height != newHeight) {
                    textBoxVO.height = newHeight;
                    dirty = true;
                }
                break;
            case "LabelItem":
                LabelVO labelVO = ((LabelItem) item).getDataVO();
                if (labelVO.height != newHeight) {
                    labelVO.height = newHeight;
                    dirty = true;
                }
                break;
            case "Image9patchItem":
            case "ImageItem":
                SimpleImageVO imageVO = ((ImageItem) item).getDataVO();
                float newScaleY = newHeight / ((ImageItem) item).getHeight();
                if (imageVO.scaleY != newScaleY) {
                    imageVO.scaleY = newScaleY;
                    dirty = true;
                }
                break;
        }
        if (dirty) {
            item.renew();
            sandbox.saveSceneCurrentSceneData();
            updateView();
            sandbox.getSelector().updateSelections();
        }
    }

    private void setItemWidth(float newWidth, String className) {
        boolean dirty = false;
        switch (className) {
            case "TextBoxItem":
                TextBoxVO textBoxVO = ((TextBoxItem) item).getDataVO();
                if (textBoxVO.width != newWidth) {
                    textBoxVO.width = newWidth;
                    dirty = true;
                }
                break;
            case "SelectBoxItem":
                SelectBoxVO selectBoxVO = ((SelectBoxItem) item).getDataVO();
                if (selectBoxVO.width != newWidth) {
                    selectBoxVO.width = newWidth;
                    dirty = true;
                }
                break;
            case "LabelItem":
                LabelVO labelVO = ((LabelItem) item).getDataVO();
                if (labelVO.width != newWidth) {
                    labelVO.width = newWidth;
                    dirty = true;
                }
                break;
            case "Image9patchItem":
            case "ImageItem":
                SimpleImageVO imageVO = ((ImageItem) item).getDataVO();
                float newScaleX = newWidth / ((ImageItem) item).getWidth();
                if (imageVO.scaleX != newScaleX) {
                    imageVO.scaleX = newScaleX;
                    dirty = true;
                }
                break;
        }
        if (dirty) {
            item.renew();
            sandbox.saveSceneCurrentSceneData();
            updateView();
            sandbox.getSelector().updateSelections();
        }
    }

    private void setItemId(String id) {
        if (item != null && !item.getDataVO().itemIdentifier.equals(id)) {
            item.getDataVO().itemIdentifier = id;
            item.renew();
            sandbox.saveSceneCurrentSceneData();
            sandbox.getUIStage().getItemsBox().initContent();
        }
    }


    public void setItemScaleX(float scaleX) {
        if (item.getDataVO().scaleX != scaleX) {
            item.getDataVO().scaleX = scaleX;
            item.renew();
            sandbox.getSelector().updateSelections();
            sandbox.saveSceneCurrentSceneData();
        }
    }

    public float getFloatValue(String stringValue, float defaultValue) {
        float value = defaultValue;
        try {
            value = Float.parseFloat(stringValue);
        } catch (NullPointerException | NumberFormatException ex) {
            System.out.println(ex);
        }
        return value;
    }

}
