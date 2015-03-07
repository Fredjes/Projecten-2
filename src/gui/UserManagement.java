package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Frederik
 */
public class UserManagement extends BorderPane {

    private ScreenSwitcher switcher;
    
    @FXML
    private Label authenicatedUserLabel;
    
    public UserManagement(ScreenSwitcher switcher) {
	this.switcher = switcher;
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
