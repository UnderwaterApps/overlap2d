package com.uwsoft.editor.tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    private BufferedImage image;

    public ImagePanel() {

    }
    
    public void setImage(String img) {
        try {                
            image = ImageIO.read(new File(img));
         } catch (IOException ex) {
              // handle exception
         }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null) {
        	g.drawImage(image, 0, 0, null);
        }
    }

}
