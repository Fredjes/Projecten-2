package gui.controls;

import gui.FXUtil;
import gui.excelwizard.ExcelWizardS1;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Brent C.
 */
public class ExcelEntry extends BorderPane {

    private ExcelWizardS1 wizard;

    @FXML
    private Label lblBestand;

    @FXML
    private Label lblBlad;

    @FXML
    private ChoiceBox<?> bestemmingBox;

    public ExcelEntry(ExcelWizardS1 wizard) {
	this.wizard = wizard;
	FXUtil.loadFXML(this, "excel_file_entry");
    }

    @FXML
    public void onClose() {
	wizard.onEntryRemoved(this);
    }

}
