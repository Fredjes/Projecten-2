package gui;

import domain.Loan;
import domain.SearchPredicate;
import domain.controllers.LoanManagementController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import persistence.LoanRepository;

/**
 *
 * @author Frederik
 */
public class LoanManagement extends BorderPane {

    @FXML
    private ListView loanList;

    @FXML
    private TextField searchBar;

    private boolean displayAll = false;

    private LoanManagementController controller;

    private FilteredList<Loan> filteredList;
    private SearchPredicate predicate;

    public LoanManagement(LoanManagementController controller) {
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

    }

    @FXML
    public void onRenewLoanPeriod() {

    }
}
