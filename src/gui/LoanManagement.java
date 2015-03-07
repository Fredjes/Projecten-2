package gui;

import domain.controllers.LoanManagementController;
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
    private LoanManagementController controller;

    public LoanManagement(ScreenSwitcher switcher, LoanManagementController controller) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "loan_management");
	this.controller = controller;
    }

    public Button getLoginButton() {
	return loginButton;
    }

    public Label getAuthenicatedUserLabel() {
	return authenicatedUserLabel;
    }

    public GridPane getNavigationGrid() {
	return navigationGrid;
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
	this.controller.processLoginRequest(this, loginButton, authenicatedUserLabel);
	this.controller.updateToAuthenticatedUser(this);
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
