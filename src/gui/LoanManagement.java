package gui;

import domain.Loan;
import domain.LocaleConfig;
import domain.SearchPredicate;
import domain.controllers.LoanManagementListItemController;
import gui.dialogs.PopupUtil;
import java.util.Calendar;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import persistence.LoanRepository;

/**
 *
 * @author Frederik De Smedt
 */
public class LoanManagement extends BorderPane {

    @FXML
    private ListView<Loan> loanList;

    @FXML
    private Button returnButton;

    @FXML
    private TextField searchBar;

    private boolean displayAll = false;

    private LoanManagementListItemController controller;

    private FilteredList<Loan> filteredList;
    private SearchPredicate predicate;

    public LoanManagement(LoanManagementListItemController controller) {
	FXUtil.loadFXML(this, "loan_management");
	this.controller = controller;
	predicate = new SearchPredicate(Loan.class, "");
	predicate.searchQueryProperty().bind(searchBar.textProperty());
	filteredList = new FilteredList<>(LoanRepository.getInstance().getLoans());
	loanList.setCellFactory(LoanManagementListItemCell.forListView());
	onSearchQuery();
	loanList.setItems(filteredList);
    }

    @FXML
    public void onSearchQuery() {
	if (displayAll) {
	    filteredList.setPredicate(predicate::test);
	} else {
	    filteredList.setPredicate(p -> predicate.test(p) && !p.getReturned());
	}
    }

    @FXML
    public void onDisplayAllLoans() {
	displayAll = !displayAll;
	onSearchQuery();
    }

    @FXML
    public void onReturnLoan() {
	if (!loanList.getSelectionModel().isEmpty()) {
	    Loan selectedLoan = loanList.getSelectionModel().getSelectedItem();
	    if (!selectedLoan.getReturned()) {
		PopupUtil.showDamageQuestionPopOver(selectedLoan, returnButton, () -> Platform.runLater(() -> {
		    PopupUtil.showNotification("Uitlening teruggebracht", "De uitlening van " + selectedLoan.getUser().getName() + " is terug gebracht.");
		    onSearchQuery();
		}));
	    }
	}
    }

    @FXML
    public void onRenewLoanPeriod() {
	if (!loanList.getSelectionModel().isEmpty()) {
	    Loan selectedLoan = loanList.getSelectionModel().getSelectedItem();
	    Calendar newDate = Calendar.getInstance();
	    newDate.setTime(selectedLoan.getDate().getTime());
	    newDate.add(Calendar.WEEK_OF_MONTH, 1);
	    selectedLoan.setDate(newDate);

	    LoanRepository.getInstance().addSyncListener(() -> {
		Platform.runLater(() -> PopupUtil.showNotification("Verlengd", "De uitlening is verlengd tot " + LocaleConfig.DATE_FORMAT.format(newDate.getTime())));
	    });

	    LoanRepository.getInstance().saveLoan(selectedLoan);
	}
    }
}
