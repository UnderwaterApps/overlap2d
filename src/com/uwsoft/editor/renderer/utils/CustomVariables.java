package com.uwsoft.editor.renderer.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by azakhary on 8/28/2014.
 */
public class CustomVariables {

    private HashMap<String, String> variables = new HashMap<String, String>();

    public CustomVariables() {

    }

    public void loadFromString(String varString) {
        variables.clear();
        String[] vars = varString.split(";");
        for(int i = 0; i < vars.length; i++) {
            String[] tmp = vars[i].split(":");
            if(tmp.length > 1) {
                setVariable(tmp[0], tmp[1]);
            }
        }
    }

    public String saveAsString() {
        String result = "";
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            result += key + ":" + value + ";";
        }
        if(result.length() > 0) {
            result = result.substring(0, result.length()-1);
        }

        return result;
    }

    public void setVariable(String key, String value) {
        variables.put(key, value);
    }

    public void removeVariable(String key) {
        variables.remove(key);
    }

    public String getStringVariable(String key) {
        return variables.get(key);
    }

    public Integer getIntegerVariable(String key) {
        Integer result = null;
        try {
            result = Integer.parseInt(variables.get(key));
        } catch(Exception e) {}

        return result;
    }

    public Float getFloatVariable(String key) {
        Float result = null;
        try {
            result = Float.parseFloat(variables.get(key));
        } catch(Exception e) {}

        return result;
    }

    public HashMap<String, String> getHashMap() {
        return variables;
    }

    public int getCount() {
        return variables.size();
    }

}
