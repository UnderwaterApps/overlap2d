/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.gdx.actors;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.uwsoft.editor.gdx.stage.BaseStage;
import com.uwsoft.editor.utils.UpdatebleTimer;
import com.uwsoft.editor.utils.UpdatebleTimer.TimerCompleteHandler;

import java.util.ArrayList;

public class ParticleActor extends Actor {

	private ArrayList<PooledEffect> effects;
	private float lastDelta = 0.0f;

	public float speed = 1.0f;
	private boolean hide = false;
	private float emmission = 0;

	public boolean continuous = true;

	public BaseStage stage;
	private String[] nms;

	private TimerCompleteHandler onRemoveTimer = new TimerCompleteHandler() {

		@Override
		public void complete(UpdatebleTimer timer) {
//			//System.out.println("TIMER COMPLETE");
			stop();
			rm();
		}
	};

	private UpdatebleTimer removeTimer = null;

	public ParticleActor(BaseStage s, String... particleNames) {
		super();
		stage = s;
		setVisible(true);
		nms = particleNames;
		initParticles(particleNames);
	}

	public void setPosition(float xx, float yy) {
		// markToRemove(false);
		setX(xx);
		setY(yy);
		if (effects == null) {
			return;
		}
		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).setPosition(getX(), getY());
		}
	}

	public void setRemoveTimer(float time) {
		if (removeTimer != null) {
			removeTimer.reset();
			removeTimer.pause();
		}
		removeTimer = new UpdatebleTimer(time, false, false, onRemoveTimer);
	}

	public void setEmmission(float emmission) {
		this.emmission = emmission;
	}

	public void start() {
		if (effects == null) {
			hide = true;
			return;
		}
		hide = false;
		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).start();
			for (int j = 0; j < effects.get(i).getEmitters().size; j++) {
				effects.get(i).getEmitters().get(j).setContinuous(continuous);
			}
		}
		if (!continuous && removeTimer != null) {
			removeTimer.reset();
			removeTimer.start();
		}
	}

	public void stop() {
		if (effects == null) {
			return;
		}
		for (int i = 0; i < effects.size(); i++) {
			for (int j = 0; j < effects.get(i).getEmitters().size; j++) {
				effects.get(i).getEmitters().get(j).setContinuous(false);
				effects.get(i).getEmitters().get(j).allowCompletion();
			}
		}
	}

	public void act(float delta) {
		speed = 1.0f;
		if (removeTimer != null) {
			removeTimer.update(delta * speed);
		}
		lastDelta = delta * speed;

		if (effects != null) {
			for (int i = 0; i < effects.size(); i++) {
				effects.get(i).setPosition(getX(), getY());
			}
		}

	}

	public void fastForward(float time) {
		act(time);
	}

	

	public void setRotation(float rotation) {
		this.setRotation(rotation);
		if (effects == null) {
			return;
		}
//		for (int i = 0; i < effects.size(); i++) {
//			ScaledNumericValue oldAngle = effects.get(i).getEmitters().get(0).getAngle();
//
//			ScaledNumericValue angle = oldAngle;
//
//			angle.setHighMin(rotation);
//			angle.setHighMax(rotation);
//
//			effects.get(i).getEmitters().get(0).setAngle(angle);
//		}
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		if (hide) {
			return;
		}
		for (int i = 0; i < effects.size(); i++) {
			//effects.get(i).update(lastDelta);
			//effects.get(i).draw(batch, parentAlpha, true);
			effects.get(i).draw(batch,lastDelta);
		}
	}

	private void initParticles(String[] eNames) {
		effects = new ArrayList<PooledEffect>();
		for (int i = 0; i < eNames.length; i++) {
			PooledEffect sEffect = null;
			//sEffect = stage.getDataManager().textureManager.getParticle(eNames[i]);
			//effects.add(sEffect);
		}

		stop();
	}

	public void rm() {

		if (effects != null) {
			for (int i = 0; i < effects.size(); i++) {
				//stage.dataManager.textureManager.freeParticle("aa",effects.get(i));
			}
		}
		effects = null;
		removeTimer = null;
		remove();
	}
}
