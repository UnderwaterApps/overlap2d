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

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;

/**
 * Created by sargis on 8/29/14.
 */
public class NinePatchUtils {

    public static Integer[] findPatches(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] row = new int[width];
        int[] column = new int[height];
        row = getPixels(image, 0, 0, width, 1, row);
        column = getPixels(image, 0, 0, 1, height, column);

        boolean[] result = new boolean[1];
        //row = getPixels(image, 0, height - 1, width, 1, row);
        //column = getPixels(image, width - 1, 0, 1, height, column);

        Pair<java.util.List<Pair<Integer>>> top = getPatches(row, result);
        Pair<java.util.List<Pair<Integer>>> left = getPatches(column, result);

        Pair<Integer> topPadding = getPadding(top.first);
        Pair<Integer> leftPadding = getPadding(left.first);

        return new Integer[]{topPadding.first, topPadding.second, leftPadding.first, leftPadding.second};
    }

    public static BufferedImage removePatches(BufferedImage image) {
        BufferedImage buffer = createTranslucentCompatibleImage(
                image.getWidth() - 2, image.getHeight() - 2);

        Graphics2D g2 = buffer.createGraphics();
        g2.drawImage(image, -1, -1, null);
        g2.dispose();
        return buffer;
    }

    public static BufferedImage convertTo9Patch(BufferedImage image, Integer[] patches, float ratio) {
        BufferedImage buffer = createTranslucentCompatibleImage(image.getWidth() + 2, image.getHeight() + 2);
        Graphics2D g2 = buffer.createGraphics();
        g2.drawImage(image, 1, 1, null);
        g2.dispose();
        draw9Patch(buffer, patches, ratio);
        return buffer;
    }

    private static GraphicsConfiguration getGraphicsConfiguration() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return environment.getDefaultScreenDevice().getDefaultConfiguration();
    }

    private static BufferedImage createTranslucentCompatibleImage(int width, int height) {
        return getGraphicsConfiguration().createCompatibleImage(width, height,
                Transparency.TRANSLUCENT);
    }

    private static Pair<Integer> getPadding(java.util.List<Pair<Integer>> pairs) {
        if (pairs.size() == 0) {
            return new Pair<>(0, 0);
        } else if (pairs.size() == 1) {
            if (pairs.get(0).first == 1) {
                return new Pair<>(pairs.get(0).second - pairs.get(0).first, 0);
            } else {
                return new Pair<>(0, pairs.get(0).second - pairs.get(0).first);
            }
        } else {
            int index = pairs.size() - 1;
            return new Pair<>(pairs.get(0).second - pairs.get(0).first,
                    pairs.get(index).second - pairs.get(index).first);
        }
    }

    private static int[] getPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
        if (w == 0 || h == 0) {
            return new int[0];
        }

        if (pixels == null) {
            pixels = new int[w * h];
        } else if (pixels.length < w * h) {
            throw new IllegalArgumentException("Pixels array must have a length >= w * h");
        }

        int imageType = img.getType();
        if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB) {
            Raster raster = img.getRaster();
            return (int[]) raster.getDataElements(x, y, w, h, pixels);
        }

        // Unmanages the image
        return img.getRGB(x, y, w, h, pixels, 0, w);
    }

    private static Pair<java.util.List<Pair<Integer>>> getPatches(int[] pixels, boolean[] startWithPatch) {
        int lastIndex = 1;
        int lastPixel = pixels[1];
        boolean first = true;

        java.util.List<Pair<Integer>> fixed = new ArrayList<>();
        java.util.List<Pair<Integer>> patches = new ArrayList<>();

        for (int i = 1; i < pixels.length - 1; i++) {
            int pixel = pixels[i];
            if (pixel != lastPixel) {
                if (lastPixel == 0xFF000000) {
                    if (first) startWithPatch[0] = true;
                    patches.add(new Pair<Integer>(lastIndex, i));
                } else {
                    fixed.add(new Pair<Integer>(lastIndex, i));
                }
                first = false;

                lastIndex = i;
                lastPixel = pixel;
            }
        }
        if (lastPixel == 0xFF000000) {
            if (first) startWithPatch[0] = true;
            patches.add(new Pair<>(lastIndex, pixels.length - 1));
        } else {
            fixed.add(new Pair<>(lastIndex, pixels.length - 1));
        }

        if (patches.size() == 0) {
            patches.add(new Pair<>(1, pixels.length - 1));
            startWithPatch[0] = true;
            fixed.clear();
        }

        return new Pair<>(fixed, patches);
    }

    private static void draw9Patch(BufferedImage image, Integer[] patches, float ratio) {
        int width = image.getWidth();
        int height = image.getHeight();
        int wStart = (int) (patches[0] * ratio) + 1; // this number should be rounded UP
        int wEnd = (int) (width - patches[1] * ratio) - 1;
        int hStart = (int) (patches[2] * ratio) + 1; // this number should be rounded UP
        int hEnd = (int) (height - patches[3] * ratio) - 1;
        for (int i = wStart; i < wEnd; i++) {
            image.setRGB(i, 0, 0xFF000000);
            image.setRGB(i, height - 1, 0xFF000000);
        }
        for (int i = hStart; i < hEnd; i++) {
            image.setRGB(0, i, 0xFF000000);
            image.setRGB(width - 1, i, 0xFF000000);
        }
    }

    static class Pair<E> {
        E first;
        E second;

        Pair(E first, E second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "Pair[" + first + ", " + second + "]";
        }
    }
}
