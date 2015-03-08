package gui;

import domain.Book;
import domain.DetailViewUtil;
import domain.Item;
import domain.PropertyListBinding;
import domain.ThemeConverter;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import persistence.ItemRepository;
import persistence.JPAUtil;

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

    private Book boundBook;

    public DetailViewBook() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_book.fxml"));
	themesBinding = new PropertyListBinding();

	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    description.setWrapText(true);
	    DetailViewUtil.initImageDragAndDrop(image);
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @FXML
    public void onSaveImage() {
	DetailViewUtil.selectImage(image);
    }

    @Override
    public void bind(Book t) {
	if (boundBook != null) {
	    Bindings.unbindBidirectional(name.textProperty(), boundBook.nameProperty());
	    Bindings.unbindBidirectional(description.textProperty(), boundBook.descriptionProperty());
	    themesBinding.unbind();
	    Bindings.unbindBidirectional(ageCategory.textProperty(), boundBook.ageCategoryProperty());
	    Bindings.unbindBidirectional(author.textProperty(), boundBook.authorProperty());
	    Bindings.unbindBidirectional(publisher.textProperty(), boundBook.publisherProperty());
	    Bindings.unbindBidirectional(image.imageProperty(), boundBook.imageProperty());
	}

	Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
	Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
	themesBinding.bind(themes.textProperty(), t.getThemeFX(), new ThemeConverter());
	Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
	Bindings.bindBidirectional(author.textProperty(), t.authorProperty());
	Bindings.bindBidirectional(publisher.textProperty(), t.publisherProperty());
	Bindings.bindBidirectional(image.imageProperty(), t.imageProperty());
	this.boundBook = t;
    }

}
