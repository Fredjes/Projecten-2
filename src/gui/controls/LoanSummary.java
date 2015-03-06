package gui.controls;

import gui.FXUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Frederik
 */
public class LoanSummary extends AnchorPane {

    @FXML
    private Label itemName;

    @FXML
    private Label toLateWarningLabel;

    @FXML
    private Label loanPeriod;

    @FXML
    private ImageView itemImage;

    @FXML
    private Label username;

    public LoanSummary() {
	FXUtil.loadFXML(this, "listview_loan");
    }
    
    @FXML
    void editAdvanced() {

    }
}
