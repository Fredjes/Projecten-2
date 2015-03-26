package gui;

import domain.controllers.MainMenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author Frederik De Smedt
 */
public class MainMenu extends BorderPane {

    private ScreenSwitcher switcher;
    private MainMenuController controller;

    @FXML
    private VBox gebruikersBeheren;

    @FXML
    private Label lblGebruikers;

    @FXML
    private VBox excelImporteren;

    @FXML
    private Label lblExcel;

    @FXML
    private VBox UitleningenBeheren;

    @FXML
    private Label lblUitleningen;

    @FXML
    private VBox voorwerpenBeheren;

    @FXML
    private Label lblVoorwerpen;

    @FXML
    private Text voorwerpenBeherenIcon;

    public Text getVoorwerpenBeherenIcon() {
	return voorwerpenBeherenIcon;
    }

    public Label getLblGebruikers() {
	return lblGebruikers;
    }

    public Label getLblExcel() {
	return lblExcel;
    }

    public Label getLblUitleningen() {
	return lblUitleningen;
    }

    public Label getLblVoorwerpen() {
	return lblVoorwerpen;
    }

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
