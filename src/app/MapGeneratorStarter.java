package app;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.ApplicationUI;

public class MapGeneratorStarter extends Application{
	
	//default is 1920x1080
	public static final int SCALE = 2;
	public static final int WIDTH = 1920 / SCALE;
	public static final int HEIGHT = 1080 / SCALE;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent rootPanel = new ApplicationUI();

		Scene scene = new Scene(rootPanel);

		primaryStage.titleProperty().setValue("Map Generator");
		primaryStage.setScene(scene);
		if(SCALE == 1) primaryStage.setFullScreen(true);
		primaryStage.setHeight(HEIGHT);
		primaryStage.setWidth(WIDTH);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
