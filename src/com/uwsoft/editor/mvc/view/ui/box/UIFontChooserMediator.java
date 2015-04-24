package com.uwsoft.editor.mvc.view.ui.box;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;

import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.proxy.FontManager;


/**
 * Created by CyberJoe on 4/22/2015.
 */
public class UIFontChooserMediator extends SimpleMediator<UIFontChooser> {
    private static final String TAG = UIFontChooserMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    private FontManager fontManager;

    public UIFontChooserMediator() {
        super(NAME, new UIFontChooser());

    }

    @Override
    public void onRegister() {
        facade = Overlap2DFacade.getInstance();
        fontManager = facade.retrieveProxy(FontManager.NAME);
    }


    @Override
    public String[] listNotificationInterests() {
        return new String[]{
                ProjectManager.PROJECT_OPENED,
                UIFontChooser.FONT_SELECTED
        };
    }

    @Override
    public void handleNotification(Notification notification) {
        super.handleNotification(notification);

        switch (notification.getName()) {
            case ProjectManager.PROJECT_OPENED:
                generateFontList();
                break;
            case UIFontChooser.FONT_SELECTED:
                fontSelected(viewComponent.getSelectedFont());
                break;
            default:
                break;
        }
    }

    private void generateFontList() {
        viewComponent.setSelectBoxItems(fontManager.getFontNamesFromMap());
    }

    private void fontSelected(String fontName) {

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontManager.getTTFByName(fontName));
        BitmapFont font = generator.generateFont(parameter);

        TextureManager textureManager = Overlap2DFacade.getInstance().retrieveProxy(TextureManager.NAME);
        textureManager.addBitmapFont(fontManager.getShortName(fontName), parameter.size, font);

    }

    public String getCurrentFont() {
        return fontManager.getShortName(viewComponent.getSelectedFont());
    }
}
