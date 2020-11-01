package application;
	
import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("controller/view/Main.fxml"));
			Scene scene = new Scene(root,600,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Trabalho Final - Aprendizado de Máquinas");
			Image icon = new Image(getClass().getResourceAsStream("images/UI/Simpsons Icon.jpg"));
			primaryStage.getIcons().add(icon);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
