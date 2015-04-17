package gui.excelwizard;

import gui.FXUtil;
import gui.ScreenSwitcher;
import gui.controls.ExcelEntry;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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

    private ObservableList<ExcelEntry> entries = FXCollections.observableArrayList();
    private int currentPosition = -1;

    private ScreenSwitcher switcher;
    private List<ExcelWizardS2> contentEditScreens = new ArrayList<>();
    private ListIterator<ExcelWizardS2> iterator;

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
		e.acceptTransferModes(TransferMode.MOVE);
	    }
	});

	dragBox.setOnDragDropped(e -> {
	    Dragboard board = e.getDragboard();
	    if (board.hasFiles()) {
		board.getFiles().stream().filter(f -> f.getPath().toLowerCase().endsWith(".xlsx")).forEach(this::onFileDropped);
	    }
	});
    }

    public void switchBarPosition(int flag) {
	if (currentPosition == flag) {
	    return;
	}
	currentPosition = flag;
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
	entries.add(new ExcelEntry(this));
    }

    /**
     * Convenience method to handle a file when dropped, checking whether the
     * file is a correct Excel-file (*.xlsx) is already done.
     *
     * @param file The excel-file
     */
    private void onFileDropped(File file) {
	// Here goes the code for reading and handling the file
	entries.add(new ExcelEntry(this));
    }

    public void onNext() {
	if (entries.size() == 0) {
	    return;
	}

	if (contentEditScreens.isEmpty()) {
	    buildContentList();
	}

	if (iterator.hasNext()) {
	    switcher.setScreen(iterator.next());
	} else {
	    //show progress screen
	}
    }

    public void onPrevious() {
	if (iterator.hasPrevious()) {
	    switcher.setScreen(iterator.previous());
	} else {
	    switcher.setScreen(this);
	}
    }

    private void buildContentList() {
	int id = 0;
	for (ExcelEntry e : entries) {
	    contentEditScreens.add(new ExcelWizardS2(this, switcher, id++));
	}

	iterator = contentEditScreens.listIterator();
    }

}
