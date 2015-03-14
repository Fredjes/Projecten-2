package gui;

import domain.Cache;
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

    private ItemManagementListItem listItem;

    public ItemManagementListItemCell() {
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
	super.updateItem(item, empty);
	if (super.isEmpty()) {
	    listItem = null;
	    super.setGraphic(null);
	} else {
	    listItem = Cache.getListItemCache().get(item);
	    super.setGraphic(listItem);
	}
    }
    
    @Override
    public void updateSelected(boolean selected) {
	super.updateSelected(selected);
    }

    @Override
    public void updateIndex(int i) {
	super.updateIndex(i);
	if (super.getItem() != null && !super.getListView().getItems().contains(super.getItem())) {
	    super.setGraphic(null);
	}
    }

}
