package gui;

import domain.IconConfig;
import domain.Item;
import domain.ItemCopy;
import domain.controllers.ItemManagementController;
import domain.controllers.LoanManagementController;
import domain.controllers.MainMenuController;
import java.util.List;
import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Frederik
 */
public class ScreenSwitcher extends StackPane {

    private MainMenu menu;
    private ItemManagement itemManagement;
    private LoanManagement loanManagement;
    private UserManagement userManagement = new UserManagement(this);
    
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

	loadIcons(menu);
	loadIcons(itemManagement);
	loadIcons(loanManagement);
	loadIcons(userManagement);
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
	getChildren().setAll(menu);
    }

    public void openItemManagement() {
	itemManagementController.updateToAuthenticatedUser(itemManagement);
	getChildren().setAll(itemManagement);
    }

    public void openUserManagement() {
	throw new UnsupportedOperationException();
    }

    public void openLoanManagement() {
	loanManagementController.updateToAuthenticatedUser(loanManagement);
	getChildren().setAll(loanManagement);
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
}
