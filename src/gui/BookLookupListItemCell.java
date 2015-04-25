package gui;

import domain.Book;
import domain.Cache;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 *
 * @author Pieter-Jan Geeroms
 */
public class BookLookupListItemCell extends ListCell<Book> {

    public static Callback<ListView<Book>, ListCell<Book>> forListView() {
        return i -> new BookLookupListItemCell();
    }

    private BookLookupListItem listItem;

    public BookLookupListItemCell() {
    }

    /**
     *
     * @param item {@link Book} to add in the list
     * @param empty Boolean that represents if the item has data or should be
     * presented as an empty row.
     */
    @Override
    protected void updateItem(Book item, boolean empty) {
        super.updateItem(item, empty);
        if (super.isEmpty()) {
            listItem = null;
            super.setGraphic(null);
        } else {
            listItem = Cache.getBookCache().get(item);
            if (listItem != null) {
                if (Platform.isFxApplicationThread()) {
                    super.setGraphic(listItem);
                } else {
                    Platform.runLater(() -> super.setGraphic(listItem));
                }
            }
        }
    }
}
