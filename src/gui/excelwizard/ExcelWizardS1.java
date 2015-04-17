package gui.excelwizard;

import gui.FXUtil;
import gui.ScreenSwitcher;
import gui.controls.ExcelEntry;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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

    private final ObservableList<ExcelEntry> entries = FXCollections.observableArrayList();

    private int currentBarPosition = -1;

    private final ScreenSwitcher switcher;

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
	addEntry(new ExcelEntry(this));
    }

    public void addEntry(ExcelEntry e) {
	entries.add(e);
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
	currentIndex++;
	return contentScreens.get(currentIndex);
    }

    private ExcelWizardS2 previous() {
	currentIndex--;
	return contentScreens.get(currentIndex);
    }

}
