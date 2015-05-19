package gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.pdfbox.exceptions.COSVisitorException;
import persistence.ItemRepository;
import persistence.LoanRepository;
import persistence.UserRepository;

/**
 * Application of the project.
 *
 * @author Frederik De Smedt
 */
public class MainApp extends Application {

    public static boolean DEVELOPMENT_MODE = false;

    @Override
    public void start(Stage primaryStage) {
	ItemRepository.getInstance().addSyncListener(() -> {
	    UserRepository.getInstance().addSyncListener(() -> {
		LoanRepository.getInstance().sync();
		try {
		    PdfExporter.saveItems();
		} catch (IOException ex) {
		    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
		}
	    });
	    UserRepository.getInstance().sync();
	});

	ItemRepository.getInstance().sync();

	Font.loadFont(getClass().getResourceAsStream("/resources/fonts/FontAwesome.otf"), 14);
	ScreenSwitcher switcher = new ScreenSwitcher();
	switcher.openItemManagement();
	primaryStage.setTitle("De Lettertuin");
	primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/resources/gui/img/logo_krekel.png")));
	Scene scene = new Scene(switcher);
	primaryStage.setScene(scene);
	primaryStage.show();

//	ScenicView.show(scene);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	DEVELOPMENT_MODE = args.length > 0 && args[0].equals("development");
	if (DEVELOPMENT_MODE) {
	    System.out.println("DEV MODE!");
	}
	launch(args);
    }

}
