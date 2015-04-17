package gui.controls;

import gui.FXUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author Frederik
 */
public class ThemeItem extends HBox {

    @FXML
    private Label themeName;

    private ObservableList<String> themes;

    private EventHandler<ActionEvent> onRemoveHandler;

    public ThemeItem(ObservableList<String> themes, String theme) {
	FXUtil.loadFXML(this, "theme_item");
	setThemes(themes);
	this.setMaxHeight(15);
	themeName.setText(theme);
    }

    public void setThemes(ObservableList<String> themes) {
	this.themes = themes;
    }

    @FXML
    public void onRemove(MouseEvent e) {
	if (themes != null) {
	    themes.remove(themeName.getText());
	}

	if (onRemoveHandler != null) {
	    ActionEvent event = new ActionEvent(e, this);
	    onRemoveHandler.handle(event);
	}
    }

    public void setOnRemove(EventHandler<ActionEvent> handler) {
	this.onRemoveHandler = handler;
    }
}
