package gui.excelwizard;

import gui.FXUtil;
import gui.ScreenSwitcher;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Brent C.
 */
public class ExcelWizardS3 extends BorderPane {

    private ScreenSwitcher switcher;

    public ExcelWizardS3(ScreenSwitcher switcher) {
	this.switcher = switcher;
	FXUtil.loadFXML(this, "excel_management_s3");
    }

}
