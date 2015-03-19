package com.uwsoft.editor.controlles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.jglfw.JglfwApplication;
import com.badlogic.gdx.backends.jglfw.JglfwApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;
import com.uwsoft.editor.data.EpisodeData;
import com.uwsoft.editor.gdx.Overlap2DListener;
import com.uwsoft.editor.utils.AppConfig;
import com.uwsoft.editor.view.EditorSplashScreen;
import com.uwsoft.editor.view.MenuToolbar;
import com.uwsoft.editor.view.WorkingArea;

import javax.swing.*;
import java.awt.*;

public class UIController extends Facade {

    public static UIController instance;

    public JFrame frame;
    public MenuToolbar menuTooolbar;
    public EditorSplashScreen splash;
    public EpisodeData currEpisode;
    public boolean isBgMode = false;
    public Application glApplication;
    private WorkingArea componentPane;
    private Overlap2DListener listener;


    public UIController(Overlap2DListener listener) {
        instance = this;
        this.listener = listener;
        SwingApiProvider sap = new SwingApiProvider();
        UIController.instance.addObserver(sap);

    }


    public void initMacOSFrame() {
        frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setTitle("Overlap2D - Public Alpha v"+ AppConfig.getInstance().version);
//
        JPanel mainPanel = new JPanel();

        frame.add(mainPanel);

        mainPanel.setLayout(new BorderLayout());
        frame.getContentPane().add(mainPanel);
        componentPane = new WorkingArea();
        mainPanel.add(componentPane, BorderLayout.CENTER);
        //
        menuTooolbar = new MenuToolbar();
        frame.setJMenuBar(menuTooolbar);
        frame.validate();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Rectangle maximumWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                double width = maximumWindowBounds.getWidth();
                double height = maximumWindowBounds.getHeight();

                int compWidth = (int) width;
                int compHeight = (int) height;
//
//                glApplication = new LwjglAWTCanvas(listener);
//
//                glApplication.getCanvas().setSize(compWidth, compHeight);
//
//                componentPane.add(glApplication.getCanvas(), BorderLayout.CENTER);
//
//                componentPane.validate();
//                frame.validate();
//                frame.setVisible(true);


                final JglfwApplicationConfiguration config = new JglfwApplicationConfiguration();
                config.width = compWidth;
                config.height = compHeight;
                config.title = frame.getTitle();
                config.runOnEDT = true;

//                config.forceExit = false;
                JglfwApplication jglfwApplication = new JglfwApplication(listener, config);
                jglfwApplication.addLifecycleListener(new LifecycleListener() {
                    @Override
                    public void pause() {

                    }

                    @Override
                    public void resume() {

                    }

                    @Override
                    public void dispose() {
                        frame.dispose();
                    }
                });
                frame.setVisible(true);
                frame.setSize(0, 0);
                frame.validate();
                glApplication = jglfwApplication;

                //System.out.println(String.valueOf(jglfwApplication.getGraphics()));


            }
        });

    }

    public void initOtherOSFrame() {
        frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setTitle("Overlap2D - Public Alpha v"+AppConfig.getInstance().version);

        menuTooolbar = new MenuToolbar();
        frame.setJMenuBar(menuTooolbar);
        LwjglAWTCanvas lwjglAWTCanvas = new LwjglAWTCanvas(listener);
//		 try {
//	            Thread.sleep(2000);
//	        }catch (Exception e ){
//	        	e.printStackTrace();
//	        }
        //listener = new LunarEditorListener(compWidth,compHeight);
//		SwingUtilities.invokeLater(new Runnable() {
//
//			@Override
//			public void run() { 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        int compWidth = (int) width;
        int compHeight = (int) height;

        lwjglAWTCanvas.getCanvas().setSize(compWidth, compHeight);

        frame.getContentPane().add(lwjglAWTCanvas.getCanvas());


        frame.pack();
        frame.setVisible(true);
        frame.setSize(compWidth, compHeight);
        glApplication = lwjglAWTCanvas;
        //}

        //});

        //frame.validate();
    }

    public int showConfirmDialog(String text) {
        int result = JOptionPane.showConfirmDialog((Component) null, text,
                "Please Confirm", JOptionPane.OK_CANCEL_OPTION);
        return result;
    }
}
