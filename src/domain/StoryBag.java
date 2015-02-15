package domain;

import java.io.Serializable;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@Access(AccessType.PROPERTY)
@NamedQueries({
    @NamedQuery(name = "StoryBag.findAll", query = "SELECT sb FROM StoryBag sb")
})
public class StoryBag extends Item implements Serializable {

    private ObservableList<ItemCopy> items = FXCollections.observableArrayList();

    public StoryBag() {
    }

    public void addItem(ItemCopy item) {
	items.add(item);
    }

    public void removeItem(ItemCopy item) {
	items.remove(item);
    }

    @Transient
    public ObservableList<ItemCopy> getObservableItems() {
	return items;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(cascade = CascadeType.PERSIST)
    public List<ItemCopy> getItems() {
	return items;
    }

    public void setItems(List<ItemCopy> items) {
	this.items = FXCollections.observableArrayList(items);
    }
}
