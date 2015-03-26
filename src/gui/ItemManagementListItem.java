package gui;

import domain.DetailViewUtil;
import domain.Item;
import domain.ItemCopy;
import domain.controllers.ItemManagementListItemController;
import gui.controls.CopyButton;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
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

/**
 *
 * @author Frederik De Smedt
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
    private ItemManagementListItemController controller;

    public ItemManagementListItem(Item item) {
	this();
	setItem(item);
	item.getObservableItemCopies().addListener((ListChangeListener.Change<? extends ItemCopy> c) -> {
	    while (c.next()) {
		if (c.wasAdded()) {
		    c.getAddedSubList().forEach(this::addCopyButtonFor);
		} else if (c.wasRemoved()) {
		    copyList.getChildren().removeIf(b -> {
			if (b instanceof CopyButton) {
			    return c.getRemoved().stream().anyMatch(ic -> ic.getCopyNumber().equals(((CopyButton) b).getCopy().getCopyNumber()));
			} else {
			    return false;
			}
		    });
		}
	    }
	});
	
	item.getItemCopies().forEach(this::addCopyButtonFor);
    }

    private void addCopyButtonFor(ItemCopy copy) {
	CopyButton button = new CopyButton(copy);
	initOnDelete(button, copy);
	copyList.getChildren().add(button);
    }

    private ItemManagementListItem() {
	FXUtil.loadFXML(this, "listview_item");
	DetailViewUtil.initImageDragAndDrop(itemImage);
	item.addListener(o -> updateBindings());
	controller = new ItemManagementListItemController(this, null);
    }

    public Button getAddCopyButton() {
	return addCopyButton;
    }

    private void setItem(Item item) {
	if (this.item.get() == item) {
	    return;
	}

	this.item.set(item);
    }

    public Item getItem() {
	return item.get();
    }

    private void updateBindings() {
	title.textProperty().bindBidirectional(item.get().nameProperty());
	description.textProperty().bindBidirectional(item.get().descriptionProperty());
	itemImage.imageProperty().bindBidirectional(item.get().imageProperty());
	copyList.getChildren().clear();
	ItemRepository.getInstance().getItemCopiesByPredicate((ItemCopy ic) -> ic.getItem() == item.get() || ic.getItem().equals(item.get())).forEach(ic -> {
	    CopyButton button = new CopyButton(ic);
	    initOnDelete(button, ic);
	    copyList.getChildren().add(button);
	});
    }

    private void initOnDelete(CopyButton button, ItemCopy backedCopy) {
	button.setOnDelete(e -> {
	    ItemRepository.getInstance().remove(backedCopy);
	    item.get().getItemCopies().removeIf(ic -> ic.getCopyNumber().equals(backedCopy.getCopyNumber()));
	});
    }

    @FXML
    public void onAdd(ActionEvent evt) {
	ItemCopy copy = ItemRepository.getInstance().createItemCopyFor(item.get());
	ItemRepository.getInstance().add(copy);
	copy.setItem(item.get());
	item.get().getItemCopies().add(copy);
	CopyButton button = new CopyButton(copy);
	initOnDelete(button, copy);
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
