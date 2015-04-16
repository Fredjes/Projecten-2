package gui.controls;

import gui.FXUtil;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Brent C.
 */
public class ExcelEntry extends BorderPane {

    public ExcelEntry() {
	FXUtil.loadFXML(this, "excel_file_entry");
    }

}
