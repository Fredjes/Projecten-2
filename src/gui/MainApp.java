package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

/**
 * Application of the project.
 *
 * @author Frederik
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
	Font.loadFont(getClass().getResourceAsStream("/resources/fonts/FontAwesome.otf"), 14);
	
	ScreenSwitcher switcher = new ScreenSwitcher();
	switcher.openMainMenu();
	
	Scene scene = new Scene(switcher);
	primaryStage.setScene(scene);
	primaryStage.show();
//	ScenicView.show(scene);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	launch(args);
    }

}
