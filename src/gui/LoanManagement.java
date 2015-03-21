package gui;

import domain.Loan;
import domain.SearchPredicate;
import domain.controllers.LoanManagementController;
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

    private LoanManagementController controller;
    
    private FilteredList<Loan> filteredList;
    private SearchPredicate predicate;
    
    public LoanManagement(LoanManagementController controller) {
	FXUtil.loadFXML(this, "loan_management");
	this.controller = controller;
	predicate = new SearchPredicate(Loan.class, "");
	filteredList = new FilteredList<>(LoanRepository.getInstance().getLoans());
	loanList.setCellFactory(LoanManagementListItemCell.forListView());
	loanList.setItems(filteredList);
    }

    @FXML
    public void onSearchQuery() {
	filteredList.setPredicate(predicate::test);
    }
    
    @FXML
    public void onDisplayAllLoans(){
    }

    @FXML
    public void onReturnLoan() {
	
    }

    @FXML
    public void onRenewLoanPeriod() {
	
    }
}
