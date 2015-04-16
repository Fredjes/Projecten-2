package gui;

import domain.Cache;
import domain.User;
import javafx.application.Platform;
import javafx.scene.Node;
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

    public UserManagementListItemCell() {
    }

    @Override
    protected void updateItem(User item, boolean empty) {
	super.updateItem(item, empty);
	if (super.isEmpty() || item == null) {
	    setGraphics(null);
	} else {
	    setGraphics(Cache.getUserCache().get(item));
	}
    }

    private void setGraphics(Node n) {
	if (Platform.isFxApplicationThread()) {
	    super.setGraphic(n);
	    super.setText(null);
	} else {
	    Platform.runLater(() -> {
		super.setGraphic(n);
		super.setText(null);
	    });
	}
    }
}
