package com.uwsoft.editor.view.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

/**
 * Created by hayk on 5/22/2015.
 */
public class CustomMenuBar {
    private static final Drawable BUTTTON_DEFAULT = VisUI.getSkin().getDrawable("menu-bg-up");
    public    Drawable defaultStyle = VisUI.getSkin().getDrawable("menu-bg-up");

    private Table mainTable;
    private Table menuItems;

    private CustomMenu currentMenu;

    private Array<CustomMenu> menus = new Array<CustomMenu>();

    public CustomMenuBar () {
        Skin skin = VisUI.getSkin();

        menuItems = new VisTable();

        mainTable = new VisTable() {
            @Override
            protected void sizeChanged () {
                super.sizeChanged();
                closeMenu();
            }
        };

        mainTable.left();
        mainTable.add(menuItems);
        mainTable.setBackground(skin.getDrawable("menu-bg"));
    }

    public void addMenu (CustomMenu menu) {
        menus.add(menu);
        menu.setMenuBar(this);
        menuItems.add(menu.getOpenButton());
    }

    public boolean removeMenu (CustomMenu menu) {
        boolean removed = menus.removeValue(menu, true);

        if (removed) {
            menu.setMenuBar(null);
            menuItems.removeActor(menu.getOpenButton());
        }

        return removed;
    }

    public void insertMenu (int index, CustomMenu menu) {
        menus.insert(index, menu);
        menu.setMenuBar(this);
        rebuild();
    }

    private void rebuild () {
        menuItems.clear();
        for (CustomMenu menu : menus)
            menuItems.add(menu.getOpenButton());
    }

    /** Closes currently open menu (if any). Used by framework and typically there is no need to call this manually */
    public void closeMenu () {
        if (currentMenu != null) {
            deselectButton(currentMenu.getOpenButton());
            currentMenu.remove();
            currentMenu = null;
        }
    }

    CustomMenu getCurrentMenu () {
        return currentMenu;
    }

    void setCurrentMenu (CustomMenu newMenu) {
        if (newMenu != null) selectButton(newMenu.getOpenButton());
        if (currentMenu != null) deselectButton(currentMenu.getOpenButton());
        currentMenu = newMenu;
    }

    /** Returns table containing all menus that should be added to Stage, typically with expandX and fillX properties. */
    public Table getTable () {
        return mainTable;
    }

    public void selectButton (TextButton button) {
        button.getStyle().up = button.getStyle().over;
    }

    public void deselectButton (TextButton button) {
        button.getStyle().up = defaultStyle;
    }
}

