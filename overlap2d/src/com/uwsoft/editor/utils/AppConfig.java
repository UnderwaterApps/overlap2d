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

import java.util.Properties;

/**
 * Created by azakhary on 9/28/2014.
 */
public class AppConfig  {

    public static AppConfig instance;

    public String version;

    public Properties properties;

    private AppConfig() {}

    public static AppConfig getInstance() {
        if(instance == null) {
            instance = new AppConfig();
            instance.loadProperties();
        }

        return instance;
    }

    private void loadProperties() {
			// this thing just refused to work so I gave up, fuck it.

        /*
        properties = new Properties();
       // Gdx.files
        File file = new File("app.properties");
        if(!file.exists()) {
        	System.out.println("NO FILE");
            file = new File("assets/app.properties");
            System.out.println("NO FILE " + file.getAbsolutePath());
        }

        //Gdx.files.internal(path)
        try {
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            version = properties.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        version = "0.1.3";
    }
}
