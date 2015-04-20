package gui.excelwizard;

import domain.AssignableTableColumn;
import domain.ExcelData;
import domain.excel.ExcelManager;
import gui.FXUtil;
import gui.ScreenSwitcher;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final int id;

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
	headers.setAll(properties);
    }

    private void ensureTableColumnCreation(int columnCount) {
	if (columns.keySet().size() < columnCount) {
	    columns.clear();
	    IntStream.range(0, columnCount).forEach(i -> columns.put(i, new AssignableTableColumn(i, headers)));
	    content.getColumns().setAll(columns.values());
	}
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
    
    public List<ExcelData> getRows(){
	return content.getItems();
    }

    public void loadData() {
	XSSFSheet sheet = ExcelManager.getInstance().getSheetById(id);
	addRows(ExcelManager.getInstance().getExcelData(sheet));
	setHeaderList(ExcelManager.getInstance().getEntry(id).getDestination().getHeaderList());
    }

    public int getExcelId(){
	return this.id;
    }
}
