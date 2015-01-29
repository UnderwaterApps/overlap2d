package com.uwsoft.editor.data.migrations;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.migrations.migrators.DummyMig;
import com.uwsoft.editor.data.migrations.migrators.VersionMigTo005;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.utils.AppConfig;

/**
 * Created by azakhary on 9/28/2014.
 */
public class ProjectVersionMigrator {

    private String projectPath;
    private ProjectVO projectVo;

    private int safetyIterator = 0;

    private Json json = new Json();

    public ProjectVersionMigrator(String projectPath, ProjectVO projectVo) {
        this.projectPath = projectPath;
        this.projectVo = projectVo;

        json.setOutputType(JsonWriter.OutputType.json);
    }

    public void start() {

        if (projectVo.projectVersion == null || projectVo.projectVersion.equals("")) {
            projectVo.projectVersion = "0.0.4";
        }

        migrationIterator();
    }

    private void migrationIterator() {
        if (projectVo.projectVersion.equals(AppConfig.getInstance().version)) return;

        if (safetyIterator > 100) {
            System.out.println("Emergency exit from version migration process due to safety lock");
            return;
        }
        safetyIterator++;

        if (projectVo.projectVersion.equals("0.0.4")) {
            VersionMigTo005 vmt = new VersionMigTo005();
            doMigartion(vmt, "0.0.5");
        }
        if (projectVo.projectVersion.equals("0.0.5")) {
            DummyMig vmt = new DummyMig();
            doMigartion(vmt, "0.0.6");
        }
        if (projectVo.projectVersion.equals("0.0.6")) {
            DummyMig vmt = new DummyMig();
            doMigartion(vmt, "0.0.7");
        }
    }

    private void doMigartion(IVersionMigrator vmt, String nextVersion) {
        vmt.setProject(projectPath);

        boolean successs = vmt.doMigration();

        if (successs) {
            setVersion(nextVersion);
            migrationIterator();
        } else {
            return;
        }
    }

    private void setVersion(String version) {
        projectVo.projectVersion = version;
        String projectVoJson = json.toJson(projectVo, ProjectVO.class);

        DataManager.writeToFile(projectPath + "/project.pit", projectVoJson);
    }
}
