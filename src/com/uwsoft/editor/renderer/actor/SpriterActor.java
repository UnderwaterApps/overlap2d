package com.uwsoft.editor.renderer.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Rectangle;
import com.brashmonkey.spriter.SCMLReader;
import com.uwsoft.editor.renderer.actor.SpriteAnimation.Animation;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.SpriterVO;
import com.uwsoft.editor.renderer.ui.IBaseItem;
import com.uwsoft.editor.renderer.utils.CustomVariables;
import com.uwsoft.editor.renderer.utils.LibGdxDrawer;
import com.uwsoft.editor.renderer.utils.LibGdxLoader;

/**
 * Created by hayk on 12/8/14.
 */
public class SpriterActor extends Actor implements IBaseItem {
    private final Essentials essentials;
    public float mulX = 1f;
    public float mulY = 1f;
    public SpriterVO dataVO;
    public boolean looping;
    protected int layerIndex = 0;
    protected boolean reverse = false;
    private boolean isLockedByLayer = false;
    private CompositeItem parentItem = null;

    private int frameHeight;
    private int frameWidth;

    private CustomVariables customVariables = new CustomVariables();
    private String currentAnimationName = "";

    private Body body;

    private LibGdxDrawer drawer;
    private Player player;
    private Data data;
    private ArrayList<String> animations = new ArrayList<String>();
    private ArrayList<String> entities = new ArrayList<String>();
    private int currentEntityIndex	=	0;
    private int currentAnimationIndex;
    public SpriterActor(SpriterVO vo, Essentials e, CompositeItem parent) {
        this(vo, e);
        setParentItem(parent);
    }

    public SpriterActor(SpriterVO vo, Essentials e) {
        essentials = e;
        dataVO = vo;
        setX(dataVO.x);
        setY(dataVO.y);
        customVariables.loadFromString(dataVO.customVars);
        this.setRotation(dataVO.rotation);

        if (dataVO.zIndex < 0) dataVO.zIndex = 0;

        if (dataVO.tint == null) {
            setTint(new Color(1, 1, 1, 1));
        } else {
            setTint(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
        }
        initSpriteAnimation();
    }

  

    private void initSpriteAnimation() {
        setOriginX(0);
        setOriginY(0);

        FileHandle handle 	=	essentials.rm.getSCMLFile(dataVO.animationName);
        data 			= 	new SCMLReader(handle.read()).getData();		
		LibGdxLoader loader = 	new LibGdxLoader(data);
		loader.load(handle.file());
		ShapeRenderer renderer	=	new ShapeRenderer();
		drawer = new LibGdxDrawer(loader, renderer);
		currentAnimationIndex	=	dataVO.animation;	
		currentEntityIndex		=	dataVO.entity;	
		initPlayer();
    }

    private void initPlayer() {
    	player = new Player(data.getEntity(currentEntityIndex));
    	player.setAnimation(currentAnimationIndex);
    	player.setScale(dataVO.scale * this.mulX);
    	setRectangle();		
	}

	private void setRectangle() {
		player.update();
		Rectangle bbox = player.getBoundingRectangle(null);
        frameWidth = (int) bbox.size.width;
		frameHeight = (int) bbox.size.height;
        setWidth(frameWidth);
        setHeight(frameHeight);	
	}

	@Override
    public void act(float delta) {
        //if (paused) return;
        super.act(delta);       
        player.update();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {    	
    	batch.setColor(1, 1, 1, parentAlpha * getColor().a);    	
        super.draw(batch, parentAlpha);
        
        player.setPosition(getX(), getY());
        player.setPivot(getWidth()/2, getHeight()/2);
        player.setScale(dataVO.scale * this.mulX);
        player.rotate(getRotation()-player.getAngle());        
        drawer.beforeDraw(player,batch);        
    }
    @Override
    public void renew() {
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        player.setScale(dataVO.scale * this.mulX);
        setRotation(dataVO.rotation);        
        customVariables.loadFromString(dataVO.customVars);       
        setRectangle();
    }

    public void setSpriterScale(float scale) {  	
    	dataVO.scale = scale;    	
    	renew();
    }
  
    public void setTint(Color tint) {
        float[] clr = new float[4];
        clr[0] = tint.r;
        clr[1] = tint.g;
        clr[2] = tint.b;
        clr[3] = tint.a;
        this.getDataVO().tint = clr;
        this.setColor(tint);
    }

    @Override
    public MainItemVO getDataVO() {
        return dataVO;
    }

    @Override
    public boolean isLockedByLayer() {
        return isLockedByLayer;
    }

    @Override
    public void setLockByLayer(boolean isLocked) {
        isLockedByLayer = isLocked;
    }

    @Override
    public boolean isComposite() {
        return false;
    }

  

    @Override
    public int getLayerIndex() {
        return layerIndex;
    }

    @Override
    public void setLayerIndex(int index) {
        layerIndex = index;
    }

    @Override
    public void updateDataVO() {
        dataVO.x = getX() / this.mulX;
        dataVO.y = getY() / this.mulY;
        dataVO.rotation = getRotation();

        if (getZIndex() >= 0) {
            dataVO.zIndex = getZIndex();
        }

        if (dataVO.layerName == null || dataVO.layerName.equals("")) {
            dataVO.layerName = "Default";
        }

        dataVO.entity		=	currentEntityIndex;
        dataVO.animation	=	currentAnimationIndex;
        dataVO.customVars = customVariables.saveAsString();
    }

    @Override
    public void applyResolution(float mulX, float mulY) {
        this.mulX = mulX;
        this.mulY = mulY;
        setX(dataVO.x * this.mulX);
        setY(dataVO.y * this.mulY);
        updateDataVO();
        initPlayer();
    }

    @Override
    public CompositeItem getParentItem() {
        return parentItem;
    }

    @Override
    public void setParentItem(CompositeItem parent) {
        parentItem = parent;
    }

  

    public CustomVariables getCustomVariables() {
        return customVariables;
    }

	@Override
	public Body getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBody(Body body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<String> getAnimations() {		
		animations	=	new ArrayList<String>();
		
		for (int i = 0; i < data.getEntity(currentEntityIndex).animations(); i++) {
			animations.add(data.getEntity(currentEntityIndex).getAnimation(i).name);
		} 
        return animations;
    }

	public void setAnimation(int i) {
		currentAnimationIndex	=	i;
	 	updateDataVO();
		initPlayer();		
	}
	public void setEntity(int i) {
		currentEntityIndex	=	i;	
		setAnimation(0);
		updateDataVO();
		initPlayer();	
	}

	public ArrayList<String> getEntities() {
		entities	=	data.getEntities();	
		return	 entities;
	}

	public String getCurrentEntityName() {
		return entities.get(currentEntityIndex);
	}
	public int getCurrentEntityIndex() {		
		return currentEntityIndex;
	}
	public String getCurrentAnimationName() {
        return currentAnimationName;
    }
    public int getCurrentAnimationIndex() {
    	return currentAnimationIndex;
    }
}
