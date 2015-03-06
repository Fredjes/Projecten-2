package gui.controls;

import gui.FXUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Frederik
 */
public class CopyButton extends StackPane {

    @FXML
    private Label exemplaarId;

    @FXML
    private ImageView icon;

    public CopyButton() {
	FXUtil.loadFXML(this, "exemplaar_button");
    }
}
