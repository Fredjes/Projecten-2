package gui;

import domain.controllers.ItemManagementController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button removeButton;

    @FXML
    private VBox itemList;

    @FXML
    private Button addButton;

    @FXML
    private Button saveButton;

    private ScreenSwitcher switcher;
    private ItemManagementController controller;

    public ItemManagement(ScreenSwitcher switcher, ItemManagementController controller) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "item_management");
	this.controller = controller;
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
}
