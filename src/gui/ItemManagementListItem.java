package gui;

import domain.Item;
import domain.ObservableListUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Frederik
 */
public class ItemManagementListItem extends AnchorPane {

    private static final ObservableListUtil.ListConverter<Item, ItemManagementListItem> converter = ItemManagementListItem::new;

    @FXML
    private HBox copyList;

    @FXML
    private Label description;

    @FXML
    private Label title;

    @FXML
    private ImageView itemImage;

    private EventHandler<ActionEvent> addHandler;

    public ItemManagementListItem() {
	FXUtil.loadFXML(this, "listview_item");
    }

    public ItemManagementListItem(Item item) {
	this();
 	title.setText(item.getName());
	description.setText(item.getDescription());
	itemImage.setImage(item.getFXImage());
    }

    @FXML
    public void onAdd(ActionEvent evt) {
	addHandler.handle(evt);
    }

    public void setOnAdd(EventHandler<ActionEvent> handler) {
	this.addHandler = handler;
    }

    public static ObservableListUtil.ListConverter<Item, ItemManagementListItem> getItemManagementListItemConverter() {
	return converter;
    }
}
