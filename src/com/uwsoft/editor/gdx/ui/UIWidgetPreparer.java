package com.uwsoft.editor.gdx.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;

import java.util.Locale;
import java.util.function.Consumer;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

/**
 * Created by Osman on 09.04.2015.
 *
 */
final public class UIWidgetPreparer {

    public static TextField textFieldBroker(TextField textField, boolean enableNumberHandling, Consumer<String> onApplyText) {

        final FocusChecker focusChecker = new FocusChecker();

        // on focus change
        textField.addListener(new FocusChecker() {
            public void keyboardFocusChanged(FocusListener.FocusEvent event, Actor actor, boolean focused) {
                super.keyboardFocusChanged(event, actor, focused);
                if (!focused) {
                    onApplyText.accept(textField.getText());
                }
            }
        });

        // on pressing enter
        textField.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    onApplyText.accept(textField.getText());
                }
                return true;
            }
        });


        final ChangeValueByInput valChanger = new ChangeValueByInput(textField, focusChecker, onApplyText);

        // number value handling
        if (enableNumberHandling) {
            textField.addListener(valChanger);
            textField.addAction(forever(run(() ->
                            valChanger.update(Gdx.graphics.getDeltaTime())
            )));
        }

        return textField;
    }

    private static class ChangeValueByInput extends ClickListener {

        private final static float TYPE_THRESHOLD = 0.15f;

        private TextField owner;
        private FocusChecker checker;
        Consumer<String> onApplyText;


        static Interpolation valChangeInterpolation = Interpolation.pow4In;
        private float holdBeginValue;
        private int holdKeyCode;
        private boolean isHolding;
        private float holdDuration;

        ChangeValueByInput(TextField owner, FocusChecker checker, Consumer<String> onApplyText) {
            this.owner = owner;
            this.checker = checker;
            this.onApplyText = onApplyText;
        }

        void update(float delta) {

            if (isHolding) {
                holdDuration += delta;
                int dir = 0;
                if (holdKeyCode == Input.Keys.UP) dir = +1;
                else if (holdKeyCode == Input.Keys.DOWN) dir = -1;
                keepChangeValue(dir);
            }
        }

        float getValue() {
            try {
                return Float.parseFloat(owner.getText());
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }

        public void keepChangeValue(int dir) {
            setValue(holdBeginValue + dir * valChangeInterpolation.apply(holdDuration));
        }

        private void setValue(float newVal) {
            String text = String.format(Locale.ENGLISH, "%.2f", newVal);
            owner.setText(text);
            onApplyText.accept(text);
        }

        @Override
        public boolean scrolled(InputEvent event, float x, float y, int amount) {
            System.out.println("scroll amount: " + amount);
            return super.scrolled(event, x, y, amount);
        }

        private void changeValueByFixStep() {
            float fixStep = 0;
            if (holdKeyCode == Input.Keys.UP) {
                fixStep = +1;
                isHolding = false;
            } else if (holdKeyCode == Input.Keys.DOWN) {
                fixStep = -1;
                isHolding = false;
            }
            setValue(getValue() + fixStep);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (checker.isFocused)
                switch (keycode) {
                    case Input.Keys.UP:
                    case Input.Keys.DOWN:

                        holdKeyCode = event.getKeyCode();
                        holdBeginValue = getValue();
                        isHolding = true;
                        holdDuration = 0;
                }
            return false;
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            if (keycode == holdKeyCode) {
                if (holdDuration < TYPE_THRESHOLD) {
                    changeValueByFixStep();
                }
                isHolding = false;
            }
            return false;
        }
    }

    private static class FocusChecker extends FocusListener {

        public boolean isFocused = false;

        @Override
        public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
            isFocused = focused;
        }
    }
}
