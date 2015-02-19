package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import persistence.JPAUtil;

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
	itemIcon.setText(IconConfig.ITEM_ICON);
	logoutIcon.setText(IconConfig.LOGOUT_ICON);
    }

    public void onItemManage() {
	switcher.openItemManagement();
    }

    public void onLogout() {
	JPAUtil.getInstance().getEntityManagerFactory().close();
	System.exit(0);
    }
}