package gui.controls;

import gui.FXUtil;
import gui.excelwizard.ExcelWizardS1;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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

    public enum Destination {

	USERS("Leerlingen"), BOOKS("Boeken"), UNKNOWN("Niet gevonden");

	private String prettyName;

	Destination(String name) {
	    prettyName = name;
	}

	@Override
	public String toString() {
	    return prettyName;
	}

    }

    public ExcelEntry(ExcelWizardS1 wizard, XSSFWorkbook workbook, XSSFSheet excelSheet, String fileName) {
	FXUtil.loadFXML(this, "excel_file_entry");
	this.wizard = wizard;
	this.workbook = workbook;
	this.sheet = excelSheet;
	lblBestand.setText(fileName);
	bestemmingBox.getItems().addAll(Destination.values());
    }

    public void loadMetadata() {
	Row columnHeader = getFirstNonEmptyRow(sheet);
	columnHeader.cellIterator().forEachRemaining((Cell c) -> {
	    if (c != null && !c.getStringCellValue().isEmpty()) {
		columnHeaders.add(c.getStringCellValue());
	    }
	});

	lblBlad.setText(sheet.getSheetName());
	determineDestination();
    }

    private Row getFirstNonEmptyRow(XSSFSheet sheet) {
	for (int row = 0; row < sheet.getLastRowNum(); row++) {
	    Row r = sheet.getRow(row);
	    if (r == null) {
		continue;
	    }
	    if (!r.getCell(r.getFirstCellNum()).getStringCellValue().isEmpty()) {
		return r;
	    }
	}
	return null;
    }

    private void determineDestination() {
	if (match(4, new String[] {"voornaam", "achternaam", "naam", "mail", "klas", "adres"}, new boolean[] {true, true, true, false, true, false})) {
	    bestemmingBox.getSelectionModel().select(Destination.USERS);
	} else {
	    bestemmingBox.getSelectionModel().select(Destination.UNKNOWN);
	}
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
