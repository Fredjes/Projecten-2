package domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StoryBag {

    private ObservableList<Item> items = FXCollections.observableArrayList();

    public StoryBag() {
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public ObservableList<Item> getItems() {
        return items;
    }

}
