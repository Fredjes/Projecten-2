package gui.excelwizard;

import gui.FXUtil;
import gui.ScreenSwitcher;
import gui.controls.ExcelEntry;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Brent C.
 */
public class ExcelWizardS1 extends BorderPane {

    @FXML
    private StackPane dragBox;

    @FXML
    private HBox infoBox;

    @FXML
    private VBox topBox;

    @FXML
    private FlowPane entryContainer;

    private int currentPosition = -1;
    private final ObservableList<ExcelEntry> entries = FXCollections.observableArrayList();

    private int currentBarPosition = -1;

    private ScreenSwitcher switcher;
    private List<ExcelWizardS2> contentEditScreens = new ArrayList<>();
    private ListIterator<ExcelWizardS2> iterator;

    private final List<ExcelWizardS2> contentScreens = new ArrayList<>();

    private final ExcelWizardS3 loadingScreen;

    private int currentIndex = -1;

    public ExcelWizardS1(ScreenSwitcher switcher) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "excel_management_s1");
	switchBarPosition(0);
	entries.addListener((ListChangeListener<ExcelEntry>) (e) -> {
	    e.next();

	    if (e.wasAdded()) {
		entryContainer.getChildren().addAll(e.getAddedSubList());
		e.getAddedSubList().forEach(switcher::loadIcons);
	    }

	    if (e.wasRemoved()) {
		entryContainer.getChildren().removeAll(e.getRemoved());
	    }

	    if (e.getList().size() >= 1) {
		switchBarPosition(1);
	    } else {
		switchBarPosition(0);
	    }
	});

	dragBox.setOnDragOver(e -> {
	    Dragboard board = e.getDragboard();
	    if (board.hasFiles() && board.getFiles().stream().anyMatch(f -> f.getPath().toLowerCase().endsWith(".xlsx"))) {
		e.acceptTransferModes(TransferMode.LINK);
	    }
	});

	dragBox.setOnDragDropped(e -> {
	    Dragboard board = e.getDragboard();
	    if (board.hasFiles()) {
		board.getFiles().stream().filter(f -> f.getPath().toLowerCase().endsWith(".xlsx")).forEach(this::onFileDropped);
	    }
	});

	loadingScreen = new ExcelWizardS3(switcher);
    }

    public final void switchBarPosition(int flag) {
	if (currentBarPosition == flag) {
	    return;
	}
	currentBarPosition = flag;
	if (flag == 1) {
	    infoBox.setVisible(false);
	    topBox.setVisible(true);
	} else {
	    topBox.setVisible(false);
	    infoBox.setVisible(true);
	}

    }

    public void onEntryRemoved(ExcelEntry entry) {
	entries.remove(entry);
    }

    @FXML
    public void onFileSelect() {

    }

    public void onNext() {
	if (entries.size() == 0) {
	    return;
	}

	if (contentScreens.isEmpty()) {
	    buildContentList();
	}

	if (hasNext()) {
	    ExcelWizardS2 n = next();
	    if (isLastBeforeLoad()) {
		n.setNextButtonText("Importeren");
	    } else {
		n.setNextButtonText("Volgende");
	    }
	    switcher.setScreen(n);
	} else {
	    switcher.setScreen(loadingScreen);
	    switcher.setNavigationAllowed(false);

	    //TODO: re-enable navigation when import is finished!
	}
    }

    public void onPrevious() {
	if (hasPrevious()) {
	    switcher.setScreen(previous());
	} else {
	    switcher.setScreen(this);
	    currentIndex = -1;
	}
    }

    private void buildContentList() {
	int id = 0;
	for (ExcelEntry e : entries) {
	    contentScreens.add(new ExcelWizardS2(this, switcher, id++));
	}
    }

    /**
     * Convenience method to handle a file when dropped, checking whether the
     * file is a correct Excel-file (*.xlsx) is already done.
     *
     * @param file The excel-file
     */
    private void onFileDropped(File file) {

	XSSFWorkbook workbook = null;
	try {
	    workbook = new XSSFWorkbook(file);
	    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
		System.out.println("excel sheet " + workbook.getSheetAt(i).getSheetName() + " " + workbook.getNumberOfSheets());

		ExcelEntry entry = new ExcelEntry(this, workbook, workbook.getSheetAt(i), file.getName());
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

    public void addEntry(ExcelEntry e) {
	entries.add(e);
    }

    private boolean hasNext() {
	return currentIndex < contentScreens.size() - 1;
    }

    private boolean isLastBeforeLoad() {
	return currentIndex == contentScreens.size() - 1;
    }

    private boolean hasPrevious() {
	return currentIndex > 0;
    }

    private ExcelWizardS2 next() {
	return contentScreens.get(++currentIndex);
    }

    private ExcelWizardS2 previous() {
	return contentScreens.get(--currentIndex);
    }
}
