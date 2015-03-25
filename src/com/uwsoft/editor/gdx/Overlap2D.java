package com.uwsoft.editor.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.kotcrab.vis.ui.VisUI;
import com.uwsoft.editor.data.manager.TextureManager;
import com.uwsoft.editor.gdx.screen.Overlap2DScreen;

import java.util.ArrayList;

public class Overlap2D extends ApplicationAdapter {

    public static final int TOOL_SELECT_MESSAGE = 1;

    public Screen currentScreen;

    public TextureManager textureManager;
    //public Music menuMusic = null;
    private ArrayList<Runnable> runnables = new ArrayList<Runnable>();

    public Overlap2D() {

    }

    public void create() {
        VisUI.load();
        currentScreen = new Overlap2DScreen();

//        UIController.instance.addObserver(editor);
//		} catch (final Exception e) {
//			SwingUtilities.invokeLater(new Runnable() {
//				@Override
//				public void run () { 
//					JOptionPane.showMessageDialog(null, "Some stupid error occurred please be patient and we will try to fix it","Stupid Error",JOptionPane.ERROR_MESSAGE);
//					//JOptionPane.showMessageDialog(null, e.getStackTrace(),e.getMessage(),JOptionPane.ERROR_MESSAGE);
//				}
//			});
//			
//			e.printStackTrace();
//		}

        //Gdx.graphics.setVSync(true);
    }

    public void setScreen(final Screen screen) {
        if (currentScreen != null) {
            currentScreen.pause();
            currentScreen.dispose();
        }
        currentScreen = null;
        currentScreen = screen;
        // currentScreen.resume();
    }


    public void pause() {
        if (currentScreen == null) {
            return;
        }
        currentScreen.pause();
    }

    public void resume() {
        if (currentScreen == null) {
            return;
        }
        currentScreen.resume();
    }

    public void render() {
        if (currentScreen == null) {
            return;
        }
        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void addOnGameThread(Runnable r) {
        runnables.add(r);
    }

    public void resize(int width, int height) {
        if (currentScreen == null) {
            return;
        }
        currentScreen.resize(width, height);
    }

    public void dispose() {
        VisUI.dispose();
        if (currentScreen == null) {
            return;
        }
        currentScreen.dispose();
    }

    public void sendMessage(int msg, String value) {
        switch (msg) {
            case 1: {

            }
            break;

            case 2: {
//				if(value.equalsIgnoreCase(LeftToolbar.SELECT_TOOL)){
//						
//				}
            }
            break;

            default:
                break;
        }
    }

}