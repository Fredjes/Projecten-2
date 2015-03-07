package gui;

import domain.controllers.MainMenuController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Frederik
 */
public class MainMenu extends BorderPane {

    private ScreenSwitcher switcher;
    private MainMenuController controller;

    public MainMenu(ScreenSwitcher switcher, MainMenuController controller) {
	this.switcher = switcher;
	this.controller = controller;
	FXUtil.loadFXML(this, "main_menu");
    }
    
    @FXML
    public void onManageUsers() {
	switcher.openUserManagement();
    }

    @FXML
    public void onImportExcel() {
	switcher.openExcelImport();
    }

    @FXML
    public void onManageLoans() {
	switcher.openLoanManagement();
    }

    @FXML
    public void onManageItems() {
	switcher.openItemManagement();
    }
}
