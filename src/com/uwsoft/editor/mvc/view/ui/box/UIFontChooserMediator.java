package com.uwsoft.editor.mvc.view.ui.box;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.tools.hiero.BMFontUtil;
import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
import com.badlogic.gdx.tools.hiero.unicodefont.effects.ColorEffect;
import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;

import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.TextureManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;


/**
 * Created by CyberJoe on 4/22/2015.
 */
public class UIFontChooserMediator extends SimpleMediator<UIFontChooser> {
    private static final String TAG = UIFontChooserMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIFontChooserMediator() {
        super(NAME, new UIFontChooser());

        // get system fonts
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Array<String> fontList = new Array<>(fonts);
        viewComponent.setSelectBoxItems(fontList);
    }

    @Override
    public void onRegister() {
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
                break;
            case UIFontChooser.FONT_SELECTED:
                fontSelected(viewComponent.getSelectedFont());
                break;
            default:
                break;
        }
    }

    private void fontSelected(String fontName) {
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz\\n1234567890\\\"!`?'.,;:()[]{}<>|/@\\\\^$-%+=#_&~*\\u007F";

        String tempDir = System.getProperty("java.io.tmpdir");

        UnicodeFont unicodeFont = new UnicodeFont(Font.decode(fontName), parameter.size, false, false);
        fontName = fontName.replaceAll(" ", "");
        unicodeFont.addGlyphs(parameter.characters);
        ColorEffect colorEffect = new ColorEffect();
        colorEffect.setColor(new Color(1, 1, 1, 1));
        unicodeFont.getEffects().add(colorEffect);
        unicodeFont.setPaddingTop(2);
        unicodeFont.setPaddingRight(2);
        unicodeFont.setPaddingBottom(2);
        unicodeFont.setPaddingLeft(2);

        unicodeFont.setNativeRendering(true);
        BMFontUtil util = new BMFontUtil(unicodeFont);
        File fontFile = new File(tempDir + "\\" + fontName);
        try {
            util.save(fontFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BitmapFont font = new BitmapFont(new FileHandle(fontFile.getAbsolutePath() + ".fnt"));

        TextureManager textureManager = Overlap2DFacade.getInstance().retrieveProxy(TextureManager.NAME);
        textureManager.addBitmapFont(fontName, parameter.size, font);
    }

    public String getCurrentFont() {
        return viewComponent.getSelectedFont().replaceAll(" ", "");
    }
}
