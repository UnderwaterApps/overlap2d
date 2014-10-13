package com.uwsoft.editor.renderer.actor;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.LightVO;
import com.uwsoft.editor.renderer.data.LightVO.LightType;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.CustomVariables;


public class LightActor extends Actor implements IBaseItem{
	
	public Light lightObject = null;
	public RayHandler rayHandler = null;
	
	private LightVO dataVO;	
	public Essentials essentials;
	public float mulX = 1f;
	public float mulY = 1f;
	
	private float direction;
	
	protected int layerIndex = 0;
	private boolean isLockedByLayer = false;
	private CompositeItem parentItem = null;
	private Image debugImg;
	private Vector2 tmpVector = new Vector2();

    private CustomVariables customVariables = new CustomVariables();

    private Body body;
	
	public LightActor(LightVO data, Essentials e, CompositeItem parent) {
		this(data, e);
		setParentItem(parent);
	}
	
	public LightActor(LightVO data, Essentials e) {
        this.essentials = e;

		rayHandler = essentials.rayHandler;
		dataVO = data;
		setX(dataVO.x);
		setY(dataVO.y);

        customVariables.loadFromString(dataVO.customVars);

		if(dataVO.type == LightType.POINT){
			createPointLight();
		}else{
			createConeLight();
		}
		
		setWidth(40);
		setHeight(40);
	}
	
//	@Override
//	public Actor hit(float x, float y, boolean touchable) {
//		if(debugImg.hit(x, y, touchable)){
//			
//		}
//		return ;
//	}

	public void createPointLight(){
		lightObject = new PointLight(rayHandler, dataVO.rays);
		//Color asd = new Color(vo.tint);
		lightObject.setColor(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
		lightObject.setDistance(dataVO.distance * mulX * PhysicsBodyLoader.SCALE);
		lightObject.setPosition(dataVO.x * mulX * PhysicsBodyLoader.SCALE, dataVO.y * mulY * PhysicsBodyLoader.SCALE);
		lightObject.setStaticLight(dataVO.isStatic);
		lightObject.setActive(true);
		lightObject.setXray(dataVO.isXRay);
		
		
	}

	public void createConeLight(){ 
		lightObject = new ConeLight(rayHandler,  dataVO.rays , Color.WHITE,1,0,0,0,0);
		//Color asd = new Color(vo.tint);
		lightObject.setColor(new Color(dataVO.tint[0], dataVO.tint[1], dataVO.tint[2], dataVO.tint[3]));
		lightObject.setDistance(dataVO.distance * mulX * PhysicsBodyLoader.SCALE);
		lightObject.setPosition(dataVO.x * mulX * PhysicsBodyLoader.SCALE, dataVO.y * mulY * PhysicsBodyLoader.SCALE);
		lightObject.setStaticLight(dataVO.isStatic);
		direction = dataVO.directionDegree;
		lightObject.setDirection(direction);
		((ConeLight)lightObject).setConeDegree(dataVO.coneDegree);
		lightObject.setActive(true);
		lightObject.setXray(dataVO.isXRay);
		
		
	}

	@Override
	public void act(float delta) {
		
		//coordinate fix
		//TODO to STAGE COORDS for relativ coords
		
		float relativeX = getX();
		float relativeY = getY();
		float relativeRotation = 0;
		
		Group currParent = this.getParent();
		while(currParent != null) {
			relativeX+=currParent.getX();
			relativeY+=currParent.getY();
			relativeRotation+=currParent.getRotation();
			currParent = currParent.getParent();
		}
		
		if(lightObject != null){
			
			float yy = 0;
			float xx = 0;
			
			if(relativeRotation != 0){
				xx = getX()*MathUtils.cosDeg(relativeRotation) - getY()*MathUtils.sinDeg(relativeRotation);
				yy = getY()*MathUtils.cosDeg(relativeRotation) + getX()*MathUtils.sinDeg(relativeRotation);
				yy=getY()-yy;
				xx=getX()-xx;
			}
			lightObject.setPosition((relativeX-xx+20)*PhysicsBodyLoader.SCALE, (relativeY-yy+20)*PhysicsBodyLoader.SCALE);
		}
		if(dataVO.type == LightType.CONE){
			lightObject.setDirection(direction+relativeRotation);
		}
		//debugImg.setX(relativeX);
		//debugImg.setY(relativeY);
		super.act(delta);                                     
	}
	
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {		
		super.draw(batch, parentAlpha);
	}

	public void changeDistance(int amount) {
		lightObject.setDistance(amount * PhysicsBodyLoader.SCALE);
	}

	public LightVO getDataVO() {
		//updateDataVO();
		return dataVO;
	}
	
	
	public void renew() {
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
		removeLights();
		if(dataVO.type == LightType.POINT){
			createPointLight();
		}else{
			createConeLight();
		}
        customVariables.loadFromString(dataVO.customVars);
	}
	
	@Override
	public float getRotation() {
		if(dataVO.type == LightType.POINT){
			return 0;
		}
		return super.getRotation();
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
	
	public void updateDataVO() {
		dataVO.distance = (int) lightObject.getDistance()/this.mulX/PhysicsBodyLoader.SCALE;
		
		System.out.println("dataVO.distance = " + dataVO.distance);
		
		dataVO.directionDegree = direction;

        if(dataVO.type == LightType.CONE) {
            dataVO.coneDegree = ((ConeLight) lightObject).getConeDegree();
        }

		dataVO.x = getX()/this.mulX;
		dataVO.y = getY()/this.mulY;

		if(dataVO.layerName == null || dataVO.layerName.equals("")) {
			dataVO.layerName = "Default";
		}

        dataVO.customVars = customVariables.saveAsString();
	}

	public void applyResolution(float mulX, float mulY) {
		this.mulX = mulX;
		this.mulY = mulY;
		setX(dataVO.x*this.mulX);
		setY(dataVO.y*this.mulY);
		if(lightObject != null){
			lightObject.setDistance(dataVO.distance * this.mulX * PhysicsBodyLoader.SCALE);
		}
		updateDataVO();			
	}

	@Override
	public int getLayerIndex() {
		return layerIndex;
	}

	@Override
	public void setLayerIndex(int index) {
		layerIndex = index;
	}
	
	public CompositeItem getParentItem() {
		return parentItem;
	}
	
	public void setParentItem(CompositeItem parentItem) {
		this.parentItem = parentItem;
	}
	
	@Override
	public void setColor(Color color) {
		if(color == null){
			return;
		}
		lightObject.setColor(color);
	}
	
	@Override
	public Color getColor() {
		return lightObject.getColor();
	}
	
	@Override
	public void setRotation(float degrees) {
		if(dataVO.type == LightType.CONE){
			lightObject.setDirection(direction+degrees);
		} 
		//super.setRotation(degrees);
	}

	@Override
	public void rotateBy(float ammount) {
		if(dataVO.type == LightType.POINT){
			lightObject.setDistance(lightObject.getDistance() + ammount*PhysicsBodyLoader.SCALE);
		} else {
			direction+=ammount;
			lightObject.setDirection(direction);
		}	
		//super.rotateBy(ammount);
	}
	
	@Override
	public void dispose() {
		removeLights();

        if(essentials.world != null && getBody() != null)essentials.world.destroyBody(getBody());
        setBody(null);
	}


    public void removeLights() {
        if(lightObject == null) return;
        lightObject.remove();
        lightObject = null;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public CustomVariables getCustomVariables() {
        return customVariables;
    }
}
