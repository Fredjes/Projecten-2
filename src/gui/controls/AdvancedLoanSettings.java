package gui.controls;

import domain.ItemCopy;
import domain.Loan;
import domain.User;
import gui.FXUtil;
import gui.dialogs.PopupUtil;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import persistence.ItemRepository;
import persistence.LoanRepository;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public class AdvancedLoanSettings extends GridPane {

    @FXML
    private DatePicker endDate;

    @FXML
    private DatePicker startDate;

    private Loan loan;

    public AdvancedLoanSettings(Loan loan) {
	FXUtil.loadFXML(this, "loan_advanced_settings");
	this.loan = loan;
	startDate.valueProperty().set(loan.getStartDate().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	startDate.valueProperty().addListener((obs, ov, nv) -> {
	    if (nv != null) {
		Instant instant = Instant.from(nv.atStartOfDay(ZoneId.systemDefault()));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date.from(instant));
		loan.setStartDate(calendar);
		updateDb(null);
	    }
	});

	endDate.valueProperty().set(loan.getDate().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	endDate.valueProperty().addListener((obs, ov, nv) -> {
	    if (nv != null) {
		Instant instant = Instant.from(nv.atStartOfDay(ZoneId.systemDefault()));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date.from(instant));
		loan.setDate(calendar);
		updateDb(null);
	    }
	});
    }

    @FXML
    public void onChangeUser() {
	User user = PopupUtil.showSelectionQuestion(UserRepository.getInstance().getUsersByPredicate(User::getVisible), "Gebruiker selecteren", "Wie leent het voorwerp uit?");
	if (user != null) {
	    loan.setUser(user);
	    updateDb(() -> PopupUtil.showNotification("Veranderd", "Gebruiker veranderd naar " + user + "."));
	}
    }

    @FXML
    public void onChangeItemCopy() {
	List<ItemCopy> itemCopyLast = new ArrayList<>();
	List<ItemCopy> itemCopies = ItemRepository.getInstance().getItemCopies().stream().filter(ic -> !ic.getLoans().stream().anyMatch(l -> !l.getReturned())).distinct().collect(Collectors.toList());
	itemCopies.stream().forEach(i -> {
	    if (itemCopyLast.stream().noneMatch(ic -> ic.getCopyNumber().equals(i.getCopyNumber()))) {
		itemCopyLast.add(i);
	    }
	});

	LoanRepository.getInstance().getLoans().stream().forEach(l -> {
	    if (!l.getReturned()) {
		itemCopyLast.removeIf(ic -> ic.getCopyNumber().equals(l.getItemCopy().getCopyNumber()));
	    }
	});

	ItemCopy copy = PopupUtil.showSelectionQuestion(FXCollections.observableArrayList(itemCopyLast), "Exemplaar selecteren", "Welk exemplaar wordt uitgeleend?");

	if (copy != null) {
	    loan.getItemCopy().getLoans().remove(loan);
	    ItemRepository.getInstance().saveItemCopy(loan.getItemCopy());
	    copy.getLoans().add(loan);
	    loan.setItemCopy(copy);
	    ItemRepository.getInstance().saveItemCopy(copy);
	    updateDb(() -> PopupUtil.showNotification("Veranderd", "Exemplaar veranderd naar " + copy + "."));
	}
    }

    private void updateDb(Runnable callback) {
	LoanRepository.getInstance().saveLoan(loan);

	if (callback != null) {
	    ItemRepository.getInstance().addSyncListener(() -> Platform.runLater(callback::run));
	}

	ItemRepository.getInstance().sync();
    }
}
