package gui;

import domain.controllers.MainMenuController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Frederik
 */
public class MainMenu extends BorderPane {

    private ScreenSwitcher switcher;
    private MainMenuController controller;

    @FXML
    private VBox gebruikersBeheren;

    @FXML
    private VBox excelImporteren;

    @FXML
    private VBox UitleningenBeheren;

    @FXML
    private VBox voorwerpenBeheren;

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

    public VBox getGebruikersBeheren() {
	return gebruikersBeheren;
    }

    public VBox getExcelImporteren() {
	return excelImporteren;
    }

    public VBox getUitleningenBeheren() {
	return UitleningenBeheren;
    }

    public VBox getVoorwerpenBeheren() {
	return voorwerpenBeheren;
    }

}
