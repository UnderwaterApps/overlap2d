package com.uwsoft.editor.data.migrations.migrators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.migrations.IVersionMigrator;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;

import java.io.File;

/**
 * Created by azakhary on 9/28/2014.
 */
public class VersionMigTo005 implements IVersionMigrator {

    private String projectPath;

    private Json json = new Json();
    private JsonReader jsonReader = new JsonReader();

    @Override
    public void setProject(String path) {
        projectPath = path;
        json.setOutputType(JsonWriter.OutputType.json);
    }

    @Override
    public boolean doMigration() {

        // Rename folder animations to spine-animations in orig (if exist);
        File animationsDir = new File(projectPath + File.separator + "assets" + File.separator + "orig" + File.separator + "animations");
        if(animationsDir.exists() && animationsDir.isDirectory()) {
            File spineAnimationsDir = new File(projectPath + File.separator + "assets" + File.separator + "orig" + File.separator + "spine-animations");
            animationsDir.renameTo(spineAnimationsDir);
        }

        // get list of resolutions
        String prjInfoFilePath = projectPath + "/project.dt";
        FileHandle projectInfoFile = Gdx.files.internal(prjInfoFilePath);
        String projectInfoContents = DataManager.readFileContents(projectInfoFile);
        ProjectInfoVO currentProjectInfoVO = json.fromJson(ProjectInfoVO.class, projectInfoContents);
        DataManager.getInstance().currentProjectInfoVO = currentProjectInfoVO;

        // run through all resolutions and remake animations for all
        for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions ) {
            DataManager.getInstance().createResizedAnimations(resolutionEntryVO);
        }

        // change sLights to sLights
        File scenesDir = new File(projectPath + File.separator + "scenes");
        for (File entry : scenesDir.listFiles()) {
            if (!entry.isDirectory()) {
                String content = DataManager.readFileContents(new FileHandle(entry));
                content = content.replaceAll("\"slights\":", "\"sLights\":");
                DataManager.writeToFile(entry.getAbsolutePath(), content);
            }
        }

        return true;
    }
}
