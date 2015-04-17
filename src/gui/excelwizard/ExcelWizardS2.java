package gui.excelwizard;

import gui.FXUtil;
import gui.ScreenSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 *
 * @author Brent C.
 */
public class ExcelWizardS2 extends BorderPane {

    @FXML
    private Text title;

    @FXML
    private Label info;

    @FXML
    private TableView<?> content;

    private ExcelWizardS1 step1;

    private ScreenSwitcher switcher;

    private final int id;

    public ExcelWizardS2(ExcelWizardS1 step1, ScreenSwitcher switcher, int id) {
	this.step1 = step1;
	this.switcher = switcher;

	this.id = id;
	FXUtil.loadFXML(this, "excel_management_s2");
	title.setText("Blad " + (id) + " koppelen");
    }

    @FXML
    public void onNext() {
	step1.onNext();
    }

    @FXML
    public void onPrevious() {
	step1.onPrevious();
    }

}
