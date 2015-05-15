package gui;

import domain.Book;
import domain.Item;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Pieter-Jan Geeroms
 */
public class BookLookupListItem extends AnchorPane {

    @FXML
    private ImageView bookImage;

    @FXML
    private GridPane detailPane;

    @FXML
    private Label bookTitle, bookDescription, bookAuthor, bookPublisher, labelAuthor, labelPublisher;

    private final ObjectProperty<Item> item = new SimpleObjectProperty<>();

    /**
     * Creates a BookLookupListItem
     *
     * @param item Item to be set in the ListItem
     */
    public BookLookupListItem(Item item) {
        this();
        setItem(item);
        setValues();
    }

    private BookLookupListItem() {
        FXUtil.loadFXML(this, "booklookupframe");
    }

    /**
     * Sets the {@link Item}
     *
     * @param item
     */
    private void setItem(Item item) {
        if (this.item.get() == item) {
            return;
        }

        this.item.set(item);
    }

    /**
     * Gets the {@link Item}
     *
     * @return The item
     */
    public Item getItem() {
        return item.get();
    }

    /**
     * Sets the fields of the ListItem
     */
    private void setValues() {
        bookTitle.textProperty().set(item.get().getName());
        bookDescription.textProperty().set(item.get().getDescription());
        bookImage.imageProperty().set(item.get().getFXImage());

        if (item.get() instanceof Book) {
            Book b = (Book) item.get();
            if (!b.getAuthor().trim().isEmpty()) {
                bookAuthor.textProperty().set(b.getAuthor());
            } else {
                detailPane.getChildren().remove(labelAuthor);
            }

            if (!b.getPublisher().trim().isEmpty()) {
                bookPublisher.textProperty().set(b.getPublisher());
            } else {
                detailPane.getChildren().remove(labelPublisher);
            }
        }
    }
}
