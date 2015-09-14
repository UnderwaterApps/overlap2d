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

package com.uwsoft.editor.utils.poly.tracer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
* http://astroboid.x50.cc/blog/2010/06/mapping-box2d-shapes-from-textures/ A
* mostly "blind" C# conversion of the TextureConverter code from Farseer, I
* even left the original german comments in place. Seems to work.
*/
public class TextureConverter {
   // User contribution from Sickbattery
   // / <summary>
   // / TODO:
   // / 1.) Das Array welches ich bekomme am besten in einen boolean array
   // verwandeln. Wurde die Geschwindigkeit verbessern
   // / </summary>
   private static int[][] ClosePixels = new int[][] { { -1, -1 }, { 0, -1 },
         { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 } };

   public static Array<Vector2> createPolygon(int[] data, int width, int height)
         throws Exception {
      PolygonCreationAssistance pca = new PolygonCreationAssistance(data,
            width, height);
      Array<Array<Vector2>> verts = createPolygon(pca);
      return verts.get(0);
   }

   public static Array<Vector2> createPolygon(int[] data, int width,
         int height, boolean holeDetection) throws Exception {
      PolygonCreationAssistance pca = new PolygonCreationAssistance(data,
            width, height);
      pca.HoleDetection = holeDetection;
      Array<Array<Vector2>> verts = createPolygon(pca);
      return verts.get(0);
   }

   public static Array<Array<Vector2>> createPolygon(int[] data, int width,
         int height, float hullTolerance, int alphaTolerance,
         boolean multiPartDetection, boolean holeDetection) throws Exception {
      PolygonCreationAssistance pca = new PolygonCreationAssistance(data,
            width, height);
      pca.setHullTolerance(hullTolerance);
      pca.setAlphaTolerance(alphaTolerance);
      pca.MultipartDetection = multiPartDetection;
      pca.HoleDetection = holeDetection;
      return createPolygon(pca);
   }

   private static Array<Array<Vector2>> createPolygon(
         PolygonCreationAssistance pca) throws Exception {
      Array<Array<Vector2>> polygons = new Array<Array<Vector2>>();
      Array<Vector2> polygon;
      Array<Vector2> holePolygon;
      Vector2 holeEntrance = null;
      Vector2 polygonEntrance = null;
      Array<Vector2> blackList = new Array<Vector2>();
      // First of all: Check the array you just got.
      if (pca.IsValid()) {
         boolean searchOn;
         do {
            if (polygons.size == 0) {
               polygon = CreateSimplePolygon(pca, new Vector2(),
                     new Vector2());
               if (polygon != null && polygon.size > 2) {
                  polygonEntrance = GetTopMostVertex(polygon);
               }
            } else if (polygonEntrance != null) {
               polygon = CreateSimplePolygon(pca, polygonEntrance,
                     new Vector2(polygonEntrance.x - 1f,
                           polygonEntrance.y));
            } else {
               break;
            }
            searchOn = false;
            if (polygon != null && polygon.size > 2) {
               if (pca.HoleDetection) {
                  do {
                     holeEntrance = GetHoleHullEntrance(pca, polygon,
                           holeEntrance);
                     if (holeEntrance != null) {
                        if (!vectorListContains(blackList, holeEntrance)) {
                           blackList.add(holeEntrance);
                           holePolygon = CreateSimplePolygon(pca,
                                 holeEntrance, new Vector2(
                                       holeEntrance.x + 1,
                                       holeEntrance.y));
                           if (holePolygon != null
                                 && holePolygon.size > 2) {
                              holePolygon.add(holePolygon.get(0));
                              Reference<Integer> vertex2IndexRef = new Reference<Integer>(
                                    0);
                              Reference<Integer> vertex1IndexRef = new Reference<Integer>(
                                    0);
                              if (SplitPolygonEdge(polygon,
                                    EdgeAlignment.Vertical,
                                    holeEntrance, vertex1IndexRef,
                                    vertex2IndexRef)) {

                                 polygon.ensureCapacity(holePolygon.size);
                                 for (int i = holePolygon.size - 1; i <= 0; i--) {
                                    polygon.insert(
                                          vertex2IndexRef.v,
                                          holePolygon.get(i));
                                 }
                              }
                           }
                        } else {
                           break;
                        }
                     } else {
                        break;
                     }
                  } while (true);
               }
               polygons.add(polygon);
               if (pca.MultipartDetection) {
                  // 1: 95 / 151
                  // 2: 232 / 252
                  //
                  polygonEntrance = new Vector2();
                  while (GetNextHullEntrance(pca, polygonEntrance,
                        polygonEntrance)) {
                     boolean inPolygon = false;
                     for (int i = 0; i < polygons.size; i++) {
                        polygon = polygons.get(i);
                        if (InPolygon(pca, polygon, polygonEntrance)) {
                           inPolygon = true;
                           break;
                        }
                     }
                     if (!inPolygon) {
                        searchOn = true;
                        break;
                     }
                  }
               }
            }
         } while (searchOn);
      } else {
         throw new Exception(
               "Sizes don't match: Color array must contain texture width * texture height elements.");
      }
      return polygons;
   }

   private static Vector2 GetHoleHullEntrance(PolygonCreationAssistance pca,
         Array<Vector2> polygon, Vector2 startVertex) throws Exception {
      Array<CrossingEdgeInfo> edges = new Array<CrossingEdgeInfo>();
      Vector2 entrance;
      int startLine;
      int endLine;
      int lastSolid = 0;
      boolean foundSolid;
      boolean foundTransparent;
      if (polygon != null && polygon.size > 0) {
         if (startVertex != null) {
            startLine = (int) startVertex.y;
         } else {
            startLine = (int) GetTopMostCoord(polygon);
         }
         endLine = (int) GetBottomMostCoord(polygon);
         if (startLine > 0 && startLine < pca.Height && endLine > 0
               && endLine < pca.Height) {
            // go from top to bottom of the polygon
            for (int y = startLine; y <= endLine; y += pca
                  .getHoleDetectionLineStepSize()) {
               // get x-coord of every polygon edge which crosses y
               edges = GetCrossingEdges(polygon, EdgeAlignment.Vertical, y);
               // we need an even number of crossing edges
               if (edges.size > 1 && edges.size % 2 == 0) {
                  for (int i = 0; i < edges.size; i += 2) {
                     foundSolid = false;
                     foundTransparent = false;
                     for (int x = (int) edges.get(i).CrossingPoint.x; x <= (int) edges
                           .get(i + 1).CrossingPoint.x; x++) {
                        if (pca.IsSolid(x, y)) {
                           if (!foundTransparent) {
                              foundSolid = true;
                              lastSolid = x;
                           }
                           if (foundSolid && foundTransparent) {
                              entrance = new Vector2(lastSolid, y);
                              if (DistanceToHullAcceptable(pca,
                                    polygon, entrance, true)) {
                                 return entrance;
                              }
                              entrance = null;
                              break;
                           }
                        } else {
                           if (foundSolid) {
                              foundTransparent = true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
      return null;
   }

   private static boolean DistanceToHullAcceptable(
         PolygonCreationAssistance pca, Array<Vector2> polygon,
         Vector2 point, boolean higherDetail) {
      if (polygon != null && polygon.size > 2) {
         Vector2 edgeVertex2 = polygon.get(polygon.size - 1).cpy();
         Vector2 edgeVertex1 = new Vector2();
         if (higherDetail) {
            for (int i = 0; i < polygon.size; i++) {
               edgeVertex1.set(polygon.get(i));
               if (LineTools.DistanceBetweenPointAndLineSegment(point,
                     edgeVertex1, edgeVertex2) <= pca.getHullTolerance()
                     || LineTools.DistanceBetweenPointAndPoint(point,
                           edgeVertex1) <= pca.getHullTolerance()) {
                  return false;
               }
               edgeVertex2.set(polygon.get(i));
            }
            return true;
         } else {
            for (int i = 0; i < polygon.size; i++) {
               edgeVertex1.set(polygon.get(i));
               if (LineTools.DistanceBetweenPointAndLineSegment(point,
                     edgeVertex1, edgeVertex2) <= pca.getHullTolerance()) {
                  return false;
               }
               edgeVertex2.set(polygon.get(i));
            }
            return true;
         }
      }
      return false;
   }

   private static boolean InPolygon(PolygonCreationAssistance pca,
         Array<Vector2> polygon, Vector2 point) throws Exception {
      boolean inPolygon = !DistanceToHullAcceptable(pca, polygon, point, true);
      if (!inPolygon) {
         Array<CrossingEdgeInfo> edges = GetCrossingEdges(polygon,
               EdgeAlignment.Vertical, (int) point.y);
         if (edges.size > 0 && edges.size % 2 == 0) {
            for (int i = 0; i < edges.size; i += 2) {
               if (edges.get(i).CrossingPoint.x <= point.x
                     && edges.get(i + 1).CrossingPoint.x >= point.x) {
                  return true;
               }
            }
            return false;
         }
         return false;
      }
      return true;
   }

   private static Vector2 GetTopMostVertex(Array<Vector2> vertices) {
      float topMostValue = Float.MAX_VALUE;
      Vector2 topMost = null;
      for (int i = 0; i < vertices.size; i++) {
         if (topMostValue > vertices.get(i).y) {
            topMostValue = vertices.get(i).y;
            topMost = vertices.get(i);
         }
      }
      return topMost.cpy();
   }

   private static float GetTopMostCoord(Array<Vector2> vertices) {
      float returnValue = Float.MAX_VALUE;
      for (int i = 0; i < vertices.size; i++) {
         if (returnValue > vertices.get(i).y) {
            returnValue = vertices.get(i).y;
         }
      }
      return returnValue;
   }

   private static float GetBottomMostCoord(Array<Vector2> vertices) {
      float returnValue = Float.MIN_VALUE;
      for (int i = 0; i < vertices.size; i++) {
         if (returnValue < vertices.get(i).y) {
            returnValue = vertices.get(i).y;
         }
      }
      return returnValue;
   }

   public static Boolean vectorEquals(Vector2 v1, Vector2 v2) {
      return v1.x == v2.x && v1.y == v2.y;
   }

   public static int vectorListIndexOf(Array list, Vector2 v) {
      for (int i = 0; i < list.size; i++) {
         Object obj = list.get(i);
         if (obj == v)
            return i;
         if (obj instanceof Vector2) {
            Vector2 vect = (Vector2) obj;
            if (vectorEquals(v, vect))
               return i;
         }
      }
      return -1;
   }

   public static Boolean vectorListContains(Array list, Vector2 v) {
      int index = vectorListIndexOf(list, v);
      return index != -1;
   }

   public static Vector2 vectorSub(Vector2 v1, Vector2 v2) {
      return new Vector2(v1.x - v2.x, v1.y - v2.y);
   }

   public static Vector2 vectorAdd(Vector2 v1, Vector2 v2) {
      return new Vector2(v1.x + v2.x, v1.y + v2.y);
   }

   public static float vectorCross(Vector2 v1, Vector2 v2) {
      return v1.x * v2.y - v1.y * v2.x;
   }

   public static Vector2 vectorCross(Vector2 v1, float scalar) {
      return new Vector2(scalar * v1.y, -scalar * v1.x);
   }

   public static float vectorDot(Vector2 v1, Vector2 v2) {
      return v1.x * v2.x + v1.y * v2.y;
   }

   public static Vector2 vectorMul(Vector2 v1, float scalar) {
      return new Vector2(v1.x * scalar, v1.y * scalar);
   }

   private static Array<CrossingEdgeInfo> GetCrossingEdges(
         Array<Vector2> polygon, EdgeAlignment edgeAlign, int checkLine)
         throws Exception {
      Array<CrossingEdgeInfo> edges = new Array<CrossingEdgeInfo>();
      Vector2 slope = new Vector2();
      Vector2 edgeVertex1 = new Vector2();
      Vector2 edgeVertex2 = new Vector2();
      Vector2 slopePreview = new Vector2();
      Vector2 edgeVertexPreview = new Vector2();
      Vector2 crossingPoint = new Vector2();
      boolean addCrossingPoint;
      if (polygon.size > 1) {
         edgeVertex2.set(polygon.get(polygon.size - 1));
         switch (edgeAlign) {
         case Vertical:
            for (int i = 0; i < polygon.size; i++) {
               edgeVertex1.set(polygon.get(i));
               if ((edgeVertex1.y >= checkLine && edgeVertex2.y <= checkLine)
                     || (edgeVertex1.y <= checkLine && edgeVertex2.y >= checkLine)) {
                  if (edgeVertex1.y != edgeVertex2.y) {
                     addCrossingPoint = true;
                     slope.set(vectorSub(edgeVertex2, edgeVertex1));
                     if (edgeVertex1.y == checkLine) {
                        edgeVertexPreview.set(polygon.get((i + 1)
                              % polygon.size));
                        slopePreview.set(vectorSub(edgeVertex1,
                              edgeVertexPreview));
                        if (slope.y > 0) {
                           addCrossingPoint = (slopePreview.y <= 0);
                        } else {
                           addCrossingPoint = (slopePreview.y >= 0);
                        }
                     }
                     if (addCrossingPoint) {
                        crossingPoint = new Vector2(
                              (checkLine - edgeVertex1.y) / slope.y
                                    * slope.x + edgeVertex1.x,
                              checkLine);
                        edges.add(new CrossingEdgeInfo(edgeVertex1,
                              edgeVertex2, crossingPoint, edgeAlign));
                     }
                  }
               }
               edgeVertex2.set(edgeVertex1);
            }
            break;
         case Horizontal:
            throw new Exception(
                  "EdgeAlignment.Horizontal isn't implemented yet. Sorry.");
         }
      }
      edges.sort();
      // Collections.sort(edges);
      return edges;
   }

   private static boolean SplitPolygonEdge(Array<Vector2> polygon,
         EdgeAlignment edgeAlign, Vector2 coordInsideThePolygon,
         Reference<Integer> vertex1IndexRef,
         Reference<Integer> vertex2IndexRef) throws Exception {
      Array<CrossingEdgeInfo> edges;
      Vector2 slope = new Vector2();
      int nearestEdgeVertex1Index = 0;
      int nearestEdgeVertex2Index = 0;
      boolean edgeFound = false;
      float shortestDistance = Float.MAX_VALUE;
      boolean edgeCoordFound = false;
      Vector2 foundEdgeCoord = new Vector2();
      vertex1IndexRef.v = 0;
      vertex2IndexRef.v = 0;
      switch (edgeAlign) {
      case Vertical:
         edges = GetCrossingEdges(polygon, EdgeAlignment.Vertical,
               (int) coordInsideThePolygon.y);
         foundEdgeCoord.y = coordInsideThePolygon.y;
         if (edges != null && edges.size > 1 && edges.size % 2 == 0) {
            float distance;
            for (int i = 0; i < edges.size; i++) {
               if (edges.get(i).CrossingPoint.x < coordInsideThePolygon.x) {
                  distance = coordInsideThePolygon.x
                        - edges.get(i).CrossingPoint.x;
                  if (distance < shortestDistance) {
                     shortestDistance = distance;
                     foundEdgeCoord.x = edges.get(i).CrossingPoint.x;
                     edgeCoordFound = true;
                  }
               }
            }
            if (edgeCoordFound) {
               shortestDistance = Float.MAX_VALUE;
               int edgeVertex2Index = polygon.size - 1;
               int edgeVertex1Index;
               for (edgeVertex1Index = 0; edgeVertex1Index < polygon.size; edgeVertex1Index++) {
                  Vector2 tempVector1 = polygon.get(edgeVertex1Index)
                        .cpy();
                  Vector2 tempVector2 = polygon.get(edgeVertex2Index)
                        .cpy();
                  distance = LineTools
                        .DistanceBetweenPointAndLineSegment(
                              foundEdgeCoord, tempVector1,
                              tempVector2);
                  if (distance < shortestDistance) {
                     shortestDistance = distance;
                     nearestEdgeVertex1Index = edgeVertex1Index;
                     nearestEdgeVertex2Index = edgeVertex2Index;
                     edgeFound = true;
                  }
                  edgeVertex2Index = edgeVertex1Index;
               }
               if (edgeFound) {
                  slope.set(vectorSub(
                        polygon.get(nearestEdgeVertex2Index),
                        polygon.get(nearestEdgeVertex1Index)));
                  slope.nor();
                  Vector2 tempVector = polygon.get(
                        nearestEdgeVertex1Index).cpy();
                  distance = LineTools.DistanceBetweenPointAndPoint(
                        tempVector, foundEdgeCoord);
                  vertex1IndexRef.v = nearestEdgeVertex1Index;
                  vertex2IndexRef.v = nearestEdgeVertex1Index + 1;
                  // distance * slope + polygon[vertex1Index]
                  polygon.insert(
                        nearestEdgeVertex1Index,
                        vectorAdd(vectorMul(slope, distance),
                              polygon.get(vertex1IndexRef.v)));
                  polygon.insert(
                        nearestEdgeVertex1Index,
                        vectorAdd(vectorMul(slope, distance),
                              polygon.get(vertex2IndexRef.v)));
                  return true;
               }
            }
         }
         break;
      case Horizontal:
         throw new Exception(
               "EdgeAlignment.Horizontal isn't implemented yet. Sorry.");
      }
      return false;
   }

   private static Array<Vector2> CreateSimplePolygon(
         PolygonCreationAssistance pca, Vector2 entrance, Vector2 last) {
      boolean entranceFound = false;
      boolean endOfHull = false;
      Array<Vector2> polygon = new Array<Vector2>();
      Array<Vector2> hullArea = new Array<Vector2>();
      Array<Vector2> endOfHullArea = new Array<Vector2>();
      Vector2 current = new Vector2();
      Vector2 zeroVec = new Vector2();
      // Get the entrance point. //todo: alle moglichkeiten testen
      if (vectorEquals(entrance, zeroVec) || !pca.InBounds(entrance)) {
         entranceFound = GetHullEntrance(pca, entrance);
         if (entranceFound) {
            current.set(entrance.x - 1f, entrance.y);
         }
      } else {
         if (pca.IsSolid(entrance)) {
            if (IsNearPixel(pca, entrance, last)) {
               current.set(last);
               entranceFound = true;
            } else {
               Vector2 temp = new Vector2();
               if (SearchNearPixels(pca, false, entrance, temp)) {
                  current.set(temp);
                  entranceFound = true;
               } else {
                  entranceFound = false;
               }
            }
         }
      }
      if (entranceFound) {
         polygon.add(entrance);
         hullArea.add(entrance);
         Vector2 next = entrance.cpy();
         do {
            // Search in the pre vision list for an outstanding point.
            Vector2 outstanding = new Vector2();
            if (SearchForOutstandingVertex(hullArea,
                  pca.getHullTolerance(), outstanding)) {
               if (endOfHull) {
                  // We have found the next pixel, but is it on the last
                  // bit of the
                  // hull?
                  if (vectorListContains(endOfHullArea, outstanding)
                        && !vectorListContains(polygon, outstanding)) {
                     // Indeed.
                     polygon.add(outstanding);
                  }
                  // That's enough, quit.
                  break;
               }
               // Add it and remove all vertices that don't matter anymore
               // (all the vertices before the outstanding).
               polygon.add(outstanding);
               int index = vectorListIndexOf(hullArea, outstanding);
               if (index == -1) {
                  int debug = 1;
               }
               if (index >= 0) {
                  // hullArea = hullArea.subList(index + 1,
                  // hullArea.size);

                  // Array<Vector2> newArray = new Array<Vector2>
                  // (hullArea.size - (index + 1));
                  int counter = 0;
                  for (int i = index + 1; i < hullArea.size; i++) {
                     Vector2 v = hullArea.get(index);
                     // newArray.add(v);
                     hullArea.set(counter, v);
                     counter++;
                  }
                  // hullArea.clear();
                  // hullArea = newArray;
                  for (int i = 0; i < index + 1; i++) {
                     hullArea.pop();
                  }
               }
            }
            // Last point gets current and current gets next. Our little
            // spider is
            // moving forward on the hull ;).
            last.set(current);
            current.set(next);
            // Get the next point on hull.
            next = new Vector2();
            if (GetNextHullPoint(pca, last, current, next)) {
               // Add the vertex to a hull pre vision list.
               hullArea.add(next);
            } else {
               // Quit
               break;
            }
            if (vectorEquals(next, entrance) && !endOfHull) {
               // It's the last bit of the hull, search on and exit at next
               // found
               // vertex.
               endOfHull = true;
               endOfHullArea.addAll(hullArea);
            }
         } while (true);
      }
      return polygon;
   }

   private static boolean SearchNearPixels(PolygonCreationAssistance pca,
         boolean searchingForSolidPixel, Vector2 current, Vector2 foundPixel) {
      int x;
      int y;
      for (int i = 0; i < 8; i++) {
         x = (int) current.x + ClosePixels[i][0];
         y = (int) current.y + ClosePixels[i][1];
         if (!searchingForSolidPixel ^ pca.IsSolid(x, y)) {
            foundPixel.set(x, y);
            return true;
         }
      }
      // Nothing found.
      foundPixel.set(0, 0);
      return false;
   }

   private static boolean IsNearPixel(PolygonCreationAssistance pca,
         Vector2 current, Vector2 near) {
      for (int i = 0; i < 8; i++) {
         int x = (int) current.x + ClosePixels[i][0];
         int y = (int) current.y + ClosePixels[i][1];
         if (x >= 0 && x <= pca.Width && y >= 0 && y <= pca.Height) {
            if (x == (int) near.x && y == (int) near.y) {
               return true;
            }
         }
      }
      return false;
   }

   private static boolean GetHullEntrance(PolygonCreationAssistance pca,
         Vector2 entrance) {
      // Search for first solid pixel.
      for (int y = 0; y < pca.Height; y++) {
         for (int x = 0; x < pca.Width; x++) {
            if (pca.IsSolid(x, y)) {
               entrance.set(x, y);
               return true;
            }
         }
      }
      // If there are no solid pixels.
      entrance.set(0, 0);
      return false;
   }

   private static boolean GetNextHullEntrance(PolygonCreationAssistance pca,
         Vector2 start, Vector2 entrance) {
      // Search for first solid pixel.
      int size = pca.Height * pca.Width;
      int x;
      boolean foundTransparent = false;
      for (int i = (int) start.x + (int) start.y * pca.Width; i < size; i++) {
         if (pca.IsSolid(i)) {
            if (foundTransparent) {
               x = i % pca.Width;
               entrance.set(x, (i - x) / pca.Width);
               return true;
            }
         } else {
            foundTransparent = true;
         }
      }
      // If there are no solid pixels.
      entrance.set(0, 0);
      return false;
   }

   private static boolean GetNextHullPoint(PolygonCreationAssistance pca,
         Vector2 last, Vector2 current, Vector2 next) {
      int x;
      int y;
      int indexOfFirstPixelToCheck = GetIndexOfFirstPixelToCheck(last,
            current);
      int indexOfPixelToCheck;
      int pixelsToCheck = 8; // _closePixels.Length;
      for (int i = 0; i < pixelsToCheck; i++) {
         indexOfPixelToCheck = (indexOfFirstPixelToCheck + i)
               % pixelsToCheck;
         x = (int) current.x + ClosePixels[indexOfPixelToCheck][0];
         y = (int) current.y + ClosePixels[indexOfPixelToCheck][1];
         if (x >= 0 && x < pca.Width && y >= 0 && y <= pca.Height) {
            if (pca.IsSolid(x, y)) // todo
            {
               next.set(x, y);
               return true;
            }
         }
      }
      next.set(0, 0);
      return false;
   }

   private static boolean SearchForOutstandingVertex(Array<Vector2> hullArea,
         float hullTolerance, Vector2 outstanding) {
      Vector2 outstandingResult = new Vector2();
      boolean found = false;
      if (hullArea.size > 2) {
         int hullAreaLastPoint = hullArea.size - 1;
         Vector2 tempVector1;
         Vector2 tempVector2 = hullArea.get(0);
         Vector2 tempVector3 = hullArea.get(hullAreaLastPoint);
         // Search between the first and last hull point.
         for (int i = 1; i < hullAreaLastPoint; i++) {
            tempVector1 = hullArea.get(i);
            // Check if the distance is over the one that's tolerable.
            if (LineTools.DistanceBetweenPointAndLineSegment(tempVector1,
                  tempVector2, tempVector3) >= hullTolerance) {
               outstandingResult.set(hullArea.get(i));
               found = true;
               break;
            }
         }
      }
      outstanding.set(outstandingResult);
      return found;
   }

   private static int GetIndexOfFirstPixelToCheck(Vector2 last, Vector2 current) {
      // .: pixel
      // l: last position
      // c: current position
      // f: first pixel for next search
      // f . .
      // l c .
      // . . .
      // Calculate in which direction the last move went and decide over the
      // next
      // first pixel.
      switch ((int) (current.x - last.x)) {
      case 1:
         switch ((int) (current.y - last.y)) {
         case 1:
            return 1;
         case 0:
            return 0;
         case -1:
            return 7;
         }
         break;
      case 0:
         switch ((int) (current.y - last.y)) {
         case 1:
            return 2;
         case -1:
            return 6;
         }
         break;
      case -1:
         switch ((int) (current.y - last.y)) {
         case 1:
            return 3;
         case 0:
            return 4;
         case -1:
            return 5;
         }
         break;
      }
      return 0;
   }
}

enum EdgeAlignment {
   Vertical, Horizontal
}

class CrossingEdgeInfo implements Comparable<CrossingEdgeInfo> {
   private EdgeAlignment _alignment;
   private Vector2 _crossingPoint;
   @SuppressWarnings("unused")
   private Vector2 _edgeVertex2;
   @SuppressWarnings("unused")
   private Vector2 _egdeVertex1;
   public Vector2 EdgeVertex1;
   public Vector2 EdgeVertex2;
   public EdgeAlignment CheckLineAlignment;
   public Vector2 CrossingPoint;

   public CrossingEdgeInfo(Vector2 edgeVertex1, Vector2 edgeVertex2,
         Vector2 crossingPoint, EdgeAlignment checkLineAlignment) {
      _egdeVertex1 = edgeVertex1.cpy();
      _edgeVertex2 = edgeVertex2.cpy();
      _alignment = checkLineAlignment;
      _crossingPoint = crossingPoint;
   }

   public int compareTo(CrossingEdgeInfo obj) {
      CrossingEdgeInfo cei = obj;
      int result = 0;
      switch (_alignment) {
      case Vertical:
         if (_crossingPoint.x < cei.CrossingPoint.y) {
            result = -1;
         } else if (_crossingPoint.x > cei.CrossingPoint.y) {
            result = 1;
         }
         break;
      case Horizontal:
         if (_crossingPoint.y < cei.CrossingPoint.y) {
            result = -1;
         } else if (_crossingPoint.y > cei.CrossingPoint.y) {
            result = 1;
         }
         break;
      }
      return result;
   }
}

// / <summary>
// / Class used as a tools container and helper for the texture-to-vertices code.
// / </summary>
class PolygonCreationAssistance {
   private int _alphaTolerance;
   private int _holeDetectionLineStepSize;
   private float _hullTolerance;

   public PolygonCreationAssistance(int[] data, int width, int height) {
      Data = data;
      Width = width;
      Height = height;
      setAlphaTolerance((byte) 20);
      setHullTolerance(1.5f);
      setHoleDetectionLineStepSize(1);
      HoleDetection = false;
      MultipartDetection = false;
   }

   private int[] Data;
   public int Width;
   public int Height;

   public int getAlphaTolerance() {
      return _alphaTolerance;
   }

   public void setAlphaTolerance(int value) {
      _alphaTolerance = value & 0xFF;
   }

   public float getHullTolerance() {
      return _hullTolerance;
   }

   public void setHullTolerance(float value) {
      float hullTolerance = value;
      if (hullTolerance > 4f)
         hullTolerance = 4f;
      if (hullTolerance < 0.9f)
         hullTolerance = 0.9f;
      _hullTolerance = hullTolerance;
   }

   public int getHoleDetectionLineStepSize() {
      return _holeDetectionLineStepSize;
   }

   private void setHoleDetectionLineStepSize(int value) {
      if (value < 1) {
         _holeDetectionLineStepSize = 1;
      } else {
         if (value > 10) {
            _holeDetectionLineStepSize = 10;
         } else {
            _holeDetectionLineStepSize = value;
         }
      }
   }

   public boolean HoleDetection;
   public boolean MultipartDetection;

   public boolean IsSolid(Vector2 pixel) {
      return IsSolid((int) pixel.x, (int) pixel.y);
   }

   public boolean IsSolid(int x, int y) {
      if (x >= 0 && x < Width && y >= 0 && y < Height) {
         int data = Data[x + y * Width];
         long mask1 = (long) data & 0xFFFFFFFFL;
         long mask2 = mask1 & 0x000000FF;
         Boolean opaque = mask2 >= _alphaTolerance;
         if (opaque || mask2 != 0) {
            int debug = 1;
         }
         return opaque;
      }
      return false;
   }

   public boolean IsSolid(int index) {
      if (index >= 0 && index < Width * Height) {
         int data = Data[index];
         long mask1 = (long) data & 0xFFFFFFFFL;
         long mask2 = mask1 & 0x000000FF;
         Boolean opaque = mask2 >= _alphaTolerance;
         if (opaque || mask2 != 0) {
            int debug = 1;
         }
         return opaque;
         // return (mask1 >= _alphaToleranceRealValue);
      }
      return false;
   }

   public boolean InBounds(Vector2 coord) {
      return (coord.x >= 0f && coord.x < Width && coord.y >= 0f && coord.y < Height);
   }

   public boolean IsValid() {
      if (Data != null && Data.length > 0)
         return Data.length == Width * Height;
      return false;
   }
}

class Reference<K> {
   public K v;

   public Reference(K v) {
      this.v = v;
   }

   @Override
   public String toString() {
      return v.toString();
   }

   @Override
   public boolean equals(Object obj) {
      return v.equals(obj);
   }

   @Override
   public int hashCode() {
      return v.hashCode();
   }
}

class LineTools {
   public static float DistanceBetweenPointAndPoint(Vector2 point1,
         Vector2 point2) {
      Vector2 v = TextureConverter.vectorSub(point1, point2);
      return v.len();
   }

   public static float DistanceBetweenPointAndLineSegment(Vector2 point,
         Vector2 lineEndPoint1, Vector2 lineEndPoint2) {
      Vector2 v = TextureConverter.vectorSub(lineEndPoint2, lineEndPoint1);
      Vector2 w = TextureConverter.vectorSub(point, lineEndPoint1);
      float c1 = TextureConverter.vectorDot(w, v);
      if (c1 <= 0)
         return DistanceBetweenPointAndPoint(point, lineEndPoint1);
      float c2 = TextureConverter.vectorDot(v, v);
      if (c2 <= c1)
         return DistanceBetweenPointAndPoint(point, lineEndPoint2);
      float b = c1 / c2;
      Vector2 pointOnLine = TextureConverter.vectorAdd(lineEndPoint1,
            TextureConverter.vectorMul(v, b));
      return DistanceBetweenPointAndPoint(point, pointOnLine);
   }
}
