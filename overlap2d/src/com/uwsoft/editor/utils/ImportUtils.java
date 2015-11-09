/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by azakhary on 7/22/2015.
 */
public class ImportUtils  {

    private static ImportUtils instance;

    public static final int TYPE_MIXED = -2;
    public static final int TYPE_UNCKNOWN = -1;
    public static final int TYPE_UNSUPPORTED = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_ANIMATION_PNG_SEQUENCE = 2;
    public static final int TYPE_SPRITE_ANIMATION_ATLAS = 3;
    public static final int TYPE_SPINE_ANIMATION = 4;
    public static final int TYPE_SPRITER_ANIMATION = 5;
    public static final int TYPE_TTF_FONT = 6;
    public static final int TYPE_BITMAP_FONT = 7;
    public static final int TYPE_PARTICLE_EFFECT = 8;
    public static final int TYPE_TEXTURE_ATLAS = 9;
    public static final int TYPE_SHADER = 10;

    private ArrayList<Integer> supportedTypes = new ArrayList<>();

    private ImportUtils() {
        supportedTypes.add(TYPE_IMAGE);
        supportedTypes.add(TYPE_ANIMATION_PNG_SEQUENCE);
        supportedTypes.add(TYPE_SPRITE_ANIMATION_ATLAS);
        supportedTypes.add(TYPE_SPINE_ANIMATION);
        supportedTypes.add(TYPE_SPRITER_ANIMATION);
        supportedTypes.add(TYPE_PARTICLE_EFFECT);
        supportedTypes.add(TYPE_SHADER);
        // TODO: not yet supported
        //supportedTypes.add(TYPE_TEXTURE_ATLAS);
        //supportedTypes.add(TYPE_TTF_FONT);
        //supportedTypes.add(TYPE_BITMAP_FONT);
    }

    public static ImportUtils getInstance() {
        if(instance == null) {
            instance = new ImportUtils();
        }

        return instance;
    }

    public static int getImportType(String[] paths) {
        int mainType = TYPE_MIXED;
        String[] names = new String[paths.length];
        for(int i = 0; i < paths.length; i++) {
            String path = paths[i];
            int type = getFileType(path);
            if(i == 0) mainType = type;
            if(mainType != type) {
                return TYPE_MIXED;
            }
            names[i] = FilenameUtils.getBaseName(path);
        }

        if(mainType == TYPE_IMAGE) {
            // check they are a PNG sequence;
            boolean isSequence = isAnimationSequence(names);
            if(isSequence) {
                mainType = TYPE_ANIMATION_PNG_SEQUENCE;
            }
        }

        if(mainType > 0 && !ImportUtils.getInstance().supportedTypes.contains(mainType)) {
            mainType = TYPE_UNSUPPORTED;
        }

        return mainType;
    }

    public static int getFileType(String path) {
        int type = checkFileTypeByExtension(path);
        if(type == TYPE_UNCKNOWN) {
            // we have to check by getting into the file
            type = checkFileTypeByContent(path);
        }

        return type;
    }

    public static int checkFileTypeByExtension(String path) {
        String ext = FilenameUtils.getExtension(path).toLowerCase();
        if(ext.equals("png")) {
            return TYPE_IMAGE;
        }
        if(ext.equals("ttf")) {
            return TYPE_TTF_FONT;
        }
        if(ext.equals("scml")) {
            return TYPE_SPRITER_ANIMATION;
        }
        if(ext.equals("vert") || ext.equals("frag")) {
            return TYPE_SHADER;
        }

        return TYPE_UNCKNOWN;
    }

    public static int checkFileTypeByContent(String path) {
        File file = new File(path);
        long fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;

        if(fileSizeInKB > 1000) {
            return TYPE_UNCKNOWN;
        }

        int type = TYPE_UNCKNOWN;

        try {
            String contents = FileUtils.readFileToString(file);

            // checking for atlas file
            if(contents.contains("format: ") && contents.contains("filter: ") && contents.contains("xy: ")) {
                type = TYPE_TEXTURE_ATLAS;
                // need to figure out if atlas is animation or just files.
                TextureAtlas atlas = new TextureAtlas(new FileHandle(file));
                String[] regionNames = new String[atlas.getRegions().size];
                for(int i = 0; i < atlas.getRegions().size; i++) {
                    regionNames[i] = atlas.getRegions().get(i).name;
                }
                boolean isSequence = isAnimationSequence(regionNames);

                if(isSequence) {
                    type =TYPE_SPRITE_ANIMATION_ATLAS;
                }

                return type;
            }

            // checking for spine animation
            if(contents.contains("\"skeleton\":{\"") || contents.contains("\"skeleton\": { \"") || contents.contains("{\"bones\":[")) {
                type = TYPE_SPINE_ANIMATION;
                return type;
            }

            // checking for particle effect
            if(contents.contains("- Options - ") && contents.contains("- Image Path -") && contents.contains("- Duration -")) {
                type = TYPE_PARTICLE_EFFECT;
                return type;
            }

        } catch (IOException e) {
        }

        return type;
    }

    public static boolean isAnimationSequence(String[] names) {
        if(names.length < 2) return false;
        int[] sequenceArray = new int[names.length];
        for(int i  = 0; i < names.length; i++) {
            String name = names[i];
            // try to remove extension if any
            if(name.indexOf(".") > 0) name = name.substring(0, name.indexOf("."));
            try {
                int intValue = Integer.parseInt(name.replaceAll("[^0-9]", ""));
                sequenceArray[i] = intValue;
            } catch (Exception e) {
                sequenceArray[i] = -10;
            }
        }
        Arrays.sort(sequenceArray);
        if(sequenceArray[0] == 0 && sequenceArray[sequenceArray.length-1] == sequenceArray.length-1) {
            return true;
        }
        if(sequenceArray[0] == 1 && sequenceArray[sequenceArray.length-1] == sequenceArray.length) {
            return true;
        }

        return false;
    }
}
