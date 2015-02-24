package gui;

import domain.Book;
import domain.DisplayUtil;
import domain.Game;
import domain.Item;
import domain.ItemCopy;
import domain.StoryBag;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import persistence.ItemRepository;

/**
 *
 * @author Frederik
 */
public class ItemManagement extends BorderPane {

    private ScreenSwitcher switcher;

    private ObservableList<ItemClass> itemSelectionList = FXCollections.observableArrayList(Arrays.asList(new ItemClass[]{
	new ItemClass("Boeken", Book.class),
	new ItemClass("Spelletjes", Game.class),
	new ItemClass("Verteltassen", StoryBag.class),
	new ItemClass("Exemplaren", ItemCopy.class)
    }));

    private ObservableList dataTableList = FXCollections.observableArrayList();
    private ObservableList filteredDataTableList = FXCollections.observableArrayList(dataTableList);

    @FXML
    private Text backButton;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView dataTable;

    @FXML
    private ComboBox<ItemClass> itemSelection;

    @FXML
    private Button manageStoryBagButton;

    @FXML
    private Button connectItemButton;

    @FXML
    private GridPane bodyGrid;

    @FXML
    private HBox buttonHeader;

    public ItemManagement(ScreenSwitcher switcher) {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/ItemManagement.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();

	    this.switcher = switcher;
	    initIcons();
	    itemSelectionList.sort((a, b) -> a.toString().compareTo(b.toString()));
	    itemSelection.setItems(itemSelectionList);
	    itemSelection.getSelectionModel().select(0);
	    dataTableList.addListener((ListChangeListener.Change c) -> onSearch());
	    onItemChange();
	} catch (IOException ioex) {
	    System.err.println("Could not load item management interface: " + ioex.getMessage());
	}
    }

    private void initIcons() {
	backButton.setText(IconConfig.BACK_ICON);
    }

    public void onSearch() {
	// New predicate each time to enforce update
	Predicate p = DisplayUtil.createPredicateForSearch(searchBar.getText(), itemSelection.getSelectionModel().getSelectedItem().getItemClass(), false);
	filteredDataTableList.clear();
	Class<?> currentClass = itemSelection.getSelectionModel().getSelectedItem().getItemClass();
	dataTableList.forEach(i -> {
	    if (currentClass.isAssignableFrom(i.getClass()) && p.test(i)) {
		filteredDataTableList.add(i);
	    }
	});
    }

    public void onManageStoryBag() {
	if (!dataTable.getSelectionModel().isEmpty()) {
	    switcher.openManageItemsPopup((StoryBag) dataTable.getSelectionModel().getSelectedItem());
	}
    }

    public void onConnectItem() {
	if (!dataTable.getSelectionModel().isEmpty()) {
	    switcher.openSelectItemPopup((ItemCopy) dataTable.getSelectionModel().getSelectedItem());
	    onSearch();
	}
    }

    public void onItemAdd() {
	// Pieter-Jan: hier moet een item van het juiste type gezet worden
	searchBar.setText("");
	onSearch();

	try {
	    Object obj = itemSelection.getSelectionModel().getSelectedItem().getItemClass().getConstructor((Class<?>[]) null).newInstance();

	    if (obj instanceof ItemCopy) {
		ItemRepository.getInstance().add((ItemCopy) obj);
	    } else {
		ItemRepository.getInstance().add((Item) obj);
	    }

	    dataTableList.add(obj);
	    dataTable.edit(dataTableList.size() - 1, (TableColumn) dataTable.getColumns().get(0));
	} catch (Exception ex) {
	}
    }

    public void onItemChange() {
	Class<?> selectedClass = itemSelection.getSelectionModel().getSelectedItem().getItemClass();
	dataTable = new TableView();
	dataTable.setItems(filteredDataTableList);
	dataTable.setFixedCellSize(60);
	dataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	this.bodyGrid.add(dataTable, 0, 1);

	if (Item.class.isAssignableFrom(selectedClass)) {
	    // setAll will not trigger tableview update event
	    ObservableList temp = ItemRepository.getInstance().getItemsByClass(selectedClass.asSubclass(Item.class));
	    dataTableList.setAll(temp);
	    dataTable.getColumns().setAll(DisplayUtil.getTableColumns(selectedClass));
	} else if (ItemCopy.class.isAssignableFrom(selectedClass)) {
	    ObservableList temp = ItemRepository.getInstance().getItemCopies();
	    dataTableList.setAll(temp);
	    onSearch();
	    dataTable.getColumns().setAll(DisplayUtil.getTableColumns(selectedClass));
	}

	dataTable.setEditable(true);

	if (DisplayUtil.isStoryBag(selectedClass)) {
	    buttonHeader.getChildren().add(manageStoryBagButton);
	} else {
	    buttonHeader.getChildren().remove(manageStoryBagButton);
	}

	if (DisplayUtil.isItemCopy(selectedClass)) {
	    buttonHeader.getChildren().add(connectItemButton);
	} else {
	    buttonHeader.getChildren().remove(connectItemButton);
	}
    }

    public void onItemDelete() {
	Object selected = dataTable.getSelectionModel().getSelectedItem();

	if (switcher.openDeletePopup(selected)) {
	    if (selected instanceof Item) {
		Item item = (Item) selected;
		ObservableList<ItemCopy> itemCopies = ItemRepository.getInstance().getItemCopiesByPredicate(ic -> ic.getItem().equals(item));
		if (itemCopies.size() > 0) {
		    try {
			itemCopies.forEach((ic) -> {
			    ItemRepository.getInstance().remove(ic, false);
			});
		    } catch (NoSuchElementException nsex) {
		    }
		}

		ItemRepository.getInstance().remove((Item) selected);
	    } else if (selected instanceof ItemCopy) {
		ItemRepository.getInstance().remove((ItemCopy) selected);
	    }
	    dataTableList.remove(selected);
	    filteredDataTableList.remove(selected);
	}
    }

    public void onBack() {
	ItemRepository.getInstance().saveChanges();
	switcher.openMainMenu();
    }
}
