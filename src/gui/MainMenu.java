package gui;

import gui.dialogs.DialogManager;
import gui.dialogs.LoginPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Frederik
 */
public class MainMenu extends BorderPane {

    @FXML
    private Button loginButton;

    @FXML
    private GridPane navigationGrid;

    private ScreenSwitcher switcher;

    public MainMenu(ScreenSwitcher switcher) {
        this.switcher = switcher;
        FXUtil.loadFXML(this, "main_menu");
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

    @FXML
    public void onLogin() {
        LoginPanel p = new LoginPanel();
        p.setOnLogin((ActionEvent e) -> {
            System.out.printf("Username: %s%nPassword: %s%n", p.getUsername(), p.getPassword());
        });
        DialogManager.showPopOver(loginButton, p);
    }
}
