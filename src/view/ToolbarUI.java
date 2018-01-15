package view;

import generator.MapGenerator;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.converter.NumberStringConverter;
import map.Map;
import app.MapGeneratorStarter;

/**
 * UI class for displaying the toolbar with the options.
 */
public class ToolbarUI extends GridPane {

  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	private MapGenerator generator;
	private MapViewUI mapView;
	
	private Label widthLabel;
	private Label heightLabel;
	
	private TextField widthTextField;
	private TextField heightTextField;
	
	private Button generateButton;
	
	private final NumberStringConverter integerConverter = new NumberStringConverter();


  /***************************************************************************
   *                                                                         *
   * Constructor                                                            *
   *                                                                         *
   **************************************************************************/

  public ToolbarUI(MapGenerator generator, MapViewUI mapView){
	  this.generator = generator;
	  this.mapView = mapView;
	  initializeSelf();
	  initializeControls();
	  layoutControls();
	  setupBindings();
  }

  /***************************************************************************
   *                                                                         *
   * Private methods                                                         *
   *                                                                         *
   **************************************************************************/
  
  private void initializeSelf() {
	  setId("toolbar");
	  setPrefSize(MapGeneratorStarter.WIDTH/6, MapGeneratorStarter.HEIGHT);
	  setPadding(new Insets(5,0,0,0));
  }
  
  private void initializeControls() {
	  widthLabel = new Label("Width");
	  heightLabel = new Label("Height");
	  
	  widthTextField = new TextField();
	  heightTextField = new TextField();
	  
	  generateButton = new Button("Generate");
	  generateButton.setOnAction(event -> {
		  Map map = generator.generate();
		  mapView.setMap(map);
	  });
  }
  
  private void layoutControls() {
	  ColumnConstraints col1 = new ColumnConstraints();
	  ColumnConstraints col2 = new ColumnConstraints();
	  col1.setPercentWidth(50);
	  col2.setPercentWidth(50);
	  getColumnConstraints().addAll(col1, col2);
	  
	  add(widthLabel, 0, 0);
	  add(heightLabel, 0, 1);
	  
	  add(widthTextField, 1, 0);
	  add(heightTextField, 1, 1);
	  
	  add(generateButton, 0, 2, 2, 1);
  }
  
  private void setupBindings() {
	  widthTextField.textProperty().bindBidirectional(generator.widthProperty, integerConverter);
	  heightTextField.textProperty().bindBidirectional(generator.heightProperty, integerConverter);
  }
}

