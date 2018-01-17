package generator;

import geometry.Point;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import map.City;
import map.Lake;
import map.Road;

public class LakeGenerator {
	
  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	private final int LAKE_AMOUNT = 2;
	
	private final Color LAKE_COLOR = new Color(112/255f, 199/255f, 244/255f, 1);
	private final Color LAKE_STROKE_COLOR = new Color(50/255f, 119/255f, 167/255f, 1);
	private final int LAKE_STROKE_WIDTH = 1;
	
	private Random r;
	
	private ArrayList<Lake> lakes = new ArrayList<Lake>();
	
  /***************************************************************************
   *                                                                         *
   * Contructor                                                              *
   *                                                                         *
   **************************************************************************/
	
	public LakeGenerator() {
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
	private Point[] generateLakeArea(Lake lake) {
		ArrayList<Point> area = new ArrayList<Point>();
		
		int nPoints = 8;
		int size = 20;
		int randomRangeDistance = 5;
		int randomRangeXY = 3;
		int trendSpeed = 9;
		
		for(int i = 0; i < nPoints; i++) {
			double angle = (double)i/nPoints*360;
			Point p = new Point((int) (size * Math.sin(Math.toRadians(angle))), (int) (size * Math.cos(Math.toRadians(angle))));
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
			
			if(size + moveDistance+trendDistance > lake.size) lake.size = size + moveDistance + trendDistance;
			
			p.x += (moveDistance+trendDistance) * Math.sin(Math.toRadians(p.angle));
			p.y += (moveDistance+trendDistance) * Math.cos(Math.toRadians(p.angle));
			
			p.x += moveX;
			p.y += moveY;
		}
		
		//Corrections
		for(int i = 0; i < nPoints/4; i++) {
			Point p = area.get(i);
			double factor = (double)i/(nPoints/4);
			int pDistance = (int) ((p.x) / Math.sin(Math.toRadians(p.angle)));
			if(i == 0) pDistance = (int) ((area.get(i+1).x) / Math.sin(Math.toRadians(area.get(i+1).angle)));
			
			int newDistance = (int) (((1-factor)*(size+trendDistance))+(factor*pDistance));
			
			p.x = (int) ((newDistance) * Math.sin(Math.toRadians(p.angle)));
			p.y = (int) ((newDistance) * Math.cos(Math.toRadians(p.angle)));
			
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
	
	private Point searchPosition(int size, int mapWidth, int mapHeight, ArrayList<City> cities, ArrayList<Road> roads) {
		int x = (int) (Math.random()*mapWidth);
		int y = (int) (Math.random()*mapHeight);
		while(!validPosition(x, y, size, mapWidth, mapHeight, cities, roads)) {
			x = (int) (Math.random()*mapWidth);
			y = (int) (Math.random()*mapHeight);
		}
		return new Point(x,y);
	}
	
	private boolean validPosition(int x, int y, int size, int mapWidth, int mapHeight, ArrayList<City> cities, ArrayList<Road> roads) {
		for(City c : cities) {
			int distance = Generator.getDistanceBetweenTwoPoints(x, y, c.xPos, c.yPos);
			if(distance < size + c.size) {
				System.out.println("");
				System.out.println("A city was the problem");
				System.out.println("Lake X: " + x + " Y: " + y + " SIZE: " + size);
				System.out.println("City X: " + c.xPos + " Y: " + c.yPos + " SIZE: " + c.size);
				return false;
			}
		}
		for(Road r : roads) {
			int currentX = (int) r.startPoint.getX();
			int currentY = (int) r.startPoint.getY();
			for(LineTo line : r.path) {
				Point p1 = new Point(currentX, currentY);
				Point p2 = new Point((int) (line.getX()), (int) (line.getY()));
				int distanceP1 = Generator.getDistanceBetweenTwoPoints(x, y, p1.x, p1.y);
				int distanceP2 = Generator.getDistanceBetweenTwoPoints(x, y, p2.x, p2.y);
				int distance = distanceP1 <= distanceP2 ? distanceP1 : distanceP2;
				if(distance < size + r.width) {
					System.out.println("");
					System.out.println("A road was the problem");
					System.out.println("Lake X: " + x + " Y: " + y + " SIZE: " + size);
					System.out.println("Road : " + currentX + "/" + currentY + " to " + (int) (line.getX()) + "/" + (int) (line.getY()));
					return false;
				}
				currentX = (int) line.getX();
				currentY = (int) line.getY();
			}
		}
		for(Lake l : lakes) {
			int distance = Generator.getDistanceBetweenTwoPoints(x, y, l.xPos, l.yPos);
			if(distance < size + l.size) {
				System.out.println("A lake was the problem");
				return false;
			}
		}
		return true;
	}

  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	public ArrayList<Lake> generateLakes(int mapWidth, int mapHeight, ArrayList<City> cities, ArrayList<Road> roads) {
		lakes.clear();
		for(int i = 0; i < LAKE_AMOUNT; i++) {
			Lake lake = new Lake();
			lake.backgroundColor = LAKE_COLOR;
			lake.strokeColor = LAKE_STROKE_COLOR;
			lake.strokeWidth = LAKE_STROKE_WIDTH;
			
			Point[] area = generateLakeArea(lake);
			
			Point position = searchPosition(lake.size, mapWidth, mapHeight, cities, roads);
			System.out.println("position chosen: " + position.x + "/" + position.y);
			
			for(Point p : area) {
				p.x += position.x;
				p.y += position.y;
			}
			
			int xSum = 0;
			int ySum = 0;
			for(Point p : area) {
				xSum += p.x;
				ySum += p.y;
			}
			lake.xPos = xSum/area.length;
			lake.yPos = ySum/area.length;
			
			lake.setArea(area);
			lakes.add(lake);
		}
		
		return lakes;
	}

  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/

}
