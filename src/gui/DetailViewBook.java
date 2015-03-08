package gui;

import domain.Book;
import domain.PropertyListBinding;
import domain.ThemeConverter;
import gui.dialogs.PopupManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

public class DetailViewBook extends TabPane implements Binding<Book> {

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private TextField themes;
    @FXML
    private TextField ageCategory;
    @FXML
    private TextField author;
    @FXML
    private TextField publisher;
    @FXML
    private ImageView image;

    private PropertyListBinding themesBinding;

    private Book boundedBook;

    private PopOver shownPopup;

    public DetailViewBook() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_book.fxml"));
	themesBinding = new PropertyListBinding();

	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    description.setWrapText(true);

	    image.setOnDragEntered(evt -> {
		if (evt.getDragboard().hasFiles()) {
		    Label shownLabel = new Label();

		    if (evt.getDragboard().getFiles().size() == 1 && evt.getDragboard().getFiles().get(0).getAbsolutePath().toLowerCase().matches(".*(png|gif|jpg|jps|mpo)$")) {
			shownLabel.setText("Afbeelding toevoegen aan voorwerp.");
		    } else {
			shownLabel.setText("Gelieve een afbeelding toe te voegen.");
		    }

		    shownLabel.setPadding(new Insets(-5, 5, -5, 5));
		    shownPopup = PopupManager.showPopOver(image, shownLabel, PopOver.ArrowLocation.RIGHT_BOTTOM);
		}
	    });

	    image.setOnDragDropped(evt -> {
		if (evt.getDragboard().hasFiles() && evt.getDragboard().getFiles().size() == 1 && evt.getDragboard().getFiles().get(0).getAbsolutePath().toLowerCase().matches(".*(png|gif|jpg|jps|mpo)$")) {
		    try {
			boundedBook.imageProperty().set(new Image(new FileInputStream(evt.getDragboard().getFiles().get(0))));
		    } catch (FileNotFoundException ex) {
			PopupManager.showNotification("Mislukt", "De afbeelding kon niet worden toegevoegd.", PopupManager.Notification.ERROR);
		    }
		}
	    });

	    image.setOnDragExited(evt -> {
		shownPopup.hide(Duration.seconds(1));
	    });

	    image.setOnDragOver(evt -> {
		if (evt.getDragboard().hasFiles() && evt.getDragboard().getFiles().size() == 1 && evt.getDragboard().getFiles().get(0).getAbsolutePath().toLowerCase().matches(".*(png|gif|jpg|jps|mpo)$")) {
		    evt.acceptTransferModes(TransferMode.LINK);
		}
	    });

	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @Override
    public void bind(Book t) {
	if (boundedBook != null) {
	    Bindings.unbindBidirectional(name.textProperty(), boundedBook.nameProperty());
	    Bindings.unbindBidirectional(description.textProperty(), boundedBook.descriptionProperty());
	    themesBinding.unbind();
	    Bindings.unbindBidirectional(ageCategory.textProperty(), boundedBook.ageCategoryProperty());
	    Bindings.unbindBidirectional(author.textProperty(), boundedBook.authorProperty());
	    Bindings.unbindBidirectional(publisher.textProperty(), boundedBook.publisherProperty());
	    Bindings.unbindBidirectional(image.imageProperty(), boundedBook.imageProperty());
	}

	Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
	Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
	themesBinding.bind(themes.textProperty(), t.getThemeFX(), new ThemeConverter());
	Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
	Bindings.bindBidirectional(author.textProperty(), t.authorProperty());
	Bindings.bindBidirectional(publisher.textProperty(), t.publisherProperty());
	Bindings.bindBidirectional(image.imageProperty(), t.imageProperty());
	this.boundedBook = t;
    }

}
