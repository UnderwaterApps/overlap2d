package com.uwsoft.editor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by azakhary on 9/28/2014.
 */
public class AppConfig {

    public static AppConfig instance;

    public String version;

    public Properties properties;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
            instance.loadProperties();
        }

        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        // Gdx.files
        File file = new File("app.properties");
        if (!file.exists()) {
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
    }
}
