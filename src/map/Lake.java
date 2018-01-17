package map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Lake extends MapElement{
	
	

  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	public Color backgroundColor;
	public Color strokeColor;
	public int strokeWidth;
	public int size;

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

  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	@Override
	public void drawOnCanvas(GraphicsContext context) {
		context.setFill(backgroundColor);
		context.fillPolygon(xPointsFromArea(), yPointsFromArea(), area.length);
		context.setStroke(strokeColor);
		context.setLineWidth(strokeWidth);
		context.strokePolygon(xPointsFromArea(), yPointsFromArea(), area.length);
	}

  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/

}
