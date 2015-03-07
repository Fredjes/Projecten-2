package gui;

import domain.controllers.MainMenuController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Frederik
 */
public class MainMenu extends BorderPane {

    @FXML
    private Button loginButton;

    @FXML
    private GridPane navigationGrid;
    
    @FXML
    private Label authenicatedUserLabel;

    private ScreenSwitcher switcher;
    private MainMenuController controller;

    public MainMenu(ScreenSwitcher switcher, MainMenuController controller) {
	this.switcher = switcher;
	this.controller = controller;
	FXUtil.loadFXML(this, "main_menu");
    }
    
    public Button getLoginButton() {
	return loginButton;
    }

    public Label getAuthenicatedUserLabel() {
	return authenicatedUserLabel;
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
    
    public GridPane getNavigationGrid(){
	return navigationGrid;
    }

    @FXML
    public void onLogin() {
	this.controller.processLoginRequest(this, loginButton, authenicatedUserLabel);
	this.controller.updateToAuthenticatedUser(this);
    }
}
