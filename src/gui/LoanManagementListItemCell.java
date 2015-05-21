package gui;

import domain.Cache;
import domain.Loan;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Frederik De Smedt
 */
public class LoanManagementListItemCell extends ListCell<Loan> {

    public static Callback<ListView<Loan>, ListCell<Loan>> forListView() {
	return l -> new LoanManagementListItemCell();
    }

    private LoanManagementListItem listItem;

    @Override
    protected void updateItem(Loan item, boolean empty) {
	super.updateItem(item, empty);
	if (super.isEmpty()) {
	    listItem = null;
	    Platform.runLater(() -> super.setGraphic(null));
	} else {
	    listItem = Cache.getLoanCache().get(item);
	    Platform.runLater(() -> super.setGraphic(listItem));
	}
    }
}
