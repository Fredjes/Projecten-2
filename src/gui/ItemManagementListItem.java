package gui;

import domain.DetailViewUtil;
import domain.Item;
import domain.ItemCopy;
import domain.controllers.ItemManagementListItemController;
import gui.controls.CopyButton;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import persistence.ItemRepository;

/**
 * A single Item displayed in the ListView of {@link ItemManagement}.
 *
 * Shouldn't be manually instantiated, rather ask {@link domain.Cache} for an
 * instance for a certain Item.
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

	if (item == null) {
	    return;
	}

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

	SortedList<ItemCopy> copies = FXCollections.observableArrayList(item.getItemCopies()).sorted((i1, i2) -> {
	    return i1.getCopyNumber().compareTo(i2.getCopyNumber());
	});

	List<ItemCopy> shouldVisit = new ArrayList<>();
	copies.forEach(c -> {
	    if(!shouldVisit.contains(c) || (shouldVisit.contains(c) && shouldVisit.get(shouldVisit.indexOf(c)).getLoans().isEmpty() && !c.getLoans().isEmpty())) {
		shouldVisit.remove(c);
		shouldVisit.add(c);
	    }
	});
	
	shouldVisit.forEach(this::addCopyButtonFor);
    }

    private void addCopyButtonFor(ItemCopy copy) {
	CopyButton button = new CopyButton(copy);
	initEvents(button, copy);
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
	    initEvents(button, ic);
	    copyList.getChildren().add(button);
	});
    }

    private void initEvents(CopyButton button, ItemCopy backedCopy) {
	button.setOnDelete(e -> {
	    ItemRepository.getInstance().remove(backedCopy);
	    item.get().getItemCopies().removeIf(ic -> ic.getCopyNumber().equals(backedCopy.getCopyNumber()));
	});

	button.setOnStartLoan(e -> {
	    backedCopy.getLoans().add(e.getLoan());
	    CopyButton newButton = new CopyButton(backedCopy);
	    initEvents(newButton, backedCopy);
	    copyList.getChildren().set(copyList.getChildren().indexOf(button), newButton);
	});
    }

    @FXML
    public void onAdd(ActionEvent evt) {
	ItemCopy copy = ItemRepository.getInstance().createItemCopyFor(item.get());
	ItemRepository.getInstance().add(copy);
	copy.setItem(item.get());
	item.get().getItemCopies().add(copy);
	CopyButton button = new CopyButton(copy);
	initEvents(button, copy);
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
