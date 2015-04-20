package gui.excelwizard;

import domain.Importable;
import domain.excel.ExcelManager;
import gui.FXUtil;
import gui.ScreenSwitcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import persistence.Repository;

/**
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
	final List<Repository> repos = new ArrayList<>();
	contentScreens.forEach(w -> {
	    Map<Integer, String> headers = w.getHeaders();

	    if (!headers.values().stream().allMatch(s -> s == null || s.isEmpty())) {
		Supplier<? extends Importable> supplier = ExcelManager.getInstance().getEntry(w.getExcelId()).getDestination().getEntityCreator();
		final int size = w.getRows().size();
		final FloatProperty current = new SimpleFloatProperty();

		w.getRows().forEach(d -> {
		    Importable i = supplier.get();
		    Map<String, BiConsumer<String, Importable>> consumers = i.createHeaderList();
		    headers.forEach((t, u) -> {
			if (d.hasData(t)) {
			    if (consumers.containsKey(u)) {
				consumers.get(u).accept(d.getData(t), i);
			    }
			}
		    });

		    i.getRepository().add(i);
		    current.set(current.get() + 1);
		});

		repos.add(supplier.get().getRepository());
	    }
	});

	repos.forEach(Repository::saveChanges);

	ExcelManager.getInstance().importComplete();
    }
}
