package gui.excelwizard;

import gui.FXUtil;
import gui.controls.ExcelEntry;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Brent C.
 */
public class ExcelWizardS1 extends BorderPane {

    @FXML
    private StackPane dragBox;

    @FXML
    private HBox infoBox;

    public ExcelWizardS1() {
	FXUtil.loadFXML(this, "excel_management_s1");
	infoBox.setVisible(false);
	dragBox.getChildren().add(new ExcelEntry());
    }

}
