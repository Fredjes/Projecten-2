package gui.controls;

import gui.FXUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Frederik De Smedt
 */
public class ItemSummary extends AnchorPane {

    @FXML
    private HBox copyList;

    @FXML
    private Label description;

    @FXML
    private Label title;

    @FXML
    private ImageView itemImage;

    @FXML
    void onAdd(ActionEvent event) {

    }

    public ItemSummary() {
	FXUtil.loadFXML(this, "listview_item");
    }
}
