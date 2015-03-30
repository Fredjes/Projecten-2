package gui;

import domain.Book;
import domain.Cache;
import domain.Cd;
import domain.DetailViewUtil;
import domain.DragUtil;
import domain.Dvd;
import domain.FilterOption;
import domain.Game;
import domain.Item;
import domain.SearchPredicate;
import domain.StoryBag;
import domain.User;
import domain.controllers.ItemManagementController;
import gui.dialogs.PopupUtil;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import persistence.ItemRepository;
import persistence.UserRepository;

/**
 *
 * @author Frederik De Smedt
 */
public class ItemManagement extends BorderPane {

    @FXML
    private TextField searchbar;

    @FXML
    private Button removeButton;

    @FXML
    private ListView<Item> itemList;

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
    private FilteredList<Item> filteredList;
    private Binding detailView;

    @FXML
    private VBox buttonBox;

    public void setController(ItemManagementController cont) {
	controller = cont;
    }

    public ItemManagementController getController() {
	return controller;
    }

    public ItemManagement(ItemManagementController controller) {
	this.controller = controller;
	searchPredicate = new SearchPredicate();
	FXUtil.loadFXML(this, "item_management");
	filteredList = new FilteredList<>((ObservableList<Item>) ItemRepository.getInstance().getItems());
	searchPredicate.searchQueryProperty().bind(searchbar.textProperty());
	itemList.setCellFactory(ItemManagementListItemCell.forListView());
	itemList.setItems(filteredList);

	itemList.setOnDragDetected(e -> {
	    Dragboard db = itemList.startDragAndDrop(TransferMode.LINK);
	    db.setDragView(Cache.getItemCache().get(itemList.getSelectionModel().getSelectedItem()).snapshot(null, null));
	    ClipboardContent content = new ClipboardContent();
	    Item selectedItem = itemList.getSelectionModel().getSelectedItem();
	    content.putString(DragUtil.createItemString(selectedItem));
	    db.setContent(content);
	    e.consume();
	});

	itemList.setOnDragOver(e -> {
	    Dragboard board = e.getDragboard();
	    if (board.hasString() && DragUtil.isRemoveItemDrag(board.getString())) {
		e.acceptTransferModes(TransferMode.MOVE);
	    }
	});
	
	itemList.setOnDragDropped(e -> {
	    Dragboard board = e.getDragboard();
	    if (board.hasString() && DragUtil.isRemoveItemDrag(board.getString())) {
		e.setDropCompleted(true);
		e.consume();
	    }
	});

	itemList.setOnMouseReleased(e -> updateDetailView());
	itemList.setOnKeyReleased(e -> updateDetailView());

	searchbar.setOnKeyReleased((e) -> {
	    updateList();
	});

	updateList();
    }

    private void updateDetailView() {
	try {
	    Item newValue = itemList.getSelectionModel().getSelectedItem();
	    if (newValue == null) {
		return;
	    }

	    User u = UserRepository.getInstance().getAuthenticatedUser();

	    if (u == null || u.getUserType() == null || u.getUserType() == User.UserType.STUDENT || u.getUserType() == User.UserType.VOLUNTEER) {
		return;
	    }

	    if (newValue instanceof Book) {
		Object temp = DetailViewUtil.getDetailPane(FilterOption.BOOK);
		detailView = (Binding<Book>) temp;
		this.setBottom((Node) temp);
	    } else if (newValue instanceof Cd) {
		Object temp = DetailViewUtil.getDetailPane(FilterOption.CD);
		detailView = (Binding<Cd>) temp;
		this.setBottom((Node) temp);
	    } else if (newValue instanceof Dvd) {
		Object temp = DetailViewUtil.getDetailPane(FilterOption.DVD);
		detailView = (Binding<Dvd>) temp;
		this.setBottom((Node) temp);
	    } else if (newValue instanceof Game) {
		Object temp = DetailViewUtil.getDetailPane(FilterOption.GAME);
		detailView = (Binding<Game>) temp;
		this.setBottom((Node) temp);
	    } else if (newValue instanceof StoryBag) {
		Object temp = DetailViewUtil.getDetailPane(FilterOption.STORYBAG);
		detailView = (Binding<StoryBag>) temp;
		this.setBottom((Node) temp);
	    }

	    detailView.bind(newValue);
	} catch (Exception ex) {
	    ex.printStackTrace();
//		System.err.println("Couldn't bind item: " + ex.getMessage());
	}
    }

    public void updateList() {
	filteredList.setPredicate(searchPredicate::test);
    }

    private void refreshSelectedFilter(Object source) {
	for (Node n : buttonBox.getChildren()) {
	    if (n instanceof Button) {
		if (source == n) {
		    n.getStyleClass().remove("item-btn");
		    n.getStyleClass().add("item-btn-selected");
		} else {
		    n.getStyleClass().add("item-btn");
		    n.getStyleClass().remove("item-btn-selected");
		}
	    }
	}
    }

    @FXML
    public void onBoek(ActionEvent evt) {
	refreshSelectedFilter(evt.getSource());
	searchPredicate.setSelectedClass(FilterOption.BOOK.getFilterClass());
	updateList();
    }

    @FXML
    public void onSpelletje(ActionEvent evt) {
	refreshSelectedFilter(evt.getSource());
	searchPredicate.setSelectedClass(FilterOption.GAME.getFilterClass());
	updateList();
    }

    @FXML
    public void onCd(ActionEvent evt) {
	refreshSelectedFilter(evt.getSource());
	searchPredicate.setSelectedClass(FilterOption.CD.getFilterClass());
	updateList();
    }

    @FXML
    public void onDvd(ActionEvent evt) {
	refreshSelectedFilter(evt.getSource());
	searchPredicate.setSelectedClass(FilterOption.DVD.getFilterClass());
	updateList();
    }

    @FXML
    public void onStoryBag(ActionEvent evt) {
	refreshSelectedFilter(evt.getSource());
	searchPredicate.setSelectedClass(FilterOption.STORYBAG.getFilterClass());
	updateList();
    }

    @FXML
    public void onAll(ActionEvent evt) {
	refreshSelectedFilter(evt.getSource());
	searchPredicate.setSelectedClass(Object.class);
	updateList();
    }

    @FXML
    public void onSave() {
	saveButton.setDisable(true);
	ItemRepository.getInstance().saveChanges();
	updateList();
	PopupUtil.showNotification("Opslaan", "De wijzigingen worden opgeslagen...");
	saveButton.setDisable(false);
    }

    @FXML
    public void onAdd() {
	if (searchPredicate.getSelectedClass() != null) {
	    try {
		if (searchPredicate.getSelectedClass().equals(Object.class)) {
		    PopupUtil.showNotification("Geen type geselecteerd", "Gelieve een type (boek, dvd, verteltas, cd, of spelletje) te selecteren alvorens een voorwerp toe te voegen!", PopupUtil.Notification.WARNING);
		    return;
		}

		searchbar.setText("");
		Item added = (Item) searchPredicate.getSelectedClass().getConstructor().newInstance();
		ItemRepository.getInstance().saveItem(added);
		added.setName(" ");
		updateList();
		added.setName("");
		itemList.getSelectionModel().select(added);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }

    @FXML
    public void onRemove() {
	if (!itemList.getSelectionModel().isEmpty()) {
	    ItemRepository.getInstance().remove(itemList.getSelectionModel().getSelectedItem());
	    updateList();
	}
    }

    public Button getSaveButton() {
	return saveButton;
    }

    public HBox getListCommands() {
	return listCommands;
    }

    public ObservableList<Item> getFilteredList() {
	return filteredList;
    }

    public Node getDetailView() {
	return (Node) this.detailView;
    }
}
