package domain;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;

/**
 * A JavaFX TableColumn to which a header name can be assigned.
 *
 * @author Frederik
 */
public class AssignableTableColumn extends TableColumn<ExcelData, String> {

    private ChoiceBox<String> selection = new ChoiceBox<>();

    public AssignableTableColumn(int index, ObservableList<String> headerSelectionList) {
	selection.setMaxWidth(Integer.MAX_VALUE);
	setGraphic(selection);
	setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getData(index)));
	bindHeaderSelectionList(headerSelectionList);
    }

    private void bindHeaderSelectionList(ObservableList<String> list) {
	selection.setItems(new SortedList<>(list, (s1, s2) -> s1.compareTo(s2)));
    }

    public boolean hasSelection() {
	return !selection.getSelectionModel().isEmpty();
    }

    public String getSelectedItem() {
	return selection.getSelectionModel().getSelectedItem();
    }

    public void select(String item) {
	if (selection.getItems().contains(item)) {
	    selection.getSelectionModel().select(item);
	}
    }
}
