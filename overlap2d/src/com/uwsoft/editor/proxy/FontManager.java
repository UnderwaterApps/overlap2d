package com.uwsoft.editor.proxy;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.puremvc.patterns.proxy.BaseProxy;
import com.uwsoft.editor.Overlap2DFacade;

/**
 * Created by azakhary on 4/24/2015.
 */
public class FontManager extends BaseProxy {

    private static final String TAG = FontManager.class.getCanonicalName();
    public static final String NAME = TAG;

    private static final String cache_name = "overlap2d-fonts-cache";

    private Preferences prefs;

    private HashMap<String, String> systemFontMap = new HashMap<>();

    public FontManager() {
        super(NAME);
    }

    @Override
    public void onRegister() {
        super.onRegister();
        facade = Overlap2DFacade.getInstance();
        prefs = Gdx.app.getPreferences(cache_name);
        generateFontsMap();
    }

    public String[] getSystemFontNames() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    }

    public String[] getSystemFontsPaths() {
        String[] result;
        if (SystemUtils.IS_OS_WINDOWS) {
            result = new String[1];
            String path = System.getenv("WINDIR");
            result[0] = path + "\\" + "Fonts";
            return result;
        } else if (SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_MAC) {
            result = new String[3];
            result[0] = System.getProperty("user.home") + File.separator + "Library/Fonts";
            result[1] = "/Library/Fonts";
            result[2] = "/System/Library/Fonts";
            return result;
        } else if (SystemUtils.IS_OS_LINUX) {
            String[] pathsToCheck = {
                    System.getProperty("user.home") + File.separator + ".fonts",
                    "/usr/share/fonts/truetype",
                    "/usr/share/fonts/TTF"
            };
            ArrayList<String> resultList = new ArrayList<>();

            for (int i = pathsToCheck.length - 1; i >= 0; i--) {
                String path = pathsToCheck[i];
                File tmp = new File(path);
                if (tmp.exists() && tmp.isDirectory() && tmp.canRead()) {
                    resultList.add(path);
                }
            }

            if (resultList.isEmpty()) {
                // TODO: show user warning, TextTool will be crash editor, because system font directories not found
                result = new String[0];
            }
            else {
                result = new String[resultList.size()];
                result = resultList.toArray(result);
            }

            return result;
        }

        return null;
    }

    public List<File> getSystemFontFiles() {
        // only retrieving ttf files
        String[] extensions = new String[]{"ttf", "TTF"};
        String[] paths = getSystemFontsPaths();

        ArrayList<File> files = new ArrayList<>();

        for (int i = 0; i < paths.length; i++) {
            File fontDirectory = new File(paths[i]);
            if (!fontDirectory.exists()) break;
            files.addAll(FileUtils.listFiles(fontDirectory, extensions, true));
        }

        return files;
    }

    public void preCacheSystemFontsMap() {
        List<File> fontFiles = getSystemFontFiles();

        for (File file : fontFiles) {
            Font f = null;
            try {
                if (!systemFontMap.containsValue(file.getAbsolutePath())) {
                    f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(file.getAbsolutePath()));
                    String name = f.getFamily();
                    systemFontMap.put(name, file.getAbsolutePath());
                }
            } catch (FontFormatException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        prefs.put(systemFontMap);
        prefs.flush();
    }

    public void loadCachedSystemFontMap() {
        systemFontMap = (HashMap<String, String>) prefs.get();
    }

    public void invalidateFontMap() {
        Array<String> names = new Array<>(getSystemFontNames());
        for (Iterator<Map.Entry<String, String>> it = systemFontMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> entry = it.next();
            if (!names.contains(entry.getKey(), false)) {
                it.remove();
            }
        }
    }

    public void generateFontsMap() {
        loadCachedSystemFontMap();
        preCacheSystemFontsMap();
        invalidateFontMap();
    }

    public HashMap<String, String> getFontsMap() {
        return systemFontMap;
    }

    public Array<String> getFontNamesFromMap() {
        AlphabeticalComparator comparator = new AlphabeticalComparator();
        Array<String> fontNames = new Array<>();

        for (Map.Entry<String, String> entry : systemFontMap.entrySet()) {
            fontNames.add(entry.getKey());
        }
        fontNames.sort(comparator);

        return fontNames;
    }

    public FileHandle getTTFByName(String fontName) {
        return new FileHandle(systemFontMap.get(fontName));
    }

    public String getShortName(String longName) {
        String path = systemFontMap.get(longName);
        return FilenameUtils.getBaseName(path);
    }

    public String getFontFilePath(String fontFaily) {
        return systemFontMap.get(fontFaily);
    }


    public class AlphabeticalComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }
}
