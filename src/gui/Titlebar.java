package gui;

import domain.PdfExporter;
import domain.controllers.TitleBarController;
import gui.dialogs.PopupUtil;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import persistence.SettingsManager;

/**
 * The titlebar shown on top of every screen.
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

    @FXML
    private Button settings;

    @FXML
    private Button btnPdf;

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

    @FXML
    public void onSettings() {
	switcher.openSettings();
    }

    @FXML
    public void onPdf() {
	try {
	    Desktop.getDesktop().open(new File(SettingsManager.INSTANCE.getString("pdfPath")));
	} catch (Exception ex) {
	    Platform.runLater(() -> {
		PopupUtil.showNotification("PDF map niet gevonden!", "De PDF map bestaat niet of kan niet geopend worden.", PopupUtil.Notification.ERROR, Duration.seconds(5));
	    });
	}
    }

    public Button getPdf() {
	return btnPdf;
    }

    public Button getSettings() {
	return settings;
    }

}
