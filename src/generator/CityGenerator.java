package generator;

import geometry.Point;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import map.City;

import com.sun.javafx.geom.Line2D;

public class CityGenerator {
	
  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	private final Color CITY_COLOR = new Color(253f/255, 252f/255, 232f/255, 1);
	
	private int MIN_TRIANGLES = 10;
	private int MAX_TRIANGLES = 20;
	
	private int MIN_POLYGON_VERTICES = 20;
	private int MAX_POLYGON_VERTICES = 40;
	
	private int MIN_POLYGON_EDGE_LENGTH = 12;
	private int MAX_POLYGON_EDGE_LENGTH = 15;
	
	private int MIN_POLYGON_ANGLE = -30;
	private int MAX_POLYGON_ANGLE = 50;
	
	private int MIN_SIZE = 100;
	private int MAX_SIZE = 300;
	
	private Random r;
	
	private ArrayList<Line2D> toAdd;
	private ArrayList<Line2D> toRemove;
	
  /***************************************************************************
   *                                                                         *
   * Contructor                                                              *
   *                                                                         *
   **************************************************************************/
	
	public CityGenerator() {
		r = new Random();
	}

  /***************************************************************************
   *                                                                         *
   * Private methods                                                         *
   *                                                                         *
   **************************************************************************/
	
	/**
	 * Try making a polygon by deforming a circle
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	private Point[] generateCityArea(City c, int xPos, int yPos) {
		ArrayList<Point> area = new ArrayList<Point>();
		
		int nPoints = 27;
		int size = 30;
		int randomRangeDistance = 3;
		int randomRangeXY = 3;
		int trendSpeed = 7;
		
		for(int i = 0; i < nPoints; i++) {
			double angle = (double)i/nPoints*360;
			Point p = new Point((int) (xPos + size * Math.sin(Math.toRadians(angle))), (int) (yPos + size * Math.cos(Math.toRadians(angle))));
			p.angle = angle;
			area.add(p);
		}
		
		int trendDistance = 0;
		for(int i = 0; i < nPoints; i++) {
			Point p = area.get(i);
			
			int distanceTrendChange = (int) ((r.nextGaussian()*trendSpeed));

			
			trendDistance += distanceTrendChange;
			if(trendDistance < -size/3) {
				trendDistance = -size/3;
			}
			if(trendDistance > size/2) {
				trendDistance = size/2;
			}
			
			int moveX = (int) (r.nextInt(randomRangeXY)-randomRangeXY/2);
			int moveY = (int) (r.nextInt(randomRangeXY)-randomRangeXY/2);
			
			int moveDistance = r.nextInt(randomRangeDistance)-randomRangeDistance/2;
			
			if(size+moveDistance+trendDistance > c.size) c.size = size + moveDistance + trendDistance;
			
			p.x += (moveDistance+trendDistance) * Math.sin(Math.toRadians(p.angle));
			p.y += (moveDistance+trendDistance) * Math.cos(Math.toRadians(p.angle));
			
			p.x += moveX;
			p.y += moveY;
		}
		
		//Corrections
		for(int i = 0; i < nPoints/4; i++) {
			Point p = area.get(i);
			double factor = (double)i/(nPoints/4);
			int pDistance = (int) ((p.x-xPos) / Math.sin(Math.toRadians(p.angle)));
			if(i == 0) pDistance = (int) ((area.get(i+1).x-xPos) / Math.sin(Math.toRadians(area.get(i+1).angle)));
			
			int newDistance = (int) (((1-factor)*(size+trendDistance))+(factor*pDistance));
			
			p.x = (int) (xPos + (newDistance) * Math.sin(Math.toRadians(p.angle)));
			p.y = (int) (yPos + (newDistance) * Math.cos(Math.toRadians(p.angle)));
			
			int moveDistance = r.nextInt(randomRangeDistance)-randomRangeDistance/2;
			p.x += (moveDistance) * Math.sin(Math.toRadians(p.angle));
			p.y += (moveDistance) * Math.cos(Math.toRadians(p.angle));
			int moveX = (int) (r.nextInt(randomRangeXY)-randomRangeXY/2);
			int moveY = (int) (r.nextInt(randomRangeXY)-randomRangeXY/2);
			p.x += moveX;
			p.y += moveY;
		}
		
		
		
		Point[] areaArray = new Point[area.size()];
		for(int i = 0; i < area.size(); i++) areaArray[i] = area.get(i);
		return areaArray;
	}
	
	/**
	 * Try to generate a polygon by making 4 quarters
	 */
//	private Point[] generateArea3(int xPos, int yPos) {
//		ArrayList<Point> area = new ArrayList<Point>();
//		
//		int gridSize = 1;
//		int width = 300;
//		int height = 300;
//		
//		Point north, east, south, west;
//		
//		north = new Point(r.nextInt(width/4)+width/8*3, 0);
//		east = new Point(width, r.nextInt(height/4)+height/8*3);
//		south = new Point(r.nextInt(width/4)+width/8*3, height);
//		west = new Point(0, r.nextInt(height/4)+height/8*3);
//		
//		int trendStrength = 20;
//		int randomness = 5;
//		//North->East
//		area.add(north);
//		
//		Point currentPoint = north;
//		int nPoints = 10;
//		
//		for(int i = 0; i < nPoints; i++) {
//			
//			int xDiff = east.x-currentPoint.x;
//			int yDiff = east.y-currentPoint.y;
//			double factor = (double)i/nPoints;
//			int pointsLeft = nPoints-i;
//			int optimalX = xDiff/pointsLeft;
//			int optimalY = yDiff/pointsLeft;
//			
//			int xTrend = (int) ((trendStrength*(1-factor))+(0*factor));
//			int yTrend = (int) ((0*(1-factor))+(trendStrength*factor));
//			
//			int xMove = r.nextInt(randomness)+optimalX-2+xTrend;
//			int yMove = r.nextInt(randomness)+optimalY-2+yTrend;
//			
//			System.out.println("XTREND: " + xTrend + ", YTREND: " + yTrend + ", factor: " + factor);
//			System.out.println("OptimalX: " +optimalX + ", realX: " + xMove + ", optimalY: " + optimalY + ", realY: " + yMove);
//			
//			int xMoveReal = (int) ((factor*optimalX)+((1-factor)*xMove));
//			int yMoveReal = (int) ((factor*optimalY)+((1-factor)*yMove));
//			
//			Point newPoint = new Point(currentPoint.x+xMoveReal, currentPoint.y+yMoveReal);
//			if(newPoint.x > east.x) newPoint.x = east.x;
//			if(newPoint.y > east.y) newPoint.y = east.y;
//			area.add(newPoint);
//			currentPoint = newPoint;
//		}
//		
//		area.add(east);
//		currentPoint = east;
//		
//		for(int i = 0; i < nPoints; i++) {
//			
//			int xDiff = south.x-currentPoint.x;
//			int yDiff = south.y-currentPoint.y;
//			double factor = (double)i/nPoints;
//			int pointsLeft = nPoints-i;
//			int optimalX = xDiff/pointsLeft;
//			int optimalY = yDiff/pointsLeft;
//			
//			int xTrend = (int) ((0*(1-factor))+((-trendStrength)*factor));
//			int yTrend = (int) ((trendStrength*(1-factor))+(0*factor));
//			
//			int xMove = r.nextInt(randomness)+optimalX-2+xTrend;
//			int yMove = r.nextInt(randomness)+optimalY-2+yTrend;
//			
//			System.out.println("XTREND: " + xTrend + ", YTREND: " + yTrend + ", factor: " + factor);
//			System.out.println("OptimalX: " +optimalX + ", realX: " + xMove + ", optimalY: " + optimalY + ", realY: " + yMove);
//			
//			int xMoveReal = (int) ((factor*optimalX)+((1-factor)*xMove));
//			int yMoveReal = (int) ((factor*optimalY)+((1-factor)*yMove));
//			
//			Point newPoint = new Point(currentPoint.x+xMoveReal, currentPoint.y+yMoveReal);
//			if(newPoint.x < south.x) newPoint.x = south.x;
//			if(newPoint.y > south.y) newPoint.y = south.y;
//			area.add(newPoint);
//			currentPoint = newPoint;
//		}
//		
//		area.add(south);
//		currentPoint = south;
//		
//		for(int i = 0; i < nPoints; i++) {
//			
//			int xDiff = west.x-currentPoint.x;
//			int yDiff = west.y-currentPoint.y;
//			double factor = (double)i/nPoints;
//			int pointsLeft = nPoints-i;
//			int optimalX = xDiff/pointsLeft;
//			int optimalY = yDiff/pointsLeft;
//			
//			int xTrend = (int) (((-trendStrength)*(1-factor))+(0*factor));
//			int yTrend = (int) ((0*(1-factor))+((-trendStrength)*factor));
//			
//			int xMove = r.nextInt(randomness)+optimalX-2+xTrend;
//			int yMove = r.nextInt(randomness)+optimalY-2+yTrend;
//			
//			System.out.println("XTREND: " + xTrend + ", YTREND: " + yTrend + ", factor: " + factor);
//			System.out.println("OptimalX: " +optimalX + ", realX: " + xMove + ", optimalY: " + optimalY + ", realY: " + yMove);
//			
//			int xMoveReal = (int) ((factor*optimalX)+((1-factor)*xMove));
//			int yMoveReal = (int) ((factor*optimalY)+((1-factor)*yMove));
//			
//			Point newPoint = new Point(currentPoint.x+xMoveReal, currentPoint.y+yMoveReal);
//			if(newPoint.x < west.x) newPoint.x = west.x;
//			if(newPoint.y < west.y) newPoint.y = west.y;
//			area.add(newPoint);
//			currentPoint = newPoint;
//		}
//		
//		area.add(west);
//		currentPoint = west;
//		
//		for(int i = 0; i < nPoints; i++) {
//			
//			int xDiff = north.x-currentPoint.x;
//			int yDiff = north.y-currentPoint.y;
//			double factor = (double)i/nPoints;
//			int pointsLeft = nPoints-i;
//			int optimalX = xDiff/pointsLeft;
//			int optimalY = yDiff/pointsLeft;
//			
//			int xTrend = (int) ((0*(1-factor))+(trendStrength*factor));
//			int yTrend = (int) (((-trendStrength)*(1-factor))+(0*factor));
//			
//			int xMove = r.nextInt(randomness)+optimalX-2+xTrend;
//			int yMove = r.nextInt(randomness)+optimalY-2+yTrend;
//			
//			System.out.println("XTREND: " + xTrend + ", YTREND: " + yTrend + ", factor: " + factor);
//			System.out.println("OptimalX: " +optimalX + ", realX: " + xMove + ", optimalY: " + optimalY + ", realY: " + yMove);
//			
//			int xMoveReal = (int) ((factor*optimalX)+((1-factor)*xMove));
//			int yMoveReal = (int) ((factor*optimalY)+((1-factor)*yMove));
//			
//			Point newPoint = new Point(currentPoint.x+xMoveReal, currentPoint.y+yMoveReal);
//			if(newPoint.x > north.x) newPoint.x = north.x;
//			if(newPoint.y < north.y) newPoint.y = north.y;
//			area.add(newPoint);
//			currentPoint = newPoint;
//		}
//		
//		//apply transformations
//		for(Point p : area) {
//			p.x *= gridSize;
//			p.x += xPos-width*gridSize/2;
//			p.y *= gridSize;
//			p.y += yPos-height*gridSize/2;
//		}
//		
//		Point[] areaArray = new Point[area.size()];
//		for(int i = 0; i < area.size(); i++) areaArray[i] = area.get(i);
//		return areaArray;
//	}
//	
//	/**
//	 * Put the clockwise as parameter
//	 * @param p1
//	 * @param p2
//	 */
//	private ArrayList<Point> generateQuarter(Point p1, Point p2, double startTrendH, double startTrendV, double endTrendH, double endTrendV) {
//		ArrayList<Point> points = new ArrayList<Point>();
//		System.out.println("");
//		int xTrend = 0, yTrend = 0;
//		
//		Point currentPoint = p1;
//		int nPoints = 5;
//		for(int i = 0; i < nPoints; i++) {
//			
//			int xDiff = p2.x-currentPoint.x;
//			int yDiff = p2.y-currentPoint.y;
//			double factor = (double)i/nPoints;
//			int pointsLeft = nPoints-i;
//			int optimalX = xDiff/pointsLeft;
//			int optimalY = yDiff/pointsLeft;
//			
//			xTrend = (int) (((startTrendH*(1-factor))+(endTrendH*factor)));
//			yTrend = (int) (((startTrendV*(1-factor))+(endTrendV*factor)));
////			
//			int xMove = r.nextInt(5)+optimalX-2+xTrend;
//			int yMove = r.nextInt(5)+optimalY-2+yTrend;
//			
//			System.out.println("XTREND: " + xTrend + ", YTREND: " + yTrend + ", factor: " + factor);
//			System.out.println("OptimalX: " +optimalX + ", realX: " + xMove + ", optimalY: " + optimalY + ", realY: " + yMove);
//			
//			int xMoveReal = (int) ((factor*optimalX)+((1-factor)*xMove));
//			int yMoveReal = (int) ((factor*optimalY)+((1-factor)*yMove));
//			
//			
//			
//			Point newPoint = new Point(currentPoint.x+xMove, currentPoint.y+yMove);
//			points.add(newPoint);
//			currentPoint = newPoint;
//		}
//		
//		return points;
//	}
	
	/**
	 * Try making a polygon by taking a point and then taking a random path from there
	 */
//	private Point[] generateArea(int xPos, int yPos) {
//		ArrayList<Point> area = new ArrayList<Point>();
//		
//		int width = r.nextInt(MAX_SIZE-MIN_SIZE)+MIN_SIZE;
//		int height = r.nextInt(MAX_SIZE-MIN_SIZE)+MIN_SIZE;
//		
//		Point currentPoint;
//		Point start = new Point(xPos + r.nextInt(width) - width/2, yPos-height/2);
//		start = new Point(xPos, yPos);
//		area.add(start);
//		currentPoint = start;
//		int currentAngle = 0;
//		int angle = 0;
//		int vertices = 36;
////		int vertices = r.nextInt(MAX_POLYGON_VERTICES-MIN_POLYGON_VERTICES)+MIN_POLYGON_VERTICES;
//		
//		System.out.println(" ");
//		for(int i = 0; i < vertices; i++) {
//			
//			int remVertices = vertices-i;
//			double optAngle = (360-currentAngle)/remVertices;
//			
//			angle = r.nextInt(MAX_POLYGON_ANGLE-MIN_POLYGON_ANGLE)+MIN_POLYGON_ANGLE;
//			double realAngle = (angle+optAngle)/2;
//			currentAngle += realAngle;
//			int distance = r.nextInt(MAX_POLYGON_EDGE_LENGTH-MIN_POLYGON_EDGE_LENGTH)+MIN_POLYGON_EDGE_LENGTH;
//			int newX = (int) (currentPoint.x + distance * Math.sin(Math.toRadians(currentAngle)));
//			int newY = (int) (currentPoint.y + distance * Math.cos(Math.toRadians(currentAngle)));
//			Point p = new Point(newX, newY);
//			currentPoint = p;
//			area.add(p);
//			System.out.println("1:     x:"+newX + ", y:"+newY + ", curAngle :"+currentAngle + ", optAngle: " + optAngle + ", angle: " + angle + ", realAngle: "+ realAngle);
//			
//		}
//		
//		//corrections
//		int endX = (int) currentPoint.x;
//		int endY = (int) currentPoint.y;
//		int corrections = 24;
//		for(int i = 0; i < corrections; i++) {
//			double factor = (corrections-i) / (double)corrections;
//			Point toCorrect = area.get(i);
//			
//			int xDiff = endX - toCorrect.x;
//			int yDiff = endY - toCorrect.y;
//			
//			double xCor = factor*(double)xDiff;
//			double yCor = factor*(double)yDiff;
//			
//			System.out.println("adapt " + i + ":x: " + xCor + " y: " + yCor);
//			
//			toCorrect.x += factor*xDiff;
//			toCorrect.y += factor*yDiff;
//		}
//		
//		Point[] areaArray = new Point[area.size()];
//		for(int i = 0; i < area.size(); i++) areaArray[i] = area.get(i);
//		return areaArray;
//	}
	
	/**
	 * Try making a polygon by starting with a triangle and then adding random triangles to it
	 */
//	private ArrayList<MapElement> generateArea2(int xPos, int yPos) {
//		ArrayList<Point2D> area = new ArrayList<Point2D>();
//		ArrayList<MapElement> el = new ArrayList<MapElement>();
//		
//		int triangles = r.nextInt(MAX_TRIANGLES-MIN_TRIANGLES)+MIN_TRIANGLES;
//		int startSize = 20;
//		
//		ArrayList<Line2D> lines = new ArrayList<Line2D>();
//		
//		lines.add(new Line2D(xPos+startSize/2, yPos-startSize, xPos, yPos));
//		lines.add(new Line2D(xPos, yPos, xPos+startSize, yPos));
//		lines.add(new Line2D(xPos+startSize, yPos, xPos+startSize/2, yPos-startSize));
//		
//		MapElement e = new MapElement();
//		Point2D[] points = {
//				new Point2D(xPos, yPos),
//				new Point2D(xPos+startSize, yPos),
//				new Point2D(xPos+startSize/2, yPos-startSize)
//		};
//		e.setArea(points);
//		e.setBackgroundColor(Color.BEIGE);
//		el.add(e);
//		int vertices = 20;
//		
//		for(int i = 0; i < vertices; i++) {
//			System.out.println("");
//			Line2D l = lines.get(r.nextInt(lines.size()));
//			int xDiff = (int) (l.x2-l.x1);
//			int yDiff = (int) (l.y2-l.y1);
//			double ranW = r.nextDouble();
//			
//			int tmpX = (int) (l.x1+xDiff*ranW);
//			int tmpY = (int) (l.y1+yDiff*ranW);
//			System.out.println("difx:"+xDiff+",ydif:"+yDiff);
//			System.out.println("x:"+tmpX+",y:"+tmpY);
//			int newX = (int) (tmpX-yDiff);
//			int newY = (int) (tmpY+xDiff);
//			
//			Line2D newLine1 = new Line2D(l.x1, l.y1, newX, newY);
//			Line2D newLine2 = new Line2D(newX, newY, l.x2, l.y2);
//			
//			MapElement e2 = new MapElement();
//			Point2D[] points2 = {
//					new Point2D(l.x1, l.y1),
//					new Point2D(l.x2, l.y2),
//					new Point2D(newX, newY)
//			};
//			e2.setArea(points2);
////			e2.setBackgroundColor(new Color((double)(i+1)/vertices, (double)(i+1)/vertices, (double)(i+1)/vertices, 1));
//			e2.setBackgroundColor(Color.BEIGE);
//			el.add(e2);
//			
//			lines.remove(l);
//			
//			lines.add(newLine1);
//			lines.add(newLine2);
//			
//			toRemove = new ArrayList<Line2D>();
//			toAdd = new ArrayList<Line2D>();
//			
//			checkFiller(lines, newLine1, el, (int) l.x1, (int) l.y1, newX, newY);
//			checkFiller(lines, newLine2, el, (int) l.x2, (int) l.y2, newX, newY);
//			
//			lines.addAll(toAdd);
//			lines.removeAll(toRemove);
//			
//			
//		}
//		
////		Point2D[] areaArray = new Point2D[area.size()];
////		for(int i = 0; i < area.size(); i++) areaArray[i] = area.get(i);
//		return el;
//	}
//	
//	private void checkFiller(ArrayList<Line2D> lines, Line2D newLine, ArrayList<MapElement> el, int oldX, int oldY, int newX, int newY) {
//		
//
//		for(Line2D l : lines) {
//			
//			if(newLine != l && !toRemove.contains(l)) {
//			
//				if(oldX == l.x1 && oldY == l.y1) {
//					System.out.println("intersection line found...checking angle");
//					double angle = angleBetween2Lines(newLine, l);
//					System.out.println("angle is " + angle);
//					if(angleCondition(angle)) {
//						System.out.println("#############FILLER TRIANGLE TIME############a");
//						Line2D lineToAdd = new Line2D(newX, newY, l.x2, l.y2);
//						toAdd.add(lineToAdd);
//						toRemove.add(newLine);
//						toRemove.add(l);
//						MapElement e3 = new MapElement();
//						Point2D[] points3 = {
//								new Point2D(newX, newY),
//								new Point2D(oldX, oldY),
//								new Point2D(l.x2, l.y2)
//						};
//						e3.setArea(points3);
//						e3.setBackgroundColor(Color.BEIGE);
//						el.add(e3);
//						
//						checkFiller(lines, lineToAdd, el, newX, newY, (int) l.x2, (int) l.y2);
//						checkFiller(lines, lineToAdd, el, (int) l.x2, (int) l.y2, newX, newY);
//					}
//				}
//				
//				else if(oldX == l.x2 && oldY == l.y2) {
//					System.out.println("intersection line found...checking angle");
//					double angle = angleBetween2Lines(newLine, l);
//					System.out.println("angle is " + angle);
//					if(angleCondition(angle)) {
//						System.out.println("#############FILLER TRIANGLE TIME############b");
//						Line2D lineToAdd = new Line2D(l.x1, l.y1, newX, newY);
//						toAdd.add(lineToAdd);
//						toRemove.add(newLine);
//						toRemove.add(l);
//						MapElement e3 = new MapElement();
//						Point2D[] points3 = {
//								new Point2D(newX, newY),
//								new Point2D(oldX, oldY),
//								new Point2D(l.x1, l.y1)
//						};
//						e3.setArea(points3);
//						e3.setBackgroundColor(Color.BEIGE);
//						el.add(e3);
//						
//						checkFiller(lines, lineToAdd, el, newX, newY, (int) l.x1, (int) l.y1);
//						checkFiller(lines, lineToAdd, el, (int) l.x1, (int) l.y1, newX, newY);
//					}
//				}
//			}
//		}
//		
//	}
//	
//	private boolean angleCondition(double angle) {
//		return ((angle < 270 && angle > 90) || (angle > -270 && angle < -90));
//	}
//	
//	private static double angleBetween2Lines(Line2D line1, Line2D line2)
//	{
//	    double angle1 = Math.atan2(line1.y1 - line1.y2,
//	                               line1.x1 - line1.x2);
//	    double angle2 = Math.atan2(line2.y1 - line2.y2,
//	                               line2.x1 - line2.x2);
//	    return Math.toDegrees(angle1-angle2);
//	}

  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	public City generateCity(int xPos, int yPos) {
		City city = new City();
		Point[] area = generateCityArea(city, xPos, yPos);
		int xSum = 0;
		int ySum = 0;
		for(Point p : area) {
			xSum += p.x;
			ySum += p.y;
		}
		city.xPos = xSum/area.length;
		city.yPos = ySum/area.length;
		city.backgroundColor = CITY_COLOR;
		city.setArea(area);
		
		return city;
	}

  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/

}
