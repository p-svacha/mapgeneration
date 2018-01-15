package view;

import generator.MapGenerator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * The application UI is the base for all UI components, it structures them.
 */
public class ApplicationUI extends StackPane {

  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/

	private MapGenerator generator;
	private ToolbarUI toolbar;
	private MapViewUI mapView;

  /***************************************************************************
   *                                                                         *
   * Constructor                                                             *
   *                                                                         *
   **************************************************************************/

  public ApplicationUI() {
    initializeSelf();
    layoutViews();
  }

  /***************************************************************************
   *                                                                         *
   * Private methods                                                         *
   *                                                                         *
   **************************************************************************/

  private void initializeSelf() {
	  String stylesheet = getClass().getResource("style.css").toExternalForm();
	  getStylesheets().add(stylesheet);
	  
	  generator = new MapGenerator();
	  mapView = new MapViewUI();
	  toolbar = new ToolbarUI(generator, mapView);
  }
  
  private void layoutViews() {
	    HBox hbox = new HBox();
	    hbox.getChildren().addAll(toolbar, mapView);

	    getChildren().add(hbox);
  }
}
