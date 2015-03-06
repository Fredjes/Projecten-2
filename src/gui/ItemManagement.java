package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Frederik
 */
public class ItemManagement extends BorderPane {

    @FXML
    private TextField searchbar;

    @FXML
    private GridPane navigationGrid;

    @FXML
    private Button loginButton;

    @FXML
    private Button removeButton;

    @FXML
    private VBox itemList;
    
    @FXML
    private Button addButton;

    @FXML
    private Button saveButton;
    
    private ScreenSwitcher switcher;

    public ItemManagement(ScreenSwitcher switcher) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "item_management");
    }

    @FXML
    public void onSearch() {

    }

    @FXML
    public void onBoek() {

    }

    @FXML
    public void onSpelletje() {

    }

    @FXML
    public void onCd() {

    }

    @FXML
    public void onDvd() {

    }

    @FXML
    public void onStoryBag() {

    }

    @FXML
    public void onSave() {

    }

    @FXML
    public void onAdd() {

    }

    @FXML
    public void onRemove() {

    }

    @FXML
    public void onLogin() {

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
