package map;

import geometry.Point;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapElement {
	
  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	protected int xPos;
	protected int yPos;
	protected Point[] area;
	protected Color backgroundColor;

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
		context.setFill(backgroundColor);
		context.fillPolygon(xPointsFromArea(), yPointsFromArea(), area.length);
	}

  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/
	
	public void setArea(Point[] area) {
		this.area = area;
	}
	
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}

}
