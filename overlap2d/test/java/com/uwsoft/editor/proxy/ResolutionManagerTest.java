package com.uwsoft.editor.proxy;

import com.google.common.io.Resources;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ResolutionManagerTest {
    private ResolutionManager resolutionManager;

    @Before
    public void setUp() throws Exception {
        resolutionManager = new ResolutionManager();
        resolutionManager.onRegister();
    }

    @Test
    public void shouldResizeImage() throws Exception {
        File targetFile = new File(Resources.getResource("images/logo.png").getFile());
        BufferedImage originImage = ImageIO.read(targetFile);
        int width = originImage.getWidth();
        int height = originImage.getHeight();

        BufferedImage bigImage = ResolutionManager.imageResize(targetFile, 2);
        assertThat(bigImage.getWidth(), is(width * 2));
        assertThat(bigImage.getHeight(), is(height * 2));

        BufferedImage smallImage = ResolutionManager.imageResize(targetFile, 0.5f);
        assertThat(smallImage.getWidth() * 2, is(width));
        assertThat(smallImage.getHeight() * 2, is(height));
    }

    @Test
    public void shouldGetResolutionRatioForBaseZero() throws Exception {
        ResolutionEntryVO resolution = new ResolutionEntryVO();
        ResolutionEntryVO originalResolution = new ResolutionEntryVO();
        resolution.width = 800;
        resolution.height = 600;
        originalResolution.width = 1024;
        originalResolution.height = 768;
        float resolutionRatio = ResolutionManager.getResolutionRatio(resolution, originalResolution);

        assertThat(resolutionRatio, is(0.78125f));
    }

    @Test
    public void shouldGetResolutionRatioForBaseOne() throws Exception {
        ResolutionEntryVO resolution = new ResolutionEntryVO();
        ResolutionEntryVO originalResolution = new ResolutionEntryVO();
        resolution.base = 1;
        resolution.width = 800;
        resolution.height = 600;
        originalResolution.width = 1024;
        originalResolution.height = 800;
        float resolutionRatio = ResolutionManager.getResolutionRatio(resolution, originalResolution);

        assertThat(resolutionRatio, is(0.75F));
    }
}