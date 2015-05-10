package gui.excelwizard;

import domain.AssignableTableColumn;
import domain.ExcelData;
import domain.excel.ExcelManager;
import domain.excel.ExcelManager.Destination;
import gui.FXUtil;
import gui.ScreenSwitcher;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.apache.poi.xssf.usermodel.XSSFSheet;

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
    private TableView<ExcelData> content;

    private ExcelWizardS1 step1;
    private ScreenSwitcher switcher;
    private HashMap<Integer, AssignableTableColumn> columns = new HashMap<>();
    private final ObservableList<String> headers = FXCollections.observableArrayList();

    @FXML
    private Button btnNext;

    private int id;

    public ExcelWizardS2(ExcelWizardS1 step1, ScreenSwitcher switcher, int id) {
	this.step1 = step1;
	this.switcher = switcher;

	this.id = id;
	FXUtil.loadFXML(this, "excel_management_s2");
	title.setText(ExcelManager.getInstance().getSheetById(id).getSheetName() + " koppelen - " + ExcelManager.getInstance().getEntry(id).getDestination().toString());
    }

    @FXML
    public void onNext() {
	step1.onNext();
    }

    @FXML
    public void onPrevious() {
	step1.onPrevious();
    }

    /**
     * Set the list of headers from which can be chosen, the list is used to
     * assign columns to properties.
     *
     * @param properties A collection of all possible properties that can be
     * selected
     */
    public void setHeaderList(Collection<String> properties) {
	if (!properties.contains("")) {
	    try {
		properties.add("");
	    } catch (UnsupportedOperationException uoex) {
		// in case of Collections.EMPTY_SET, in which a blank field isn't necessary
	    }
	}

	headers.setAll(properties);
    }

    private void ensureTableColumnCreation(int columnCount) {
	if (columns.keySet().size() < columnCount) {
	    columns.clear();
	    IntStream.range(0, columnCount).forEach(i -> columns.put(i, new AssignableTableColumn(i, headers)));
	    content.getColumns().setAll(columns.values());
	}

	predictColumnNames();
    }

    private void predictColumnNames() {
	if (ExcelManager.getInstance().getEntry(id).getDestination() == Destination.UNKNOWN) {
	    return;
	}

	final Map<String, Predicate<String>> predictionPredicates = ExcelManager.getInstance().getEntry(id).getDestination().getEntityCreator().get().createHeaderAssignmentList();
	List<String> excelHeaders = ExcelManager.getInstance().getColumnHeaders(ExcelManager.getInstance().getSheetById(id));

	columns.forEach((i, c) -> {
	    if (excelHeaders.size() > i) {
		String excelHeader = excelHeaders.get(i);
		predictionPredicates.forEach((s, p) -> {
		    if (p.test(excelHeader)) {
			c.select(s);
		    }
		});
	    }
	});
    }

    public void addRows(Collection<ExcelData> data) {
	ensureTableColumnCreation(data.stream().mapToInt(ed -> ed.getSize()).max().getAsInt());
	content.getItems().addAll(data);
    }

    public void addRows(ExcelData... data) {
	addRows(Arrays.asList(data));
    }

    public void setNextButtonText(String txt) {
	btnNext.setText(txt);
    }

    public Map<Integer, String> getHeaders() {
	Map<Integer, String> map = new HashMap<>();
	columns.forEach((k, v) -> map.put(k, v.getSelectedItem()));
	return map;
    }

    public List<ExcelData> getRows() {
	return content.getItems();
    }

    private boolean loaded = false;

    public void loadData() {
	if (!loaded) {
	    XSSFSheet sheet = ExcelManager.getInstance().getSheetById(id);
	    setHeaderList(ExcelManager.getInstance().getEntry(id).getDestination().getHeaderList());
	    addRows(ExcelManager.getInstance().getExcelData(sheet));
	    loaded = true;
	}
    }

    public int getExcelId() {
	return this.id;
    }

    public void setExcelId(int id) {
	this.id = id;
    }
}
