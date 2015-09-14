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

package com.uwsoft.editor.proxy;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.uwsoft.editor.data.manager.PreferencesManager;
import com.uwsoft.editor.data.vo.SceneConfigVO;
import com.uwsoft.editor.view.menu.Overlap2DMenuBar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.puremvc.patterns.proxy.BaseProxy;
import com.uwsoft.editor.data.migrations.ProjectVersionMigrator;
import com.uwsoft.editor.data.vo.EditorConfigVO;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.view.stage.Sandbox;
import com.uwsoft.editor.view.ui.widget.ProgressHandler;
import com.uwsoft.editor.Overlap2DFacade;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.utils.MySkin;
import com.uwsoft.editor.utils.AppConfig;
import com.uwsoft.editor.utils.Overlap2DUtils;


public class ProjectManager extends BaseProxy {
    private static final String TAG = ProjectManager.class.getCanonicalName();
    public static final String NAME = TAG;
    private static final String EVENT_PREFIX = "com.uwsoft.editor.proxy.ProjectManager";

    public static final String PROJECT_OPENED = EVENT_PREFIX + ".PROJECT_OPENED";
    public static final String PROJECT_DATA_UPDATED = EVENT_PREFIX + ".PROJECT_DATA_UPDATED";

    public ProjectVO currentProjectVO;
    public ProjectInfoVO currentProjectInfoVO;
    private String currentWorkingPath;
    private String workspacePath;
    private String DEFAULT_FOLDER = "Overlap2D";
    private float currentPercent = 0.0f;
    private ProgressHandler handler;
    private EditorConfigVO editorConfigVO;


    public ProjectManager() {
        super(NAME);
    }


    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
        initWorkspace();
    }

    @Override
    public void onRemove() {
        super.onRemove();
    }

    public ProjectVO getCurrentProjectVO() {
        return currentProjectVO;
    }

    public ProjectInfoVO getCurrentProjectInfoVO() {
        return currentProjectInfoVO;
    }

    private void initWorkspace() {
        try {
            editorConfigVO = getEditorConfig();
            String myDocPath = Overlap2DUtils.MY_DOCUMENTS_PATH;
            workspacePath = myDocPath + "/" + DEFAULT_FOLDER;
            FileUtils.forceMkdir(new File(workspacePath));
            currentWorkingPath = workspacePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changePercentBy(float value) {
        currentPercent += value;
        handler.progressChanged(currentPercent);
    }

    public void createEmptyProject(String projectName, int width, int height, int pixelPerWorldUnit) throws IOException {

        if (workspacePath.endsWith(File.separator)) {
            workspacePath = workspacePath.substring(0, workspacePath.length() - 1);
        }

        String projPath = workspacePath + File.separator + projectName;
        currentWorkingPath = workspacePath;

        FileUtils.forceMkdir(new File(projPath));
        FileUtils.forceMkdir(new File(projPath + File.separator + "export"));
        FileUtils.forceMkdir(new File(projPath + File.separator + "assets"));
        FileUtils.forceMkdir(new File(projPath + File.separator + "scenes"));
        FileUtils.forceMkdir(new File(projPath + File.separator + "assets/orig"));
        FileUtils.forceMkdir(new File(projPath + File.separator + "assets/orig/images"));
        FileUtils.forceMkdir(new File(projPath + File.separator + "assets/orig/particles"));
        FileUtils.forceMkdir(new File(projPath + File.separator + "assets/orig/animations"));
        FileUtils.forceMkdir(new File(projPath + File.separator + "assets/orig/pack"));


        // create project file
        ProjectVO projVo = new ProjectVO();
        projVo.projectName = projectName;
        projVo.projectVersion = ProjectVersionMigrator.dataFormatVersion;

        // create project info file
        ProjectInfoVO projInfoVo = new ProjectInfoVO();
        projInfoVo.originalResolution.name = "orig";
        projInfoVo.originalResolution.width = width;
        projInfoVo.originalResolution.height = height;
        projInfoVo.pixelToWorld = pixelPerWorldUnit;

        //TODO: add project orig resolution setting
        currentProjectVO = projVo;
        currentProjectInfoVO = projInfoVo;
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        sceneDataManager.createNewScene("MainScene");
        FileUtils.writeStringToFile(new File(projPath + "/project.pit"), projVo.constructJsonString(), "utf-8");
        FileUtils.writeStringToFile(new File(projPath + "/project.dt"), projInfoVo.constructJsonString(), "utf-8");

    }

    public void setLastOpenedPath(String path) {
        editorConfigVO.lastOpenedSystemPath = path;
        saveEditorConfig();
    }

    private void saveEditorConfig() {
        try {
            File root = new File(new File(".").getAbsolutePath()).getParentFile();
            String configFilePath = root.getAbsolutePath() + "/" + EditorConfigVO.EDITOR_CONFIG_FILE;
            FileUtils.writeStringToFile(new File(configFilePath), editorConfigVO.constructJsonString(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openProjectAndLoadAllData(String projectName) {
        openProjectAndLoadAllData(projectName, null);
    }

    public void openProjectAndLoadAllData(String projectName, String resolution) {
        String projectPath = currentWorkingPath + "/" + projectName;
        String prjFilePath = projectPath + "/project.pit";

        PreferencesManager prefs = PreferencesManager.getInstance();
        prefs.buildRecentHistory();
        prefs.pushHistory(prjFilePath);
        facade.sendNotification(Overlap2DMenuBar.RECENT_LIST_MODIFIED);

        File prjFile = new File(prjFilePath);
        if (prjFile.exists() && !prjFile.isDirectory()) {
            FileHandle projectFile = Gdx.files.internal(prjFilePath);
            String projectContents = null;
            try {
                projectContents = FileUtils.readFileToString(projectFile.file());
                Json json = new Json();
                json.setIgnoreUnknownFields(true);
                ProjectVO vo = json.fromJson(ProjectVO.class, projectContents);
                goThroughVersionMigrationProtocol(projectPath, vo);
                currentProjectVO = vo;
                String prjInfoFilePath = projectPath + "/project.dt";
                FileHandle projectInfoFile = Gdx.files.internal(prjInfoFilePath);
                String projectInfoContents = FileUtils.readFileToString(projectInfoFile.file());
                ProjectInfoVO voInfo = json.fromJson(ProjectInfoVO.class, projectInfoContents);
                currentProjectInfoVO = voInfo;

            } catch (IOException e) {
                e.printStackTrace();
            }
            ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
            if (resolution == null) {
                resolutionManager.currentResolutionName = currentProjectVO.lastOpenResolution.isEmpty() ? "orig" : currentProjectVO.lastOpenResolution;
            } else {
                resolutionManager.currentResolutionName = resolution;
                currentProjectVO.lastOpenResolution = resolutionManager.currentResolutionName;
                saveCurrentProject();

            }
            checkForConsistancy();
            loadProjectData(projectName);
        }
    }

    private void goThroughVersionMigrationProtocol(String projectPath, ProjectVO projectVo) {
        ProjectVersionMigrator pvm = new ProjectVersionMigrator(projectPath, projectVo);
        pvm.start();
    }

    private void checkForConsistancy() {
        // check if current project requires cleanup
        // Cleanup unused meshes
        // 1. open all scenes make list of mesh_id's and then remove all unused meshes
        HashSet<String> uniqueMeshIds = new HashSet<String>();
        FileHandle sourceDir = new FileHandle(currentWorkingPath + "/" + currentProjectVO.projectName + "/scenes/");
        for (FileHandle entry : sourceDir.list(Overlap2DUtils.DT_FILTER)) {
            if (!entry.file().isDirectory()) {
                Json json = new Json();
                json.setIgnoreUnknownFields(true);
                SceneVO sceneVO = json.fromJson(SceneVO.class, entry);
                if (sceneVO.composite == null) continue;
                ArrayList<MainItemVO> items = sceneVO.composite.getAllItems();

                for (CompositeItemVO libraryItem : currentProjectInfoVO.libraryItems.values()) {
                    if (libraryItem.composite == null) continue;
                    items = libraryItem.composite.getAllItems();
                }
            }
        }
    }


    private void loadProjectData(String projectName) {
        // All legit loading assets
        ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
        ResourceManager resourceManager = facade.retrieveProxy(ResourceManager.NAME);
        resourceManager.loadCurrentProjectData(currentWorkingPath, projectName, resolutionManager.currentResolutionName);
    }


    public String getCurrentWorkingPath() {
        return currentWorkingPath;
    }

    public String getWorkspacePath() {
        return editorConfigVO.lastOpenedSystemPath.isEmpty() ? workspacePath : editorConfigVO.lastOpenedSystemPath;
    }

    public void setWorkspacePath(String path) {
        workspacePath = path;
    }

    public void saveCurrentProject() {
        try {
            FileUtils.writeStringToFile(new File(currentWorkingPath + "/" + currentProjectVO.projectName + "/project.pit"), currentProjectVO.constructJsonString(), "utf-8");
            FileUtils.writeStringToFile(new File(currentWorkingPath + "/" + currentProjectVO.projectName + "/project.dt"), currentProjectInfoVO.constructJsonString(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCurrentProject(SceneVO vo) {
        saveCurrentProject();
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        sceneDataManager.saveScene(vo);
    }


    private ArrayList<File> getScmlFileImagesList(FileHandle fileHandle) {
        ArrayList<File> images = new ArrayList<File>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            org.w3c.dom.Document document = db.parse(fileHandle.file());
            NodeList nodeList = document.getElementsByTagName("file");
            for (int x = 0, size = nodeList.getLength(); x < size; x++) {
                String absolutePath = fileHandle.path();
                String path = absolutePath.substring(0, FilenameUtils.indexOfLastSeparator(fileHandle.path())) + File.separator + nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue();
                File imgFile = new File(path);
                images.add(imgFile);
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return images;
    }


    public void importSpineAnimationsIntoProject(final Array<FileHandle> fileHandles, ProgressHandler progressHandler) {
        if (fileHandles == null) {
            return;
        }
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            for (FileHandle handle : fileHandles) {
                File copiedFile = importExternalAnimationIntoProject(handle);
                if (copiedFile.getName().toLowerCase().endsWith(".atlas")) {
                    ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                    resolutionManager.resizeSpineAnimationForAllResolutions(copiedFile, currentProjectInfoVO);
                } else if (copiedFile.getName().toLowerCase().endsWith(".scml")) {
                    //resizeSpriterAnimationForAllResolutions(copiedFile, currentProjectInfoVO);
                }
            }

        });
        executor.execute(() -> {
            changePercentBy(100 - currentPercent);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.progressComplete();
        });
        executor.shutdown();

    }

    public File importExternalAnimationIntoProject(FileHandle animationFileSource) {
        try {
            String fileName = animationFileSource.name();
            if (!Overlap2DUtils.JSON_FILTER.accept(null, fileName) &&
                    !Overlap2DUtils.SCML_FILTER.accept(null, fileName)) {
                //showError("Spine animation should be a .json file with atlas in same folder \n Spriter animation should be a .scml file with images in same folder");
                return null;
            }

            String fileNameWithOutExt = FilenameUtils.removeExtension(fileName);
            String sourcePath;
            String animationDataPath;
            String targetPath;
            if (Overlap2DUtils.JSON_FILTER.accept(null, fileName)) {
                sourcePath = animationFileSource.path();

                animationDataPath = FilenameUtils.getFullPathNoEndSeparator(sourcePath);
                targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/spine-animations" + File.separator + fileNameWithOutExt;
                FileHandle atlasFileSource = new FileHandle(animationDataPath + File.separator + fileNameWithOutExt + ".atlas");
                if (!atlasFileSource.exists()) {
                    //showError("the atlas file needs to have same name and location as the json file");
                    return null;
                }

                FileUtils.forceMkdir(new File(targetPath));
                File jsonFileTarget = new File(targetPath + File.separator + fileNameWithOutExt + ".json");
                File atlasFileTarget = new File(targetPath + File.separator + fileNameWithOutExt + ".atlas");
                Array<File> imageFiles = getAtlasPages(atlasFileSource);

                FileUtils.copyFile(animationFileSource.file(), jsonFileTarget);
                FileUtils.copyFile(atlasFileSource.file(), atlasFileTarget);

                for (File imageFile : imageFiles) {
                    FileHandle imgFileTarget = new FileHandle(targetPath + File.separator + imageFile.getName());
                    FileUtils.copyFile(imageFile, imgFileTarget.file());
                }

                return atlasFileTarget;


            } else if (Overlap2DUtils.SCML_FILTER.accept(null, fileName)) {
                targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/spriter-animations" + File.separator + fileNameWithOutExt;
                File scmlFileTarget = new File(targetPath + File.separator + fileNameWithOutExt + ".scml");
                ArrayList<File> imageFiles = getScmlFileImagesList(animationFileSource);

                FileUtils.copyFile(animationFileSource.file(), scmlFileTarget);
                for (File imageFile : imageFiles) {
                    File imgFileTarget = new File(targetPath + File.separator + imageFile.getName());
                    FileUtils.copyFile(imageFile, imgFileTarget);
                }
                return scmlFileTarget;


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void importSpriteAnimationsIntoProject(final Array<FileHandle> fileHandles, ProgressHandler progressHandler) {
        if (fileHandles == null) {
            return;
        }
        handler = progressHandler;

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {

            String newAnimName = null;

            String rawFileName = fileHandles.get(0).name();
            String fileExtension = FilenameUtils.getExtension(rawFileName);
            if (fileExtension.equals("png")) {
                Settings settings = new Settings();
                settings.square = true;
                settings.flattenPaths = true;

                TexturePacker texturePacker = new TexturePacker(settings);
                FileHandle pngsDir = new FileHandle(fileHandles.get(0).parent().path());
                for (FileHandle entry : pngsDir.list(Overlap2DUtils.PNG_FILTER)) {
                    texturePacker.addImage(entry.file());
                }
                String fileNameWithoutExt = FilenameUtils.removeExtension(rawFileName);
                String fileNameWithoutFrame = fileNameWithoutExt.replaceAll("\\d*$", "");
                String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/sprite-animations" + File.separator + fileNameWithoutFrame;
                File targetDir = new File(targetPath);
                if (targetDir.exists()) {
                    try {
                        FileUtils.deleteDirectory(targetDir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                texturePacker.pack(targetDir, fileNameWithoutFrame);
                newAnimName = fileNameWithoutFrame;
            } else {
                for (FileHandle fileHandle : fileHandles) {
                    try {
                        Array<File> imgs = getAtlasPages(fileHandle);
                        String fileNameWithoutExt = FilenameUtils.removeExtension(fileHandle.name());
                        String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/sprite-animations" + File.separator + fileNameWithoutExt;
                        File targetDir = new File(targetPath);
                        if (targetDir.exists()) {
                            FileUtils.deleteDirectory(targetDir);
                        }
                        for (File img : imgs) {
                            FileUtils.copyFileToDirectory(img, targetDir);
                        }
                        FileUtils.copyFileToDirectory(fileHandle.file(), targetDir);
                        newAnimName = fileNameWithoutExt;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (newAnimName != null) {
                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                resolutionManager.resizeSpriteAnimationForAllResolutions(newAnimName, currentProjectInfoVO);
            }
        });
        executor.execute(() -> {
            changePercentBy(100 - currentPercent);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.progressComplete();
        });
        executor.shutdown();
    }

    private Array<File> getAtlasPages(FileHandle fileHandle) {
        Array<File> imgs = new Array<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileHandle.read()), 64);
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.trim().length() == 0) {
                    line = reader.readLine();
                    imgs.add(new File(FilenameUtils.getFullPath(fileHandle.path()) + line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgs;
    }

    private Array<FileHandle> getAtlasPageHandles(FileHandle fileHandle) {
        Array<File> imgs = getAtlasPages(fileHandle);

        Array<FileHandle> imgHandles = new Array<>();
        for(int i = 0; i < imgs.size; i++) {
            imgHandles.add(new FileHandle(imgs.get(i)));
        }

        return  imgHandles;
    }

    private boolean addParticleEffectImages(FileHandle fileHandle, Array<FileHandle> imgs) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileHandle.read()), 64);
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.trim().equals("- Image Path -")) {
                    line = reader.readLine();
                    if(line.contains("\\") || line.contains("/")) {
                        // then it's a path let's see if exists.
                        File tmp = new File(line);
                        if(tmp.exists()) {
                            imgs.add(new FileHandle(tmp));
                        } else {
                            line = FilenameUtils.getBaseName(line) + ".png";
                            File file = new File(FilenameUtils.getFullPath(fileHandle.path()) + line);
                            if(file.exists()) {
                                imgs.add(new FileHandle(file));
                            } else {
                                return false;
                            }
                        }
                    } else {
                        File file = new File(FilenameUtils.getFullPath(fileHandle.path()) + line);
                        if(file.exists()) {
                            imgs.add(new FileHandle(file));
                        } else {
                            return false;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void importParticlesIntoProject(final Array<FileHandle> fileHandles, ProgressHandler progressHandler) {
        if (fileHandles == null) {
            return;
        }
        final String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/particles";
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Array<FileHandle> imgs = new Array<>();
            for (FileHandle fileHandle : fileHandles) {
                if (!fileHandle.isDirectory() && fileHandle.exists()) {
                    try {
                        //copy images
                        boolean allImagesFound = addParticleEffectImages(fileHandle, imgs);
                        if(allImagesFound) {
                            // copy the fileHandle
                            String newName = fileHandle.name();
                            File target = new File(targetPath + "/" + newName);
                            FileUtils.copyFile(fileHandle.file(), target);
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                        //System.out.println("Error importing particles");
                        //showError("Error importing particles \n Particle Atals not found \n Please place particle atlas and particle effect fileHandle in the same directory ");
                    }
                }
            }
            if(imgs.size > 0) {
                copyImageFilesForAllResolutionsIntoProject(imgs, false);
            }
            ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
            resolutionManager.rePackProjectImagesForAllResolutions();
        });
        executor.execute(() -> {
            changePercentBy(100 - currentPercent);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.progressComplete();
        });
        executor.shutdown();
    }

    public void importAtlasesIntoProject(final Array<FileHandle> files, ProgressHandler progressHandler) {
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            for (FileHandle fileHandle : files) {
                // TODO: logic goes here
            }
        });
        executor.execute(() -> {
            changePercentBy(100 - currentPercent);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.progressComplete();
        });
        executor.shutdown();
    }



    public void importImagesIntoProject(final Array<FileHandle> files, ProgressHandler progressHandler) {
        if (files == null) {
            return;
        }
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            copyImageFilesForAllResolutionsIntoProject(files, true);
            ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
            resolutionManager.rePackProjectImagesForAllResolutions();
        });
        executor.execute(() -> {
            changePercentBy(100 - currentPercent);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.progressComplete();
        });
        executor.shutdown();
    }

    private void copyImageFilesForAllResolutionsIntoProject(Array<FileHandle> files, Boolean performResize) {
        copyImageFilesIntoProject(files, currentProjectInfoVO.originalResolution, performResize);
        int totalWarnings = 0;
        for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions) {
            totalWarnings += copyImageFilesIntoProject(files, resolutionEntryVO, performResize);
        }
        if (totalWarnings > 0) {
            DialogUtils.showOKDialog(Sandbox.getInstance().getUIStage(), "Warning", totalWarnings + " images were not resized for smaller resolutions due to already small size ( < 3px )");
        }
    }

    /**
     * @param files
     * @param resolution
     * @param performResize
     * @return number of images that did needed to be resized but failed
     */
    private int copyImageFilesIntoProject(Array<FileHandle> files, ResolutionEntryVO resolution, Boolean performResize) {
        float ratio = ResolutionManager.getResolutionRatio(resolution, currentProjectInfoVO.originalResolution);
        String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + resolution.name + "/images";
        float perCopyPercent = 95.0f / files.size;

        int resizeWarningsCount = 0;

        for (FileHandle handle : files) {
            if (!Overlap2DUtils.PNG_FILTER.accept(null, handle.name())) {
                continue;
            }
            try {
                BufferedImage bufferedImage;
                if (performResize) {
                    bufferedImage = ResolutionManager.imageResize(handle.file(), ratio);
                    if (bufferedImage == null) {
                        bufferedImage = ImageIO.read(handle.file());
                        resizeWarningsCount++;
                    }
                } else {
                    bufferedImage = ImageIO.read(handle.file());
                }

                File target = new File(targetPath);
                if (!target.exists()) {
                    File newFile = new File(targetPath);
                    newFile.mkdir();
                }

                ImageIO.write(bufferedImage, "png", new File(targetPath + "/" + handle.name().replace("_", "")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            changePercentBy(perCopyPercent);
        }

        return resizeWarningsCount;
    }

    public void importFontIntoProject(Array<FileHandle> fileHandles, ProgressHandler progressHandler) {
        if (fileHandles == null) {
            return;
        }
        String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/freetypefonts";
        handler = progressHandler;
        float perCopyPercent = 95.0f / fileHandles.size;
        for (FileHandle fileHandle : fileHandles) {
            if (!Overlap2DUtils.TTF_FILTER.accept(null, fileHandle.name())) {
                continue;
            }
            try {
                File target = new File(targetPath);
                if (!target.exists()) {
                    File newFile = new File(targetPath);
                    newFile.mkdir();
                }
                File fileTarget = new File(targetPath + "/" + fileHandle.name());
                FileUtils.copyFile(fileHandle.file(), fileTarget);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(perCopyPercent);
            changePercentBy(perCopyPercent);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                changePercentBy(100 - currentPercent);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.progressComplete();
            });
            executor.shutdown();
        }
    }

    public void importStyleIntoProject(final FileHandle handle, ProgressHandler progressHandler) {
        if (handle == null) {
            return;
        }
        final String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/styles";
        FileHandle fileHandle = Gdx.files.absolute(handle.path());
        final MySkin skin = new MySkin(fileHandle);
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            for (int i = 0; i < skin.fontFiles.size(); i++) {
                File copyFontFile = new File(handle.path(), skin.fontFiles.get(i) + ".fnt");
                File copyImageFile = new File(handle.path(), skin.fontFiles.get(i) + ".png");
                if (!handle.isDirectory() && handle.exists() && copyFontFile.isFile() && copyFontFile.exists() && copyImageFile.isFile() && copyImageFile.exists()) {
                    File fileTarget = new File(targetPath + "/" + handle.name());
                    File fontTarget = new File(targetPath + "/" + copyFontFile.getName());
                    File imageTarget = new File(targetPath + "/" + copyImageFile.getName());
                    try {
                        FileUtils.copyFile(handle.file(), fileTarget);
                        FileUtils.copyFile(copyFontFile, fontTarget);
                        FileUtils.copyFile(copyImageFile, imageTarget);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("SOME FILES ARE MISSING");
                }
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                changePercentBy(100 - currentPercent);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.progressComplete();
            }
        });
        executor.shutdown();
    }

    /**
     * @depricated
     */
    public void copyDefaultStyleIntoProject() {
        /*
        String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/styles";
        ResourceManager textureManager = facade.retrieveProxy(ResourceManager.NAME);
        File source = new File("assets/ui");
        if (!(source.exists() && source.isDirectory())) {
            try {
                JarUtils.copyResourcesToDirectory(JarUtils.getThisJar(getClass()), "ui", targetPath);
                textureManager.loadCurrentProjectSkin(targetPath);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File fileTarget = new File(targetPath);
        try {
            FileUtils.copyDirectory(source, fileTarget);
            textureManager.loadCurrentProjectSkin(targetPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        */
    }

    public String getFreeTypeFontPath() {
        return currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/freetypefonts";
    }

    public void exportProject() {

        String defaultBuildPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/export";
        exportPacks(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            exportPacks(currentProjectVO.projectMainExportPath);
        }
        exportAnimations(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            exportAnimations(currentProjectVO.projectMainExportPath);
        }
        exportParticles(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            exportParticles(currentProjectVO.projectMainExportPath);
        }
        exportFonts(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            exportFonts(currentProjectVO.projectMainExportPath);
        }
        exportStyles(defaultBuildPath);
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        sceneDataManager.buildScenes(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            sceneDataManager.buildScenes(currentProjectVO.projectMainExportPath);
        }
    }


    private void exportStyles(String targetPath) {
        String srcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig";
        FileHandle origDirectoryHandle = Gdx.files.absolute(srcPath);
        FileHandle stylesDirectory = origDirectoryHandle.child("styles");
        File fileTarget = new File(targetPath + "/" + stylesDirectory.name());
        try {
            FileUtils.copyDirectory(stylesDirectory.file(), fileTarget);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void exportParticles(String targetPath) {
        String srcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig";
        FileHandle origDirectoryHandle = Gdx.files.absolute(srcPath);
        FileHandle particlesDirectory = origDirectoryHandle.child("particles");
        File fileTarget = new File(targetPath + "/" + particlesDirectory.name());
        try {
            FileUtils.copyDirectory(particlesDirectory.file(), fileTarget);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportFonts(String targetPath) {
        String srcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig";
        FileHandle origDirectoryHandle = Gdx.files.absolute(srcPath);
        FileHandle fontsDirectory = origDirectoryHandle.child("freetypefonts");
        File fileTarget = new File(targetPath + "/" + fontsDirectory.name());
        try {
            FileUtils.copyDirectory(fontsDirectory.file(), fileTarget);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }


    private void exportAnimations(String targetPath) {
        exportSpineAnimationForResolution("orig", targetPath);
        exportSpriteAnimationForResolution("orig", targetPath);
        exportSpriterAnimationForResolution("orig", targetPath);
        for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions) {
            exportSpineAnimationForResolution(resolutionEntryVO.name, targetPath);
            exportSpriteAnimationForResolution(resolutionEntryVO.name, targetPath);
            exportSpriterAnimationForResolution(resolutionEntryVO.name, targetPath);
        }
    }

    private void exportSpineAnimationForResolution(String res, String targetPath) {
        String spineSrcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + res + File.separator + "spine-animations";
        try {
            FileUtils.forceMkdir(new File(targetPath + File.separator + res + File.separator + "spine_animations"));
            File fileSrc = new File(spineSrcPath);
            String finalTarget = targetPath + File.separator + res + File.separator + "spine_animations";

            File fileTargetSpine = new File(finalTarget);

            FileUtils.copyDirectory(fileSrc, fileTargetSpine);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void exportSpriteAnimationForResolution(String res, String targetPath) {
        String spineSrcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + res + File.separator + "sprite-animations";
        try {
            FileUtils.forceMkdir(new File(targetPath + File.separator + res + File.separator + "sprite_animations"));
            File fileSrc = new File(spineSrcPath);
            String finalTarget = targetPath + File.separator + res + File.separator + "sprite_animations";

            File fileTargetSprite = new File(finalTarget);

            FileUtils.copyDirectory(fileSrc, fileTargetSprite);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void exportSpriterAnimationForResolution(String res, String targetPath) {
        String spineSrcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + res + File.separator + "spriter-animations";
        try {
            FileUtils.forceMkdir(new File(targetPath + File.separator + res + File.separator + "spriter_animations"));
            File fileSrc = new File(spineSrcPath);
            String finalTarget = targetPath + File.separator + res + File.separator + "spriter_animations";
            File fileTargetSpriter = new File(finalTarget);
            FileUtils.copyDirectory(fileSrc, fileTargetSpriter);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void exportPacks(String targetPath) {
        String srcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets";
        FileHandle assetDirectoryHandle = Gdx.files.absolute(srcPath);
        FileHandle[] assetDirectories = assetDirectoryHandle.list();
        for (FileHandle assetDirectory : assetDirectories) {
            if (assetDirectory.isDirectory()) {
                FileHandle assetDirectoryFileHandle = Gdx.files.absolute(assetDirectory.path());
                FileHandle[] packFiles = assetDirectoryFileHandle.child("pack").list();
                for (FileHandle packFile : packFiles) {
                    File fileTarget = new File(targetPath + "/" + assetDirectory.name() + "/" + packFile.name());
                    try {
                        FileUtils.copyFile(packFile.file(), fileTarget);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public void setExportPaths(File path) {
        currentProjectVO.projectMainExportPath = path.getPath();
    }

    public void setTexturePackerSizes(int width, int height) {
        currentProjectVO.texturepackerWidth = String.valueOf(width);
        currentProjectVO.texturepackerHeight = String.valueOf(height);
    }

    public void setTexturePackerDuplicate(boolean duplicate) {
        currentProjectVO.texturepackerDuplicate = duplicate;
    }

    public String getRootPath() {
        File root = new File(new File(".").getAbsolutePath()).getParentFile();
        return root.getAbsolutePath();
    }

    private EditorConfigVO getEditorConfig() {
        EditorConfigVO editorConfig = new EditorConfigVO();
        String configFilePath = getRootPath() + "/" + EditorConfigVO.EDITOR_CONFIG_FILE;
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            try {
                FileUtils.writeStringToFile(new File(configFilePath), editorConfig.constructJsonString(), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Json gson = new Json();
            String editorConfigJson = null;
            try {
                editorConfigJson = FileUtils.readFileToString(Gdx.files.absolute(configFilePath).file());
                editorConfig = gson.fromJson(EditorConfigVO.class, editorConfigJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return editorConfig;
    }

    public void createNewProject(String projectPath, int originWidth, int originHeight, int pixelPerWorldUnit) {
        if (projectPath == null || projectPath.equals("")) {
            return;
        }
        String projectName = new File(projectPath).getName();

        if (projectName.equals("")) {
            return;
        }

        try {
            createEmptyProject(projectName, originWidth, originHeight, pixelPerWorldUnit);
            openProjectAndLoadAllData(projectName);
            String workSpacePath = projectPath.substring(0, projectPath.lastIndexOf(projectName));
            if (workSpacePath.length() > 0) {
                setLastOpenedPath(workSpacePath);
                setWorkspacePath(workSpacePath);
            }
            Sandbox.getInstance().loadCurrentProject();
            facade.sendNotification(PROJECT_OPENED);
            
            //Set title with opened file path
            Gdx.graphics.setTitle(getFormatedTitle(projectPath));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openProjectFromPath(String path) {
        File projectFile = new File(path);
        File projectFolder = projectFile.getParentFile();
        String projectName = projectFolder.getName();
        currentWorkingPath = projectFolder.getParentFile().getPath();
        editorConfigVO.lastOpenedSystemPath = currentWorkingPath;
        saveEditorConfig();

        // here we load all data
        openProjectAndLoadAllData(projectName);
        Sandbox.getInstance().loadCurrentProject();
        
        facade.sendNotification(ProjectManager.PROJECT_OPENED);

        //Set title with opened file path
        Gdx.graphics.setTitle(getFormatedTitle(path));
    }


	private String getFormatedTitle(String path) {
		//App Name + Version + path to opened file
		return "Overlap2D - Public Aplha v" + AppConfig.getInstance().version + " - [ " + path + " ]";
	}

    public SceneConfigVO getCurrentSceneConfigVO() {
        for(int i = 0; i < currentProjectVO.sceneConfigs.size(); i++) {
            if(currentProjectVO.sceneConfigs.get(i).sceneName.equals(Sandbox.getInstance().getSceneControl().getCurrentSceneVO().sceneName)) {
                return currentProjectVO.sceneConfigs.get(i);
            }
        }

        SceneConfigVO newConfig = new SceneConfigVO();
        newConfig.sceneName = Sandbox.getInstance().getSceneControl().getCurrentSceneVO().sceneName;
        currentProjectVO.sceneConfigs.add(newConfig);

        return newConfig;
    }

    public void importShaderIntoProject(Array<FileHandle> files, ProgressHandler progressHandler) {
        if (files == null) {
            return;
        }
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            for (FileHandle handle : files) {
                // check if shaders folder exists
                String shadersPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/shaders";
                File destination = new File(currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/shaders/"+handle.name());
                try {
                    FileUtils.forceMkdir(new File(shadersPath));
                    FileUtils.copyFile(handle.file(), destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        executor.execute(() -> {
            changePercentBy(100 - currentPercent);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.progressComplete();
        });
        executor.shutdown();
    }
}