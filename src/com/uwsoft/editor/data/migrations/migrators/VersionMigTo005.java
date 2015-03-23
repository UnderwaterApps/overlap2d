package com.uwsoft.editor.data.migrations.migrators;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.migrations.IVersionMigrator;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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
        if (animationsDir.exists() && animationsDir.isDirectory()) {
            File spineAnimationsDir = new File(projectPath + File.separator + "assets" + File.separator + "orig" + File.separator + "spine-animations");
            animationsDir.renameTo(spineAnimationsDir);
        }

        // get list of resolutions
        String prjInfoFilePath = projectPath + "/project.dt";
        FileHandle projectInfoFile = Gdx.files.internal(prjInfoFilePath);
        String projectInfoContents = null;
        try {
            projectInfoContents = FileUtils.readFileToString(projectInfoFile.file());
            ProjectInfoVO currentProjectInfoVO = json.fromJson(ProjectInfoVO.class, projectInfoContents);
            DataManager.getInstance().currentProjectInfoVO = currentProjectInfoVO;

            // run through all resolutions and remake animations for all
            for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions) {
                DataManager.getInstance().createResizedAnimations(resolutionEntryVO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // change sLights to sLights
        File scenesDir = new File(projectPath + File.separator + "scenes");
        for (File entry : scenesDir.listFiles()) {
            if (!entry.isDirectory()) {
                try {
                    String content = FileUtils.readFileToString(new FileHandle(entry).file());
                    content = content.replaceAll("\"slights\":", "\"sLights\":");
                    FileUtils.writeStringToFile(new File(entry.getAbsolutePath()), content, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return true;
    }
}
