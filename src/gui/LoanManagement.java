package gui;

import domain.controllers.LoanManagementController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Frederik
 */
public class LoanManagement extends BorderPane {

    @FXML
    private VBox loanList;

    @FXML
    private TextField searchBar;

    private LoanManagementController controller;

    public LoanManagement(LoanManagementController controller) {
	FXUtil.loadFXML(this, "loan_management");
	this.controller = controller;
    }

    @FXML
    public void onSearchQuery() {

    }

    @FXML
    public void onDisplayAllLoans() {

    }

    @FXML
    public void onReturnLoan() {

    }

    @FXML
    public void onRenewLoanPeriod() {

    }
}
