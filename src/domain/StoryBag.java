package domain;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Access(AccessType.FIELD)
public class StoryBag extends Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    private ObservableList<ItemCopy> items = FXCollections.observableArrayList();

    public StoryBag() {
    }

    public void addItem(ItemCopy item) {
	items.add(item);
    }

    public void removeItem(ItemCopy item) {
	items.remove(item);
    }

    public ObservableList<ItemCopy> getObservableItems() {
	return items;
    }

    @Access(AccessType.PROPERTY)
    public List<ItemCopy> getItems() {
	return items;
    }

    public void setItems(List<ItemCopy> items) {
	this.items = FXCollections.observableArrayList(items);
    }
}
