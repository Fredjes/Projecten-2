package gui;

import domain.Item;
import domain.ObservableListUtil;
import domain.SearchPredicate;
import domain.controllers.ItemManagementController;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import persistence.ItemRepository;

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
    private SearchPredicate searchPredicate;

    public ItemManagement(ScreenSwitcher switcher, ItemManagementController controller) {
	searchPredicate = new SearchPredicate();
	this.switcher = switcher;
	FXUtil.loadFXML(this, "item_management");
	this.controller = controller;
	ItemRepository.getInstance().sync();
	Bindings.bindContent(itemList.getChildren(), ObservableListUtil.convertObservableList(SearchPredicate.filteredListFor((ObservableList<Item>) ItemRepository.getInstance().getItems(), searchPredicate), ItemManagementListItem.getItemManagementListItemConverter()));
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
