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

    // With popups, add the new view as a different layer (so that the original view is still visible in background)
    public boolean openDeletePopup(Item i) {
	throw new UnsupportedOperationException();
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
