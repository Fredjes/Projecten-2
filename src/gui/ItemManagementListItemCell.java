package gui;

import domain.Cache;
import domain.Item;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Frederik De Smedt
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
	    listItem = Cache.getItemCache().get(item);
	    if (listItem != null) {
		Platform.runLater(() -> super.setGraphic(listItem));
	    }
	}
    }
}
