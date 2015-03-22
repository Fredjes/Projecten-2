package gui;

import domain.Item;
import domain.ItemCopy;
import domain.Loan;
import domain.LocaleConfig;
import domain.User;
import java.util.Calendar;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Frederik
 */
public class LoanManagementListItem extends AnchorPane {

    @FXML
    private Button editLoanButton;

    @FXML
    private Label itemName;

    @FXML
    private Label toLateWarningLabel;

    @FXML
    private Label loanPeriod;

    @FXML
    private ImageView itemImage;

    @FXML
    private Label username;
    
    private Loan loan;

    public LoanManagementListItem(Loan loan) {
	FXUtil.loadFXML(this, "listview_loan");

	this.loan = loan;
	
	loan.itemCopyProperty().addListener(this::updateCopy);
	loan.userProperty().addListener(this::updateUser);
	loan.dateProperty().addListener(this::updateDate);

	updateCopy(null, null, loan.getItemCopy());
	updateUser(null, null, loan.getUser());
	updateDate(null, null, loan.getDate());
	
	loan.dateProperty().addListener((Observable o) -> updateDate());
	updateDate();
    }

    private void updateDate() {
	loanPeriod.setText(LocaleConfig.DATE_FORMAT.format(loan.getStartDate().getTime()) + " - " + LocaleConfig.DATE_FORMAT.format(loan.getDate().getTime()));
    }

    private void updateCopy(Observable obs, ItemCopy oldCopy, ItemCopy newCopy) {
	final ChangeListener<Item> updateItemListener = (o, oi, ni) -> {
	    itemImage.imageProperty().unbind();
	    itemName.textProperty().unbind();
	    itemImage.imageProperty().bind(loan.getItemCopy().getItem().imageProperty());
	    itemName.textProperty().bind(loan.getItemCopy().getItem().nameProperty());
	};

	if (oldCopy != null) {
	    itemName.textProperty().unbind();
	    itemImage.imageProperty().unbind();

	    oldCopy.itemProperty().removeListener(updateItemListener);
	}

	newCopy.itemProperty().addListener(updateItemListener);
	updateItemListener.changed(loan.itemCopyProperty().get().itemProperty(), null, loan.itemCopyProperty().get().itemProperty().get());
    }

    private void updateUser(Observable user, User oldUser, User newUser) {
	if (oldUser != null) {
	    username.textProperty().unbind();
	}

	username.textProperty().bind(newUser.nameProperty());
    }

    private void updateDate(Observable user, Calendar oldDate, Calendar newDate) {
	toLateWarningLabel.visibleProperty().set(Calendar.getInstance().getTimeInMillis() >= newDate.getTimeInMillis());
    }

    @FXML
    public void editAdvanced() {
	throw new UnsupportedOperationException("not implemented yet");
    }
}
