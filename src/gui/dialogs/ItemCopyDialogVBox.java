package gui.dialogs;

import domain.DisplayUtil;
import domain.Item;
import java.io.IOException;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author Brent
 */
public class ItemCopyDialogVBox extends VBox {

    @FXML
    private ListView<Item> listView;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField searchBox;

    private ObservableList<Item> filteredDataList;
    private ObservableList<Item> fullDataList = FXCollections.observableArrayList();

    private Item selected = null;

    public ItemCopyDialogVBox() {
	try {
	    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/resources/gui/StoryBagDialog.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    filteredDataList = listView.getItems();
	    listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Item>() {

		@Override
		public void changed(ObservableValue<? extends Item> observable, Item oldValue, Item newValue) {
		    //selected = newValue;
		    updateInfoLabel();
		}

	    });
	} catch (IOException ex) {
	    System.err.println("Could not load StoryBag Dialog FXML");
	}
    }

    public Item getSelectedItem() {
	if (listView.getSelectionModel().isEmpty()) {
	    return null;
	}
	return listView.getSelectionModel().getSelectedItem();
    }

    public void setItems(ObservableList<? extends Item> items) {
	filteredDataList.clear();
	fullDataList.addAll(items);
	applyFilter();
    }

    public void setSelectedItem(Item i) {
	listView.getSelectionModel().select(i);
	updateInfoLabel();
    }

    @FXML
    private void applyFilter() {
	String text = searchBox.getText();
	Predicate<Item> variablesContain = DisplayUtil.createPredicateForSearch(text, Item.class, true);
	Predicate<Item> itemContains = i -> i.toString().toLowerCase().contains(text.toLowerCase());
	selected = getSelectedItem() == null ? selected : getSelectedItem();
	filteredDataList.clear();
	fullDataList.forEach(i -> {
	    if (variablesContain.test(i) || itemContains.test(i)) {
		filteredDataList.add(i);
		if (selected == i) {
		    listView.getSelectionModel().select(i);
		}
	    }
	});
	updateInfoLabel();
    }

    private void updateInfoLabel() {
	infoLabel.setText("Geselecteerd: " + (getSelectedItem() == null ? "Geen" : getSelectedItem().toString()));
    }
}
