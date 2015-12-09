package com.uwsoft.editor.proxy;

import com.runner.LibgdxRunner;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.vo.ProjectVO;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Random;

import static java.io.File.separator;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(LibgdxRunner.class)
public class ProjectManagerTest {
    private Random random = new Random();
    private ProjectManager projectManager;
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        projectManager = new ProjectManager();
        Overlap2DFacade overlap2DFacade = Overlap2DFacade.getInstance();
        overlap2DFacade.registerProxy(projectManager);
        overlap2DFacade.registerProxy(new SceneDataManager());
    }

    @Test
    public void shouldGetProjectManagerInformation() throws Exception {
        String rootPath = projectManager.getRootPath();

        assertThat(rootPath, not(nullValue()));
    }

    @Test
    public void shouldAbleToCreateNewProject() throws Exception {
        projectManager.createEmptyProject(String.format("%d%s", random.nextLong(), separator), 800, 600, 1);

        ProjectVO currentProjectVO = projectManager.getCurrentProjectVO();
        ProjectInfoVO currentProjectInfoVO = projectManager.getCurrentProjectInfoVO();

        assertThat(currentProjectVO.texturepackerWidth, is("4096"));
        assertThat(currentProjectVO.texturepackerHeight, is("4096"));
        assertThat(currentProjectVO.projectVersion, not(nullValue()));

        assertThat(currentProjectInfoVO.originalResolution.width, is(800));
        assertThat(currentProjectInfoVO.originalResolution.height, is(600));
        assertThat(currentProjectInfoVO.pixelToWorld, is(1));
    }

    /*
    @Test
    public void shouldExportProject() throws Exception {
        String projectPath = temporaryFolder.newFolder().getAbsolutePath() + separator + "test";
        String exportPath = temporaryFolder.newFolder().getAbsolutePath() + separator + "export";
        projectManager.createEmptyProject(projectPath, 800, 600, 1);
        File exportFolder = new File(exportPath);
        assertThat(exportFolder.list().length, is(0));
        projectManager.setExportPaths(new File(exportPath));
        projectManager.exportProject();

        assertThat(exportFolder.list().length, is(4));
    }

    @Test
    public void shouldCreateDTFileAfterSaveProject() throws Exception {
        String projectPath = temporaryFolder.newFolder().getAbsolutePath() + separator + "test2";
        String exportPath = temporaryFolder.newFolder().getAbsolutePath() + separator + "export2";

        projectManager.createEmptyProject(projectPath, 800, 600, 1);
        File exportFolder = new File(exportPath);
        File[] files = exportFolder.listFiles((dir, name) -> {
            return name.contains("project.dt");
        });
        FileUtils.forceDelete(files[0]);
        projectManager.saveCurrentProject();
        files = exportFolder.listFiles((dir, name) -> {
            return name.contains("project.dt");
        });
        assertThat(files.length, is(1));
    }*/
}