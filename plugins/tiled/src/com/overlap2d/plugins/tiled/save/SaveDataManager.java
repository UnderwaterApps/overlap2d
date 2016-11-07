package com.overlap2d.plugins.tiled.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Created by mariam on 3/24/16.
 */
public class SaveDataManager {

    public DataToSave dataToSave;

    private Json json;
    private FileHandle fileHandle;


    public SaveDataManager(String projectPath) {
        json = new Json();
        fileHandle = Gdx.files.absolute(projectPath + "/tiled_plugin.dt");
        load();
    }

    private void load() {
        if (!fileHandle.exists()) {
            dataToSave = new DataToSave();
            return;
        }

        String jsonString = fileHandle.readString();
        dataToSave = json.fromJson(DataToSave.class, jsonString);
    }

    public void save() {
        String dataString = json.toJson(dataToSave);
        fileHandle.writeString(dataString, false);
    }
}
