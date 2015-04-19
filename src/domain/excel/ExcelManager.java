package domain.excel;

import gui.controls.ExcelEntry;
import gui.excelwizard.ExcelWizardS1;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Brent C.
 */
public class ExcelManager {

    private static ExcelManager INSTANCE;

    private final ObservableList<ExcelEntry> entries = FXCollections.observableArrayList();

    private Map<XSSFSheet, List<String>> columnHeaders = new HashMap<>();

    public enum Destination {

	USERS("Leerlingen", new ExcelConstraintBuilder().needsStrict("voornaam").needsStrict("achternaam").needsTolerant("adres").needsTolerant("mail").allowTolerance(0.35f).build()),
	BOOKS("Boeken", new ExcelConstraintBuilder().needsStrict("titel").needsStrict("auteur").needsStrict("uitgeverij").needsStrict("thema").needsTolerant("inhoud").needsStrict("leeftijd").allowTolerance(0.3f).build()),
	GAMES("Spelletjes"),
	UNKNOWN("Niet gevonden");

	private String prettyName;

	private ConstraintCollection constraintCollection;

	Destination(String name) {
	    prettyName = name;
	}

	private Destination(String prettyName, ConstraintCollection constraintCollection) {
	    this.prettyName = prettyName;
	    this.constraintCollection = constraintCollection;
	}

	@Override
	public String toString() {
	    return prettyName;
	}

	public ConstraintCollection getConstraintCollection() {
	    return constraintCollection;
	}

    }

    private ExcelManager() {
    }

    public static ExcelManager getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new ExcelManager();
	}
	return INSTANCE;
    }

    public void addEntry(ExcelEntry entry) {
	entries.add(entry);
    }

    public void removeEntry(ExcelEntry entry) {
	entries.remove(entry);
    }

    public ExcelEntry getEntry(int id) {
	return entries.get(id);
    }

    public ObservableList<ExcelEntry> getEntries() {
	return entries;
    }

    public void readExcelFile(ExcelWizardS1 wizard, File file) {
	if (file == null) {
	    return;
	}
	XSSFWorkbook workbook = null;
	try {
	    workbook = new XSSFWorkbook(file);
	    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
		ExcelEntry entry = new ExcelEntry(wizard, workbook, workbook.getSheetAt(i), file.getName());
		entry.loadMetadata();
		addEntry(entry);
	    }
	} catch (IOException | InvalidFormatException ex) {
	    ex.printStackTrace();
	} finally {
	    try {
		workbook.close();
	    } catch (IOException ex) {
		Logger.getLogger(ExcelWizardS1.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public Destination determineDestination(XSSFSheet sheet) {

	float highest = 0.0f;
	Destination dest = Destination.UNKNOWN;
	Map<Destination, Float> results = new HashMap<>();
	for (Destination d : Destination.values()) {
	    if (d.getConstraintCollection() == null) {
		results.put(d, 0.0f);
		continue;
	    }
	    results.put(d, d.getConstraintCollection().test(sheet));
	}

	for (Entry<Destination, Float> e : results.entrySet()) {
	    if (e.getValue() > highest) {
		highest = e.getValue();
		dest = e.getKey();
	    }
	}
	System.out.println(dest + " won with " + highest + " for " + sheet.getSheetName());
	if (highest > 0.0f) {
	    return dest;
	} else {
	    return Destination.UNKNOWN;
	}
    }

    public List<String> getColumnHeaders(XSSFSheet sheet) {
	if (columnHeaders.get(sheet) != null && !columnHeaders.get(sheet).isEmpty()) {
	    return columnHeaders.get(sheet);
	} else if (columnHeaders.get(sheet) == null) {
	    columnHeaders.put(sheet, new ArrayList<>());
	}

	Row columnHeader = getFirstNonEmptyRow(sheet);
	columnHeader.cellIterator().forEachRemaining((Cell c) -> {
	    if (c != null && !c.getStringCellValue().isEmpty()) {
		columnHeaders.get(sheet).add(c.getStringCellValue());
	    }
	});
	return columnHeaders.get(sheet);
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

    public File selectExcel() {
	FileChooser chooser = new FileChooser();
	chooser.setTitle("Kies een excel bestand");
	chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel 2007 Bestanden", "*.xlsx"));
	File selectedFile = chooser.showOpenDialog(null);

	if (selectedFile == null) {
	    return null;
	}
	return selectedFile;
    }

}
