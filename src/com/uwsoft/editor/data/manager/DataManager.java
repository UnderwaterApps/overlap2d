package com.uwsoft.editor.data.manager;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.controlles.ResolutionManager;
import com.uwsoft.editor.data.JarUtils;
import com.uwsoft.editor.data.migrations.ProjectVersionMigrator;
import com.uwsoft.editor.data.vo.EditorConfigVO;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.gdx.stage.UIStage;
import com.uwsoft.editor.gdx.ui.ProgressHandler;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.MeshVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.data.SceneVO;
import com.uwsoft.editor.renderer.resources.FontSizePair;
import com.uwsoft.editor.renderer.utils.MySkin;
import com.uwsoft.editor.tools.TextureUnpackerFixed;
import com.uwsoft.editor.utils.AppConfig;
import com.uwsoft.editor.utils.OSType;


public class DataManager {

    private static DataManager instance = null;
    public String curResolution;
    private String currentWorkingPath;
    private String workspacePath;
    private String DEFAULT_FOLDER = "Overlap2D";
    public ProjectVO currentProjectVO;
    public ProjectInfoVO currentProjectInfoVO;
    private float currentPercent = 0.0f;
    private ProgressHandler handler;
    private EditorConfigVO editorConfigVO;

    private UIStage stage;

    public DataManager() {
        initWorkspace();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }

        return instance;
    }

    public static String getMyDocumentsLocation() {
        String myDocuments = null;


        try {
            if (OSType.getOS_Type() == OSType.MacOS) {
                myDocuments = System.getProperty("user.home") + File.separator + "Documents";
            }
            if (OSType.getOS_Type() == OSType.Windows) {
                Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
                p.waitFor();

                InputStream in = p.getInputStream();
                byte[] b = new byte[in.available()];
                in.read(b);
                in.close();

                myDocuments = new String(b);
                myDocuments = myDocuments.split("\\s\\s+")[4];
            }
            if (OSType.getOS_Type() == OSType.Linux) {
                myDocuments = System.getProperty("user.home") + File.separator + "Documents";
            }


        } catch (Throwable t) {
            t.printStackTrace();
        }

        return myDocuments;
    }

    public static int getMinSquareNum(int num) {

        // if (num < 1024) return 1024;

        // if (num < 2048) return 2048;

        // if (num < 4096) return 4096;


        return 4096;
    }

    public void setStage(UIStage stage) {
        this.stage = stage;
    }

    private void showError(String txt) {
        if (stage != null) {
            stage.showInfoDialog(txt);
        }
    }

    public ProjectVO getCurrentProjectVO() {
        return currentProjectVO;
    }

    public ProjectInfoVO getCurrentProjectInfoVO() {
        return currentProjectInfoVO;
    }

    private void initWorkspace() {
        //System.out.println("AAAAAAAAAA : " + new File(".").getAbsolutePath());
        // return new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath().toURI());
        editorConfigVO = getEditorConfig();
        String myDocPath = getMyDocumentsLocation();

        // Initializing default workspace
        workspacePath = myDocPath + "/" + DEFAULT_FOLDER;

        createIfNotExist(workspacePath);
        currentWorkingPath = workspacePath;
    }

    private void changePercentBy(float value) {
        currentPercent += value;
        handler.progressChanged(currentPercent);
    }

    private File createIfNotExist(String dirPath) {
        File theDir = new File(dirPath);
        boolean result = false;
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            result = theDir.mkdir();
        }

        if (result)
            return theDir;
        return null;
    }

    public static void writeToFile(String path, String content) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), "utf-8"));
            writer.write(content);
        } catch (IOException ex) {
            // report
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }

    public void createEmptyProject(String projectName, int width, int height) {

        if (workspacePath.endsWith(File.separator)) {
            workspacePath = workspacePath.substring(0, workspacePath.length() - 1);
        }

        String projPath = workspacePath + File.separator + projectName;
        currentWorkingPath = workspacePath;

        File dir = createIfNotExist(projPath);
        /*
        if (dir == null) {
            // did not create dir, probably already exist
            return;
        }
        */

        // directory created need to populate with other folders

        createIfNotExist(projPath + File.separator + "export");
        createIfNotExist(projPath + File.separator + "assets");
        createIfNotExist(projPath + File.separator + "scenes");

        createIfNotExist(projPath + File.separator + "assets/orig");
        createIfNotExist(projPath + File.separator + "assets/orig/images");
        createIfNotExist(projPath + File.separator + "assets/orig/particles");
        createIfNotExist(projPath + File.separator + "assets/orig/animations");
        createIfNotExist(projPath + File.separator + "assets/orig/pack");


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

        createEmptyScene("MainScene", workspacePath);
        writeToFile(projPath + "/project.pit", projVo.constructJsonString());
        writeToFile(projPath + "/project.dt", projInfoVo.constructJsonString());

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
        File root = new File(new File(".").getAbsolutePath()).getParentFile();
        String configFilePath = root.getAbsolutePath() + "/" + EditorConfigVO.EDITOR_CONFIG_FILE;
        writeToFile(configFilePath, editorConfigVO.constructJsonString());
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
            String projectContents = readFileContents(projectFile);
            Json json = new Json();
            ProjectVO vo = json.fromJson(ProjectVO.class, projectContents);
            currentProjectVO = vo;

            goThroughVersionMigrationProtocol(projectPath, vo);

            String prjInfoFilePath = projectPath + "/project.dt";
            FileHandle projectInfoFile = Gdx.files.internal(prjInfoFilePath);
            String projectInfoContents = readFileContents(projectInfoFile);
            ProjectInfoVO voInfo = json.fromJson(ProjectInfoVO.class, projectInfoContents);

            currentProjectInfoVO = voInfo;

            if (resolution == null) {
                curResolution = currentProjectVO.lastOpenResolution.isEmpty() ? "orig" : currentProjectVO.lastOpenResolution;
            } else {
                curResolution = resolution;
                currentProjectVO.lastOpenResolution = curResolution;
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
        HashSet<Integer> uniqueMeshIds = new HashSet<>();
        FileHandle sourceDir = new FileHandle(currentWorkingPath + "/" + currentProjectVO.projectName + "/scenes/");
        for (FileHandle entry : sourceDir.list(new DTFilenameFilter())) {
            if (!entry.file().isDirectory()) {
                Json json = new Json();
                SceneVO sceneVO = json.fromJson(SceneVO.class, entry);
                if (sceneVO.composite == null) continue;
                ArrayList<MainItemVO> items = sceneVO.composite.getAllItems();
                for (MainItemVO vo : items) {
                    if (vo.meshId == -1) continue;
                    uniqueMeshIds.add(vo.meshId);
                }
                for (CompositeItemVO libraryItem : sceneVO.libraryItems.values()) {
                    if (libraryItem.composite == null) continue;
                    items = libraryItem.composite.getAllItems();
                    for (MainItemVO vo : items) {
                        if (vo.meshId == -1) continue;
                        uniqueMeshIds.add(vo.meshId);
                    }
                }
            }
        }
        // addsset list
        for (Integer meshId : currentProjectInfoVO.assetMeshMap.values()) {
            uniqueMeshIds.add(meshId);
        }

        // check for not used meshes and remove
        Iterator<Map.Entry<Integer, MeshVO>> iter = currentProjectInfoVO.meshes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, MeshVO> entry = iter.next();
            if (!uniqueMeshIds.contains(entry.getKey())) {
                iter.remove();
                System.out.println("meshe removed");
            }
        }
    }

    public float getCurrentMul() {
        ResolutionEntryVO curRes = getCurrentProjectInfoVO().getResolution(curResolution);
        float mul = 1f;
        if (!curResolution.equals("orig")) {
            if (curRes.base == 0) {
                mul = (float) curRes.width / (float) getCurrentProjectInfoVO().originalResolution.width;
            } else {
                mul = (float) curRes.height / (float) getCurrentProjectInfoVO().originalResolution.height;
            }
        }

        return mul;
    }

    public void preloadSceneSpecificData(SceneVO sceneVO, String resolution) {
        if (sceneVO == null || sceneVO.composite == null) return;

        FontSizePair[] fonts = sceneVO.composite.getRecursiveFontList();

        TextureManager.getInstance().loadBitmapFonts(fonts, getCurrentMul());
    }

    private void loadProjectData(String projectName) {
        // All legit loading assets
        TextureManager.getInstance().loadCurrentProjectAssets(currentWorkingPath + "/" + projectName + "/assets/" + curResolution + "/pack/pack.atlas");
        TextureManager.getInstance().loadCurrentProjectSkin(currentWorkingPath + "/" + projectName + "/assets/orig/styles");
        TextureManager.getInstance().loadCurrentProjectLabelStylePath(currentWorkingPath + "/" + projectName + "/assets/orig/freetypefonts");
        TextureManager.getInstance().loadCurrentProjectParticles(currentWorkingPath + "/" + projectName + "/assets/orig/particles");
        TextureManager.getInstance().loadCurrentProjectSpineAnimations(currentWorkingPath + "/" + projectName + "/assets/", curResolution);
        TextureManager.getInstance().loadCurrentProjectSpriteAnimations(currentWorkingPath + "/" + projectName + "/assets/", curResolution);
        TextureManager.getInstance().loadCurrentProjectSpriterAnimations(currentWorkingPath + "/" + projectName + "/assets/", curResolution);
    }

    public SceneVO createNewScene(String name) {
        SceneVO vo = createEmptyScene(name, currentWorkingPath);
        String projPath = currentWorkingPath + "/" + currentProjectVO.projectName;
        writeToFile(projPath + "/project.dt", currentProjectInfoVO.constructJsonString());
        return vo;
    }

    private SceneVO createEmptyScene(String name, String path) {
        String projPath = path + "/" + currentProjectVO.projectName;
        SceneVO vo = new SceneVO();
        vo.sceneName = name;
        writeToFile(projPath + "/scenes/" + vo.sceneName + ".dt", vo.constructJsonString());
        currentProjectInfoVO.scenes.add(vo);
        return vo;
    }

    public static String readFileContents(FileHandle file) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(file.reader());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return null;
        }
        StringBuffer sb = new StringBuffer("");
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = sb.toString();

        return json;
    }

    public String getCurrentWorkingPath() {
        return currentWorkingPath;
    }

    public void setCurrentWorkingPath(String currentWorkingPath) {
        this.currentWorkingPath = currentWorkingPath;
    }

    public String getWorkspacePath() {
        return editorConfigVO.lastOpenedSystemPath.isEmpty() ? workspacePath : editorConfigVO.lastOpenedSystemPath;
    }

    public void setWorkspacePath(String path) {
        workspacePath = path;
    }

    public String getCurrProjectScenePathByName(String sceneName) {
        String scenePath = currentWorkingPath + "/" + currentProjectVO.projectName + "/scenes/" + sceneName + ".dt";

        return scenePath;
    }

    public void saveScene(SceneVO vo) {
        writeToFile(currentWorkingPath + "/" + currentProjectVO.projectName + "/scenes/" + vo.sceneName + ".dt", vo.constructJsonString());
    }

    public void saveCurrentProject() {
        writeToFile(currentWorkingPath + "/" + currentProjectVO.projectName + "/project.pit", currentProjectVO.constructJsonString());
        writeToFile(currentWorkingPath + "/" + currentProjectVO.projectName + "/project.dt", currentProjectInfoVO.constructJsonString());
    }

    public void rePackProjectImages(ResolutionEntryVO resEntry) {
        Settings settings = new Settings();

        settings.flattenPaths = true;
        settings.maxHeight = getMinSquareNum(resEntry.height);
        settings.maxWidth = getMinSquareNum(resEntry.height);
        settings.filterMag = TextureFilter.Linear;
        settings.filterMin = TextureFilter.Linear;

        TexturePacker tp = new TexturePacker(settings);

        String sourcePath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + resEntry.name + "/images";
        String outputPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + resEntry.name + "/pack";

        FileHandle sourceDir = new FileHandle(sourcePath);
        File outputDir = new File(outputPath);

        if (!outputDir.exists()) {
            createIfNotExist(outputPath);
        }

        if (outputDir.exists()) {
            try {
                FileUtils.cleanDirectory(outputDir);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        for (FileHandle entry : sourceDir.list()) {
            String filename = entry.file().getName();
            String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length()).toLowerCase();
            if (extension.equals("png")) {
                tp.addImage(entry.file());
            }
        }

        tp.pack(outputDir, "pack");
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


    private ArrayList<File> getAtlasFileImagesList(File file) {
        ArrayList<File> images = new ArrayList<File>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    line = br.readLine();
                    String absolutePath = file.getAbsolutePath();
                    String path = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator)) + File.separator + line;
                    File imgFile = new File(path);
                    images.add(imgFile);
                }
            }
            br.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
		        for(int x=0,size= nodeList.getLength(); x<size; x++) {
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
		}
        catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    	
    	return images;
    }


    public void importExternalAnimationsIntoProject(final ArrayList<File> files, ProgressHandler progressHandler) {
        handler = progressHandler;
        currentPercent = 0;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (File file : files) {
                    File copiedFile = importExternalAnimationIntoProject(file);
                    if(copiedFile.getName().toLowerCase().endsWith(".atlas")){                    	
                    	resizeSpineAnimationForAllResolutions(copiedFile, currentProjectInfoVO);
                    }else if(copiedFile.getName().toLowerCase().endsWith(".scml")){
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
        JsonFilenameFilter jsonFilenameFilter = new JsonFilenameFilter();
        ScmlFilenameFilter scmlFilenameFilter = new ScmlFilenameFilter();
        if (!jsonFilenameFilter.accept(null, animationFileSource.getName()) && !scmlFilenameFilter.accept(null, animationFileSource.getName())) {
            showError("Spine animation should be a .json file with atlas in same folder \n Spriter animation should be a .scml file with images in same folder");

            return null;
        }
        
        String fileNameWithOutExt = FilenameUtils.removeExtension(animationFileSource.getName());
        String sourcePath;
        String animationDataPath;
        String targetPath;
        if(jsonFilenameFilter.accept(null, animationFileSource.getName())){
        	sourcePath = animationFileSource.getAbsolutePath();
        	animationDataPath = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator)) + File.separator;
        	targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/spine-animations" + File.separator + fileNameWithOutExt;
        	File atlasFileSource = new File(animationDataPath + File.separator + fileNameWithOutExt + ".atlas");
            if (!atlasFileSource.exists()) {
                showError("the atlas file needs to have same name and location as the json file");

                return null;
            }
            createIfNotExist(targetPath);
            File jsonFileTarget = new File(targetPath + File.separator + fileNameWithOutExt + ".json");
            File atlasFileTarget = new File(targetPath + File.separator + fileNameWithOutExt + ".atlas");
            ArrayList<File> imageFiles = getAtlasFileImagesList(atlasFileSource);
            try {
                FileUtils.copyFile(animationFileSource, jsonFileTarget);
                FileUtils.copyFile(atlasFileSource, atlasFileTarget);

                for (int i = 0; i < imageFiles.size(); i++) {
                    File imgFileTarget = new File(targetPath + File.separator + imageFiles.get(i).getName());
                    FileUtils.copyFile(imageFiles.get(i), imgFileTarget);
                }

                return atlasFileTarget;

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else if(scmlFilenameFilter.accept(null, animationFileSource.getName())){
        	sourcePath = animationFileSource.getAbsolutePath();
        	targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/spriter-animations" + File.separator + fileNameWithOutExt;
        	File scmlFileTarget = new File(targetPath + File.separator + fileNameWithOutExt + ".scml");
        	ArrayList<File> imageFiles = getScmlFileImagesList(animationFileSource); 
        	try {
                 FileUtils.copyFile(animationFileSource, scmlFileTarget);
                 for (int i = 0; i < imageFiles.size(); i++) {
                     File imgFileTarget = new File(targetPath + File.separator + imageFiles.get(i).getName());
                     FileUtils.copyFile(imageFiles.get(i), imgFileTarget);
                 }
                 return scmlFileTarget;

             } catch (IOException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
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
                    resizeSpriteAnimationForAllResolutions(newAnimName, currentProjectInfoVO);
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
        ArrayList<File> imgs = new ArrayList<>();
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
                        	showError("Error importing particles \n Particle Atals not found \n Please place particle atlas and particle effect file in the same directory ");
                        }
                    }
                }
                rePackProjectImagesForAllResolutions();
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
                rePackProjectImagesForAllResolutions();
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

    public void unpackAtlasIntoTmpFolder(File atlasFile, String tmpDir) {
        FileHandle atlasFileHandle = new FileHandle(atlasFile);
        TextureAtlas.TextureAtlasData atlasData = new TextureAtlas.TextureAtlasData(atlasFileHandle, atlasFileHandle.parent(), false);
        TextureUnpackerFixed unpacker = new TextureUnpackerFixed();
        unpacker.splitAtlas(atlasData, tmpDir);
    }


	public void resizeImagesTmpDirToResolution(String packName, File sourceFolder, ResolutionEntryVO resolution, File targetFolder) {
        float ratio = ResolutionManager.getResolutionRatio(resolution, currentProjectInfoVO.originalResolution);

        if (targetFolder.exists()) {
            try {
                FileUtils.cleanDirectory(targetFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // now pack
        Settings settings = new Settings();

        settings.flattenPaths = true;
        settings.maxHeight = getMinSquareNum(resolution.height);
        settings.maxWidth = getMinSquareNum(resolution.height);
        settings.filterMag = TextureFilter.Linear;
        settings.filterMin = TextureFilter.Linear;

        TexturePacker tp = new TexturePacker(settings);
        for (final File fileEntry : sourceFolder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                BufferedImage bufferedImage = ResolutionManager.imageResize(fileEntry, ratio);
                tp.addImage(bufferedImage, FilenameUtils.removeExtension(fileEntry.getName()));
            }
        }

        tp.pack(targetFolder, packName);
    }
	public void resizeSpriterImagesTmpDirToResolution(String packName, File sourceFolder, ResolutionEntryVO resolution, File targetFolder) {
		float ratio = ResolutionManager.getResolutionRatio(resolution, currentProjectInfoVO.originalResolution);
		
		if (targetFolder.exists()) {
			try {
				FileUtils.cleanDirectory(targetFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			targetFolder.mkdirs();
		}
		for (final File fileEntry : sourceFolder.listFiles()) {
			if (!fileEntry.isDirectory()) {
				BufferedImage bufferedImage = ResolutionManager.imageResize(fileEntry, ratio);								
				try {					
					File outputfile = new File(targetFolder.getPath()+"/"+fileEntry.getName());
					ImageIO.write(bufferedImage, FilenameUtils.getExtension(fileEntry.getName()), outputfile);
				} catch (IOException e) {
					System.out.println(e);
					e.printStackTrace();
				}
			}
		}
	}

    public void createResizedSpineAnimation(String animName, ResolutionEntryVO resolution) {
        String currProjectPath = currentWorkingPath + File.separator + currentProjectVO.projectName;

        File animAtlasFile = new File(currProjectPath + File.separator + "assets/orig/spine-animations/" + animName + "/" + animName + ".atlas");

        String tmpPath = currProjectPath + File.separator + "assets/orig/spine-animations/" + animName + "/tmp";
        File tmpFolder = new File(tmpPath);

        createIfNotExist(currProjectPath + File.separator + "assets/" + resolution.name + "/spine-animations/");
        createIfNotExist(currProjectPath + File.separator + "assets/" + resolution.name + "/spine-animations/" + animName);

        String targetPath = currProjectPath + File.separator + "assets/" + resolution.name + "/spine-animations/" + animName;
        File targetFolder = new File(targetPath);

        unpackAtlasIntoTmpFolder(animAtlasFile, tmpPath);
        resizeImagesTmpDirToResolution(animName, tmpFolder, resolution, targetFolder);

        try {
            FileUtils.deleteDirectory(tmpFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createResizedSpriteAnimation(String animName, ResolutionEntryVO resolution) {
        String currProjectPath = currentWorkingPath + File.separator + currentProjectVO.projectName;
        File animAtlasFile = new File(currProjectPath + File.separator + "assets/orig/sprite-animations/" + animName + "/" + animName + ".atlas");

        String tmpPath = currProjectPath + File.separator + "assets/orig/sprite-animations/" + animName + "/tmp";
        File tmpFolder = new File(tmpPath);

        createIfNotExist(currProjectPath + File.separator + "assets/" + resolution.name + "/sprite-animations/");
        createIfNotExist(currProjectPath + File.separator + "assets/" + resolution.name + "/spine-animations/" + animName);

        String targetPath = currProjectPath + File.separator + "assets/" + resolution.name + "/sprite-animations/" + animName;
        File targetFolder = new File(targetPath);

        unpackAtlasIntoTmpFolder(animAtlasFile, tmpPath);
        resizeImagesTmpDirToResolution(animName, tmpFolder, resolution, targetFolder);

        try {
            FileUtils.deleteDirectory(tmpFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createResizedAnimations(ResolutionEntryVO resolution) {
        String currProjectPath = currentWorkingPath + File.separator + currentProjectVO.projectName;

        // Unpack spine orig
        File spineSourceDir = new File(currProjectPath + File.separator + "assets/orig/spine-animations");
        if (spineSourceDir != null && spineSourceDir.exists()) {
            for (File entry : spineSourceDir.listFiles()) {
                if (entry.isDirectory()) {
                    String animName = FilenameUtils.removeExtension(entry.getName());
                    createResizedSpineAnimation(animName, resolution);
                }
            }
        }

        //Unpack sprite orig
        File spriteSourceDir = new File(currProjectPath + File.separator + "assets/orig/sprite-animations");
        if (spriteSourceDir != null && spriteSourceDir.exists()) {
            for (File entry : spriteSourceDir.listFiles()) {
                if (entry.isDirectory()) {
                    String animName = FilenameUtils.removeExtension(entry.getName());
                    createResizedSpriteAnimation(animName, resolution);
                }
            }
        }
    }


    public void resizeSpriteAnimationForAllResolutions(String animName, ProjectInfoVO currentProjectInfoVO) {
        String currProjectPath = currentWorkingPath + File.separator + currentProjectVO.projectName;

        File atlasFile = new File(currProjectPath + File.separator + "assets" + File.separator + "orig" + File.separator + "sprite-animations" + File.separator + animName + File.separator + animName + ".atlas");

        String tmpDir = currProjectPath + File.separator + "assets" + File.separator + "orig" + File.separator + "sprite-animations" + File.separator + animName + File.separator + "tmp";
        File sourceFolder = new File(tmpDir);

        unpackAtlasIntoTmpFolder(atlasFile, tmpDir);

        for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions) {
            String spriteAnimationsRoot = currProjectPath + File.separator + "assets" + File.separator + resolutionEntryVO.name + File.separator + "sprite-animations";
            createIfNotExist(spriteAnimationsRoot);
            String targetPath = spriteAnimationsRoot + File.separator + animName;
            File targetFolder = new File(targetPath);

            resizeImagesTmpDirToResolution(animName, sourceFolder, resolutionEntryVO, targetFolder);
        }

        try {
            FileUtils.deleteDirectory(sourceFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resizeSpineAnimationForAllResolutions(File atlasFile, ProjectInfoVO currentProjectInfoVO) {

        String fileNameWithOutExt = FilenameUtils.removeExtension(atlasFile.getName());
        String tmpDir = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/spine-animations" + File.separator + fileNameWithOutExt + File.separator + "tmp";
        File sourceFolder = new File(tmpDir);

        unpackAtlasIntoTmpFolder(atlasFile, tmpDir);

        for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions) {
            createIfNotExist(currentWorkingPath + "/" + currentProjectVO.projectName + File.separator + "assets" + File.separator + resolutionEntryVO.name + File.separator + "spine-animations");
            String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + File.separator + "assets" + File.separator + resolutionEntryVO.name + File.separator + "spine-animations" + File.separator + fileNameWithOutExt;
            createIfNotExist(targetPath);
            File targetFolder = new File(targetPath);

            resizeImagesTmpDirToResolution(atlasFile.getName(), sourceFolder, resolutionEntryVO, targetFolder);
        }

        try {
            FileUtils.deleteDirectory(sourceFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void resizeSpriterAnimationForAllResolutions(File scmlFile, ProjectInfoVO currentProjectInfoVO) {
    	
    	String fileNameWithOutExt = FilenameUtils.removeExtension(scmlFile.getName());
    	String imagesDir 	= currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/spriter-animations" + File.separator + fileNameWithOutExt;
    	String tmpDir 		= currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/spriter-animations" + File.separator + fileNameWithOutExt + File.separator + "tmp";
    	
    	File imagesFolder = new File(imagesDir);
        File sourceFolder = new File(tmpDir);
       
        try {
			copyOnlyImageFilesIntoTmpFolder(imagesFolder, sourceFolder);
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
    	for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions) {
    		createIfNotExist(currentWorkingPath + "/" + currentProjectVO.projectName + File.separator + "assets" + File.separator + resolutionEntryVO.name + File.separator + "spriter-animations");
    		String targetPath = currentWorkingPath + "/" + currentProjectVO.projectName + File.separator + "assets" + File.separator + resolutionEntryVO.name + File.separator + "spriter-animations" + File.separator + fileNameWithOutExt;
    		createIfNotExist(targetPath);
    		File targetFolder = new File(targetPath);    		
    		resizeSpriterImagesTmpDirToResolution(scmlFile.getName(), sourceFolder, resolutionEntryVO, targetFolder);
    		try {
				FileUtils.copyFileToDirectory(scmlFile, targetFolder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
    		
    	}
    	 try {
             FileUtils.deleteDirectory(sourceFolder);
         } catch (IOException e) {
             e.printStackTrace();
         }

    }

    private void copyOnlyImageFilesIntoTmpFolder(File imagesFolder, File sourceFolder) throws IOException {
		File[] children = imagesFolder.listFiles();
     	for (int i = 0; i < children.length; i++) {
			if(children[i].getName().toLowerCase().endsWith(".png")){
				if (!sourceFolder.exists()) {
					sourceFolder.mkdirs();
					System.out.println("hre");
				}
				FileUtils.copyFileToDirectory(children[i], sourceFolder);				
			}
		}		
	}

	public void rePackProjectImagesForAllResolutions() {
        rePackProjectImages(currentProjectInfoVO.originalResolution);
        for (ResolutionEntryVO resolutionEntryVO : currentProjectInfoVO.resolutions) {
            rePackProjectImages(resolutionEntryVO);
        }
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

        File source = new File("assets/ui");
        if (!(source.exists() && source.isDirectory())) {
            try {
                JarUtils.copyResourcesToDirectory(JarUtils.getThisJar(getClass()), "ui", targetPath);
                TextureManager.getInstance().loadCurrentProjectSkin(targetPath);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File fileTarget = new File(targetPath);
        try {
            FileUtils.copyDirectory(source, fileTarget);
            TextureManager.getInstance().loadCurrentProjectSkin(targetPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void copyDefaultFontIntoProject() {
        System.out.println("here");
        String targetPath = currentWorkingPath + File.separator + currentProjectVO.projectName + File.separator + "assets" + File.separator + "orig" + File.separator + "freetypefonts";
        FileHandle source = null;
        if (OSType.getOS_Type() == OSType.MacOS) {
            source = Gdx.files.internal("freetypefonts");
        } else if (OSType.getOS_Type() == OSType.Windows) {
            source = Gdx.files.internal("assets/freetypefonts");
        } else {
            source = Gdx.files.internal("freetypefonts");
        }
        System.out.println(source.exists() + " " + source.isDirectory());
        if (!source.exists()) {
            try {
                JarUtils.copyResourcesToDirectory(JarUtils.getThisJar(getClass()), "freetypefonts", targetPath);
                //TextureManager.getInstance().loadCurrentProjectSkin(targetPath);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File fileTarget = new File(targetPath);
        try {
            FileUtils.copyDirectory(source.file(), fileTarget);
            //TextureManager.getInstance().loadCurrentProjectSkin(targetPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }


    public void createNewResolution(String name, int width, int height, String resolutionBase, ProgressHandler handler) {

        ResolutionManager resManager = new ResolutionManager(currentProjectVO, currentProjectInfoVO, currentWorkingPath);

        resManager.createNewResolution(name, width, height, resolutionBase, handler);

    }

    public void deleteResolution(int index) {
        ResolutionEntryVO resolutionEntryVO = currentProjectInfoVO.resolutions.remove(index);
        try {
            FileUtils.deleteDirectory(new File(currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + resolutionEntryVO.name));
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        saveCurrentProject();
        String name = stage.sandboxStage.getCurrentSceneVO().sceneName;
        DataManager.getInstance().openProjectAndLoadAllData(DataManager.getInstance().getCurrentProjectVO().projectName, "orig");
        stage.sandboxStage.loadCurrentProject(name);
        stage.loadCurrentProject();
    }

    public String getFreeTypeFontPath() {
        return currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/orig/freetypefonts";
    }

    public void buildProject() {

        String defaultBuildPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/export";
        buildPacks(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            buildPacks(currentProjectVO.projectMainExportPath);
        }
        buildAnimations(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            buildAnimations(currentProjectVO.projectMainExportPath);
        }
        buildParticles(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            buildParticles(currentProjectVO.projectMainExportPath);
        }
        buildFonts(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            buildFonts(currentProjectVO.projectMainExportPath);
        }
        buildStyles(defaultBuildPath);
        buildScenes(defaultBuildPath);
        if (!currentProjectVO.projectMainExportPath.isEmpty()) {
            buildScenes(currentProjectVO.projectMainExportPath);
        }
    }

    private void buildScenes(String targetPath) {
        String srcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/scenes";
        FileHandle scenesDirectoryHandle = Gdx.files.absolute(srcPath);
        File fileTarget = new File(targetPath + "/" + scenesDirectoryHandle.name());
        try {
            FileUtils.copyDirectory(scenesDirectoryHandle.file(), fileTarget);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //copy project dt
        try {
            FileUtils.copyFile(new File(currentWorkingPath + "/" + currentProjectVO.projectName + "/project.dt"), new File(targetPath + "/project.dt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildStyles(String targetPath) {
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

    private void buildParticles(String targetPath) {
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

    private void buildFonts(String targetPath) {
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


    private void buildAnimations(String targetPath) {
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
        createIfNotExist(targetPath + File.separator + res + File.separator + "spine_animations");
        File fileSrc = new File(spineSrcPath);
        String finalTarget = targetPath + File.separator + res + File.separator + "spine_animations";

        File fileTargetSpine = new File(finalTarget);
        try {
            FileUtils.copyDirectory(fileSrc, fileTargetSpine);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void exportSpriteAnimationForResolution(String res, String targetPath) {
        String spineSrcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + res + File.separator + "sprite-animations";
        createIfNotExist(targetPath + File.separator + res + File.separator + "sprite_animations");
        File fileSrc = new File(spineSrcPath);
        String finalTarget = targetPath + File.separator + res + File.separator + "sprite_animations";

        File fileTargetSprite = new File(finalTarget);
        try {
            FileUtils.copyDirectory(fileSrc, fileTargetSprite);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }
    
    private void exportSpriterAnimationForResolution(String res, String targetPath) {
    	String spineSrcPath = currentWorkingPath + "/" + currentProjectVO.projectName + "/assets/" + res + File.separator + "spriter-animations";
    	createIfNotExist(targetPath + File.separator + res + File.separator + "spriter_animations");
    	File fileSrc = new File(spineSrcPath);
    	String finalTarget = targetPath + File.separator + res + File.separator + "spriter_animations";
    	
    	File fileTargetSpriter = new File(finalTarget);
    	try {
    		FileUtils.copyDirectory(fileSrc, fileTargetSpriter);
    	} catch (IOException e) {
    		//e.printStackTrace();
    	}
    }

    private void buildPacks(String targetPath) {
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

    public String getRootPath() {
        File root = new File(new File(".").getAbsolutePath()).getParentFile();
        return root.getAbsolutePath();
    }

    private EditorConfigVO getEditorConfig() {
        EditorConfigVO editorConfig = new EditorConfigVO();
        String configFilePath = getRootPath() + "/" + EditorConfigVO.EDITOR_CONFIG_FILE;
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            writeToFile(configFilePath, editorConfig.constructJsonString());
        } else {
            Json gson = new Json();
            String editorConfigJson = readFileContents(Gdx.files.absolute(configFilePath));
            editorConfig = gson.fromJson(EditorConfigVO.class, editorConfigJson);
        }
        return editorConfig;
    }

    public void deleteCurrentScene() {
        if (currentProjectVO.lastOpenScene.equals("MainScene")) {
            return;
        }
        deleteScene(currentProjectVO.lastOpenScene);
    }

    private void deleteScene(String sceneName) {
        ArrayList<SceneVO> scenes = currentProjectInfoVO.scenes;
        SceneVO sceneToDelete = null;
        for (SceneVO scene : scenes) {
            if (scene.sceneName.equals(sceneName)) {
                sceneToDelete = scene;
                break;
            }
        }
        if (sceneToDelete != null) {
            scenes.remove(sceneToDelete);
        }
        currentProjectInfoVO.scenes = scenes;
        String projPath = currentWorkingPath + "/" + currentProjectVO.projectName;
        writeToFile(projPath + "/project.dt", currentProjectInfoVO.constructJsonString());
        try {
            FileUtils.forceDelete(new File(projPath + "/scenes/" + sceneName + ".dt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
