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

package com.uwsoft.editor.mvc.proxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.Json;
import com.puremvc.patterns.proxy.BaseProxy;
import com.uwsoft.editor.controlles.ResolutionManager;
import com.uwsoft.editor.data.JarUtils;
import com.uwsoft.editor.data.manager.SceneDataManager;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.data.migrations.ProjectVersionMigrator;
import com.uwsoft.editor.data.vo.EditorConfigVO;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.gdx.ui.ProgressHandler;
import com.uwsoft.editor.renderer.data.*;
import com.uwsoft.editor.renderer.utils.MySkin;
import com.uwsoft.editor.utils.AppConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DataManager extends BaseProxy {
    private static final String TAG = DataManager.class.getCanonicalName();
    public static final String NAME = TAG;
    public ProjectVO currentProjectVO;
    public ProjectInfoVO currentProjectInfoVO;
    //    private ResolutionManager resolutionManager;
//    private SceneDataManager sceneDataManager;
//    private TextureManager textureManager;
    private String currentWorkingPath;
    private String workspacePath;
    private String DEFAULT_FOLDER = "Overlap2D";
    private float currentPercent = 0.0f;
    private ProgressHandler handler;
    private EditorConfigVO editorConfigVO;


    public DataManager() {
        super(NAME);
    }

    public static String getMyDocumentsLocation() {
        String myDocuments = null;
        try {
            if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
                myDocuments = System.getProperty("user.home") + File.separator + "Documents";
            }
            if (SystemUtils.IS_OS_WINDOWS) {
                Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
                p.waitFor();

                InputStream in = p.getInputStream();
                byte[] b = new byte[in.available()];
                in.read(b);
                in.close();

                myDocuments = new String(b);
                myDocuments = myDocuments.split("\\s\\s+")[4];
            }
            if (SystemUtils.IS_OS_LINUX) {
                myDocuments = System.getProperty("user.home") + File.separator + "Documents";
            }


        } catch (Throwable t) {
            t.printStackTrace();
        }

        return myDocuments;
    }

    @Override
    public void onRegister() {
        super.onRegister();
        initWorkspace();
//        resolutionManager = new ResolutionManager(this);
//        sceneDataManager = new SceneDataManager(this);
//        textureManager = new TextureManager(this);
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
            String myDocPath = getMyDocumentsLocation();
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

    public void createEmptyProject(String projectName, int width, int height) throws IOException {

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
        projVo.projectVersion = AppConfig.getInstance().version;

        // create project info file
        ProjectInfoVO projInfoVo = new ProjectInfoVO();
        projInfoVo.originalResolution.name = "orig";
        projInfoVo.originalResolution.width = width;
        projInfoVo.originalResolution.height = height;

        //TODO: add project orig resolution setting
        currentProjectVO = projVo;
        currentProjectInfoVO = projInfoVo;
        SceneDataManager sceneDataManager = facade.retrieveProxy(SceneDataManager.NAME);
        sceneDataManager.createNewScene("MainScene");
        FileUtils.writeStringToFile(new File(projPath + "/project.pit"), projVo.constructJsonString(), "utf-8");
        FileUtils.writeStringToFile(new File(projPath + "/project.dt"), projInfoVo.constructJsonString(), "utf-8");

    }

    public void openProjectFromPath(String path) {
        File projectFile = new File(path);
        File projectFolder = projectFile.getParentFile();
        String projectName = projectFolder.getName();
        currentWorkingPath = projectFolder.getParentFile().getPath();
        editorConfigVO.lastOpenedSystemPath = currentWorkingPath;
        saveEditorConfig();
        openProjectAndLoadAllData(projectName);
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

        File prjFile = new File(prjFilePath);
        if (prjFile.exists() && !prjFile.isDirectory()) {
            FileHandle projectFile = Gdx.files.internal(prjFilePath);
            String projectContents = null;
            try {
                projectContents = FileUtils.readFileToString(projectFile.file());
                Json json = new Json();
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
                resolutionManager.curResolution = currentProjectVO.lastOpenResolution.isEmpty() ? "orig" : currentProjectVO.lastOpenResolution;
            } else {
                resolutionManager.curResolution = resolution;
                currentProjectVO.lastOpenResolution = resolutionManager.curResolution;
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
        for (FileHandle entry : sourceDir.list(new DTFilenameFilter())) {
            if (!entry.file().isDirectory()) {
                Json json = new Json();
                SceneVO sceneVO = json.fromJson(SceneVO.class, entry);
                if (sceneVO.composite == null) continue;
                ArrayList<MainItemVO> items = sceneVO.composite.getAllItems();
                for (MainItemVO vo : items) {
                    if (vo.meshId.equals("-1")) continue;
                    uniqueMeshIds.add(vo.meshId);
                }
                for (CompositeItemVO libraryItem : sceneVO.libraryItems.values()) {
                    if (libraryItem.composite == null) continue;
                    items = libraryItem.composite.getAllItems();
                    for (MainItemVO vo : items) {
                        if (vo.meshId.equals("-1")) continue;
                        uniqueMeshIds.add(vo.meshId);
                    }
                }
            }
        }
        // addsset list
        for (String meshId : currentProjectInfoVO.assetMeshMap.values()) {
            uniqueMeshIds.add(meshId);
        }

        // check for not used meshes and remove
        Iterator<Map.Entry<String, MeshVO>> iter = currentProjectInfoVO.meshes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, MeshVO> entry = iter.next();
            if (!uniqueMeshIds.contains(entry.getKey())) {
                System.out.println("KEY " + entry.getKey());
                iter.remove();
                System.out.println("meshe removed");
            }
        }
    }


    private void loadProjectData(String projectName) {
        // All legit loading assets
        ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
        textureManager.loadCurrentProjectData(currentWorkingPath, projectName, resolutionManager.curResolution);
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


    private ArrayList<File> getImageListFromAtlas(File file) throws IOException {
        ArrayList<File> images = new ArrayList<File>();
        BufferedReader br;

        br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.equals("- Image Path -")) {
                line = br.readLine();
                String absolutePath = file.getAbsolutePath();
                if (line.contains("/") || line.indexOf("\\") > 0) {
                    line = line.substring(line.lastIndexOf(File.separator), line.length());
                }
                String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator)) + File.separator + line;
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    images.add(imgFile);
                } else {
                    throw new IOException("Particle image file missing.");
                }
            }
        }
        br.close();
        return images;
    }

    private ArrayList<File> getScmlFileImagesList(File file) {
        ArrayList<File> images = new ArrayList<File>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            org.w3c.dom.Document document = db.parse(file);
            NodeList nodeList = document.getElementsByTagName("file");
            for (int x = 0, size = nodeList.getLength(); x < size; x++) {
                String absolutePath = file.getAbsolutePath();
                String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator)) + File.separator + nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue();

                File imgFile = new File(path);
                images.add(imgFile);
            }
        } catch (SAXException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return images;
    }


    public void importExternalSpineAnimationsIntoProject(final ArrayList<File> files, ProgressHandler progressHandler) {
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (File file : files) {
                    File copiedFile = importExternalAnimationIntoProject(file);
                    if (copiedFile.getName().toLowerCase().endsWith(".atlas")) {
                        ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                        resolutionManager.resizeSpineAnimationForAllResolutions(copiedFile, currentProjectInfoVO);
                    } else if (copiedFile.getName().toLowerCase().endsWith(".scml")) {
                        //resizeSpriterAnimationForAllResolutions(copiedFile, currentProjectInfoVO);
                    }
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

    public File importExternalAnimationIntoProject(File animationFileSource) {
        try {
            JsonFilenameFilter jsonFilenameFilter = new JsonFilenameFilter();
            ScmlFilenameFilter scmlFilenameFilter = new ScmlFilenameFilter();
            if (!jsonFilenameFilter.accept(null, animationFileSource.getName()) && !scmlFilenameFilter.accept(null, animationFileSource.getName())) {
                //showError("Spine animation should be a .json file with atlas in same folder \n Spriter animation should be a .scml file with images in same folder");

                return null;
            }

            String fileNameWithOutExt = FilenameUtils.removeExtension(animationFileSource.getName());
            String sourcePath;
            String animationDataPath;
            String targetPath;
            if (jsonFilenameFilter.accept(null, animationFileSource.getName())) {
                sourcePath = animationFileSource.getAbsolutePath();
                animationDataPath = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator)) + File.separator;
                targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/spine-animations" + File.separator + fileNameWithOutExt;
                File atlasFileSource = new File(animationDataPath + File.separator + fileNameWithOutExt + ".atlas");
                if (!atlasFileSource.exists()) {
                    //showError("the atlas file needs to have same name and location as the json file");

                    return null;
                }

                FileUtils.forceMkdir(new File(targetPath));
                File jsonFileTarget = new File(targetPath + File.separator + fileNameWithOutExt + ".json");
                File atlasFileTarget = new File(targetPath + File.separator + fileNameWithOutExt + ".atlas");
                ArrayList<File> imageFiles = getImageListFromAtlas(atlasFileSource);

                FileUtils.copyFile(animationFileSource, jsonFileTarget);
                FileUtils.copyFile(atlasFileSource, atlasFileTarget);

                for (File imageFile : imageFiles) {
                    File imgFileTarget = new File(targetPath + File.separator + imageFile.getName());
                    FileUtils.copyFile(imageFile, imgFileTarget);
                }

                return atlasFileTarget;


            } else if (scmlFilenameFilter.accept(null, animationFileSource.getName())) {
                targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/spriter-animations" + File.separator + fileNameWithOutExt;
                File scmlFileTarget = new File(targetPath + File.separator + fileNameWithOutExt + ".scml");
                ArrayList<File> imageFiles = getScmlFileImagesList(animationFileSource);

                FileUtils.copyFile(animationFileSource, scmlFileTarget);
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


    public void importExternalSpriteAnimationsIntoProject(final ArrayList<File> files, ProgressHandler progressHandler) {
        if (files.size() == 0) {
            return;
        }
        handler = progressHandler;

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {

                String newAnimName = null;

                String rawFileName = files.get(0).getName();
                String fileExtension = FilenameUtils.getExtension(rawFileName);
                if (fileExtension.equals("png")) {
                    Settings settings = new Settings();
                    settings.square = true;
                    settings.flattenPaths = true;

                    TexturePacker texturePacker = new TexturePacker(settings);
                    FileHandle pngsDir = new FileHandle(files.get(0).getParentFile().getAbsolutePath());
                    for (FileHandle entry : pngsDir.list(new PngFilenameFilter())) {
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
                    for (File file : files) {
                        try {
                            ArrayList<File> imgs = getAtlasPages(file);
                            String fileNameWithoutExt = FilenameUtils.removeExtension(file.getName());
                            String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/sprite-animations" + File.separator + fileNameWithoutExt;
                            File targetDir = new File(targetPath);
                            if (targetDir.exists()) {
                                FileUtils.deleteDirectory(targetDir);
                            }
                            for (File img : imgs) {
                                FileUtils.copyFileToDirectory(img, targetDir);
                            }
                            FileUtils.copyFileToDirectory(file, targetDir);
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

    private ArrayList<File> getAtlasPages(File file) {
        ArrayList<File> imgs = new ArrayList<File>();
        try {
            FileHandle fileHandle = new FileHandle(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileHandle.read()), 64);
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                if (line.trim().length() == 0) {
                    line = reader.readLine();
                    imgs.add(new FileHandle(file.getParentFile()).child(line).file());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgs;
    }


    public void importExternalParticlesIntoProject(final ArrayList<File> files, ProgressHandler progressHandler) {
        final String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/particles";
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (File file : files) {
                    if (file.isFile() && file.exists()) {
                        try {
                            //copy images
                            ArrayList<File> imgs = getImageListFromAtlas(file);
                            copyImageFilesForAllResolutionsIntoProject(imgs, false);
                            // copy the file
                            String newName = file.getName();
                            File target = new File(targetPath + "/" + newName);
                            FileUtils.copyFile(file, target);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error importing particles");
                            //showError("Error importing particles \n Particle Atals not found \n Please place particle atlas and particle effect file in the same directory ");
                        }
                    }
                }
                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                resolutionManager.rePackProjectImagesForAllResolutions();
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

    public void importExternalImagesIntoProject(final ArrayList<File> files, ProgressHandler progressHandler) {
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                copyImageFilesForAllResolutionsIntoProject(files, true);
                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                resolutionManager.rePackProjectImagesForAllResolutions();
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

    private void copyImageFilesForAllResolutionsIntoProject(ArrayList<File> files, Boolean performResize) {
        copyImageFilesIntoProject(files, currentProjectInfoVO.originalResolution, performResize);
        for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions) {
            copyImageFilesIntoProject(files, resolutionEntryVO, performResize);
        }
    }

    private void copyImageFilesIntoProject(ArrayList<File> files, ResolutionEntryVO resolution, Boolean performResize) {
        float ratio = ResolutionManager.getResolutionRatio(resolution, currentProjectInfoVO.originalResolution);
        String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + resolution.name + "/images";
        PngFilenameFilter pngFilenameFilter = new PngFilenameFilter();
        float perCopyPercent = 95.0f / files.size();
        for (File file : files) {
            if (!pngFilenameFilter.accept(null, file.getName())) {
                continue;
            }
            try {
                BufferedImage bufferedImage = performResize ? ResolutionManager.imageResize(file, ratio) : ImageIO.read(file);

                File target = new File(targetPath);
                if (!target.exists()) {
                    File newFile = new File(targetPath);
                    newFile.mkdir();
                }

                ImageIO.write(bufferedImage, "png", new File(targetPath + "/" + file.getName().replace("_", "")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            changePercentBy(perCopyPercent);
        }
    }

    public void importExternalFontIntoProject(ArrayList<File> externalfiles, ProgressHandler progressHandler) {
        String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/freetypefonts";
        TffFilenameFilter ttfFilenameFilter = new TffFilenameFilter();
        handler = progressHandler;
        float perCopyPercent = 95.0f / externalfiles.size();
        for (File file : externalfiles) {
            if (!ttfFilenameFilter.accept(null, file.getName())) {
                continue;
            }
            try {
                File target = new File(targetPath);
                if (!target.exists()) {
                    File newFile = new File(targetPath);
                    newFile.mkdir();
                }
                File fileTarget = new File(targetPath + "/" + file.getName());

                FileUtils.copyFile(file, fileTarget);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(perCopyPercent);
            changePercentBy(perCopyPercent);
            ExecutorService executor = Executors.newSingleThreadExecutor();
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
    }

    public void importExternalStyleIntoProject(final File file, ProgressHandler progressHandler) {
        final String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/styles";
        FileHandle fileHandle = Gdx.files.absolute(file.getAbsolutePath());
        final MySkin skin = new MySkin(fileHandle);
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < skin.fontFiles.size(); i++) {
                    File copyFontFile = new File(file.getParentFile(), skin.fontFiles.get(i) + ".fnt");
                    File copyImageFile = new File(file.getParentFile(), skin.fontFiles.get(i) + ".png");
                    if (file.isFile() && file.exists() && copyFontFile.isFile() && copyFontFile.exists() && copyImageFile.isFile() && copyImageFile.exists()) {
                        File fileTarget = new File(targetPath + "/" + file.getName());
                        File fontTarget = new File(targetPath + "/" + copyFontFile.getName());
                        File imageTarget = new File(targetPath + "/" + copyImageFile.getName());
                        try {
                            FileUtils.copyFile(file, fileTarget);
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

    public void copyDefaultStyleIntoProject() {
        String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/styles";
        TextureManager textureManager = facade.retrieveProxy(TextureManager.NAME);
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

    public void setTexturePackerSizes(String width, String height) {
        currentProjectVO.texturepackerWidth = width;
        currentProjectVO.texturepackerHeight = height;
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


    public static class PngFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".png");
        }
    }

    public static class TffFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            System.out.println(name + "   " + name.toLowerCase().endsWith(".ttf"));
            return name.toLowerCase().endsWith(".ttf");
        }
    }

    public static class JsonFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".json");
        }
    }

    public static class ScmlFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".scml");
        }
    }

    public static class DTFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".dt");
        }
    }


}
