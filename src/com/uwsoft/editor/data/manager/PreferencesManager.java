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

package com.uwsoft.editor.data.manager;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesManager {
	private static final String TAG = PreferencesManager.class.getCanonicalName();
	private static PreferencesManager instance = null;
	private final Preferences prefs;
	
	public PreferencesManager() {
		prefs = Gdx.app.getPreferences("Overlap2DPrefs");
		initPrefs();
	}
	
	private void initPrefs() {
		if (!contains("recentHistory")) {
			putInteger("recentHistory",10);
			flush();
		}
	}
	
	public static PreferencesManager getInstance() {
		if (instance == null)
			instance = new PreferencesManager();
		
		return instance;
	}
	
	// Map to Prefs
	public boolean contains(String key) { return prefs.contains(key); }
	public void flush() { prefs.flush(); }
	public Map<String,?> get() { return prefs.get(); }
	public boolean getBoolean(String key) { return prefs.getBoolean(key); }
	public boolean getBoolean(String key, boolean defVal) { return prefs.getBoolean(key,defVal); }
	public float getFloat(String key) { return prefs.getFloat(key); }
	public float getFloat(String key, float defVal) { return prefs.getFloat(key,defVal); }
	public int getInteger(String key) { return prefs.getInteger(key); }
	public int getInteger(String key, int defVal) { return prefs.getInteger(key,defVal); }
	public long getLong(String key) { return prefs.getLong(key); }
	public long getLong(String key, long defVal) { return prefs.getLong(key,defVal); }
	public String getString(String key) { return prefs.getString(key); }
	public String getString(String key, String defVal) { return prefs.getString(key,defVal); }
	public Preferences put(Map<String,?> vals) { return prefs.put(vals); }
	public Preferences putBoolean(String key, boolean val) { return prefs.putBoolean(key, val); }
	public Preferences putFloat(String key, float val) { return prefs.putFloat(key, val); }
	public Preferences putInteger(String key, int val) { return prefs.putInteger(key, val); }
	public Preferences putLong(String key, int val) { return prefs.putLong(key, val); }
	public Preferences putString(String key, String val) { return prefs.putString(key, val); }
	public void remove(String key) { prefs.remove(key); }
	
	// Custom Functions
	
	// Recent Project Manager
	// Count: recentHistory
	// Array: recent.0, recent.1, recent.2, etc, etc.
	private ArrayList<String> recentHistory;
	
	public void buildRecentHistory() {
		recentHistory = new ArrayList<String>();
		for (int i = 0; i < getInteger("recentHistory"); i++) {
			if (!contains(String.format("recent.%d",i)))
				break;
			recentHistory.add(getString(String.format("recent.%d",i)));
		}
	}
	
	public ArrayList<String> getRecentHistory() {
		return recentHistory;
	}
	
	public void storeRecentHistory() {
		for (int i = 0; i < recentHistory.size(); i++)
		{
			if (i > getInteger("recentHistory"))
				break;
			prefs.remove(String.format("recent.%d",i));
			prefs.putString(String.format("recent.%d",i), recentHistory.get(i));
		}
		flush();
	}
	
	public void pushHistory(String file) {
		if (recentHistory.contains(file))
			popHistory(file);
		recentHistory.add(0,file);
		storeRecentHistory();
	}
	
	public void popHistory(String file) {
		if (recentHistory.contains(file)) {
			recentHistory.remove(file);
			storeRecentHistory();
		}
	}
	
	public void popHistory() {
		recentHistory.remove(-1);
		storeRecentHistory();
	}
}
