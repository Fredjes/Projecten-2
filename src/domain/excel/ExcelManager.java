package domain.excel;

import domain.Book;
import domain.Cd;
import domain.Dvd;
import domain.ExcelData;
import domain.Game;
import domain.Importable;
import domain.StoryBag;
import domain.User;
import domain.User.UserType;
import gui.controls.ExcelEntry;
import gui.excelwizard.ExcelWizardS1;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;
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

    private HashMap<XSSFSheet, List<ExcelData>> excelDataPerSheet = new HashMap<>();

    private Runnable importFinished;

    public enum Destination {

	USERS("Leerlingen", new ExcelConstraintBuilder().needsStrict("voornaam").needsStrict("achternaam").needsTolerant("adres").needsTolerant("mail").allowTolerance(1 / 4f).build(), () -> new User(UserType.STUDENT)),
	BOOKS("Boeken", new ExcelConstraintBuilder().needsStrict("titel").needsStrict("auteur").needsStrict("uitgeverij").needsStrict("thema").needsTolerant("inhoud").needsStrict("leeftijd").allowTolerance(1 / 6f).build(), Book::new),
	GAMES("Spelletjes", new ExcelConstraintBuilder().needsStrict("thema").needsStrict("leeftijd").needsStrict("uitgeverij").needsTolerant("spel").build(), Game::new),
	CD("Cd", new ExcelConstraintBuilder().needsStrict("soort").build(), Cd::new),
	DVD("Dvd", new ExcelConstraintBuilder().needsStrict("soort").build(), Dvd::new),
	STORYBAGS("Verteltassen", new ExcelConstraintBuilder().needsStrict("auteur").needsTolerant("verteltas").needsStrict("thema").allowTolerance(1 / 3f).build(), StoryBag::new),
	UNKNOWN("Niet gevonden");

	private String prettyName;
	private Supplier<? extends Importable> supplier;
	private ConstraintCollection constraintCollection;

	Destination(String name) {
	    prettyName = name;
	}

	private Destination(String prettyName, ConstraintCollection constraintCollection, Supplier<Importable> supplier) {
	    this.prettyName = prettyName;
	    this.constraintCollection = constraintCollection;
	    this.supplier = supplier;
	}

	@Override
	public String toString() {
	    return prettyName;
	}

	public ConstraintCollection getConstraintCollection() {
	    return constraintCollection;
	}

	public Supplier<? extends Importable> getEntityCreator() {
	    return supplier;
	}

	public Set<String> getHeaderList() {
	    if (supplier != null) {
		// Create a copy so that modification operations (add and remove) are supported
		Set<String> setCopy = new HashSet<>(supplier.get().createHeaderList().keySet());
		return setCopy;
	    } else {
		return Collections.EMPTY_SET;
	    }
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
		XSSFSheet sheet = workbook.getSheetAt(i);
		if (!getColumnHeaders(sheet).isEmpty()) {
		    ExcelEntry entry = new ExcelEntry(wizard, workbook, sheet, file.getName());
		    entry.loadMetadata();
		    addEntry(entry);
		    getExcelData(workbook.getSheetAt(i)); //so it loads data into cache
		}
	    }
	} catch (IOException | InvalidFormatException ex) {
	    ex.printStackTrace();
	} finally {
	    try {
		workbook.close();
	    } catch (IOException ex) {
		//unable to clean up temporary files
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
	//System.out.println(dest + " won with " + highest + " for " + sheet.getSheetName());
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

	if (columnHeader == null) {
	    return new ArrayList<>();
	}

	Iterator<Cell> iterator = columnHeader.cellIterator();
	for (int i = 0; i < columnHeader.getFirstCellNum(); i++) {
	    iterator.next();
	}

	iterator.forEachRemaining((Cell c) -> {
	    if (c != null) {
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
	    if (!r.getCell(r.getFirstCellNum()).getStringCellValue().isEmpty() || !r.getCell(r.getFirstCellNum() + 1).getStringCellValue().isEmpty()) {
		return r;
	    }
	}
	return null;
    }

    public List<ExcelData> getExcelData(XSSFSheet sheet) {
	if (excelDataPerSheet.containsKey(sheet)) {
	    return excelDataPerSheet.get(sheet);
	}

	List<ExcelData> out = new ArrayList<>();
	for (int row = getFirstNonEmptyRow(sheet).getRowNum() + 1; row < sheet.getLastRowNum(); row++) {
	    Row r = sheet.getRow(row);

	    if (r == null) {
		continue;
	    }

	    ExcelData data = new ExcelData();

	    int first = r.getFirstCellNum();
	    while (r.getCell(first).getCellType() == Cell.CELL_TYPE_STRING && (r.getCell(first).getStringCellValue() == null || r.getCell(first).getStringCellValue().isEmpty())) {
		first++;
	    }

	    for (int column = first; column < r.getLastCellNum(); column++) {
		Cell c = r.getCell(column);

		if (c == null) {
		    continue;
		}

		switch (c.getCellType()) {
		    case Cell.CELL_TYPE_STRING:
			data.setData(column - first, c.getStringCellValue());
			break;
		    case Cell.CELL_TYPE_NUMERIC:
			data.setData(column - first, String.valueOf(c.getNumericCellValue()));
			break;
		}
	    }

	    out.add(data);
	}

	excelDataPerSheet.put(sheet, out);
	return out;
    }

    public XSSFSheet getSheetById(int id) {
	if (id >= entries.size() || id < 0) {
	    return null;
	}
	return entries.get(id).getSheet();
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

    public void importComplete() {
	entries.clear();
	excelDataPerSheet.clear();
	columnHeaders.clear();
	importFinished.run();
    }

    public void setOnImportFinished(Runnable r) {
	importFinished = r;
    }
}
