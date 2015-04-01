package gui.controls;

import domain.Icon;
import domain.IconConfig;
import gui.FXUtil;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 *
 * @author Brent C.
 */
public class SearchTextField extends HBox {

    @FXML
    private TextField textfield;

    @FXML
    private Text label;

    public SearchTextField() {
	FXUtil.loadFXML(this, "search_field");
    }

    public StringProperty textProperty() {
	return textfield.textProperty();
    }

    public TextField getTextfield() {

	return textfield;
    }

    public void setTextfield(TextField textfield) {
	this.textfield = textfield;
    }

    public Text getLabel() {
	return label;
    }

    public void setLabel(Text label) {
	this.label = label;
    }

    public void setIcon(String iconClass) {
	Icon i = IconConfig.getIconFor(iconClass);
	if (i != null) {
	    IconConfig.setIcon(label, i);
	}
    }

}
