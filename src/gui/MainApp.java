package gui;

import gui.dialogs.PopupUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import persistence.ItemRepository;
import persistence.LoanRepository;
import persistence.Repository;
import persistence.UserRepository;

/**
 * Application of the project.
 *
 * @author Frederik De Smedt
 */
public class MainApp extends Application {

    public static boolean DEVELOPMENT_MODE = false;

    public static final String ADMIN_PASSWORD = ".gnvxKQ5Y";
    private boolean otherLoaded = false;

    @Override
    public void start(Stage primaryStage) {
	Runnable loadLoans = () -> {
	    if (!otherLoaded) {
		otherLoaded = true;
		return;
	    }

	    LoanRepository.getInstance().sync();
	};
	
	UserRepository.getInstance().addSyncListener(loadLoans);
	ItemRepository.getInstance().addSyncListener(loadLoans);
	ItemRepository.getInstance().sync();
	UserRepository.getInstance().sync();

	Font.loadFont(getClass().getResourceAsStream("/resources/fonts/FontAwesome.otf"), 14);
	ScreenSwitcher switcher = new ScreenSwitcher();
	switcher.openItemManagement();
	primaryStage.setTitle("De Lettertuin");
	primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/resources/gui/img/logo_krekel.png")));
	Scene scene = new Scene(switcher);
	primaryStage.setScene(scene);
	primaryStage.show();
	primaryStage.setOnCloseRequest(evt -> {
	    if (PopupUtil.confirm("Wijzingen opslaan", "Wilt u al de wijzigingen opslaan?")) {
		Repository.saveAllChanges();
	    }
	});

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
