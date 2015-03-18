package com.uwsoft.editor.gdx.actors;

import java.awt.Cursor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.actors.basic.PixelRect;
import com.uwsoft.editor.gdx.sandbox.EditingMode;
import com.uwsoft.editor.gdx.stage.SandboxStage;
import com.uwsoft.editor.renderer.actor.IBaseItem;
import com.uwsoft.editor.renderer.actor.LabelItem;

public class SelectionRectangle extends PixelRect {

    public static final int LT = 0;
    public static final int T = 1;
    public static final int RT = 2;
    public static final int R = 3;
    public static final int RB = 4;
    public static final int B = 5;
    public static final int LB = 6;
    public static final int L = 7;
    private IBaseItem host;
    private TextureManager tm;
    private SandboxStage stage;
    private float[] touchDiff = new float[2];
    private Group transformGroup;
    private Image[] miniRects = new Image[8];
    private EditingMode mode;

    public SelectionRectangle(SandboxStage sm) {
        super(sm.textureManager, 0, 0);
        this.tm = sm.textureManager;
        stage = sm;
        setTouchable(Touchable.disabled);
        setVisible(false);
        setOpacity(0.4f);

        transformGroup = new Group();
        addActor(transformGroup);
        initTransformGroup();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (mode != EditingMode.TRANSFORM) return;

        Vector2 mouseCoords = getMouseLocalCoordinates();

        stage.setCurrentlyTransforming(null, -1);

        boolean isOver = false;

        for (int i = 0; i < 8; i++) {
            final int currRectIndex = i;
            Rectangle rect = new Rectangle(miniRects[currRectIndex].getX() - 2, miniRects[currRectIndex].getY() - 2, 8, 8);
            if (rect.contains(mouseCoords) && !(getHostAsActor() instanceof LabelItem)) {
                stage.setCurrentlyTransforming(getHost(), currRectIndex);
                isOver = true;
                switch (currRectIndex) {
                    case LT:
                        stage.setCursor(Cursor.NW_RESIZE_CURSOR);
                        break;
                    case T:
                        stage.setCursor(Cursor.N_RESIZE_CURSOR);
                        break;
                    case RT:
                        stage.setCursor(Cursor.NE_RESIZE_CURSOR);
                        break;
                    case R:
                        stage.setCursor(Cursor.E_RESIZE_CURSOR);
                        break;
                    case RB:
                        stage.setCursor(Cursor.SE_RESIZE_CURSOR);
                        break;
                    case B:
                        stage.setCursor(Cursor.S_RESIZE_CURSOR);
                        break;
                    case LB:
                        stage.setCursor(Cursor.SW_RESIZE_CURSOR);
                        break;
                    case L:
                        stage.setCursor(Cursor.W_RESIZE_CURSOR);
                        break;
                }
            }
        }

        if (!isOver) {
            stage.setCursor(Cursor.DEFAULT_CURSOR);
        }


        // change size according to zoom
    }

    private Vector2 getMouseLocalCoordinates() {
        Vector2 vec = new Vector2();

        vec.x = Gdx.input.getX() - (-stage.getCamera().position.x + stage.getWidth() / 2) - getX();
        vec.y = (stage.getHeight() - Gdx.input.getY()) - (-stage.getCamera().position.y + stage.getHeight() / 2) - getY();

        return vec;
    }

    private void initTransformGroup() {
        miniRects[LT] = getMiniRect();
        miniRects[T] = getMiniRect();
        miniRects[RT] = getMiniRect();
        miniRects[R] = getMiniRect();
        miniRects[RB] = getMiniRect();
        miniRects[B] = getMiniRect();
        miniRects[LB] = getMiniRect();
        miniRects[L] = getMiniRect();
    }

    private void positionTransformables() {
        miniRects[LT].setX(-3);
        miniRects[LT].setY(getHeight() - 2);
        miniRects[T].setX(getWidth() / 2 - 3);
        miniRects[T].setY(getHeight() - 2);
        miniRects[RT].setX(getWidth() - 3);
        miniRects[RT].setY(getHeight() - 2);
        miniRects[R].setX(getWidth() - 3);
        miniRects[R].setY(getHeight() / 2 - 2);
        miniRects[RB].setX(getWidth() - 3);
        miniRects[RB].setY(-2);
        miniRects[B].setX(getWidth() / 2 - 3);
        miniRects[B].setY(-2);
        miniRects[LB].setX(-3);
        miniRects[LB].setY(-2);
        miniRects[L].setX(-3);
        miniRects[L].setY(getHeight() / 2 - 2);
    }

    private Image getMiniRect() {
        Image rect = new Image(tm.getEditorAsset("pixel"));
        rect.setScale(6);
        rect.setColor(new Color(1, 1, 1, 1));

        transformGroup.addActor(rect);

        return rect;
    }

    public void claim(IBaseItem itm) {
        host = itm;
        Actor hostAsActor = getHostAsActor();
        setX(hostAsActor.getX());
        setY(hostAsActor.getY());
        setRotation(hostAsActor.getRotation());
        setWidth(hostAsActor.getWidth() *(hostAsActor instanceof LabelItem ? 1 : hostAsActor.getScaleX()));
        setHeight(hostAsActor.getHeight()*(hostAsActor instanceof LabelItem ? 1 : hostAsActor.getScaleY()));
    }

    public void update() {
        //setX(getHostAsActor().getX() - ((getHostAsActor().getScaleX()-1)*getHostAsActor().getWidth()/2));
        //setY(getHostAsActor().getY() - ((getHostAsActor().getScaleY()-1)*getHostAsActor().getHeight()/2));
        Actor hostAsActor = getHostAsActor();
        setX(hostAsActor.getX());
        setY(hostAsActor.getY());
        setOriginX(hostAsActor.getOriginX());
        setOriginY(hostAsActor.getOriginY());
        setRotation(hostAsActor.getRotation());
        setWidth(hostAsActor.getWidth()*(hostAsActor instanceof LabelItem ? 1 : hostAsActor.getScaleX()));
        setHeight(hostAsActor.getHeight()*(hostAsActor instanceof LabelItem ? 1 : hostAsActor.getScaleY()));

        positionTransformables();
    }

    public void show() {
        update();

        setVisible(true);
    }

    public void hide() {
        setVisible(false);
    }

    public void release() {
        host = null;
        setVisible(false);
        remove();
    }

    public IBaseItem getHost() {
        return host;
    }

    public Actor getHostAsActor() {
        return (Actor) host;
    }

    public void setTouchDiff(float x, float y) {
        touchDiff[0] = x;
        touchDiff[1] = y;
    }

    public float[] getTouchDiff() {
        return touchDiff;
    }

    public void setMode(EditingMode mode) {
        this.mode = mode;
        if (mode == EditingMode.TRANSFORM && !(getHostAsActor() instanceof LabelItem) ) {
            transformGroup.setVisible(true);
        } else {
            transformGroup.setVisible(false);
        }
    }

}
