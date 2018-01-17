package generator;

import geometry.Point;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import map.Lake;
import map.River;


public class RiverGenerator {

  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/

	private final static String DEBUG_PREFIX = "RiverGenerator: ";
	
	private final int RIVER_SEGMENT_SIZE = 10; //after what distance does river angle change
	private final double RIVER_MAX_CURVE_ANGLE = 30; //how much can river angle change every segmen
	private final int RIVER_MIN_SPLIT_ANGLE = 40; //i.e. 90 the two new rivers have min 90° angle between them
	private final int RIVER_MAX_SPLIT_ANGLE = 50; //i.e. 90 the two new rivers have max 90° angle between them
	private final int INITIAL_MAIN_RIVERS = 1; //how many river are initially added
	private final double RIVER_SPLIT_CHANCE = 0.01; //0-1
	private final double RIVER_DRAIN_CHANCE = 0.01; //0-1
	
	private final Color MAJOR_RIVER_COLOR = new Color(112/255f, 199/255f, 244/255f, 1);
	private final int MAJOR_RIVER_WIDTH = 8;
	private final Color MINOR_RIVER_COLOR = new Color(112/255f, 199/255f, 244/255f, 1);
	private final int MINOR_RIVER_WIDTH = 3;
	
	private ArrayList<River> rivers = new ArrayList<River>();

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
	
	private Point riverIntersects(River river) {
		int currentX;
		int currentY;
		
		int riverStartX;
		int riverStartY;
		
		if(river.path.size() == 0) {
			return null;
		}
		else if(river.path.size() == 1) {
			riverStartX = (int) river.startPoint.getX();
			riverStartY = (int) river.startPoint.getY();
		}
		else {
			riverStartX = (int) river.path.get(river.path.size()-2).getX();
			riverStartY = (int) river.path.get(river.path.size()-2).getY();
		}
		
		int riverEndX = (int) river.path.get(river.path.size()-1).getX();
		int riverEndY = (int) river.path.get(river.path.size()-1).getY();
		
		for(River r : rivers) {
			if(river != r  && river.category > r.category && !r.wentInactiveThisLoop) {
				currentX = (int) r.startPoint.getX();
				currentY = (int) r.startPoint.getY();
				for(LineTo line : r.path) {
					Point p = Generator.getLineSegmentIntersectionPoint(riverStartX, riverStartY, riverEndX, riverEndY, currentX, currentY, (int) (line.getX()), (int) (line.getY()));
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
	
	private boolean hasActiveRivers() {
		for(River r : rivers) {
			if(r.active) return true;
		}
		return false;
	}
	
  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	public ArrayList<River> generateRivers(int width, int height, ArrayList<Lake> lakes) {
		rivers.clear();
		ArrayList<River> toAdd = new ArrayList<River>();
		
		for(Lake l : lakes) {
			int initialX = l.xPos;
			int initialY = l.yPos;
			int initialAngle = (int) (Math.random()*360);
			
			rivers.add(new River(initialX, initialY, initialAngle, initialAngle, 1, 0));
		}
		
		for(int i = 0; i < INITIAL_MAIN_RIVERS; i++) {
			int initialX = (int) (Math.random()*width);
			int initialY = (int) (Math.random()*height);
			int initialAngle = (int) (Math.random()*360);
			
			rivers.add(new River(initialX, initialY, initialAngle, initialAngle, 0, 0));
			rivers.add(new River(initialX, initialY, 180+initialAngle, 180+initialAngle, 0, 0));
			System.out.println(DEBUG_PREFIX + "River starting point: " + initialX + "/" + initialY);
		}
		
		
		while(hasActiveRivers()) {
			for(River r : rivers) {
				r.wentInactiveThisLoop = false;
				if(r.active) {
					
					//check out of map bounds
					if(r.x < 0 || r.x > width || r.y < 0 || r.y > height || 
							(r.category > 0 && Math.random() < RIVER_DRAIN_CHANCE)) {
						r.active = false;
						r.wentInactiveThisLoop = true;
					}
					//check intersection with other rivers
					else if(r.path.size() > 1 && riverIntersects(r) != null && !(r.splits == 0 && r.category == 0)) {
						Point p = riverIntersects(r);
						r.path.get(r.path.size()-1).xProperty().setValue(p.x);
						r.path.get(r.path.size()-1).yProperty().setValue(p.y);
						r.active = false;
						r.wentInactiveThisLoop = true;
					}
					else {
						//river split
						if(Math.random() < RIVER_SPLIT_CHANCE && r.path.size() > 2) {
							r.hasSplitted = true;
							int splitAngleRange = RIVER_MAX_SPLIT_ANGLE - RIVER_MIN_SPLIT_ANGLE;
							int angle = (int) (Math.random()*splitAngleRange+RIVER_MIN_SPLIT_ANGLE);
							toAdd.add(new River(r.x, r.y, r.initialAngle, r.currentAngle+(angle/2), r.category, r.splits));
							toAdd.add(new River(r.x, r.y, r.initialAngle, r.currentAngle-(angle/2), r.category, r.splits+1));
							r.active = false;
							r.wentInactiveThisLoop = true;
						}
						
						//add one segment to river
						if(r.active) {
							
							double angleAdjust = 0; //used to avoid cycles in rivers
							
							if(r.currentAngle > r.initialAngle + 180) {
								angleAdjust = -RIVER_MAX_CURVE_ANGLE / 3 * 2;
								System.out.println(DEBUG_PREFIX + "Angle adjustment active at: " + r.x + "/" + r.y);
							}
							else if(r.currentAngle < r.initialAngle - 180) {
								angleAdjust = RIVER_MAX_CURVE_ANGLE / 3 * 2;
								System.out.println(DEBUG_PREFIX + "Angle adjustment active at: " + r.x + "/" + r.y);
							}
							else angleAdjust = 0;
							
							double angleDiff = Math.random()*RIVER_MAX_CURVE_ANGLE*2-RIVER_MAX_CURVE_ANGLE+angleAdjust;
							
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
			}
			rivers.addAll(toAdd);
			toAdd.clear();
		}
		
		for(River r : rivers) {
			if(r.category == 0) {
				r.color = MAJOR_RIVER_COLOR;
				r.width = MAJOR_RIVER_WIDTH;
			}
			else {
				r.color = MINOR_RIVER_COLOR;
				r.width = MINOR_RIVER_WIDTH;
			}
		}

		return rivers;
	}

  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/
	
}
