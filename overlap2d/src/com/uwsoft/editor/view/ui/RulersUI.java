package com.uwsoft.editor.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.utils.Guide;
import com.uwsoft.editor.view.stage.Sandbox;

/**
 * Created by azakhary on 7/18/2015.
 */
public class RulersUI extends Actor {
	
    private static final String CLASS_NAME = "com.uwsoft.editor.view.ui.RulersUI";
    public static final String ACTION_GUIDES_MODIFIED = CLASS_NAME + "ACTION_GUIDES_MODIFIED";
    public static final String RIGHT_CLICK_RULER = CLASS_NAME + "RIGHT_CLICK_RULER";

    private static final int rulerBoxSize = 14;

    private static final int topOffset = 49;
    private static final int leftOffset = 40;

    private static final int separatorsCount = 20;

    private static final Color BG_COLOR = new Color(48f/255f, 48f/255f, 48f/255f, 1f);
    private static final Color LINE_COLOR = new Color(85f/255f, 85f/255f, 85f/255f, 1f);
    private static final Color GUIDE_COLOR = new Color(255f/255f, 94f/255f, 0f/255f, 0.5f);
    private static final Color OVER_GUIDE_COLOR = new Color(255f/255f, 173f/255f, 125f/255f, 1f);
    private static final Color TEXT_COLOR = new Color(194f/255f, 194f/255f, 194f/255f, 1f);
    
    //Allows the ChangeRulerXPositionCommand to change the guide's position from the function UpdateGuideManully
	private static Guide editableDraggingGuide = null;

    private ShapeRenderer shapeRenderer;

    private Rectangle horizontalRect, verticalRect;

    private boolean isShowingPixels = false;

    private float viewMeasurableWidth;
    private float viewMeasurableHeight;

    private float gridMeasuringSize;
    private float gridMeasuringSizeInWorld;
    private float gridMeasureToDisplayScale;

    private Array<VisLabel> labels = new Array<>();

    private Array<Guide> guides = new Array<>();
    private Guide mouseOverGuide = null;

    private VisLabel guidePosLbl;

    private Guide draggingGuide = null;

    public RulersUI() {
        shapeRenderer = new ShapeRenderer();

        horizontalRect = new Rectangle();
        verticalRect = new Rectangle();

        guidePosLbl = new VisLabel();

        addListeners();
    }

    private void addListeners() {
        addListener(new ClickListener() {

            private boolean isTouchingDownRuler;
            private boolean isTouchDownRulerVertical;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);

                Circle touchCircle =  new Circle();
                touchCircle.radius = 5;
                touchCircle.setPosition(x - Gdx.graphics.getWidth() / 2, y - Gdx.graphics.getHeight() / 2);

                isTouchingDownRuler = false;
                if(verticalRect.contains(touchCircle.x, touchCircle.y)) {
                    isTouchDownRulerVertical = true;
                    isTouchingDownRuler = true;
                }
                if(horizontalRect.contains(touchCircle.x, touchCircle.y)) {
                    isTouchDownRulerVertical = false;
                    isTouchingDownRuler = true;
                }

                // check for collision with guides.
                Guide collisionGuide = guideCollision(x, y);
                if(collisionGuide != null) {
                    draggingGuide = collisionGuide;
                }
                
                if (button == 1) {
                	editableDraggingGuide = draggingGuide;
                	Overlap2DFacade.getInstance().sendNotification(RIGHT_CLICK_RULER);
                }

                return true;
            }
            
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);

                Vector2 downPost = new Vector2(getTouchDownX(), getTouchDownY());
                if (isTouchingDownRuler && draggingGuide == null && downPost.dst(x, y) > 3) {
                    draggingGuide = new Guide(isTouchDownRulerVertical);
                    guides.add(draggingGuide);
                }

                //Changes the dragging guide's position to the world position
                if(draggingGuide != null) {
                    Vector2 worldCoords = hereToWorld(new Vector2(x-Gdx.graphics.getWidth()/2, y-Gdx.graphics.getHeight()/2));
                    if (draggingGuide.isVertical) {
                        draggingGuide.pos = worldCoords.x;
                        if (!isShowingPixels)
                        	snap(draggingGuide);
                    } else {
                        draggingGuide.pos = worldCoords.y;
                        if (!isShowingPixels) {
                        	snap(draggingGuide);
                        }
                    }
                }

            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if(getTapCount() >= 2) {
                    // double click toggles the mode
                    isShowingPixels = !isShowingPixels;
                }

                if(draggingGuide != null) {
                    if((draggingGuide.isVertical && x-Gdx.graphics.getWidth()/2 < verticalRect.x+verticalRect.getWidth()) ||
                            (!draggingGuide.isVertical && y-Gdx.graphics.getHeight()/2 >  horizontalRect.y)) {
                        guides.removeValue(draggingGuide, true);
                    }

                    Overlap2DFacade.getInstance().sendNotification(ACTION_GUIDES_MODIFIED);
                }
                draggingGuide = null;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                mouseOverGuide = guideCollision(x, y);
                return super.mouseMoved(event, x, y);
            }
        });
    }

    @Override
    public void act(float delta) {
        if(!isVisible()) return;

        super.act(delta);

        horizontalRect.set(-Gdx.graphics.getWidth()/2+leftOffset, Gdx.graphics.getHeight()/2 - rulerBoxSize-topOffset, Gdx.graphics.getWidth()-leftOffset, rulerBoxSize);
        verticalRect.set(-Gdx.graphics.getWidth()/2+leftOffset, -Gdx.graphics.getHeight()/2, rulerBoxSize, Gdx.graphics.getHeight()-topOffset);

        //calculating sizes
        viewMeasurableWidth = Sandbox.getInstance().getViewport().getWorldWidth() * Sandbox.getInstance().getCamera().zoom;
        viewMeasurableHeight = Sandbox.getInstance().getViewport().getWorldHeight() * Sandbox.getInstance().getCamera().zoom;

        if(isShowingPixels) {
            viewMeasurableWidth = viewMeasurableWidth * Sandbox.getInstance().getPixelPerWU();
            viewMeasurableHeight = viewMeasurableHeight * Sandbox.getInstance().getPixelPerWU();
        }

        gridMeasureToDisplayScale = Gdx.graphics.getWidth()/viewMeasurableWidth;

        gridMeasuringSize = viewMeasurableWidth/separatorsCount;
        if(gridMeasuringSize <= 10) {
            gridMeasuringSize = Math.round(gridMeasuringSize);

        } else if (gridMeasuringSize > 10 && gridMeasuringSize <= 20) {
            gridMeasuringSize = Math.round(gridMeasuringSize/5)*5;
        } else {
            gridMeasuringSize = Math.round(gridMeasuringSize/10)*10;
        }

        gridMeasuringSizeInWorld = gridMeasuringSize;
        if (isShowingPixels) {
            gridMeasuringSizeInWorld = gridMeasuringSize/Sandbox.getInstance().getPixelPerWU();
        }

    }

    private Vector2 worldToHere(Vector2 tmp) {
        tmp = Sandbox.getInstance().worldToScreen(tmp);
        tmp.x-=Gdx.graphics.getWidth()/2;
        tmp.y-=Gdx.graphics.getHeight()/2;

        return tmp;
    }

    private Vector2 hereToWorld(Vector2 tmp) {
        tmp.x+=Gdx.graphics.getWidth()/2;
        tmp.y+=Gdx.graphics.getHeight()/2;
        tmp = Sandbox.getInstance().screenToWorld(tmp);

        return tmp;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        OrthographicCamera uiCamera = (OrthographicCamera) getStage().getCamera();

        Gdx.gl.glLineWidth(1.0f);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(uiCamera.projection);

        drawShapes();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        batch.setColor(Color.WHITE);

        drawBatch(batch, parentAlpha);
    }

    public void drawShapes() {
        drawBg();
        drawLines();
    }

    public void drawBg() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(BG_COLOR);
        shapeRenderer.rect(horizontalRect.x, horizontalRect.y, horizontalRect.width, horizontalRect.height);
        shapeRenderer.rect(verticalRect.x, verticalRect.y, verticalRect.width, verticalRect.height);

        shapeRenderer.end();
    }

    public void drawLines() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(LINE_COLOR);

        // Static Lines for Aesthetics
        shapeRenderer.line(horizontalRect.x + rulerBoxSize, horizontalRect.y, horizontalRect.x + horizontalRect.width, horizontalRect.y);
        shapeRenderer.line(verticalRect.x + verticalRect.width+1, verticalRect.y, verticalRect.x + verticalRect.width+1, verticalRect.y + verticalRect.height - rulerBoxSize);

        //Functional lines to show grid
        Vector2 startPoint = new Vector2(horizontalRect.x + rulerBoxSize, verticalRect.y);
        Vector2 worldStartPoint = hereToWorld(startPoint);
        worldStartPoint.x -= worldStartPoint.x % gridMeasuringSizeInWorld;
        worldStartPoint.y -= worldStartPoint.y % gridMeasuringSizeInWorld;
        Vector2 worldStartPointCpy = new Vector2(worldStartPoint);
        Vector2 gridCurrPoint = worldToHere(worldStartPoint);

        labels.clear();

        String postFix = "";
        if(isShowingPixels) {
            postFix = "px";
            worldStartPointCpy.x *= Sandbox.getInstance().getPixelPerWU();
            worldStartPointCpy.y *= Sandbox.getInstance().getPixelPerWU();
        }

        float gridSize = gridMeasuringSize*gridMeasureToDisplayScale;
        int iterator = 0;
        while(gridCurrPoint.x < horizontalRect.x+horizontalRect.getWidth()) {
            shapeRenderer.line(gridCurrPoint.x, horizontalRect.y, gridCurrPoint.x, horizontalRect.y+rulerBoxSize);
            shapeRenderer.line(gridCurrPoint.x+gridSize/2, horizontalRect.y, gridCurrPoint.x+gridSize/2, horizontalRect.y+rulerBoxSize/2);

            VisLabel label = Pools.obtain(VisLabel.class);
            label.setPosition(Gdx.graphics.getWidth()/2 + gridCurrPoint.x+2, Gdx.graphics.getHeight()/2 + horizontalRect.y+7);
            label.setColor(TEXT_COLOR);
            label.setText((int)Math.abs(worldStartPointCpy.x + iterator * gridMeasuringSize)+postFix);
            labels.add(label);

            gridCurrPoint.x+=gridSize;
            iterator++;
        }
        iterator = 0;
        while(gridCurrPoint.y < verticalRect.y+verticalRect.getHeight()) {
            shapeRenderer.line(verticalRect.x+verticalRect.getWidth(), gridCurrPoint.y, verticalRect.x+verticalRect.getWidth()-rulerBoxSize, gridCurrPoint.y);
            shapeRenderer.line(verticalRect.x+verticalRect.getWidth(), gridCurrPoint.y+gridSize/2, verticalRect.x+verticalRect.getWidth()-rulerBoxSize/2, gridCurrPoint.y+gridSize/2);

            VisLabel label = Pools.obtain(VisLabel.class);
            label.setColor(TEXT_COLOR);
            String lblText = (int)Math.abs(worldStartPointCpy.y + iterator * gridMeasuringSize)+"";
            lblText = verticalize(lblText);
            label.setText(lblText);
            label.setWrap(true);
            label.setPosition(Gdx.graphics.getWidth()/2 + verticalRect.x+3, Gdx.graphics.getHeight()/2 + gridCurrPoint.y - label.getPrefHeight()/2);
            labels.add(label);

            gridCurrPoint.y+=gridSize;
            iterator++;
        }

        drawGuides();

        shapeRenderer.end();
    }

    public void drawGuides() {
        for(int i = 0; i < guides.size; i++) {
            Guide guide = guides.get(i);

            if(mouseOverGuide == guide) {
                shapeRenderer.setColor(OVER_GUIDE_COLOR);
            } else {
                shapeRenderer.setColor(GUIDE_COLOR);
            }

            if(guide.isVertical) {
                Vector2 localCoords = worldToHere(new Vector2(guide.pos, 0));
                if(localCoords.x > verticalRect.x+verticalRect.width) {
                    shapeRenderer.line(localCoords.x, -Gdx.graphics.getHeight() / 2, localCoords.x, horizontalRect.y);
                }
            } else {
                Vector2 localCoords = worldToHere(new Vector2(0, guide.pos));
                if(localCoords.y < horizontalRect.y) {
                    shapeRenderer.line(verticalRect.x + verticalRect.getWidth(), localCoords.y, Gdx.graphics.getWidth(), localCoords.y);
                }
            }
        }
    }

    public void drawBatch(Batch batch, float parentAlpha) {
        for(int i = 0; i < labels.size; i++) {
            labels.get(i).draw(batch, parentAlpha);
            Pools.free(labels.get(i));
        }

        if(draggingGuide != null) {
            float pos = draggingGuide.pos;
            String axis = "Y";
            String postfix = "";
            if(draggingGuide.isVertical) axis = "X";
            if(isShowingPixels) {
                pos = draggingGuide.pos * Sandbox.getInstance().getPixelPerWU();
                postfix="px";
            }
            
            //Rounds the guide's position to the nearest 100th, if in World Unit mode
            String positionAsString = "" + pos;
            if (!isShowingPixels) {
            	pos = (float) Math.round(pos * 100) / 100;
            	positionAsString = String.format("%.2f", pos);
            }
            else
            	pos = (float) (Math.round(pos * 100)/100);

            guidePosLbl.setText(axis+": "+ positionAsString + postfix);
            guidePosLbl.setPosition(Gdx.input.getX()+15, Gdx.graphics.getHeight() - Gdx.input.getY()+15);
            guidePosLbl.draw(batch, parentAlpha);
        }
    }

    private String verticalize(String text) {
        String newText = "";
        for(int i = 0; i < text.length(); i++) {
            newText += text.charAt(i) + "\n";
        }

        return newText;
    }

    @Override
    public Actor hit (float x, float y, boolean touchable) {
        if(verticalRect.contains(x-Gdx.graphics.getWidth()/2, y-Gdx.graphics.getHeight()/2) || horizontalRect.contains(x-Gdx.graphics.getWidth()/2, y-Gdx.graphics.getHeight()/2)) {
            return this;
        }

        mouseOverGuide = guideCollision(x, y);
        if(mouseOverGuide != null) {
            return this;
        }

        return null;
    }

    public Guide guideCollision(float x, float y) {
        Vector2 point = new Vector2(x-Gdx.graphics.getWidth()/2, y-Gdx.graphics.getHeight()/2);
        point = hereToWorld(point);

        Circle touchCircle =  new Circle();
        touchCircle.radius = 3f/Sandbox.getInstance().getPixelPerWU();
        touchCircle.setPosition(point.x, point.y);


        for(int i = 0; i < guides.size; i++) {
            if(guides.get(i).isVertical) {
                // this is really weird that I have to substract half of radius.... I am totally lost.
                if(touchCircle.contains(guides.get(i).pos- touchCircle.radius/2f, touchCircle.y)) {
                    return guides.get(i);
                }
            } else {
                if(touchCircle.contains(touchCircle.x, guides.get(i).pos- touchCircle.radius/2f)) {
                    return guides.get(i);
                }
            }
        }

        return null;
    }
    
    //Snaps to nearest quarter if less than 0.04 World Units away
    private void snap(Guide guide) {
    	float snapDistance = 0.04f;
    	float absoluteValPos = Math.abs(guide.pos);
    	float nearestQuarter = Math.round(absoluteValPos * 4) / 4f;
    	if (Math.abs(absoluteValPos - nearestQuarter) < snapDistance) {
    		absoluteValPos = nearestQuarter;
    	}
    	if (guide.pos < 0)
    		absoluteValPos *= -1;
    	guide.pos = absoluteValPos;
    }
    
    public static Guide getPreviousGuide() {
    	return editableDraggingGuide;
    }
    
    //Allows the ChangeRulerXPositionCommand to change the guide's position
    public static void updateGuideManually(float destination) {
    	editableDraggingGuide.pos = destination;
    }

    public Array<Guide> getGuides() {
        return guides;
    }

    public void setGuides(Array<Guide> guides) {
        this.guides = guides;
    }
}
