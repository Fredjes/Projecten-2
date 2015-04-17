package gui;

import domain.Book;
import domain.BookUtil;
import domain.DetailViewUtil;
import gui.controls.ThemeItem;
import gui.dialogs.PopupUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.controlsfx.control.PopOver;

public class DetailViewBook extends TabPane implements Binding<Book> {

    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private HBox themes;
    @FXML
    private TextField ageCategory;
    @FXML
    private TextField author;
    @FXML
    private TextField publisher;
    @FXML
    private TextField isbn;
    @FXML
    private Button zoeken;
    @FXML
    private ImageView image;

    @FXML
    private ProgressIndicator loadingIcon;

    private List<Book> books = new ArrayList<>();
    private Book boundBook;
    private ThemeManager themeManager;

    private Runnable bookSearch = () -> {
	Platform.runLater(() -> {
	    loadingIcon.setVisible(true);
	});
	if (isbn.getText().matches("^[0-9 -]*$")) {
	    books = BookUtil.searchIsbn(isbn.getText());
	} else {
	    books = BookUtil.searchTitle(isbn.getText());
	}
	Platform.runLater(() -> {
	    loadingIcon.setVisible(false);
	    if (books.size() > 0) {
		setDetailFields(0);
		if (books.size() > 1) {
		    Book selectedBook = PopupUtil.showSelectionQuestion(FXCollections.observableArrayList(books), "Meerdere boeken gevonden", "Kies het gewenste boek uit de lijst van gevonden boeken");
		    if (selectedBook != null) {
			setDetailFields(books.indexOf(selectedBook));
		    }
		}
	    } else {
		PopupUtil.showPopOver(zoeken, new Label(" Er werd geen boek gevonden! "), PopOver.ArrowLocation.LEFT_TOP);
	    }
	});
    };

    public DetailViewBook() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/gui/detailview_book.fxml"));

	try {
	    loader.setRoot(this);
	    loader.setController(this);
	    loader.load();
	    description.setWrapText(true);
	    DetailViewUtil.initImageDragAndDrop(image);
	    DetailViewUtil.setBounds(this);
	    themeManager = new ThemeManager(themes);
	} catch (IOException ex) {
	    throw new RuntimeException(ex);
	}
    }

    @FXML
    public void onTyping() {
	if (isbn.getText().matches("^[0-9 -]*$")) {
	    zoeken.setText("Zoeken op ISBN");
	} else {
	    zoeken.setText("Zoeken op titel");
	}
    }

    @FXML
    public void onSearch() {
	Thread t = new Thread(bookSearch);
	t.setName("Book search thread");
	t.start();
    }

    private void setDetailFields(int i) {
	Book book = books.get(i);
	name.setText(book.getName());
	description.setText(book.getDescription());
	ageCategory.setText(book.getAgeCategory());
	author.setText(book.getAuthor());
	publisher.setText(book.getPublisher());
	image.setImage(book.getFXImage());
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
	    Bindings.unbindBidirectional(ageCategory.textProperty(), boundBook.ageCategoryProperty());
	    Bindings.unbindBidirectional(author.textProperty(), boundBook.authorProperty());
	    Bindings.unbindBidirectional(publisher.textProperty(), boundBook.publisherProperty());
	    Bindings.unbindBidirectional(image.imageProperty(), boundBook.imageProperty());
	}

	Bindings.bindBidirectional(name.textProperty(), t.nameProperty());
	Bindings.bindBidirectional(description.textProperty(), t.descriptionProperty());
	Bindings.bindBidirectional(ageCategory.textProperty(), t.ageCategoryProperty());
	Bindings.bindBidirectional(author.textProperty(), t.authorProperty());
	Bindings.bindBidirectional(publisher.textProperty(), t.publisherProperty());
	Bindings.bindBidirectional(image.imageProperty(), t.imageProperty());
	themeManager.bind(t.getThemeFX());
	this.boundBook = t;
    }

}
