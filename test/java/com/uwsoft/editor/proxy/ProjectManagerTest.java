package com.uwsoft.editor.proxy;

import com.runner.LibgdxRunner;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static java.io.File.separator;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(LibgdxRunner.class)
public class ProjectManagerTest {
    private Random random = new Random();
    private ProjectManager projectManager;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        projectManager = new ProjectManager();
        projectManager.onRegister();
    }

    @Test
    public void shouldGetProjectManagerInformation() throws Exception {
        String rootPath = projectManager.getRootPath();
        String workspacePath = projectManager.getWorkspacePath();
        String currentWorkingPath = projectManager.getCurrentWorkingPath();

        assertThat(rootPath, not(nullValue()));
        assertThat(workspacePath, not(nullValue()));
        assertThat(currentWorkingPath, not(nullValue()));
    }

    @Test
    public void shouldAbleToCreateNewProject() throws Exception {
        Overlap2DFacade.getInstance().registerProxy(new SceneDataManager());
        Overlap2DFacade.getInstance().registerProxy(projectManager);
        projectManager.createEmptyProject(String.format("%d%s", random.nextLong(), separator), 800, 600, 1);

        ProjectVO currentProjectVO = projectManager.getCurrentProjectVO();
        ProjectInfoVO currentProjectInfoVO = projectManager.getCurrentProjectInfoVO();

        assertThat(currentProjectVO.texturepackerWidth, is("4096"));
        assertThat(currentProjectVO.texturepackerHeight, is("4096"));
        assertThat(currentProjectVO.projectVersion, not(nullValue()));

        assertThat(currentProjectInfoVO.originalResolution.width, is(800));
        assertThat(currentProjectInfoVO.originalResolution.height, is(600));
        assertThat(currentProjectInfoVO.pixelToWorld, is(1));
        cleanDirectory(currentProjectVO);
    }

    @Test
    public void shouldExportProject() throws Exception {
        Overlap2DFacade.getInstance().registerProxy(new SceneDataManager());
        Overlap2DFacade.getInstance().registerProxy(projectManager);
        projectManager.createEmptyProject(String.format("%d%s", random.nextLong(), separator), 800, 600, 1);

        ProjectVO currentProjectVO = projectManager.getCurrentProjectVO();
        File exportFolder = new File(projectManager.getCurrentWorkingPath(), currentProjectVO.projectName + "export");
        assertThat(exportFolder.list().length, is(0));
        projectManager.exportProject();

        assertThat(exportFolder.list().length, is(4));
        cleanDirectory(currentProjectVO);
    }

    @Test
    public void shouldCreateDTFileAfterSaveProject() throws Exception {
        Overlap2DFacade.getInstance().registerProxy(new SceneDataManager());
        Overlap2DFacade.getInstance().registerProxy(projectManager);
        projectManager.createEmptyProject(String.format("%d%s", random.nextLong(), separator), 800, 600, 1);
        File exportFolder = new File(projectManager.getCurrentWorkingPath(), projectManager.currentProjectVO.projectName);
        File[] files = exportFolder.listFiles((dir, name) -> {
            return name.contains("project.dt");
        });
        FileUtils.forceDelete(files[0]);
        projectManager.saveCurrentProject();
        files = exportFolder.listFiles((dir, name) -> {
            return name.contains("project.dt");
        });
        assertThat(files.length, is(1));
    }

    private void cleanDirectory(ProjectVO currentProjectVO) {
        File projectFolder = new File(projectManager.getCurrentWorkingPath(), currentProjectVO.projectName);
        try {
            FileUtils.deleteDirectory(projectFolder);
        } catch (IOException e) {

        }
    }
}