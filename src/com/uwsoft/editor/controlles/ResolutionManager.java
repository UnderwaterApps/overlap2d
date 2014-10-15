package com.uwsoft.editor.controlles;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import com.uwsoft.editor.data.manager.TextureManager;
import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.files.FileHandle;
import com.mortennobel.imagescaling.ResampleOp;
import com.uwsoft.editor.data.manager.DataManager;
import com.uwsoft.editor.data.vo.ProjectVO;
import com.uwsoft.editor.gdx.ui.ProgressHandler;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.utils.NinePatchUtils;

public class ResolutionManager {
    private static final String EXTENSION_9PATCH = ".9.png";
    private String mainPath;
    private ProjectVO projectVo;
    private ProjectInfoVO projectInfoVo;

    private float currentPercent = 0.0f;

    private ProgressHandler handler;

    public ResolutionManager(ProjectVO projectVo, ProjectInfoVO projectInfoVo, String mainPath) {
        this.mainPath = mainPath;
        this.projectVo = projectVo;
        this.projectInfoVo = projectInfoVo;
    }

    public static BufferedImage imageResize(File file, float ratio) {
        BufferedImage destinationBufferedImage = null;
        try {
            BufferedImage sourceBufferedImage = ImageIO.read(file);
            if (ratio == 1.0) {
                return sourceBufferedImage;
            }
            int newWidth = Math.max(3, Math.round(sourceBufferedImage.getWidth() * ratio));
            int newHeight = Math.max(3, Math.round(sourceBufferedImage.getHeight() * ratio));
            String name = file.getName();
            Integer[] patches = null;
            if (name.endsWith(EXTENSION_9PATCH)) {
                patches = NinePatchUtils.findPatches(sourceBufferedImage);
                sourceBufferedImage = NinePatchUtils.removePatches(sourceBufferedImage);                


                newWidth = Math.round(sourceBufferedImage.getWidth() * ratio);
                newHeight = Math.round(sourceBufferedImage.getHeight() * ratio);
                System.out.println(sourceBufferedImage.getWidth());
                
                destinationBufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = destinationBufferedImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g2.drawImage(sourceBufferedImage, 0, 0, newWidth, newHeight, null);
                g2.dispose();
                
            } else {
            	// resize with bilinear filter
            	ResampleOp resampleOp = new ResampleOp(newWidth, newHeight);
                destinationBufferedImage = resampleOp.filter(sourceBufferedImage, null);
            }
        	
            
            if (patches != null) {
                destinationBufferedImage = NinePatchUtils.convertTo9Patch(destinationBufferedImage, patches, ratio);
            }

        } catch (IOException ignored) {

        }
        
        
        return destinationBufferedImage;
    }


    public static float getResolutionRatio(ResolutionEntryVO resolution, ResolutionEntryVO originalResolution) {
        float a;
        float b;
        switch (resolution.base) {
            default:
            case 0:
                a = resolution.width;
                b = originalResolution.width;
                break;
            case 1:
                a = resolution.height;
                b = originalResolution.height;
                break;
        }
        return a / b;
    }

//    private static BufferedImage convertTo9Patch(BufferedImage image) {

//    }

    private void changePercentBy(float value) {
        currentPercent += value;
        handler.progressChanged(currentPercent);
    }

    public void createNewResolution(String name, int width, int height, final String resolutionBase, final ProgressHandler handler) {
        this.handler = handler;
        final ResolutionEntryVO newResolution = new ResolutionEntryVO();
        newResolution.name = name;
        newResolution.width = width;
        newResolution.height = height;
        newResolution.base = resolutionBase.equals("width") ? 0 : 1;
        projectInfoVo.resolutions.add(newResolution);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // create new folder structure
                String projPath = mainPath + "/" + projectVo.projectName;
                String sourcePath = projPath + "/" + "assets/orig/images";
                String targetPath = projPath + "/" + "assets/" + newResolution.name + "/images";
                createIfNotExist(sourcePath);
                createIfNotExist(projPath + "/" + "assets/" + newResolution.name + "/pack");
                copyTexturesFromTo(sourcePath, targetPath);
                resizeTextures(targetPath, newResolution);
                DataManager.getInstance().rePackProjectImages(newResolution);
                DataManager.getInstance().createResizedAnimations(newResolution);
                changePercentBy(5);
            }
        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataManager.getInstance().saveCurrentProject();
                handler.progressComplete();
            }
        });
        executor.shutdown();
    }

    private void resizeTextures(String path, ResolutionEntryVO resolution) {
        float ratio = getResolutionRatio(resolution, projectInfoVo.originalResolution);
        FileHandle targetDir = new FileHandle(path);
        FileHandle[] entries = targetDir.list(new DataManager.PngFilenameFilter());
        float perResizePercent = 95.0f / entries.length;
        for (FileHandle entry : entries) {
            try {
                File file = entry.file();
                File destinationFile = new File(path + "/" + file.getName());
                System.out.println(destinationFile);
                ImageIO.write(ResolutionManager.imageResize(file, ratio), "png", destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            changePercentBy(perResizePercent);
        }
    }

    private void copyTexturesFromTo(String fromPath, String toPath) {
        FileHandle sourceDir = new FileHandle(fromPath);
        FileHandle[] entries = sourceDir.list(new DataManager.PngFilenameFilter());
        float perCopyPercent = 10.0f / entries.length;
        for (FileHandle entry : entries) {
            File file = entry.file();
            String filename = file.getName();
            File target = new File(toPath + "/" + filename);
            try {
                FileUtils.copyFile(file, target);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        changePercentBy(perCopyPercent);
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
        else return null;
    }
}

