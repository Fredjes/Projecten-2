package gui;

import domain.controllers.TitleBarController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

/**
 *
 * @author Frederik De Smedt
 */
public class Titlebar extends GridPane {

    @FXML
    private TilePane navigationGrid;

    @FXML
    private Button loginButton;

    @FXML
    private Text title;

    @FXML
    private Label authenicatedUserLabel;

    @FXML
    private HBox voorwerpenBeheren;

    @FXML
    private HBox uitleningenBeheren;

    @FXML
    private HBox gebruikersBeheren;

    @FXML
    private HBox excelImporteren;

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
    }

    @FXML
    public void onHome() {
	switcher.openMainMenu();
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

    public HBox getVoorwerpenBeheren() {
	return voorwerpenBeheren;
    }

    public HBox getUitleningenBeheren() {
	return uitleningenBeheren;
    }

    public HBox getGebruikersBeheren() {
	return gebruikersBeheren;
    }

    public HBox getExcelImporteren() {
	return excelImporteren;
    }

    public TitleBarController getController() {
	return controller;
    }

    void setController(TitleBarController controller) {
	this.controller = controller;
    }

}
