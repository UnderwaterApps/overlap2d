package com.uwsoft.editor.renderer;


import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.data.Essentials;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.spine.SpineReflectionHelper;


/**
 * Default Stage for Overlap2D that will take care of
 * Creating default scene loader, loading and simulating physics,
 * loading and simulating light system and creating skeleton renderer for spine animations
 * This class is intended to be extended
 *
 * @author Avetis Zakharyan | http://www.overlap2d.com
 */
public class Overlap2DStage extends Stage {

	public SceneLoader sceneLoader;
	
	public Essentials essentials;

    private float timeAcc = 0;
    
    public boolean physiscStopped = false;
	
	public Overlap2DStage() {
		super();
		initStage();
	}
	
	public Overlap2DStage(Viewport viewPort) {		
		super(viewPort);
		initStage();
	}

    /**
     * initializes empty holders for future data like essentials
     * to hold RayHandler for lights, and skeletonRenderer for spine animations
     */
	protected void initStage() {
		essentials = new Essentials();
		SpineReflectionHelper refdata = new SpineReflectionHelper();
		if(refdata.isSpineAviable){
			essentials.spineReflectionHelper = refdata;
		}
		initLightsConfiguration();
	}

    /**
     * Loads particular scene into Actor as well as
     * initializes ambient light, physics bodies and adds root actor to the stage for instant rendering
     * It is recommended that you override this
     *
     * @param name - scene name as it is in file system without ".dt" extension
     */
    public void loadScene(String name) {
        sceneLoader.loadScene(name);

        setAmbienceInfo(sceneLoader.getSceneVO());

        addActor(sceneLoader.getRoot());
    }

    /**
     * Currently creates the Default ResourceManager,
     * loads all the assets that can possible be on every possible scene
     * And does it for default "orig" resolution only.
     *
     * IMPORTANT
     * This is a very default solution and it is extremely recommended
     * for you to override this for non test projects
     *
     * @see com.uwsoft.editor.renderer.SceneLoader
     * @see com.uwsoft.editor.renderer.resources.ResourceManager
     */
	public void initSceneLoader() {
        ResourceManager rm = new ResourceManager();

        // Loading all assets/resources into memory
        rm.initAllResources();

		sceneLoader = new SceneLoader(essentials);
		essentials.rayHandler.setWorld(essentials.world);
        essentials.rm = rm;
	}

    /**
     * Does the routine of configuring the default light system
     * by setting gray ambient light, and some settings
     */
	protected void initLightsConfiguration() {
		RayHandler.setGammaCorrection(true);
		RayHandler.useDiffuseLight(true);

        RayHandler rayHandler = new RayHandler(null);
		rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 1f);
		rayHandler.setCulling(true);	
		rayHandler.setBlur(true);
		rayHandler.setBlurNum(3);
		rayHandler.setShadows(true);
		rayHandler.setCombinedMatrix(getCamera().combined);

        essentials.rayHandler = rayHandler;
	}

	@Override
	public void act(float delta)
	{
        if(essentials.world != null && !physiscStopped) {
            // physics is enabled
            while (timeAcc < delta) {
                timeAcc += 1f/60; 
                essentials.world.step(1f/60, 10, 10);
            }
            timeAcc -= delta;
        }

        if(essentials.rayHandler != null) {
            essentials.rayHandler.setCombinedMatrix(getCamera().combined.scl(1/PhysicsBodyLoader.SCALE));
        }

		super.act(delta);
	}
	
	@Override
	public void draw () {
		super.draw();

        if(essentials.rayHandler != null) {
            essentials.rayHandler.updateAndRender();
        }
	}
	
	public void setAmbienceInfo(SceneVO vo) {
		Color clr = new Color(vo.ambientColor[0], vo.ambientColor[1], vo.ambientColor[2], vo.ambientColor[3]);
        essentials.rayHandler.setAmbientLight(clr);
	}


    /**
     *
     * @return Physics World
     */
    public World getWorld() {
        return essentials.world;
    }
}
