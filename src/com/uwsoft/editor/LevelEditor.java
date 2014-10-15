package com.uwsoft.editor;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.uwsoft.editor.controlles.CustomExceptionHandler;
import com.uwsoft.editor.controlles.UIController;
import com.uwsoft.editor.gdx.LunarEditorListener;
import com.uwsoft.editor.utils.AppConfig;
import com.uwsoft.editor.utils.OSType;

import java.util.Properties;


public class LevelEditor {

	private static LunarEditorListener listener;

	
	public static void main(String[] argv) {
        //Get project version
        System.out.println("Current Build Version: " + AppConfig.getInstance().version);

//		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());

		listener = new LunarEditorListener();
		if(OSType.getOS_Type() == OSType.MacOS) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Overlap2D");
		}

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final UIController uc = new UIController(listener);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				

				if(OSType.getOS_Type() == OSType.MacOS) {
					uc.initMacOSFrame();
				}else{
					uc.initOtherOSFrame();
				}
				//uc.initUI();
				//uc.addMainComponent();
			}
		});
	}
	
//	public static int calc (){
//		int t = 2;
//	      int d = "QCL".hashCode() % 3000;
//	      int f = "KOM".hashCode() % 3000;
//	      for (int h = 0; h <= f; h++)
//	         t = (t ^ h) % d;
//	      return t;
//	}
	
	
}
