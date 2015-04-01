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

package com.uwsoft.editor.utils;



public class UpdatebleTimer implements Updatable {
	public float		mSeconds;
	private float		mSecondsPassed		= 0;
	private TimerCompleteHandler	mCompleteHandler;
	private boolean		mIsLooping			= false;
	private boolean		mIsPlay				= true;
	
	public boolean		canSlowSpeed		= false;
	
	public UpdateHandler updateHandler = null;

	public UpdatebleTimer(final float pSeconds, final TimerCompleteHandler pCompleteHandler) {
		this(pSeconds, false, true, pCompleteHandler);
	}

	public UpdatebleTimer(final float pSeconds, final boolean pIsLooping, final TimerCompleteHandler pCompleteHandler) {
		this(pSeconds, pIsLooping, true, pCompleteHandler);
	}

	public UpdatebleTimer(final float pSeconds, final boolean pIsLooping, final boolean pIsPlay, final TimerCompleteHandler pCompleteHandler) {
		
		mSeconds = pSeconds;
		mCompleteHandler = pCompleteHandler;
		mIsLooping = pIsLooping;
		mIsPlay = pIsPlay;
	}

	public void pause() {
		mIsPlay = false;
	}

	public void start() {
		mIsPlay = true;
	}

	public boolean isRuning() {
		return mIsPlay;
	}

	public void dispose() {
		mIsPlay = false;
	}

	public void setCompleteListener(TimerCompleteHandler completeListener) {
		mCompleteHandler = completeListener;
	}

	public void update(float pSecondsElapsed) {
//		if(stage != null && canSlowSpeed){
//			pSecondsElapsed*=stage.gameSpeed;
//		}
		if (mIsPlay) {
			if (mIsLooping) {
				mSecondsPassed += pSecondsElapsed;
				if (mSecondsPassed > mSeconds) {
					mCompleteHandler.complete(this);
					mSecondsPassed = 0;
				}
			} else  {
				if(updateHandler != null)
					updateHandler.update(pSecondsElapsed);
				mSecondsPassed += pSecondsElapsed;
				if (mSecondsPassed > mSeconds) {
					if(mCompleteHandler != null){
						mCompleteHandler.complete(this);
					}
					mIsPlay = false;
				}
			}
		}
	}
	
	public void setCompleted(){
		mSecondsPassed = mSeconds + 1;
	}
	
	

	public void reset() {
		mSecondsPassed = 0;
	}
	
	public interface UpdateHandler {
		public void update(float delta);
	}
	public interface TimerCompleteHandler {
		public void complete(UpdatebleTimer timer);
	}
}
