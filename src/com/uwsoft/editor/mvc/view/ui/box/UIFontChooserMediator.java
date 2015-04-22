package com.uwsoft.editor.mvc.view.ui.box;

import com.badlogic.gdx.tools.hiero.unicodefont.UnicodeFont;
import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.mediator.SimpleMediator;
import com.puremvc.patterns.observer.Notification;
import com.uwsoft.editor.Overlap2D;
import com.uwsoft.editor.gdx.sandbox.Sandbox;
import com.uwsoft.editor.mvc.proxy.ProjectManager;

import java.awt.*;

/**
 * Created by CyberJoe on 4/22/2015.
 */
public class UIFontChooserMediator extends SimpleMediator<UIFontChooser> {
    private static final String TAG = UIFontChooserMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIFontChooserMediator() {
        super(NAME, new UIFontChooser());
    }

    @Override
    public void onRegister() {
        // get system fonts
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Array<String> fontList = new Array<>(fonts);
        viewComponent.setSelectBoxItems(fontList);
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
        UnicodeFont unicodeFont = new UnicodeFont(Font.decode(fontName), 12, false, false);
    }
}
