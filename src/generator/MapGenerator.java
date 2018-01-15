package generator;

import java.util.ArrayList;
import java.util.Random;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import map.Map;
import map.MapElement;
import app.MapGeneratorStarter;

public class MapGenerator {
	
  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	public IntegerProperty widthProperty = new SimpleIntegerProperty(MapGeneratorStarter.WIDTH/6*5-10);
	public IntegerProperty heightProperty = new SimpleIntegerProperty(MapGeneratorStarter.HEIGHT-30);
	
	private ArrayList<MapElement> cities = new ArrayList<MapElement>();
	
	private CityGenerator cityGenerator;
	
	private Random r;

  /***************************************************************************
   *                                                                         *
   * Contructor                                                              *
   *                                                                         *
   **************************************************************************/
	
	public MapGenerator() {
		cityGenerator = new CityGenerator();
		r = new Random();
	}

  /***************************************************************************
   *                                                                         *
   * Private methods                                                         *
   *                                                                         *
   **************************************************************************/
	
	public static Color randomColor() {
		Random r = new Random();
		double red = r.nextDouble();
		double green = r.nextDouble();
		double blue = r.nextDouble();
		return new Color(red, green, blue, 1);
	}

  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	public Map generate() {
		Map map = new Map();
		cities.clear();
		
		map.setBackgroundColor(Color.BLACK);
		
		for(int i = 0; i < 1;i ++)
		cities.add(cityGenerator.generateCity(400, 300));

		
		ArrayList<MapElement> allElements = new ArrayList<MapElement>();
		allElements.addAll(cities);
		map.setMapElements(allElements);
		
		map.setWidth(widthProperty.getValue());
		map.setHeight(heightProperty.getValue());
		
		return map;
	}

  /***************************************************************************
   *                                                                         *
   * Getters & Setters                                                       *
   *                                                                         *
   **************************************************************************/
}
