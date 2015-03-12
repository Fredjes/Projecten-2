package gui;

import domain.DetailViewUtil;
import domain.Item;
import domain.ItemCopy;
import domain.ObservableListUtil;
import domain.User;
import gui.controls.CopyButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import persistence.ItemRepository;
import persistence.UserRepository;

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

    @FXML
    private Button addCopyButton;

    @FXML
    private HBox managementTab;

    private Item backedItem;

    private ItemManagementListItem() {
	FXUtil.loadFXML(this, "listview_item");
    }

    public Button getAddCopyButton() {
	return addCopyButton;
    }

    public ItemManagementListItem(Item item) {
	this();

	this.backedItem = item;
	title.textProperty().bind(item.nameProperty());
	description.textProperty().bind(item.descriptionProperty());
	itemImage.imageProperty().bindBidirectional(item.imageProperty());
	DetailViewUtil.initImageDragAndDrop(itemImage);
	ItemRepository.getInstance().getItemCopiesByPredicate((ItemCopy ic) -> ic.getItem() == backedItem || ic.getItem().equals(backedItem)).forEach(ic -> {
	    CopyButton button = new CopyButton(ic);
	    initOnDelete(button, ic);
	    copyList.getChildren().add(button);
	});
    }

    public Item getBackedItem() {
	return backedItem;
    }

    private void initOnDelete(CopyButton button, ItemCopy backedCopy) {
	button.setOnDelete(e -> {
	    ItemRepository.getInstance().remove(backedCopy);
	    backedItem.getItemCopies().remove(backedCopy);
	    copyList.getChildren().remove(button);
	});
    }

    @FXML
    public void onAdd(ActionEvent evt) {
	ItemCopy copy = ItemRepository.getInstance().createItemCopyFor(backedItem);
	copy.setItem(backedItem);
	backedItem.getItemCopies().add(copy);
	CopyButton button = new CopyButton(copy);
	initOnDelete(button, copy);
	copyList.getChildren().add(button);

    }

    public static ObservableListUtil.ListConverter<Item, ItemManagementListItem> getItemManagementListItemConverter() {
	return converter;
    }

    private ObservableList<ItemCopy> getCopiesOfItem(Item item) {
	FilteredList<ItemCopy> list = new FilteredList<>(ItemRepository.getInstance().getItemCopies());
	list.setPredicate(ic -> ic.getItem() == item);
	return list;
    }

    public HBox getManagementTab() {
	return managementTab;
    }
}
