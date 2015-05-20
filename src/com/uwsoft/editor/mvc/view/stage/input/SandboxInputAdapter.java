package com.uwsoft.editor.mvc.view.stage.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.view.stage.SandboxMediator;
import com.uwsoft.editor.renderer.conponents.NodeComponent;
import com.uwsoft.editor.renderer.conponents.ParentNodeComponent;
import com.uwsoft.editor.renderer.conponents.ViewPortComponent;

public class SandboxInputAdapter implements InputProcessor {

	private Overlap2DFacade facade;
	private Family root;
	private Engine engine;
	private ImmutableArray<Entity> entities;
	private ComponentMapper<NodeComponent> nodeCM; 
	private ComponentMapper<InputListenerComponent> inputListenerCM; 
	private InputListenerComponent inpputListenerComponent;
	private Entity target;
	private Vector2 hitTargetLocalCoordinates = new Vector2();

	public SandboxInputAdapter() {
		facade = Overlap2DFacade.getInstance();
		SandboxMediator sandboxMediator = facade.retrieveMediator(SandboxMediator.NAME);
		engine = sandboxMediator.getViewComponent().getEngine();
		//family = Family.all(InputListenerComponent.class).get();
		root = Family.all(ViewPortComponent.class).get();
		inputListenerCM = ComponentMapper.getFor(InputListenerComponent.class);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		entities = engine.getEntitiesFor(root);
		hitTargetLocalCoordinates.set(screenY, screenY);
		for (int i = 0, n = entities.size(); i < n; i++){
			Entity entity = entities.get(i);
			
			NodeComponent nodeComponent = nodeCM.get(entity); 
			Entity hit = nodeComponent.hit(hitTargetLocalCoordinates);
			if(hit != null){
				inpputListenerComponent = inputListenerCM.get(hit);
				Array<InputListener> asd = inpputListenerComponent.getAllListeners();
				for (int j = 0, s = asd.size; j < s; j++){
					if (asd.get(j).touchDown(entity, screenX, screenY, pointer, button)){
						target = entity;
						return true;
					}
				}
			}
			
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(target == null){
			return false;
		}
		inpputListenerComponent = inputListenerCM.get(target);
		Array<InputListener> asd = inpputListenerComponent.getAllListeners();
		for (int j = 0, s = asd.size; j < s; j++){
			asd.get(j).touchUp(target, screenX, screenY, pointer, button);
		}
		target = null;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(target == null){
			return false;
		}
		inpputListenerComponent = inputListenerCM.get(target);
		Array<InputListener> asd = inpputListenerComponent.getAllListeners();
		for (int j = 0, s = asd.size; j < s; j++){
			asd.get(j).touchDragged(target, screenX, screenY, pointer);
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		entities = engine.getEntitiesFor(root);
		for (int i = 0, n = entities.size(); i < n; i++){
			Entity entity = entities.get(i);
			inpputListenerComponent = inputListenerCM.get(entity);
			Array<InputListener> asd = inpputListenerComponent.getAllListeners();
			for (int j = 0, s = asd.size; j < s; j++){
				if (asd.get(j).mouseMoved(entity, screenX, screenY)){
					return true;
				}
			}
			
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		entities = engine.getEntitiesFor(root);
		for (int i = 0, n = entities.size(); i < n; i++){
			Entity entity = entities.get(i);
			inpputListenerComponent = inputListenerCM.get(entity);
			Array<InputListener> asd = inpputListenerComponent.getAllListeners();
			for (int j = 0, s = asd.size; j < s; j++){
				if (asd.get(j).scrolled(entity,amount)){
					return true;
				}
			}
			
		}
		return false;
	}

}
