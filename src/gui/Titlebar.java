package gui;

import domain.controllers.TitleBarController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author Frederik
 */
public class Titlebar extends GridPane {

    @FXML
    private GridPane navigationGrid;

    @FXML
    private Button loginButton;

    @FXML
    private Text title;

    @FXML
    private Label authenicatedUserLabel;
    
    private ScreenSwitcher switcher;
    private TitleBarController controller;

    public Titlebar(ScreenSwitcher switcher, TitleBarController controller) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "titlebar");
	this.controller = controller;
    }
    

    @FXML
    public void onLogin() {
	this.switcher.processLoginRequest(loginButton, authenicatedUserLabel);
	this.controller.updateToAuthenticatedUser(this);
    }

    @FXML
    public void onManageItems() {
	switcher.openItemManagement();
    }

    @FXML
    public void onManageLoans() {
	switcher.openLoanManagement();
    }

    @FXML
    public void onManageUsers() {
	switcher.openUserManagement();
    }

    @FXML
    public void onImportExcel() {
	switcher.openExcelImport();
    }
}
