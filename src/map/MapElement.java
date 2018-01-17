package map;

import geometry.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapElement {
	
  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	public int xPos;
	public int yPos;
	protected Point[] area;

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
	
	protected double[] xPointsFromArea() {
		double[] xPoints = new double[area.length];
		for(int i = 0; i < area.length; i++) {
			xPoints[i] = area[i].x;
		}
		return xPoints;
	}
	
	protected double[] yPointsFromArea() {
		double[] yPoints = new double[area.length];
		for(int i = 0; i < area.length; i++) {
			yPoints[i] = area[i].y;
		}
		return yPoints;
	}

  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	public void drawOnCanvas(GraphicsContext context) {
		System.out.println("If you see this message, you forgot to override drawOnCanvas() in a MapElement.");
	}

  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/
	
	public void setArea(Point[] area) {
		this.area = area;
	}

}
