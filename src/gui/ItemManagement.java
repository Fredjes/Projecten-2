package gui;

import domain.Book;
import domain.Cd;
import domain.DetailFactory;
import domain.Dvd;
import domain.FilterOption;
import domain.Game;
import domain.Item;
import domain.ObservableListUtil;
import domain.SearchPredicate;
import domain.StoryBag;
import domain.controllers.ItemManagementController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
    private ListView itemList;

    @FXML
    private Button addButton;

    @FXML
    private Button saveButton;

    @FXML
    private ScrollPane listScroller;

    private ItemManagementController controller;
    private SearchPredicate searchPredicate;
    private ObservableList<ItemManagementListItem> filteredList;
    private FilteredList<Item> predicateList;
    private Binding detailView;

    public ItemManagement(ItemManagementController controller) {
	searchPredicate = new SearchPredicate();
	FXUtil.loadFXML(this, "item_management");
	this.controller = controller;
	searchPredicate.searchQueryProperty().bind(searchbar.textProperty());
	searchbar.setOnKeyReleased(e -> updateList());
	updateList();
	itemList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	    detailView.bind(((ItemManagementListItem)newValue).getBackedItem());
	});
    }

    private void updateList() {
	itemList.setItems(FXCollections.observableArrayList());
	predicateList = (FilteredList<Item>) SearchPredicate.filteredListFor((ObservableList<Item>) ItemRepository.getInstance().getItems(), searchPredicate);
	filteredList = ObservableListUtil.convertObservableList(predicateList, ItemManagementListItem.getItemManagementListItemConverter());
	itemList.setItems(filteredList);
    }

    @FXML
    public void onBoek() {
	Object temp = DetailFactory.getDetailPane(FilterOption.BOOK);
	detailView = (Binding<Book>) temp;
	this.setBottom((Node)temp);
	searchPredicate.setSelectedClass(FilterOption.BOOK.getFilterClass());
	updateList();
    }

    @FXML
    public void onSpelletje() {
	Object temp = DetailFactory.getDetailPane(FilterOption.GAME);
	detailView = (Binding<Game>) temp;
	this.setBottom((Node)temp);
	searchPredicate.setSelectedClass(FilterOption.GAME.getFilterClass());
	updateList();
    }

    @FXML
    public void onCd() {
	Object temp = DetailFactory.getDetailPane(FilterOption.CD);
	detailView = (Binding<Cd>) temp;
	this.setBottom((Node)temp);
	searchPredicate.setSelectedClass(FilterOption.CD.getFilterClass());
	updateList();
    }

    @FXML
    public void onDvd() {
	Object temp = DetailFactory.getDetailPane(FilterOption.DVD);
	detailView = (Binding<Dvd>) temp;
	this.setBottom((Node)temp);
	searchPredicate.setSelectedClass(FilterOption.DVD.getFilterClass());
	updateList();
    }

    @FXML
    public void onStoryBag() {
	Object temp = DetailFactory.getDetailPane(FilterOption.STORYBAG);
	detailView = (Binding<StoryBag>) temp;
	this.setBottom((Node)temp);
	searchPredicate.setSelectedClass(FilterOption.STORYBAG.getFilterClass());
	updateList();
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
