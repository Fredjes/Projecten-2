package gui;

import domain.Cache;
import domain.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Frederik
 */
public class UserManagementListItemCell extends ListCell<User> {
    
    private UserManagementListItem listItem;

    public static Callback<ListView<User>, ListCell<User>> forListView() {
	return l -> new UserManagementListItemCell();
    }

    public UserManagementListItemCell() {
    }

    @Override
    protected void updateItem(User item, boolean empty) {
	super.updateItem(item, empty);
	if (super.isEmpty()) {
	    listItem = null;
//	    Platform.runLater(() -> super.setGraphic(null));
	} else {
	    final ChangeListener<Boolean> listener = (obs, ov, nv) -> {
		if (!nv) {
		    super.setGraphic(null);
		} else {
		    super.setGraphic(listItem);
		}
	    };

	    if (listItem != null) {
		listItem.getUser().visibleProperty().removeListener(listener);
	    }

	    listItem = Cache.getUserCache().get(item);
	    listItem.getUser().visibleProperty().addListener(listener);
	    if (listItem != null) {
		if (Platform.isFxApplicationThread()) {
		    if (listItem.getUser().getVisible()) {
			super.setGraphic(listItem);
		    } else {
			super.setGraphic(null);
		    }
		} else {
		    Platform.runLater(() -> {
			if (listItem.getUser()== null || listItem.getUser().getVisible()) {
			    super.setGraphic(listItem);
			} else {
			    super.setGraphic(null);
			}
		    });
		}
	    }
	}
    }
}
