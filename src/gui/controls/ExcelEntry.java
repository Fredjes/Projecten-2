package gui.controls;

import domain.excel.ExcelManager;
import domain.excel.ExcelManager.Destination;
import gui.FXUtil;
import gui.excelwizard.ExcelWizardS1;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    private ChoiceBox<Destination> bestemmingBox;

    private List<String> columnHeaders = new ArrayList<>();

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    public ExcelEntry(ExcelWizardS1 wizard, XSSFWorkbook workbook, XSSFSheet excelSheet, String fileName) {
	FXUtil.loadFXML(this, "excel_file_entry");
	this.wizard = wizard;
	this.workbook = workbook;
	this.sheet = excelSheet;
	lblBestand.setText(fileName.substring(0, fileName.length() - 5));
	bestemmingBox.getItems().addAll(Destination.values());
    }

    public void loadMetadata() {
	lblBlad.setText(sheet.getSheetName());
	determineDestination();
    }

    private void determineDestination() {
	bestemmingBox.getSelectionModel().select(ExcelManager.getInstance().determineDestination(sheet));
    }

    private boolean match(int margin, String[] args, boolean[] strict) {
	int found = 0;
	boolean match = false;
	for (int i = 0; i < args.length; i++) {
	    String constraint = args[i];
	    boolean isStrict = strict[i];
	    for (String header : columnHeaders) {
		if (isStrict) {
		    if (header.toLowerCase().equals(constraint)) {
			found++;
			match = true;
		    }
		} else {
		    if (header.toLowerCase().contains(constraint)) {
			found++;
			match = true;
		    }
		}
	    }

	}
	return found >= margin && match;
    }

    @FXML
    public void onClose() {
	wizard.onEntryRemoved(this);
    }

}
