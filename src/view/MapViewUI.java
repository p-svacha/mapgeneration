package view;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import map.Map;
import app.MapGeneratorStarter;

/**
 * UI class for displaying the toolbar with the options.
 */
public class MapViewUI extends StackPane {

  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	private ToolbarUI toolbar;
	
  /***************************************************************************
   *                                                                         *
   * Constructor                                                             *
   *                                                                         *
   **************************************************************************/

  public MapViewUI(){
	  initializeSelf();
  }

  /***************************************************************************
   *                                                                         *
   * Private methods                                                         *
   *                                                                         *
   **************************************************************************/

  private void initializeSelf() {
	  setId("mapview");
	  setPrefSize(MapGeneratorStarter.WIDTH/6*5, MapGeneratorStarter.HEIGHT);
  }
  
  /***************************************************************************
   *                                                                         *
   * Public methods                                                          *
   *                                                                         *
   **************************************************************************/
  
  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/
  
  public void setMap(Map map) {
	  getChildren().clear();
	  Canvas canvas = map.getMapAsCanvas();
	  canvas.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
		public void handle(MouseEvent e) {
			toolbar.setCoordinates((int) (e.getX()), (int) (e.getY())); 
		}
	  });
	  getChildren().add(canvas);
  }
  
  public void setToolbar(ToolbarUI toolbar) {
	  this.toolbar = toolbar;
  }
  
}

