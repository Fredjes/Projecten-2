package gui.controls;

import domain.Damage;
import domain.ItemCopy;
import domain.Loan;
import domain.User;
import gui.FXUtil;
import gui.LoanEvent;
import gui.dialogs.PopupUtil;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import persistence.LoanRepository;
import persistence.UserRepository;

/**
 * A popover that allows all functionality for a given {@link ItemCopy}.
 *
 * @author Frederik De Smedt
 */
public class CopyPopOver extends BorderPane {

    @FXML
    private ChoiceBox<Damage> damage;

    @FXML
    private TextField locationTxt;

    @FXML
    private Label copyNumber;

    @FXML
    private Label locationLabel;

    @FXML
    private Button btnDelete;

    @FXML
    private Button startLoanButton;

    @FXML
    private Label damageLabel;

    @FXML
    private HBox buttonGroup;

    @FXML
    private Label exemplaarTitle;

    private ItemCopy backedCopy;
    private EventHandler<LoanEvent> onDelete;
    private EventHandler<LoanEvent> onStartLoan;

    public CopyPopOver(ItemCopy copy) {
	FXUtil.loadFXML(this, "popover_exemplaar");
	damage.setItems(FXCollections.observableArrayList(Damage.values()));
	this.backedCopy = copy;
	locationTxt.textProperty().bindBidirectional(copy.locationProperty());
	damage.getSelectionModel().select(copy.getDamage());
	copy.damageProperty().unbind();
	damage.getSelectionModel().selectedItemProperty().addListener(i -> copy.setDamage(damage.getSelectionModel().getSelectedItem()));
	copyNumber.textProperty().bindBidirectional(copy.copyNumberProperty());
	locationLabel.setTextFill(Color.BLACK);
	damageLabel.setTextFill(Color.BLACK);
	exemplaarTitle.setTextFill(Color.rgb(108, 122, 137));
	startLoanButton.setDisable(copy.getLoans().stream().anyMatch(i -> !i.getReturned()));
	copy.getObservableLoans().addListener((Observable obs) -> {
	    startLoanButton.setDisable(copy.getLoans().stream().anyMatch(i -> !i.getReturned()));
	});

	copy.damageProperty().addListener((obs, ov, nv) -> {
	    startLoanButton.setDisable(nv == Damage.HIGH_DAMAGE);
	});
    }

    public void update() {
	if (UserRepository.getInstance().getAuthenticatedUser() == null || UserRepository.getInstance().getAuthenticatedUser().getUserType() != User.UserType.TEACHER) {
	    buttonGroup.getChildren().remove(btnDelete);
	    locationTxt.setDisable(true);
	    damage.setDisable(true);
	}
    }

    @FXML
    public void onDelete(ActionEvent event) {
	if (onDelete != null) {
	    LoanEvent evt = new LoanEvent(null);
	    onDelete.handle(evt);
	}
    }

    @FXML
    public void onStartLoan(ActionEvent event) {
	User selectedUser = PopupUtil.showSelectionQuestion(UserRepository.getInstance().getUsersByPredicate(User::getVisible), "Gebruiker selecteren", "Wie leent het voorwerp uit?");
	if (selectedUser == null) {
	    return;
	}

	Loan loan = new Loan(backedCopy, selectedUser);

	LoanRepository.getInstance().add(loan);
	LoanRepository.getInstance().saveChanges();
	backedCopy.getLoans().add(loan);
	loan.getUser().getLoans().add(loan);
	loan.getItemCopy().getLoans().add(loan);

	if (onStartLoan != null) {
	    LoanEvent evt = new LoanEvent(loan);
	    onStartLoan.handle(evt);
	}

	PopupUtil.showNotification("Uitlening", String.format("%s, %s is uitgeleend aan %s.", backedCopy.getItem().getName(), backedCopy.getCopyNumber(), selectedUser.getName()));
    }

    public void setOnDelete(EventHandler<LoanEvent> handler) {
	this.onDelete = handler;
    }

    public void setOnStartLoan(EventHandler<LoanEvent> handler) {
	this.onStartLoan = handler;
    }
}
