package map;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

public class River extends MapElement{
	
  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	public MoveTo startPoint;
	public ArrayList<LineTo> path = new ArrayList<LineTo>();
	public int width;
	public Color color;
	
	//Generating parameters
	public int x;
	public int y;
	
	public double trendAngle;
	public double currentAngle;
	
	public int depth; // how many times did roads split up to now
	
	public boolean active;
	public boolean wentInactiveThisLoop; //used when two roads intersect that only one stops working

  /***************************************************************************
   *                                                                         *
   * Contructor                                                              *
   *                                                                         *
   **************************************************************************/
	
	public River(int x, int y, double trendAngle, int depth) {
		this.x = x;
		this.y = y;
		this.trendAngle = trendAngle;
		this.currentAngle = trendAngle;
		this.depth = depth;
		startPoint = new MoveTo(x, y);
		active = true;
	}

  /***************************************************************************
   *                                                                         *
   * Private methods                                                         *
   *                                                                         *
   **************************************************************************/

  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	@Override
	public void drawOnCanvas(GraphicsContext context) {
		
		context.setStroke(color);
		context.setLineWidth(width);
		
		context.beginPath();
		context.moveTo(startPoint.getX(), startPoint.getY());
        for (PathElement pe : path){
                context.lineTo(((LineTo)pe).getX(), ((LineTo)pe).getY());
        }
		context.stroke();
		context.closePath();
	}

  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/

}
