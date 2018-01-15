package map;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Map{
	
  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	private int width;
	private int height;
	
	private ArrayList<MapElement> mapElements;
	
	private Color backgroundColor;
	
  /***************************************************************************
   *                                                                         *
   * Constructor                                                             *
   *                                                                         *
   **************************************************************************/
	
	public Map() {
	}
	
  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	public Canvas getMapAsCanvas() {
		Canvas canvas = new Canvas(width, height);
		GraphicsContext context = canvas.getGraphicsContext2D();
		
		//BG
		context.setFill(backgroundColor);
		context.fillRect(0, 0, width, height);
		
		//Elements
		for(MapElement e : mapElements) e.drawOnCanvas(context);
		
		return canvas;
	}
	
  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	
	public void setMapElements(ArrayList<MapElement> mapElements) {
		this.mapElements = mapElements;
	}

}
