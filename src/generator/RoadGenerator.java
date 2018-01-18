package generator;

import geometry.Point;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import map.City;
import map.Road;


public class RoadGenerator {

  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/

	private final static String DEBUG_PREFIX = "RoadGenerator: ";
	
	private final int ROAD_SEGMENT_SIZE = 10; //after what distance does road angle change
	private final double ROAD_MAX_CURVE_ANGLE = 10; //how much can road angle change every segmen
	private final double ROAD_SPLIT_ANGLE_RANGE = 90; //i.e. 90 means it splits from 45° to 135°
	private final int INITIAL_ADDITIONAL_ROADS = 0; //how many roads are initially added besides city roads
	private final double ROAD_SPLIT_CHANCE = 0.02; //0-1
	private final int ROAD_MERGE_DISTANCE = 10; //distance where close roads merge
	
	private final Color MAJOR_ROAD_COLOR = new Color(133/255f, 42/255f, 31/255f, 1);
	private final int MAJOR_ROAD_WIDTH = 4;
	private final Color MINOR_ROAD_COLOR = new Color(212/255f, 152/255f, 74/255f, 1);
	private final int MINOR_ROAD_WIDTH = 2;
	
	private ArrayList<Road> roads = new ArrayList<Road>();

  /***************************************************************************
   *                                                                         *
   * Contructor                                                              *
   *                                                                         *
   **************************************************************************/

  /***************************************************************************
   *                                                                         *
   * Private methods                                                         *
   *                                                                         *
   **************************************************************************/
	
	private Point roadIntersects(Road road) {
		int currentX;
		int currentY;
		
		int roadStartX;
		int roadStartY;
		
		if(road.path.size() == 0) {
			return null;
		}
		else if(road.path.size() == 1) {
			roadStartX = (int) road.startPoint.getX();
			roadStartY = (int) road.startPoint.getY();
		}
		else {
			roadStartX = (int) road.path.get(road.path.size()-2).getX();
			roadStartY = (int) road.path.get(road.path.size()-2).getY();
		}
		
		int roadEndX = (int) road.path.get(road.path.size()-1).getX();
		int roadEndY = (int) road.path.get(road.path.size()-1).getY();
		
		for(Road r : roads) {
			if(road != r  && !(road.depth == 0 && r.depth > 0) && !r.wentInactiveThisLoop) {
				currentX = (int) r.startPoint.getX();
				currentY = (int) r.startPoint.getY();
				for(LineTo line : r.path) {
					Point p = Generator.getLineSegmentIntersectionPoint(roadStartX, roadStartY, roadEndX, roadEndY, currentX, currentY, (int) (line.getX()), (int) (line.getY()));
					currentX = (int) line.getX();
					currentY = (int) line.getY();
					if(p != null) {
						System.out.println(DEBUG_PREFIX + "Roads intersecting at " + currentX + "/" + currentY);
						return p;
					}
				}
			}
		}
		return null;
	}
	
	private void checkRoadMerge(Road road) {
		for(Road r : roads) {
			if(road != r) {
				int currentX = (int) r.startPoint.getX();
				int currentY = (int) r.startPoint.getY();
				for(LineTo line : r.path) {
					int distance = Generator.getDistanceBetweenTwoPoints(road.x, road.y, currentX, currentY);
					if(distance < ROAD_MERGE_DISTANCE) {
						System.out.println(DEBUG_PREFIX + "Roads merging at " + currentX + "/" + currentY);
						LineTo lineToMerge = road.path.get(road.path.size()-1);
						lineToMerge.xProperty().setValue(currentX);
						lineToMerge.yProperty().setValue(currentY);
						road.active = false;
					}
					currentX = (int) line.getX();
					currentY = (int) line.getY();
				}
			}
		}
	}
	
	private boolean hasActiveRoads() {
		for(Road r : roads) {
			if(r.active) return true;
		}
		return false;
	}
	
  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	public ArrayList<Road> generate(int width, int height, ArrayList<City> cities) {
		System.out.println("");
		roads.clear();
		ArrayList<Road> toAdd = new ArrayList<Road>();
		
		for(City c : cities) {
			int initialX = c.xPos;
			int initialY = c.yPos;
			int initialAngle = (int) (Math.random()*360);
			
			roads.add(new Road(initialX, initialY, initialAngle, 0));
			roads.add(new Road(initialX, initialY, 180+initialAngle, 0));
		}
		
		for(int i = 0; i < INITIAL_ADDITIONAL_ROADS; i++) {
			int initialX = (int) (Math.random()*width);
			int initialY = (int) (Math.random()*height);
			int initialAngle = (int) (Math.random()*360);
			
			roads.add(new Road(initialX, initialY, initialAngle, 0));
			roads.add(new Road(initialX, initialY, 180+initialAngle, 0));
		}
		
		
		while(hasActiveRoads()) {
			for(Road r : roads) {
				r.wentInactiveThisLoop = false;
				if(r.active) {
					
					//check out of bounds
					if(r.x < 0 || r.x > width || r.y < 0 || r.y > height) {
						r.active = false;
						r.wentInactiveThisLoop = true;
					}
					
					//check road intersection
					else if(r.path.size() > 1 && roadIntersects(r) != null) {
						Point p = roadIntersects(r);
						r.path.get(r.path.size()-1).xProperty().setValue(p.x);
						r.path.get(r.path.size()-1).yProperty().setValue(p.y);
						r.active = false;
						r.wentInactiveThisLoop = true;
					}
					else {
						
						//check road merge
						if(r.path.size() > 5 && r.active) checkRoadMerge(r);
						
						if(r.active) {
							//road split
							if(Math.random() < ROAD_SPLIT_CHANCE && r.path.size() > 2) {
								int negative = Math.random() > 0.5 ? -1 : 1;
								toAdd.add(new Road(r.x, r.y, r.currentAngle+negative*(90+Math.random()*ROAD_SPLIT_ANGLE_RANGE-ROAD_SPLIT_ANGLE_RANGE/2), r.depth+1));
							}
							
							double angleDiff = Math.random()*ROAD_MAX_CURVE_ANGLE*2-ROAD_MAX_CURVE_ANGLE;
							
							r.currentAngle += angleDiff;
							
							double moveX = (int) (ROAD_SEGMENT_SIZE * Math.sin(Math.toRadians(r.currentAngle)));
							double moveY = (int) (ROAD_SEGMENT_SIZE * Math.cos(Math.toRadians(r.currentAngle)));
							
							int newX = r.x += moveX;
							int newY = r.y += moveY;
							
							r.x = newX;
							r.y = newY;
							
							r.path.add(new LineTo(newX, newY));
						}
					}
				}
			}
			roads.addAll(toAdd);
			toAdd.clear();
		}
		
		for(Road r : roads) {
			if(r.depth < 1) {
				r.color = MAJOR_ROAD_COLOR;
				r.width = MAJOR_ROAD_WIDTH;
			}
			else {
				r.color = MINOR_ROAD_COLOR;
				r.width = MINOR_ROAD_WIDTH;
			}
		}

		Collections.reverse(roads);
		return roads;
	}

  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/
	
}
