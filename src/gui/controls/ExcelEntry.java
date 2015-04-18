package gui.controls;

import gui.FXUtil;
import gui.excelwizard.ExcelWizardS1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
    private ChoiceBox<?> bestemmingBox;

    private List<String> columnHeaders = new ArrayList<>();

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    public ExcelEntry(ExcelWizardS1 wizard, XSSFWorkbook workbook, XSSFSheet excelSheet, String fileName) {
	FXUtil.loadFXML(this, "excel_file_entry");
	this.wizard = wizard;
	this.workbook = workbook;
	this.sheet = excelSheet;
	lblBestand.setText(fileName);
    }

    public void loadMetadata() throws IOException, InvalidFormatException {
	Row columnHeader = sheet.getRow(0);
	columnHeader.cellIterator().forEachRemaining((c) -> columnHeaders.add(c.getStringCellValue()));

	lblBlad.setText(sheet.getSheetName());
    }

    @FXML
    public void onClose() {
	wizard.onEntryRemoved(this);
    }

}
