package gui;

import domain.Cache;
import domain.User;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Frederik
 */
public class UserManagementListItemCell extends ListCell<User> {

    public static Callback<ListView<User>, ListCell<User>> forListView() {
	return l -> new UserManagementListItemCell();
    }

    private UserManagementListItem listItem;
    
    @Override
    protected void updateItem(User item, boolean empty) {
	super.updateItem(item, empty);
	if (super.isEmpty()) {
	    listItem = null;
	    super.setGraphic(null);
	} else {
	    listItem = Cache.getUserCache().get(item);
	    Platform.runLater(() -> super.setGraphic(listItem));
	}
    }
}
