package gui.controls;

import domain.Damage;
import gui.FXUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Frederik
 */
public class CopyPopOver extends BorderPane {

    @FXML
    private ChoiceBox<Damage> damage;

    @FXML
    private TextField location;

    public CopyPopOver() {
	FXUtil.loadFXML(this, "popover_exemplaar");
	damage.setItems(FXCollections.observableArrayList(Damage.values()));
    }

    @FXML
    void onDelete(ActionEvent event) {

    }

    @FXML
    void onStartLoan(ActionEvent event) {

    }
}
