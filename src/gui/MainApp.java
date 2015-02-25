package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import persistence.ItemRepository;
import persistence.JPAUtil;

/**
 * Application of the project.
 *
 * @author Frederik
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
	    if (JPAUtil.getInstance().getEntityManagerFactory().isOpen()) {
		ItemRepository.getInstance().saveChanges();
		JPAUtil.getInstance().getEntityManagerFactory().close();
	    }
	}));

	Font.loadFont(getClass().getResourceAsStream("/resources/fonts/FontAwesome.otf"), 14);
	ItemRepository.getInstance().sync();

	ScreenSwitcher switcher = new ScreenSwitcher();
	switcher.openMainMenu();
	primaryStage.setTitle("Mediatheek Applicatie");
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
