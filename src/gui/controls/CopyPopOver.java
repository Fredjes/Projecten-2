package gui.controls;

import domain.Damage;
import domain.ItemCopy;
import gui.FXUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Frederik
 */
public class CopyPopOver extends BorderPane {

    @FXML
    private ChoiceBox<Damage> damage;

    @FXML
    private TextField location;

    @FXML
    private Label copyNumber;

    @FXML
    private Label locationLabel;

    @FXML
    private Label damageLabel;

    private ItemCopy backedCopy;
    private EventHandler<ActionEvent> onDelete;
    private EventHandler<ActionEvent> onStartLoan;

    public CopyPopOver(ItemCopy copy) {
	FXUtil.loadFXML(this, "popover_exemplaar");
	damage.setItems(FXCollections.observableArrayList(Damage.values()));
	this.backedCopy = copy;
	Bindings.bindBidirectional(location.textProperty(), copy.locationProperty());
	damage.getSelectionModel().select(copy.getDamage());
	copy.damageProperty().bind(damage.getSelectionModel().selectedItemProperty());
	copyNumber.textProperty().bind(copy.copyNumberProperty());
	locationLabel.setTextFill(Color.BLACK);
	damageLabel.setTextFill(Color.BLACK);
    }

    @FXML
    public void onDelete(ActionEvent event) {
	if (onDelete != null) {
	    onDelete.handle(event);
	}
    }

    @FXML
    public void onStartLoan(ActionEvent event) {
	if (onStartLoan != null) {
	    onStartLoan.handle(event);
	}
    }

    public void setOnDelete(EventHandler<ActionEvent> handler) {
	this.onDelete = handler;
    }

    public void setOnStartLoan(EventHandler<ActionEvent> handler) {
	this.onStartLoan = handler;
    }
}
