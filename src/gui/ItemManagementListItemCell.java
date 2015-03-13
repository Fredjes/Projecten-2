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

    @Override
    protected void updateItem(Item item, boolean empty) {
	this.listItem.setItem(empty ? null : item);
	super.setGraphic(listItem);
    }

}
