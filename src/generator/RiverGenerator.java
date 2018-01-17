package generator;

import geometry.Point;

import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import map.City;
import map.Road;


public class RiverGenerator {

  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/

	private final int RIVER_SEGMENT_SIZE = 10; //after what distance does river angle change
	private final double RIVER_MAX_CURVE_ANGLE = 10; //how much can river angle change every segmen
	private final double RIVER_SPLIT_ANGLE_RANGE = 90; //i.e. 90 means it splits from 45° to 135°
	private final int INITIAL_RIVERS = 0; //how many river are initially added
	private final double RIVER_SPLIT_CHANCE = 0.03; //0-1
	
	private final Color MAJOR_RIVER_COLOR = new Color(133/255f, 42/255f, 31/255f, 1);
	private final int MAJOR_RIVER_WIDTH = 4;
	private final Color MINOR_RIVER_COLOR = new Color(212/255f, 152/255f, 74/255f, 1);
	private final int MINOR_RIVER_WIDTH = 2;
	
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
//					System.out.println("searching intersection from");
//					System.out.println(roadStartX + "/" + roadStartY + " to " + roadEndX + "/" + roadEndY);
//					System.out.println(currentX + "/" + currentY + " to " + (int) (line.getX()) + "/" + (int) (line.getY()));
					Point p = Generator.getLineSegmentIntersectionPoint(roadStartX, roadStartY, roadEndX, roadEndY, currentX, currentY, (int) (line.getX()), (int) (line.getY()));
					currentX = (int) line.getX();
					currentY = (int) line.getY();
					if(p != null) {
						return p;
					}
				}
			}
		}
		return null;
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
	
	public ArrayList<Road> generate(int width, int height, ArrayList<Lake> lakes) {
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
					
					if(r.x < 0 || r.x > width || r.y < 0 || r.y > height) {
						r.active = false;
						r.wentInactiveThisLoop = true;
					}
					else if(r.path.size() > 1 && roadIntersects(r) != null) {
						Point p = roadIntersects(r);
						r.path.get(r.path.size()-1).xProperty().setValue(p.x);
						r.path.get(r.path.size()-1).yProperty().setValue(p.y);
						r.active = false;
						r.wentInactiveThisLoop = true;
					}
					else {
						if(Math.random() < RIVER_SPLIT_CHANCE && r.path.size() > 2) {
							int negative = Math.random() > 0.5 ? -1 : 1;
							toAdd.add(new Road(r.x, r.y, r.currentAngle+negative*(90+Math.random()*RIVER_SPLIT_ANGLE_RANGE-RIVER_SPLIT_ANGLE_RANGE/2), r.depth+1));
						}
						
						double angleDiff = Math.random()*RIVER_MAX_CURVE_ANGLE*2-RIVER_MAX_CURVE_ANGLE;
						
						r.currentAngle += angleDiff;
						
						double moveX = (int) (RIVER_SEGMENT_SIZE * Math.sin(Math.toRadians(r.currentAngle)));
						double moveY = (int) (RIVER_SEGMENT_SIZE * Math.cos(Math.toRadians(r.currentAngle)));
						
						int newX = r.x += moveX;
						int newY = r.y += moveY;
						
						r.x = newX;
						r.y = newY;
						
						r.path.add(new LineTo(newX, newY));
					}
				}
			}
			roads.addAll(toAdd);
			toAdd.clear();
		}
		
		for(Road r : roads) {
			if(r.depth < 1) {
				r.color = MAJOR_RIVER_COLOR;
				r.width = MAJOR_RIVER_WIDTH;
			}
			else {
				r.color = MINOR_RIVER_COLOR;
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
