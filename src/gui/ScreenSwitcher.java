package gui;

import domain.IconConfig;
import domain.Item;
import domain.ItemCopy;
import domain.controllers.ItemManagementController;
import domain.controllers.LoanManagementController;
import domain.controllers.MainMenuController;
import domain.controllers.TitleBarController;
import gui.dialogs.LoginPanel;
import gui.dialogs.PopupManager;
import java.util.List;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.PopOver;
import persistence.UserRepository;

/**
 *
 * @author Frederik
 */
public class ScreenSwitcher extends BorderPane {

    private MainMenu menu;
    private ItemManagement itemManagement;
    private LoanManagement loanManagement;
    private UserManagement userManagement = new UserManagement(this);
    private Titlebar titlebar = new Titlebar(this, new TitleBarController());

    private UserRepository USER_REPO_INSTANCE = UserRepository.getInstance();

    private MainMenuController mainMenuController = new MainMenuController();
    private ItemManagementController itemManagementController = new ItemManagementController();
    private LoanManagementController loanManagementController = new LoanManagementController();

    public ScreenSwitcher() {
	this.menu = new MainMenu(this, mainMenuController);
	this.itemManagement = new ItemManagement(this, itemManagementController);
	this.loanManagement = new LoanManagement(this, loanManagementController);

	setPrefSize(1200, 650);
	setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
	setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);

	getStylesheets().add("/resources/css/global.css");
	setTop(titlebar);

	loadIcons(menu);
	loadIcons(itemManagement);
	loadIcons(loanManagement);
	loadIcons(userManagement);
	loadIcons(titlebar);
    }

    public void loadIcons(Node node) {
	if (node instanceof Parent) {
	    Parent p = (Parent) node;
	    if (!p.getChildrenUnmodifiable().isEmpty()) {
		p.getChildrenUnmodifiable().forEach(this::loadIcons);
	    }
	}

	List<String> classes = node.getStyleClass();
	classes.stream().forEach(clazz -> IconConfig.identify(node, clazz));
    }

    public void openMainMenu() {
	mainMenuController.updateToAuthenticatedUser(menu);
	setCenter(menu);
    }

    public void openItemManagement() {
	itemManagementController.updateToAuthenticatedUser(itemManagement);
	setCenter(itemManagement);
    }

    public void openUserManagement() {
	throw new UnsupportedOperationException();
    }

    public void openLoanManagement() {
	loanManagementController.updateToAuthenticatedUser(loanManagement);
	setCenter(loanManagement);
    }

    public void openExcelImport() {
	throw new UnsupportedOperationException();
    }

    public boolean openDeletePopup(Object o) {
	Alert a = new Alert(AlertType.CONFIRMATION);
	if (o instanceof ItemCopy) {
	    ItemCopy copy = (ItemCopy) o;
	    buildDeleteAlert(copy, a);
	} else if (o instanceof Item) {
	    Item item = (Item) o;
	    buildDeleteAlert(item, a);
	} else {
	    return false;
	}

	ButtonType okButton = new ButtonType("Ja", ButtonData.OK_DONE);
	ButtonType cancelButton = new ButtonType("Nee", ButtonData.CANCEL_CLOSE);

	a.getButtonTypes().setAll(okButton, cancelButton);

	Optional<ButtonType> result = a.showAndWait();
	return result.get() == okButton;
    }

    private void buildDeleteAlert(Item item, Alert a) {
	a.setTitle("Voorwerp verwijderen");
	a.setHeaderText("Bent u zeker dat u dit voorwerp wilt verwijderen?");
	a.setContentText("U staat op het punt om '" + item.getName() + "' definitief te verwijderen. Alle exemplaren van dit voorwerp zullen ongeldig zijn en ook verwijderd worden.");
    }

    private void buildDeleteAlert(ItemCopy copy, Alert a) {
	a.setTitle("Exemplaar verwijderen");
	a.setHeaderText("Bent u zeker dat u dit exemplaar wilt verwijderen?");
	a.setContentText("U staat op het punt om '" + copy.getItem().getName() + " #" + copy.getCopyNumber() + "' definitief te verwijderen.");
    }

    /**
     * Log in using the email of the user and a non-encrypted password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return true if the email and password are correct
     */
    public boolean login(String email, String password) {
	return USER_REPO_INSTANCE.authenticate(email, password);
    }

    public void processLoginRequest(Button loginButton, Label authenticatedUserLabel) {
	if (USER_REPO_INSTANCE.getAuthenticatedUser() != null) {
	    USER_REPO_INSTANCE.logout();
	    loginButton.setText("Aanmelden");
	    authenticatedUserLabel.setText("");
	} else {
	    LoginPanel loginPanel = new LoginPanel();
	    PopOver pop = PopupManager.showPopOver(loginButton, loginPanel);
	    loginPanel.setOnLogin(e -> {
		if (login(loginPanel.getUsername(), loginPanel.getPassword())) {
		    authenticatedUserLabel.setText("Welkom " + USER_REPO_INSTANCE.getAuthenticatedUser().getName());
		    pop.hide();
		    loginButton.setText("Afmelden");
		} else {
		    loginPanel.resetPanel(true);
		    authenticatedUserLabel.setText("");
		}
	    });
	}
    }
}
