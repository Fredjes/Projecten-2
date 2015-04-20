package gui.excelwizard;

import domain.excel.ExcelManager;
import domain.excel.ExcelManager.Destination;
import gui.FXUtil;
import gui.ScreenSwitcher;
import gui.controls.ExcelEntry;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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

    private int currentBarPosition = -1;

    private ScreenSwitcher switcher;

    private final List<ExcelWizardS2> contentScreens = new ArrayList<>();

    private final ExcelWizardS3 loadingScreen;

    private int currentIndex = -1;

    public ExcelWizardS1(ScreenSwitcher switcher) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "excel_management_s1");
	switchBarPosition(0);
	ExcelManager.getInstance().getEntries().addListener((ListChangeListener<ExcelEntry>) (e) -> {
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

	loadingScreen = new ExcelWizardS3(this, switcher);
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
	ExcelManager.getInstance().removeEntry(entry);
	contentScreens.removeIf(cs -> ExcelManager.getInstance().getEntry(cs.getExcelId()) == entry);
    }

    @FXML
    public void onFileSelect() {
	onFileDropped(ExcelManager.getInstance().selectExcel());
    }

    public void onNext() {
	if (ExcelManager.getInstance().getEntries().size() == 0) {
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

	    n.loadData();
	    switcher.setScreen(n);
	} else {
	    switcher.setScreen(loadingScreen);
	    switcher.setNavigationAllowed(false);
	    ExcelManager.getInstance().setOnImportFinished(() -> {
		switcher.setNavigationAllowed(true);
		contentScreens.clear();
		currentIndex = -1;
	    });
	    
	    loadingScreen.startImport();
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
	for (ExcelEntry e : ExcelManager.getInstance().getEntries()) {
	    contentScreens.add(new ExcelWizardS2(this, switcher, id++));
	}
    }

    public List<ExcelWizardS2> getContentScreens() {
	return contentScreens;
    }

    /**
     * Convenience method to handle a file when dropped, checking whether the
     * file is a correct Excel-file (*.xlsx) is already done.
     *
     * @param file The excel-file
     */
    private void onFileDropped(File file) {
	ExcelManager.getInstance().readExcelFile(this, file);
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
	ExcelWizardS2 next = contentScreens.get(++currentIndex);

	while (currentIndex < contentScreens.size() - 1 && ExcelManager.getInstance().getEntry(contentScreens.get(currentIndex + 1).getExcelId()).getDestination() == Destination.UNKNOWN) {
	    currentIndex++;
	}

	return next;
    }

    private ExcelWizardS2 previous() {
	return contentScreens.get(--currentIndex);
    }
}
