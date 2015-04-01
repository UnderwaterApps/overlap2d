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

import java.util.ArrayList;
import java.util.HashMap;

public class CustomVarsParser {

	public static String getVar(String varStr, String key) {
		String[] vars = varStr.split("&"); 
    	for(int i = 0; i < vars.length; i++) {
    		String[] tmp = vars[i].split("=");
    		if(tmp.length == 2 && tmp[0].equalsIgnoreCase(key)) {
    			return tmp[1];
    		}
    	}
    	
    	return null;
	}
	
	public static float[] getColor(String varStr, String key) {
		String res = getVar(varStr, key);
		if(res == null) return null;
		String[] tmp = res.split(",");
		float[] color = {Float.parseFloat(tmp[0]), Float.parseFloat(tmp[1]), Float.parseFloat(tmp[2])};
		
    	return color;
	}	
	
	public static ArrayList<String> getArr(String varStr, String key) {
		int iterator = 0;
		ArrayList<String> result = new ArrayList<String>();
		String value = CustomVarsParser.getVar(varStr, key+"["+iterator+"]");
		while(value != null) {
			result.add(value);
			iterator++;
			value = CustomVarsParser.getVar(varStr, key+"["+iterator+"]");
		}
		
		return result;
	}	
	
	public static String addConfigVar(String varStr, String key, String value) {
		String val = CustomVarsParser.getVar(varStr, key);
		if(val == null) {
			if(varStr == null || varStr.length() == 0) {
				varStr=key+"="+value;
			} else {
				varStr+="&"+key+"="+value;
			}
		} else {
			String newStr = "";
			String[] vars = varStr.split("&"); 
	    	for(int i = 0; i < vars.length; i++) {
	    		String[] tmp = vars[i].split("=");
	    		if(tmp.length == 2) {
	    			if(tmp[0].equalsIgnoreCase(key)) {
	    				newStr+="&"+tmp[0]+"="+value;
	    			} else {
	    				newStr+="&"+tmp[0]+"="+tmp[1];
	    			}
	    		}
	    	}
	    	varStr = newStr;
		}
		
		return varStr;
	}	
	
	public static HashMap<String, String> getVariablesMap(String varStr) {
		HashMap<String, String> result = new HashMap<String, String>();
		String[] vars = varStr.split("&"); 
    	for(int i = 0; i < vars.length; i++) {
    		String[] tmp = vars[i].split("=");
    		if(tmp.length == 2) {
    			result.put(tmp[0], tmp[1]);
    		}
    	}
    	
    	return result;
	}
	
	public static String removeVar(String varStr, String key) {
		String val = CustomVarsParser.getVar(varStr, key);
		if(val != null) {
			String newStr = "";
			String[] vars = varStr.split("&"); 
	    	for(int i = 0; i < vars.length; i++) {
	    		String[] tmp = vars[i].split("=");
	    		if(tmp.length == 2) {
	    			if(tmp[0].equalsIgnoreCase(key)) {
	    				//do nothing
	    			} else {
	    				newStr+="&"+tmp[0]+"="+tmp[1];
	    			}
	    		}
	    	}
	    	varStr = newStr;
		}
		
		return varStr;		
	}
}
