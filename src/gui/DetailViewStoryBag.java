package gui;

import domain.DetailViewUtil;
import domain.PropertyListBinding;
import domain.StoryBag;
import domain.ThemeConverter;
import domain.controllers.StoryBagController;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DetailViewStoryBag extends TabPane implements Binding<StoryBag> {

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private TextField themes;
    @FXML
    private TextField ageCategory;
    @FXML
    private ListView lstItems;
    @FXML
    private ImageView image;

    private PropertyListBinding themesBinding;

    private StoryBag boundedStoryBag;

    private StoryBagController controller;

    public DetailViewStoryBag(StoryBagController controller) {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_storybag.fxml"));
	this.controller = controller;
	themesBinding = new PropertyListBinding();

	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    description.setWrapText(true);
	    DetailViewUtil.initImageDragAndDrop(image);
	    initListItemDrag();
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    private void initListItemDrag() {
	final String copyDragText = "COPY_DRAG:";
	lstItems.setOnDragEntered(de -> {
	    System.out.println(de.getDragboard().getString());
	    Dragboard board = de.getDragboard();
	    if (board.hasString() && board.getString().startsWith(copyDragText)) {
		System.out.println("accepted");
		de.acceptTransferModes(TransferMode.LINK);
	    }
	});

	lstItems.setOnDragDropped(de -> {
	    Dragboard board = de.getDragboard();
	    if (board.hasString() && board.getString().startsWith(copyDragText)) {
		controller.addItemCopyToStoryBagByCopyId(board.getString().substring(copyDragText.length()), lstItems);
		de.setDropCompleted(true);
	    }
	});
    }

    @FXML
    public void onSaveImage() {
	DetailViewUtil.selectImage(image);
    }

    public ListView getStoredItems() {
	return lstItems;
    }

    @Override
    public void bind(StoryBag t) {
	if (boundedStoryBag != null) {
	    Bindings.unbindBidirectional(name.textProperty(), boundedStoryBag.nameProperty());
	    Bindings.unbindBidirectional(description.textProperty(), boundedStoryBag.descriptionProperty());
	    themesBinding.unbind();
	    Bindings.unbindBidirectional(ageCategory.textProperty(), boundedStoryBag.ageCategoryProperty());
	    Bindings.unbindBidirectional(image.imageProperty(), boundedStoryBag.imageProperty());
	}

	Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
	Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
	themesBinding.bind(themes.textProperty(), t.getThemeFX(), new ThemeConverter());
	Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
	lstItems.setItems(t.getObservableItems());
	Bindings.bindBidirectional(image.imageProperty(), t.imageProperty());
	this.boundedStoryBag = t;
    }

}
