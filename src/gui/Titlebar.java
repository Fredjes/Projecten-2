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

    @FXML
    private Button voorwerpenBeheren;

    @FXML
    private Button uitleningenBeheren;

    @FXML
    private Button gebruikersBeheren;

    @FXML
    private Button excelImporteren;

    private ScreenSwitcher switcher;
    private TitleBarController controller;

    public Titlebar(ScreenSwitcher switcher, TitleBarController controller) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "titlebar");
	this.controller = controller;
    }

    Titlebar(ScreenSwitcher switcher) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "titlebar");
    }

    public void setTitle(String title) {
	this.title.textProperty().set(title);
    }

    @FXML
    public void onLogin() {
	this.switcher.processLoginRequest(loginButton, authenicatedUserLabel);
	this.controller.updateToAuthenticatedUser();
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

    public Button getVoorwerpenBeheren() {
	return voorwerpenBeheren;
    }

    public Button getUitleningenBeheren() {
	return uitleningenBeheren;
    }

    public Button getGebruikersBeheren() {
	return gebruikersBeheren;
    }

    public Button getExcelImporteren() {
	return excelImporteren;
    }

    public TitleBarController getController() {
	return controller;
    }

    void setController(TitleBarController controller) {
	this.controller = controller;
    }

}
