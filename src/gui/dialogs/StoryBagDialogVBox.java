package gui.dialogs;

import domain.DisplayUtil;
import domain.ItemCopy;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
public class StoryBagDialogVBox extends VBox {

    @FXML
    private ListView listView;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField searchBox;

    private ObservableList<ItemCopy> filteredDataList;
    private ObservableList<ItemCopy> fullDataList = FXCollections.observableArrayList();
    private ObservableList<ItemCopy> selectedItems = FXCollections.observableArrayList();

    public StoryBagDialogVBox() {
	try {
	    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/resources/gui/StoryBagDialog.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    filteredDataList = listView.getItems();
	    listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    listView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<ItemCopy>() {

		@Override
		public void onChanged(ListChangeListener.Change<? extends ItemCopy> c) {
		    c.next();
		    if (c.wasAdded()) {
			selectedItems.addAll(c.getAddedSubList());
		    } else if (c.wasRemoved()) {
			selectedItems.removeAll(c.getRemoved());
		    }

		    updateInfoLabel();
		}

	    });

	} catch (IOException ex) {
	    System.err.println("Could not load StoryBag Dialog FXML");
	}
    }

    public ObservableList<ItemCopy> getSelectedItems() {
	return listView.getSelectionModel().getSelectedItems();
    }

    public void setItems(ObservableList<ItemCopy> itemCopies) {
	filteredDataList.clear();
	fullDataList.addAll(itemCopies);
	applyFilter();
    }

    public void setSelectedItems(List<ItemCopy> items) {
	selectedItems.addAll(items);
	items.forEach(i -> listView.getSelectionModel().select(i));
	updateInfoLabel();
    }

    @FXML
    private void applyFilter() {
	String text = searchBox.getText();
	Predicate<ItemCopy> variablesContain = DisplayUtil.createPredicateForSearch(text, ItemCopy.class, true);
	Predicate<ItemCopy> itemContains = i -> i.toString().toLowerCase().contains(text.toLowerCase());
	filteredDataList.clear();
	fullDataList.forEach(i -> {
	    if (variablesContain.test(i) || itemContains.test(i)) {
		filteredDataList.add(i);
		if (selectedItems.contains(i)) {
		    listView.getSelectionModel().select(i);
		}
	    }
	});
	updateInfoLabel();
    }

    private void updateInfoLabel() {
	infoLabel.setText(listView.getSelectionModel().getSelectedItems().size() + "/" + filteredDataList.size() + " geselecteerd");
    }
}
