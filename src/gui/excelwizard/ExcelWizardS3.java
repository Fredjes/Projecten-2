package gui.excelwizard;

import domain.Importable;
import domain.Importer;
import domain.excel.ExcelManager;
import gui.FXUtil;
import gui.ScreenSwitcher;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import persistence.Repository;

/**
 * A GUI representing the third step in the excel import wizard: importing the
 * actual data and displaying that the imports are complete.
 *
 * @author Brent C.
 */
public class ExcelWizardS3 extends BorderPane {

    private ScreenSwitcher switcher;
    private List<ExcelWizardS2> contentScreens;

    public ExcelWizardS3(ExcelWizardS1 mainScreen, ScreenSwitcher switcher) {
	this.switcher = switcher;
	this.contentScreens = mainScreen.getContentScreens();
	FXUtil.loadFXML(this, "excel_management_s3");
    }

    @FXML
    public void onMainScreen() {
	switcher.openMainMenu();
    }

    public void startImport() {
	contentScreens.forEach(w -> {
	    Map<Integer, String> headers = w.getHeaders();

	    if (!headers.values().stream().allMatch(s -> s == null || s.isEmpty())) {
		Supplier<? extends Importable> supplier = ExcelManager.getInstance().getEntry(w.getExcelId()).getDestination().getEntityCreator();
		final int size = w.getRows().size();
		final FloatProperty current = new SimpleFloatProperty();
		Importer importer = supplier.get().createImporter();

		w.getRows().forEach(d -> {
		    Importable i = supplier.get();
		    headers.forEach((t, u) -> {
			importer.setField(u, d.getData(t));
		    });

		    importer.nextEntity();
		    current.set(current.get() + 1);
		});

		importer.persistEntities();
	    }
	});

	Repository.saveAllChanges();
	ExcelManager.getInstance().importComplete();
    }
}
