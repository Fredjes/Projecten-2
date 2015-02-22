package gui.dialogs;

import domain.DisplayUtil;
import domain.ItemCopy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class StoryBagDialogVBox extends VBox {

    @FXML
    private ListView listView;

    @FXML
    private Label infoLabel;

    @FXML
    private TextField searchBox;

    private ObservableList<ItemCopy> filteredDataList;
    private ObservableList<ItemCopy> fullDataList = FXCollections.observableArrayList();

    public StoryBagDialogVBox() {
	try {
	    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/resources/gui/StoryBagDialog.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    filteredDataList = listView.getItems();
	    listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	    listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemCopy>() {

		@Override
		public void changed(ObservableValue<? extends ItemCopy> observable, ItemCopy oldValue, ItemCopy newValue) {
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
	items.forEach(i -> listView.getSelectionModel().select(i));
	updateInfoLabel();
    }

    @FXML
    private void applyFilter() {
	String text = searchBox.getText();
	Predicate<ItemCopy> variablesContain = DisplayUtil.createPredicateForSearch(text, ItemCopy.class);
	Predicate<ItemCopy> itemContains = i -> i.toString().toLowerCase().contains(text.toLowerCase());
	List<ItemCopy> selected = new ArrayList(getSelectedItems());
	filteredDataList.clear();
	fullDataList.forEach(i -> {
	    if (variablesContain.test(i) || itemContains.test(i)) {
		filteredDataList.add(i);
		if (selected.contains(i)) {
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
