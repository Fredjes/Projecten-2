package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Frederik
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
	BorderPane pane = new BorderPane();
	Scene scene = new Scene(pane, 600, 400);
	primaryStage.setScene(scene);
	primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	launch(args);
    }

}
