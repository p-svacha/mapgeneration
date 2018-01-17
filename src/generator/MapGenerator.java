package generator;

import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import map.City;
import map.Lake;
import map.Map;
import map.MapElement;
import map.River;
import map.Road;
import app.MapGeneratorStarter;

public class MapGenerator {
	
  /***************************************************************************
   *                                                                         *
   * Fields                                                                  *
   *                                                                         *
   **************************************************************************/
	
	public IntegerProperty widthProperty = new SimpleIntegerProperty(MapGeneratorStarter.WIDTH/6*5-10);
	public IntegerProperty heightProperty = new SimpleIntegerProperty(MapGeneratorStarter.HEIGHT-30);
	
	private ArrayList<City> cities = new ArrayList<City>();
	private ArrayList<Road> roads = new ArrayList<Road>();
	private ArrayList<Lake> lakes = new ArrayList<Lake>();
	private ArrayList<River> rivers = new ArrayList<River>();
	
	private CityGenerator cityGenerator;
	private RoadGenerator roadGenerator;
	private LakeGenerator lakeGenerator;
	private RiverGenerator riverGenerator;
	
  /***************************************************************************
   *                                                                         *
   * Contructor                                                              *
   *                                                                         *
   **************************************************************************/
	
	public MapGenerator() {
		cityGenerator = new CityGenerator();
		roadGenerator = new RoadGenerator();
		lakeGenerator = new LakeGenerator();
		riverGenerator = new RiverGenerator();
	}

  /***************************************************************************
   *                                                                         *
   * Public Methods                                                          *
   *                                                                         *
   **************************************************************************/
	
	public Map generate() {
		int width = widthProperty.getValue();
		int height = heightProperty.getValue();
		Map map = new Map();
		cities.clear();
		roads.clear();
		lakes.clear();
		rivers.clear();
		
		map.setBackgroundColor(new Color(234/255f, 242/255f, 229/255f, 1));
		
		for(int i = 0; i < 12;i ++) {
			boolean tooClose = false;
			int x, y;
			do{
				x = (int) (Math.random()*width);
				y = (int) (Math.random()*height);
				tooClose = false;
				for(City c : cities) {
					if(Generator.getDistanceBetweenTwoPoints(x, y, c.xPos, c.yPos) < 100) {
						tooClose = true;
					}
				}
				
			} while(tooClose);
			
			cities.add(cityGenerator.generateCity(x, y));
		}
		
		roads.addAll(roadGenerator.generate(width, height, cities));
		lakes.addAll(lakeGenerator.generateLakes(width, height, cities, roads));
		rivers.addAll(riverGenerator.generateRivers(width, height, lakes));

		
		ArrayList<MapElement> allElements = new ArrayList<MapElement>();
		
		allElements.addAll(cities);
		allElements.addAll(rivers);
		allElements.addAll(roads);
		allElements.addAll(lakes);
		
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
