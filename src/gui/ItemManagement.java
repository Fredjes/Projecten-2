package gui;

import domain.Book;
import domain.Cd;
import domain.DetailViewUtil;
import domain.Dvd;
import domain.FilterOption;
import domain.Game;
import domain.Item;
import domain.ObservableListUtil;
import domain.SearchPredicate;
import domain.StoryBag;
import domain.controllers.ItemManagementController;
import gui.dialogs.PopupUtil;
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
import javafx.scene.layout.HBox;
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

    @FXML
    private HBox listCommands;

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
	    try {
		if (newValue == null) {
		    return;
		}

		Item i = ((ItemManagementListItem) newValue).getBackedItem();

		if (i instanceof Book) {
		    Object temp = DetailViewUtil.getDetailPane(FilterOption.BOOK);
		    detailView = (Binding<Book>) temp;
		    this.setBottom((Node) temp);
		} else if (i instanceof Cd) {
		    Object temp = DetailViewUtil.getDetailPane(FilterOption.CD);
		    detailView = (Binding<Cd>) temp;
		    this.setBottom((Node) temp);
		} else if (i instanceof Dvd) {
		    Object temp = DetailViewUtil.getDetailPane(FilterOption.DVD);
		    detailView = (Binding<Dvd>) temp;
		    this.setBottom((Node) temp);
		} else if (i instanceof Game) {
		    Object temp = DetailViewUtil.getDetailPane(FilterOption.GAME);
		    detailView = (Binding<Game>) temp;
		    this.setBottom((Node) temp);
		} else if (i instanceof StoryBag) {
		    Object temp = DetailViewUtil.getDetailPane(FilterOption.STORYBAG);
		    detailView = (Binding<StoryBag>) temp;
		    this.setBottom((Node) temp);
		}

		detailView.bind(i);
	    } catch (Exception ex) {
		ex.printStackTrace();
//		System.err.println("Couldn't bind item: " + ex.getMessage());
	    }
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
	searchPredicate.setSelectedClass(FilterOption.BOOK.getFilterClass());
	updateList();
    }

    @FXML
    public void onSpelletje() {
	searchPredicate.setSelectedClass(FilterOption.GAME.getFilterClass());
	updateList();
    }

    @FXML
    public void onCd() {
	searchPredicate.setSelectedClass(FilterOption.CD.getFilterClass());
	updateList();
    }

    @FXML
    public void onDvd() {
	searchPredicate.setSelectedClass(FilterOption.DVD.getFilterClass());
	updateList();
    }

    @FXML
    public void onStoryBag() {
	searchPredicate.setSelectedClass(FilterOption.STORYBAG.getFilterClass());
	updateList();
    }

    @FXML
    public void onAll() {
	searchPredicate.setSelectedClass(Object.class);
	updateList();
    }

    @FXML
    public void onSave() {
	saveButton.setDisable(true);
	ItemRepository.getInstance().saveChanges();
	ItemRepository.getInstance().sync();
	updateList();
	PopupUtil.showNotification("Opgeslagen", "De wijzigingen zijn succesvol opgeslagen.");
	saveButton.setDisable(false);
    }

    @FXML
    public void onAdd() {
	if (searchPredicate.getSelectedClass() != null) {
	    try {
		Item added = (Item) searchPredicate.getSelectedClass().getConstructor().newInstance();
		ItemRepository.getInstance().add(added);
		updateList();
		ItemManagementListItem listItem = new ItemManagementListItem(added);
		itemList.getItems().add(listItem);
		itemList.getSelectionModel().select(listItem);
	    } catch (Exception ex) {
		PopupUtil.showNotification("Geen type geselecteerd", "Gelieve een type (boek, dvd, verteltas, cd, of spelletje) te selecteren alvorens een voorwerp toe te voegen!", PopupUtil.Notification.WARNING);
	    }
	}
    }

    @FXML
    public void onRemove() {
	if (!itemList.getSelectionModel().isEmpty()) {
	    ItemRepository.getInstance().remove(((ItemManagementListItem) itemList.getSelectionModel().getSelectedItem()).getBackedItem());
	    updateList();
	}
    }

    public Button getSaveButton() {
	return saveButton;
    }

    public HBox getListCommands() {
	return listCommands;
    }

}
