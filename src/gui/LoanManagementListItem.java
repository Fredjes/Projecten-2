package gui;

import domain.Item;
import domain.Loan;
import domain.LocaleConfig;
import domain.User;
import domain.controllers.LoanManagementListItemController;
import gui.controls.AdvancedLoanSettings;
import gui.dialogs.PopupUtil;
import java.util.Calendar;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import persistence.ItemRepository;

/**
 * A representation of a single {@link Loan}, used by {@link LoanManagement}.
 *
 * @author Frederik De Smedt
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
    private Item item;

    public LoanManagementListItem(Loan loan) {
	FXUtil.loadFXML(this, "listview_loan");

	this.loan = loan;
	new LoanManagementListItemController(this, null);

	loan.itemCopyProperty().addListener((Observable o) -> updateItem());
	loan.userProperty().addListener(this::updateUser);
	loan.dateProperty().addListener(this::updateDate);
	ItemRepository.getInstance().getItems().addListener((Observable o) -> updateItem());
	updateItem();

	loan.dateProperty().addListener((Observable o) -> updateDate());
	updateDate();
    }

    private void updateAll() {
	if (item == null) {
	    return;
	}

	updateCopy();
	updateUser(null, null, loan.getUser());
	updateDate(null, null, loan.getDate());
    }

    private void updateItem() {
	if (loan.getItemCopy().getItem().getId() != 0) {
	    ItemRepository.getInstance().getItems().stream().filter((item) -> (item.getId() == loan.getItemCopy().getItem().getId())).forEach(item -> {
		this.item = item;
		updateAll();
	    });
	} else {
	    ItemRepository.getInstance().saveItem(item); // Method will be called automatically
	}
    }

    private void updateDate() {
	loanPeriod.setText(LocaleConfig.DATE_FORMAT.format(loan.getStartDate().getTime()) + " - " + LocaleConfig.DATE_FORMAT.format(loan.getDate().getTime()));
    }

    private void updateCopy() {
	itemImage.imageProperty().unbind();
	itemName.textProperty().unbind();
	itemImage.imageProperty().bind(item.imageProperty());
	itemName.textProperty().bind(Bindings.concat(item.nameProperty(), ", ", loan.getItemCopy().copyNumberProperty()));
    }

    private void updateUser(Observable user, User oldUser, User newUser) {
	if (oldUser != null) {
	    username.textProperty().unbind();
	}

	if (newUser != null) {
	    username.textProperty().bind(newUser.nameProperty());
	}
    }

    private void updateDate(Observable user, Calendar oldDate, Calendar newDate) {
	toLateWarningLabel.visibleProperty().set(Calendar.getInstance().getTimeInMillis() >= newDate.getTimeInMillis() && !loan.getReturned());
    }

    @FXML
    public void editAdvanced() {
	PopupUtil.showPopOver(editLoanButton, new AdvancedLoanSettings(loan));
    }

    public Button getEditLoanButton() {
	return editLoanButton;
    }
}
