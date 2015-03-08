package gui.controls;

import domain.Book;
import domain.Cd;
import domain.Dvd;
import domain.FontCache;
import domain.Game;
import domain.ItemCopy;
import domain.StoryBag;
import gui.FXUtil;
import gui.dialogs.PopupUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Frederik
 */
public class CopyButton extends StackPane {

    @FXML
    private Label copyId;

    @FXML
    private Label icon;

    private ItemCopy backedCopy;

    private CopyPopOver popOverContent;
    private PopOver popOver;

    public CopyButton(ItemCopy copy) {
	FXUtil.loadFXML(this, "exemplaar_button");
	
	DropShadow ds = new DropShadow();
	ds.setOffsetX(1f);
	ds.setOffsetY(1f);
	copyId.setEffect(ds);
	
	this.backedCopy = copy;
	popOverContent = new CopyPopOver(backedCopy);
	super.setOnMouseClicked(e -> showDetails());
	copyId.setText(copy.getCopyNumber().substring(1));
	createIcon();
    }

    private void createIcon() {
	icon.setFont(FontCache.getIconFont(30));
	icon.setTextFill(Color.BLACK);
	if (backedCopy.getItem() instanceof Book) {
	    icon.setText("\uf02d");
	} else if (backedCopy.getItem() instanceof Game){
	    icon.setText("\uf091");
	} else if (backedCopy.getItem() instanceof Dvd){
	    icon.setText("\uf008");
	} else if (backedCopy.getItem() instanceof Cd){
	    icon.setText("\uf025");
	} else if (backedCopy.getItem() instanceof StoryBag){
	    icon.setText("\uf0b1");
	}
    }

    public void showDetails() {
	popOver = PopupUtil.showPopOver(this, popOverContent);
    }

    public void setOnDelete(EventHandler<ActionEvent> handler) {
	popOverContent.setOnDelete(addPopUpCloseHandle(handler));
    }

    public void setOnStartLoan(EventHandler<ActionEvent> handler) {
	popOverContent.setOnStartLoan(addPopUpCloseHandle(handler));
    }

    private EventHandler<ActionEvent> addPopUpCloseHandle(EventHandler<ActionEvent> handler) {
	return evt -> {
	    if (popOver != null) {
		popOver.hide();
	    }

	    handler.handle(evt);
	};
    }
}
