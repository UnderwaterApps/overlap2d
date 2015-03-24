package com.uwsoft.editor.view;

import com.uwsoft.editor.data.manager.DataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuToolbar extends JMenuBar {

    /**
     *
     */
    private static final long serialVersionUID = 8002237307491647143L;
    public final JMenu sceneMenu;
    public JMenuItem createNewSceneBtn;
    public JMenuItem createProjectBtn;
    public JMenuItem openProjectBtn;
    public JMenuItem saveProjectBtn;
    public JMenuItem importBtn;
    public JMenuItem cutBtn;
    public JMenuItem copyBtn;
    public JMenuItem pasteBtn;
    public JMenuItem undoBtn;
    public JMenuItem redoBtn;
    public JMenuItem prefBtn;
    public JMenuItem exportBtn;
    public JMenuItem exportSettingsBtn;
    public JMenu editItems;
    public JMenuItem exitBtn;
    public JMenuItem triggerBtn;
    public JMenuItem deleteCurrentSceneBtn;


    public MenuToolbar() {
        JMenu fileItems = new JMenu("File");
        editItems = new JMenu("Edit");
        editItems.setEnabled(false);
        JMenu tools = new JMenu("Tools");
        JMenu help = new JMenu("Help");

        createProjectBtn = new JMenuItem("New Project");
        createProjectBtn.setEnabled(true);
        openProjectBtn = new JMenuItem("Open Project");
        openProjectBtn.setEnabled(true);
        saveProjectBtn = new JMenuItem("Save Project");
        saveProjectBtn.setEnabled(false);

        sceneMenu = new JMenu("Scene");
        sceneMenu.setEnabled(false);


        importBtn = new JMenuItem("Import to Library");
        importBtn.setEnabled(false);
        exportBtn = new JMenuItem("Export");
        exportBtn.setEnabled(false);
        exportSettingsBtn = new JMenuItem("Export Settings");
        exportSettingsBtn.setEnabled(false);

        cutBtn = new JMenuItem("Cut");
        cutBtn.setEnabled(true);
        cutBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        copyBtn = new JMenuItem("Copy");
        copyBtn.setEnabled(true);
        copyBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        pasteBtn = new JMenuItem("Paste");
        pasteBtn.setEnabled(true);
        pasteBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        undoBtn = new JMenuItem("Undo");
        undoBtn.setEnabled(true);
        undoBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        redoBtn = new JMenuItem("Redo");
        pasteBtn.setEnabled(true);
        redoBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        prefBtn = new JMenuItem("Preferences");
        prefBtn.setEnabled(true);

        exitBtn = new JMenuItem("Exit");
        saveProjectBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        exportBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        exportSettingsBtn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.EXIT_GDX);
                //UIController.instance.glApplication.stop();
                System.exit(0);
            }

        });

        undoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.UNDO);
            }

        });

        redoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.REDO);
            }

        });

        cutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.CUT_ACTION);
            }

        });

        copyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.COPY_ACTION);
            }

        });

        pasteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.PASTE_ACTION);
            }

        });


        createProjectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // UIController.instance.sendNotification(NameConstants.CREATE_PROJECT);
            }

        });

        openProjectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //Create a file chooser
                final JFileChooser fc = new JFileChooser(DataManager.getInstance().getWorkspacePath());
                // fc.showOpenDialog(UIController.instance.frame);
                if (fc.getSelectedFile() == null) {
                    return;
                }
                String dirpath = fc.getSelectedFile().getAbsolutePath().toString();
                if (dirpath != null && dirpath.length() > 0) {
                    //UIController.instance.sendNotification(NameConstants.OPEN_PROJECT, dirpath);
                }
            }

        });

        saveProjectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.SAVE_PROJECT);
            }

        });

        importBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.IMPORT_DIALOG_SHOW);
            }

        });


        exportSettingsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.SHOW_BUILD_SETTING);
            }

        });

        exportBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.BUILD_PROJECT);
            }

        });


        fileItems.add(createProjectBtn);
        fileItems.add(openProjectBtn);
        fileItems.add(saveProjectBtn);
        fileItems.add(sceneMenu);
        fileItems.add(importBtn);
        fileItems.add(exportBtn);
        fileItems.add(exportSettingsBtn);
        fileItems.add(exitBtn);

        editItems.add(cutBtn);
        editItems.add(copyBtn);
        editItems.add(pasteBtn);
        editItems.add(undoBtn);
        editItems.add(redoBtn);
        //editItems.add(prefBtn);


        //Editors.add(triggerBtn);
        //Editors.add(itemEditor);

        //Tools.add(obstacle);
        //Tools.add(particleList);

        add(fileItems);
        add(editItems);
        // add(tools);
        // add(help);


        this.setBackground(new java.awt.Color(83, 83, 83));
    }

    public void addScene(final String sceneName) {
        JMenuItem sceneMenuItem = new JMenuItem(sceneName);
        sceneMenu.add(sceneMenuItem);
        sceneMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.LOAD_SCENE, sceneName);
            }
        });
    }

    public void initScenesMenu() {
        sceneMenu.removeAll();
        createNewSceneBtn = new JMenuItem("Create New Scene");
        deleteCurrentSceneBtn = new JMenuItem("Delete Current Scene");
        createNewSceneBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // UIController.instance.sendNotification(NameConstants.CRATE_NEW_SCENE);
            }
        });
        deleteCurrentSceneBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //UIController.instance.sendNotification(NameConstants.DELETE_CURRENT_SCENE);
            }
        });
        sceneMenu.add(createNewSceneBtn);
        sceneMenu.add(deleteCurrentSceneBtn);
        sceneMenu.addSeparator();
    }

	/*
    public void setSaveActive() {
		saveLvlBtn.setEnabled(true);
		saveLvlAsBtn.setEnabled(true);
	}
	
	public void setWorldEditorActive() {
		worldPropBtn.setEnabled(true);
		aiMsgs.setEnabled(true);
	}
	*/

}
