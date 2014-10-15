package com.uwsoft.editor.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JWindow;

import com.uwsoft.editor.tools.ImagePanel;

public class EditorSplashScreen extends JWindow {
	
	public EditorSplashScreen() {
		super();
		JPanel content = (JPanel) getContentPane();
	    content.setBackground(Color.white);

	    // Set the window's bounds, centering the window
	    int width = 430;
	    int height = 286;
	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (screen.width - width) / 2;
	    int y = (screen.height - height) / 2;
	    setBounds(x, y, width, height);

	    ImagePanel img = new ImagePanel();
	    img.setImage("editor_assets/splash.jpg");
	    content.add(img);
	}

}
