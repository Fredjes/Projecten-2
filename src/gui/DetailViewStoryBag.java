package gui;

import domain.DetailViewUtil;
import domain.DragCommand;
import domain.DragUtil;
import domain.Item;
import domain.PropertyListBinding;
import domain.StoryBag;
import gui.controls.ItemTile;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

/**
 * A DetailView which can be bound to a specific StoryBag.
 *
 */
public class DetailViewStoryBag extends TabPane implements Binding<StoryBag> {

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private HBox themes;
    @FXML
    private TextField ageCategory;
    @FXML
    private TilePane lstItems;
    @FXML
    private ImageView image;

    private PropertyListBinding themesBinding;
    private ThemeManager themeManager;

    private StoryBag boundStoryBag;

    public DetailViewStoryBag() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_storybag.fxml"));
	themesBinding = new PropertyListBinding();

	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    description.setWrapText(true);
	    DetailViewUtil.initImageDragAndDrop(image);
	    DetailViewUtil.setBounds(this);
	    initListItemDrag();
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
	
	themeManager = new ThemeManager(themes);
    }

    private void createItemRemoveHandler(ItemTile item) {
	item.setOnSuccessfullDrag(e -> boundStoryBag.removeItem(item.getItem()));
    }

    private void initListItemDrag() {
	lstItems.setOnDragOver(de -> {
	    if (de.getDragboard().hasContent(DragCommand.DRAG_COMMAND_DATA_FORMAT) && DragCommand.isItemDrag((DragCommand)de.getDragboard().getContent(DragCommand.DRAG_COMMAND_DATA_FORMAT))) {
		de.acceptTransferModes(TransferMode.ANY);
	    }
	});

	lstItems.setOnDragDropped(de -> {
	    if (de.getDragboard().hasContent(DragCommand.DRAG_COMMAND_DATA_FORMAT) && DragCommand.isItemDrag((DragCommand)de.getDragboard().getContent(DragCommand.DRAG_COMMAND_DATA_FORMAT))) {
		Item item = DragUtil.getItem((DragCommand) de.getDragboard().getContent(DragCommand.DRAG_COMMAND_DATA_FORMAT));
		if (!boundStoryBag.getItems().contains(item)) {
		    boundStoryBag.addItem(item);
		}
	    }
	});

	lstItems.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
	    while (c.next()) {
		if (c.wasAdded()) {
		    c.getAddedSubList().stream().filter(n -> n instanceof ItemTile).map(n -> (ItemTile) n).forEach(this::createItemRemoveHandler);
		}
	    }
	});
    }

    @FXML
    public void onSaveImage() {
	DetailViewUtil.selectImage(image);
    }

    public TilePane getStoredItems() {
	return lstItems;
    }

    private ListChangeListener<Item> listChangeListener = (ListChangeListener.Change<? extends Item> c) -> {
	while (c.next()) {
	    if (c.wasAdded()) {
		c.getAddedSubList().forEach(i -> lstItems.getChildren().add(new ItemTile(i)));
	    } else if (c.wasRemoved()) {
		List<Node> shouldRemove = lstItems.getChildren().stream().filter(node -> {
		    if (!(node instanceof ItemTile)) {
			return false;
		    }

		    return c.getRemoved().contains(((ItemTile) node).getItem());
		}).collect(Collectors.toList());

		lstItems.getChildren().removeAll(shouldRemove);
	    }
	}
    };

    @Override
    public void bind(StoryBag t) {
	if (boundStoryBag != null) {
	    Bindings.unbindBidirectional(name.textProperty(), boundStoryBag.nameProperty());
	    Bindings.unbindBidirectional(description.textProperty(), boundStoryBag.descriptionProperty());
	    themesBinding.unbind();
	    Bindings.unbindBidirectional(ageCategory.textProperty(), boundStoryBag.ageCategoryProperty());
	    Bindings.unbindBidirectional(image.imageProperty(), boundStoryBag.imageProperty());
	    boundStoryBag.getObservableItems().removeListener(listChangeListener);
	}

	Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
	Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
	themeManager.bind(t.getThemeFX());
	Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());

	lstItems.getChildren().clear();
	t.getObservableItems().stream().map(ItemTile::new).forEach(lstItems.getChildren()::add);
	t.getObservableItems().addListener(listChangeListener);
	Bindings.bindBidirectional(image.imageProperty(), t.imageProperty());
	this.boundStoryBag = t;
    }

}
