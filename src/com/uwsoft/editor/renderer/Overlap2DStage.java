package com.uwsoft.editor.renderer;


import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.spine.SpineReflectionHelper;

public class Overlap2DStage extends Stage {

	public SceneLoader sceneLoader;
	public RayHandler rayHandler;
	public SkeletonRenderer skeletonRenderer;
	public IResource rm;
	public Essentials essentials = new Essentials();
	
	public Overlap2DStage() {
		super();
		initStage();
	}
	
	public Overlap2DStage(Viewport viewPort) {		
		super(viewPort);
		initStage();
	}
	
	private void initStage() {
		RayHandler.setGammaCorrection(true);
		RayHandler.useDiffuseLight(true);
		
		rayHandler = new RayHandler(null);
		rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 1f);
		rayHandler.setCulling(true);	
		rayHandler.setBlur(true);
		rayHandler.setBlurNum(3);
		rayHandler.setShadows(true);
		rayHandler.setCombinedMatrix(getCamera().combined);
		
		skeletonRenderer = new SkeletonRenderer();
		
		essentials.rayHandler = rayHandler;
		SpineReflectionHelper refdata = new SpineReflectionHelper();
		if(refdata.isSpineAviable){
			essentials.spineReflectionHelper = refdata;
		}
		
	}
	
	@Override
	public void act(float delta)
	{
		rayHandler.setCombinedMatrix(getCamera().combined);
		super.act(delta);
	}
	
	@Override
	public void draw () {
		super.draw();
		rayHandler.updateAndRender();
	}
	
	public void setAmbienceInfo(SceneVO vo) {
		Color clr = new Color(vo.ambientColor[0], vo.ambientColor[1], vo.ambientColor[2], vo.ambientColor[3]);
        rayHandler.setAmbientLight(clr);
	}
}
