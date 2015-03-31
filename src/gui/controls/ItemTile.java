package gui.controls;

import domain.DragCommand;
import domain.DragUtil;
import domain.Item;
import gui.FXUtil;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

/**
 *
 * @author Frederik
 */
public class ItemTile extends VBox {

    @FXML
    private ImageView image;

    @FXML
    private Label title;

    private Item item;

    private EventHandler<DragEvent> onDragCompleteEventHandler;

    public ItemTile(Item item) {
	FXUtil.loadFXML(this, "image_title_tile");
	image.imageProperty().bind(item.imageProperty());
	title.textProperty().bind(item.nameProperty());
	this.item = item;
	this.setOnDragDetected(e -> {
	    Dragboard board = this.startDragAndDrop(TransferMode.MOVE);
	    board.setDragView(image.snapshot(null, null));
	    ClipboardContent content = new ClipboardContent();
	    content.put(DragCommand.DRAG_COMMAND_DATA_FORMAT, new DragCommand(item, DragUtil.ITEM_REMOVE));
	    board.setContent(content);
	});

	this.setOnDragDone(e -> {
	    if (e.getAcceptedTransferMode() != null) {
		if (onDragCompleteEventHandler != null) {
		    onDragCompleteEventHandler.handle(e);
		}
	    }
	});
    }

    public void setOnSuccessfullDrag(EventHandler<DragEvent> handler) {
	this.onDragCompleteEventHandler = handler;
    }

    public Item getItem() {
	return item;
    }
}
