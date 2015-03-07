package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Frederik
 */
public class LoanManagement extends BorderPane {

    @FXML
    private VBox loanList;

    @FXML
    private GridPane navigationGrid;

    @FXML
    private TextField searchBar;

    @FXML
    private Button loginButton;
    
    @FXML
    private Label authenicatedUserLabel;

    private ScreenSwitcher switcher;

    public LoanManagement(ScreenSwitcher switcher) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "loan_management");
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

    @FXML
    public void onLogin() {

    }

    @FXML
    public void onManageUsers() {
	switcher.openUserManagement();
    }

    @FXML
    public void onImportExcel() {
	switcher.openExcelImport();
    }

    @FXML
    public void onManageLoans() {
	switcher.openLoanManagement();
    }

    @FXML
    public void onManageItems() {
	switcher.openItemManagement();
    }
}
