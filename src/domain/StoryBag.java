package domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * An item storing other items.
 *
 * @author Frederik De Smedt
 */
@Entity
@Access(AccessType.PROPERTY)
@NamedQueries({
    @NamedQuery(name = "StoryBag.findAll", query = "SELECT sb FROM StoryBag sb")
})
public class StoryBag extends Item implements Serializable {

    private ObservableList<Item> items = FXCollections.observableArrayList();

    public StoryBag() {
    }

    public StoryBag(String name, String description, List<String> theme, String ageCategory) {
	super(name, description, theme, ageCategory);
    }

    public void addItem(Item item) {
	items.add(item);
    }

    public void removeItem(Item item) {
	items.remove(item);
    }

    @Transient
    public ObservableList<Item> getObservableItems() {
	return items;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    public List<Item> getItems() {
	return items;
    }

    public void setItems(List<Item> items) {
	this.items = FXCollections.observableArrayList(items);
    }

    @Override
    public String toString() {
	return getName() + " (Inhoud: " + (getItems().isEmpty() ? "geen " : getItems().size() + " ") + (getItems().size() == 1 ? "voorwerp" : "voorwerpen") + ")";
    }

    @Override
    public boolean test(String query) {
	if (super.test(query)) {
	    return true;
	}

	for (Item item : getItems()) {
	    if (item.test(query)) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public Importer createImporter() {
	return new StoryBagImporter();
    }

    private class StoryBagImporter extends ItemImporter<StoryBag> {

	public StoryBagImporter() {
	    super(StoryBag::new);
	}

	@Override
	public String predictField(String columnName) {
	    if (SearchPredicate.containsAnyIgnoreCase(columnName, "verteltas", "tas")) {
		return "Titel";
	    } else {
		return super.predictField(columnName);
	    }
	}
    }

}
