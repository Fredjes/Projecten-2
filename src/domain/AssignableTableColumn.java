package domain;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;

/**
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
	selection.setItems(list);
    }

    public boolean hasSelection() {
	return !selection.getSelectionModel().isEmpty();
    }

    public String getSelectedItem() {
	return selection.getSelectionModel().getSelectedItem();
    }
}
