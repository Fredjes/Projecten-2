package gui;

import domain.Item;
import domain.ItemCopy;
import domain.StoryBag;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Frederik
 */
public class ScreenSwitcher extends StackPane {

    private MainMenu menu = new MainMenu(this);
    private ItemManagement itemManagement = new ItemManagement(this);

    public ScreenSwitcher() {
	setPrefSize(1000, 650);
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
	throw new UnsupportedOperationException();
    }

    public boolean openSelectItemPopup(ItemCopy copy) {
	throw new UnsupportedOperationException();
    }
}
