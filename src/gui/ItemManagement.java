package gui;

import domain.Book;
import domain.DisplayUtil;
import domain.Game;
import domain.Item;
import domain.ItemCopy;
import domain.StoryBag;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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

    @FXML
    private Text backButton;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView dataTable;

    @FXML
    private ComboBox<ItemClass> itemSelection;

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
	    dataTable.setItems(dataTableList);
	} catch (IOException ioex) {
	    System.err.println("Could not load item mangement interface: " + ioex.getMessage());
	}
    }

    private void initIcons() {
	backButton.setText(IconConfig.BACK_ICON);
    }

    public void onSearch() {

    }

    public void onItemAdd() {
	// Pieter-Jan: hier moet een item van het juiste type gezet worden
    }

    public void onItemChange() {
	Class<?> selectedClass = itemSelection.getSelectionModel().getSelectedItem().getItemClass();
	dataTable.getColumns().setAll(DisplayUtil.getTableColumns(selectedClass));

	if (Item.class.isAssignableFrom(selectedClass)) {
	    // setAll will not trigger tableview update event
	    ObservableList temp = ItemRepository.getInstance().getItemsByClass(selectedClass.asSubclass(Item.class));
	    dataTableList.addAll(temp);
	    dataTableList.retainAll(temp);
	    dataTable.getColumns().setAll(DisplayUtil.getTableColumns(selectedClass));
	} else if (ItemCopy.class.isAssignableFrom(selectedClass)) {
	    ObservableList temp = ItemRepository.getInstance().getItemCopies();
	    dataTableList.addAll(temp);
	    dataTableList.retainAll(temp);
	    dataTable.getColumns().setAll(DisplayUtil.getTableColumns(selectedClass));
	}
    }

    public void onItemDelete() {
	Item selected = (Item) dataTable.getSelectionModel().getSelectedItem();

	if (switcher.openDeletePopup(selected)) {
	    dataTableList.remove(selected);
	}
    }

    public void onBack() {
	ItemRepository.getInstance().saveChanges();
	switcher.openMainMenu();
    }
}
