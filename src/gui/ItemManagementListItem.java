package gui;

import domain.DetailViewUtil;
import domain.Item;
import domain.ItemCopy;
import domain.ObservableListUtil;
import domain.User;
import gui.controls.CopyButton;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    private final ObjectProperty<Item> item = new SimpleObjectProperty<>();
    private Item oldItem;

    public ItemManagementListItem() {
	FXUtil.loadFXML(this, "listview_item");
	DetailViewUtil.initImageDragAndDrop(itemImage);
	item.addListener(o -> updateBindings());
    }

    public Button getAddCopyButton() {
	return addCopyButton;
    }

    public void setItem(Item item) {
	this.item.set(item);
    }

    public Item getItem() {
	return item.get();
    }

    private void updateBindings() {
	if (oldItem != null) {
	    title.textProperty().unbind();
	    description.textProperty().unbind();
	    itemImage.imageProperty().unbindBidirectional(oldItem.imageProperty());
	}

	title.textProperty().bind(item.get().nameProperty());
	description.textProperty().bind(item.get().descriptionProperty());
	itemImage.imageProperty().bindBidirectional(item.get().imageProperty());
	ItemRepository.getInstance().getItemCopiesByPredicate((ItemCopy ic) -> ic.getItem() == item.get() || ic.getItem().equals(item.get())).forEach(ic -> {
	    CopyButton button = new CopyButton(ic);
	    initOnDelete(button, ic);
	    copyList.getChildren().add(button);
	});

	if (UserRepository.getInstance().getAuthenticatedUser() == null || UserRepository.getInstance().getAuthenticatedUser().getUserType() != User.UserType.TEACHER) {
	    addCopyButton.setVisible(false);
	}

	oldItem = item.get();
    }

    private void initOnDelete(CopyButton button, ItemCopy backedCopy) {
	button.setOnDelete(e -> {
	    ItemRepository.getInstance().remove(backedCopy);
	    item.get().getItemCopies().remove(backedCopy);
	    copyList.getChildren().remove(button);
	});
    }

    @FXML
    public void onAdd(ActionEvent evt) {
	ItemCopy copy = ItemRepository.getInstance().createItemCopyFor(item.get());
	copy.setItem(item.get());
	item.get().getItemCopies().add(copy);
	CopyButton button = new CopyButton(copy);
	initOnDelete(button, copy);
	copyList.getChildren().add(button);

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
