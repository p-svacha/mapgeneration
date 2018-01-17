package generator;

import java.util.Random;

import javafx.scene.paint.Color;
import geometry.Point;

public class Generator {
	
	public static Point getLineSegmentIntersectionPoint(float p0_x, float p0_y, float p1_x, float p1_y, 
		    float p2_x, float p2_y, float p3_x, float p3_y)
	{
	
		Point intersection = null;
	
	    float s1_x, s1_y, s2_x, s2_y;
	    s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
	    s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;

	    float s, t;
	    s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
	    t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

	    if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
	    {
	    	intersection = new Point(0, 0);
	        intersection.x = (int) (p0_x + (t * s1_x));
            intersection.y  = (int) (p0_y + (t * s1_y));
	    }

	    return intersection;
	}
	
	public static int getDistanceBetweenTwoPoints(int x1, int y1, int x2, int y2) {
		int xDiff = x2 - x1;
		int yDiff = y2 - y1;
		return (int) Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
	}
	
    public static boolean getCircleLineIntersection(Point pointA, Point pointB, Point center, double radius) {
       
    	int minX = pointA.x < pointB.x ? pointA.x : pointB.x;
     	int maxX = pointA.x > pointB.x ? pointA.x : pointB.x;
     	int minY = pointA.y < pointB.y ? pointA.y : pointB.y;
     	int maxY = pointA.y > pointB.y ? pointA.y : pointB.y;
    	
    	double baX = pointB.x - pointA.x;
        double baY = pointB.y - pointA.y;
        double caX = center.x - pointA.x;
        double caY = center.y - pointA.y;

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return false;
            //or empty list
        }
        // if disc == 0 ... dealt with later
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Point p1 = new Point((int) (pointA.x - baX * abScalingFactor1), (int) (pointA.y - baY * abScalingFactor1));
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            if(p1.x > minX && p1.x < maxX && p1.y > minY && p1.y < maxY) return true;
        	//return Collections.singletonList(p1);
        }
        Point p2 = new Point((int) (pointA.x - baX * abScalingFactor2), (int) (pointA.y - baY * abScalingFactor2));
        if(p2.x > minX && p2.x < maxX && p2.y > minY && p2.y < maxY) return true;
        return false;
        //return Arrays.asList(p1, p2);
    }
	
	public static Color getRandomColor() {
		Random r = new Random();
		double red = r.nextDouble();
		double green = r.nextDouble();
		double blue = r.nextDouble();
		return new Color(red, green, blue, 1);
	}

}
