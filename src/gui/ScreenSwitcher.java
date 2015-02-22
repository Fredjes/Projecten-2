package gui;

import domain.Item;
import domain.ItemCopy;
import domain.StoryBag;
import gui.dialogs.DialogManager;
import java.util.List;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Frederik
 */
public class ScreenSwitcher extends StackPane {

    private MainMenu menu = new MainMenu(this);
    private ItemManagement itemManagement = new ItemManagement(this);

    public ScreenSwitcher() {
	setPrefSize(1200, 650);
	setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
	setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);

	getStylesheets().addAll("/resources/css/font-awesome.css", "/resources/css/global.css");
    }

    public void openMainMenu() {
	getChildren().setAll(menu);
    }

    public void openItemManagement() {
	getChildren().setAll(itemManagement);
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

    public boolean openManageItemsPopup(StoryBag bag) {
	List<ItemCopy> result = DialogManager.showStorybagItemSelectionDialog(bag);
	if (result != null) {
	    bag.setItems(result);
	}
	return false;
    }

    public boolean openSelectItemPopup(ItemCopy copy) {
	Item result = DialogManager.showItemCopyItemSelectionDialog(copy);
	if (result != null) {
	    copy.setItem(result);
	}
	return false;
    }
}
