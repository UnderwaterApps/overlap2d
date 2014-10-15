package com.uwsoft.editor.utils;

public class Line2D {
	
	private float x1, y1, x2, y2;
	
	public Line2D(float x1, float y1,float  x2, float y2) {
		this.x1 = x1;
		this.y2 = y2;
		this.x2 = x2;
		this.y1 = y1;
	}
	
    public float getX1() {
		return x1;
	}

	public float getY1() {
		return y1;
	}

	public float getX2() {
		return x2;
	}

	public float getY2() {
		return y2;
	}
	
	public double ptSegDist(double px, double py) {
		 return Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py));
	}

	public static double ptSegDist(double x1, double y1, double x2, double y2, double px, double py) {
		 return Math.sqrt(ptSegDistSq(x1, y1, x2, y2, px, py));
	}
	
	public static double ptSegDistSq(double x1, double y1, double x2, double y2, double px, double py) {
      double pd2 = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
  
      double x, y;
      if (pd2 == 0)
        {
          // Points are coincident.
          x = x1;
          y = y2;
        }
      else
        {
          double u = ((px - x1) * (x2 - x1) + (py - y1) * (y2 - y1)) / pd2;
  
          if (u < 0)
            {
              // "Off the end"
              x = x1;
              y = y1;
            }
          else if (u > 1.0)
            {
              x = x2;
              y = y2;
            }
          else
            {
              x = x1 + u * (x2 - x1);
              y = y1 + u * (y2 - y1);
            }
        }
  
      return (x - px) * (x - px) + (y - py) * (y - py);
    }

}
