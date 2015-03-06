package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 *
 * @author Frederik
 */
public class MainMenu extends BorderPane {

    @FXML
    private Text logoutIcon;

    @FXML
    private Text itemIcon;
    private ScreenSwitcher switcher;

    public MainMenu(ScreenSwitcher switcher) {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/MainMenu.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();

	    this.switcher = switcher;
	    initIcons();
	} catch (Exception ex) {
	    System.err.println("Could not load main menu: " + ex.getMessage());
	}
    }

    private void initIcons() {
    }

    public void onItemManage() {
	switcher.openItemManagement();
    }

    public void onLogout() {
	System.exit(0);
    }
}
