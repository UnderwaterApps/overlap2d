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

package com.uwsoft.editor.gdx.tween;

import java.util.ArrayList;

public class TimeLineVO {
	public boolean yoyo = false;
	public boolean repeate = false;
	public String tweenId = ""; //object name that must be tweened;
	public String tweenObjectName = ""; //object name that must be tweened;
	public ArrayList<TweenVO> tweens = new ArrayList<TweenVO>(1);
	//public float durations[]; // for each sector
//	public ArrayList<TriggerAction> triggers;//triggers for each sector start o end //TODO this must be done later
	public int accelerationTypes[]; // acceleration types //TODO later may be
}
