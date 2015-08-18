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

package com.overlap2d.plugins.ninepatch;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

/**
 * Created by azakhary on 8/18/2015.
 */
public class ImageUtils {

    /** Returns the pads, or null if the image had no pads or the pads match the splits. Pads are an int[4] that has left, right,
     * top, bottom. */
    public int[] getPads (BufferedImage image, String name, int[] splits) {
        WritableRaster raster = image.getRaster();

        int bottom = raster.getHeight() - 1;
        int right = raster.getWidth() - 1;

        int startX = getSplitPoint(raster, name, 1, bottom, true, true);
        int startY = getSplitPoint(raster, name, right, 1, true, false);

        // No need to hunt for the end if a start was never found.
        int endX = 0;
        int endY = 0;
        if (startX != 0) endX = getSplitPoint(raster, name, startX + 1, bottom, false, true);
        if (startY != 0) endY = getSplitPoint(raster, name, right, startY + 1, false, false);

        // Ensure pixels after the end are not invalid.
        getSplitPoint(raster, name, endX + 1, bottom, true, true);
        getSplitPoint(raster, name, right, endY + 1, true, false);

        // No pads.
        if (startX == 0 && endX == 0 && startY == 0 && endY == 0) {
            return null;
        }

        // -2 here is because the coordinates were computed before the 1px border was stripped.
        if (startX == 0 && endX == 0) {
            startX = -1;
            endX = -1;
        } else {
            if (startX > 0) {
                startX--;
                endX = raster.getWidth() - 2 - (endX - 1);
            } else {
                // If no start point was ever found, we assume full stretch.
                endX = raster.getWidth() - 2;
            }
        }
        if (startY == 0 && endY == 0) {
            startY = -1;
            endY = -1;
        } else {
            if (startY > 0) {
                startY--;
                endY = raster.getHeight() - 2 - (endY - 1);
            } else {
                // If no start point was ever found, we assume full stretch.
                endY = raster.getHeight() - 2;
            }
        }

        int[] pads = new int[] {startX, endX, startY, endY};

        if (splits != null && Arrays.equals(pads, splits)) {
            return null;
        }

        return pads;
    }

    /** Returns the splits, or null if the image had no splits or the splits were only a single region. Splits are an int[4] that
     * has left, right, top, bottom. */
    public int[] getSplits (BufferedImage image, String name) {
        WritableRaster raster = image.getRaster();

        int startX = getSplitPoint(raster, name, 1, 0, true, true);
        int endX = getSplitPoint(raster, name, startX, 0, false, true);
        int startY = getSplitPoint(raster, name, 0, 1, true, false);
        int endY = getSplitPoint(raster, name, 0, startY, false, false);

        // Ensure pixels after the end are not invalid.
        getSplitPoint(raster, name, endX + 1, 0, true, true);
        getSplitPoint(raster, name, 0, endY + 1, true, false);

        // No splits, or all splits.
        if (startX == 0 && endX == 0 && startY == 0 && endY == 0) return null;

        // Subtraction here is because the coordinates were computed before the 1px border was stripped.
        if (startX != 0) {
            startX--;
            endX = raster.getWidth() - 2 - (endX - 1);
        } else {
            // If no start point was ever found, we assume full stretch.
            endX = raster.getWidth() - 2;
        }
        if (startY != 0) {
            startY--;
            endY = raster.getHeight() - 2 - (endY - 1);
        } else {
            // If no start point was ever found, we assume full stretch.
            endY = raster.getHeight() - 2;
        }

        return new int[] {startX, endX, startY, endY};
    }

    /** Hunts for the start or end of a sequence of split pixels. Begins searching at (startX, startY) then follows along the x or y
     * axis (depending on value of xAxis) for the first non-transparent pixel if startPoint is true, or the first transparent pixel
     * if startPoint is false. Returns 0 if none found, as 0 is considered an invalid split point being in the outer border which
     * will be stripped. */
    static private int getSplitPoint (WritableRaster raster, String name, int startX, int startY, boolean startPoint, boolean xAxis) {
        int[] rgba = new int[4];

        int next = xAxis ? startX : startY;
        int end = xAxis ? raster.getWidth() : raster.getHeight();
        int breakA = startPoint ? 255 : 0;

        int x = startX;
        int y = startY;
        while (next != end) {
            if (xAxis)
                x = next;
            else
                y = next;

            raster.getPixel(x, y, rgba);
            if (rgba[3] == breakA) return next;

            if (!startPoint && (rgba[0] != 0 || rgba[1] != 0 || rgba[2] != 0 || rgba[3] != 255)) {
                // error
            }

            next++;
        }

        return 0;
    }
}
