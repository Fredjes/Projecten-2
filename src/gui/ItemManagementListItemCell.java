package gui;

import domain.Item;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Frederik
 */
public class ItemManagementListItemCell extends ListCell<Item> {

    public static Callback<ListView<Item>, ListCell<Item>> forListView() {
	return i -> new ItemManagementListItemCell();
    }

    private ItemManagementListItem listItem = ItemManagementListItemCache.getInstance().getItemManagementListItem();

    public ItemManagementListItemCell() {
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
	this.listItem.setItem(empty ? null : item);
	super.updateItem(item, empty);
	if (super.isEmpty()) {
	    super.setGraphic(null);
	} else {
	    super.setGraphic(listItem);
	}
    }
    
    @Override
    public void updateSelected(boolean selected) {
	super.updateSelected(selected); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateIndex(int i) {
	super.updateIndex(i);
	if (super.getItem() != null && !super.getListView().getItems().contains(super.getItem())) {
	    super.setGraphic(null);
	}
    }

}
