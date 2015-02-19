package gui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Frederik
 */
public class ItemManagement extends BorderPane {

    private ScreenSwitcher switcher;

    @FXML
    private Text backButton;

    @FXML
    private TextField searchBar;

    @FXML
    private TableView dataTable;

    @FXML
    private ComboBox itemSelection;

    public ItemManagement(ScreenSwitcher switcher) {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/ItemManagement.fxml"));
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();

	    this.switcher = switcher;
	    initIcons();
	} catch (IOException ioex) {
	    System.err.println("Could not load item mangement interface: " + ioex.getMessage());
	}
    }

    private void initIcons() {
	backButton.setText(IconConfig.BACK_ICON);
    }

    public void onSearch() {

    }

    public void onItemAdd() {

    }

    public void onItemDelete() {

    }

    public void onBack() {
	switcher.openMainMenu();
    }
}
