package view;

import app.MapGeneratorStarter;
import javafx.scene.layout.StackPane;
import map.Map;

/**
 * UI class for displaying the toolbar with the options.
 */
public class MapViewUI extends StackPane {

  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
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
	  getChildren().add(map.getMapAsCanvas());
  }
  
}

