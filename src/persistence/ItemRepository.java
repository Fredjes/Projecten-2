package persistence;

import domain.Item;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;

/**
 * The repository used to handle the storing of items, can synchronize save changes with the database (using JPA).
 *
 * @author Brent
 */
public class ItemRepository {

    private static ItemRepository INSTANCE;

    private ObservableList<Item> items = FXCollections.observableArrayList();

    private ItemRepository() {
    }

    /**
     * Will get all items from the database and add them in the internal list. Observers of the ObservableList will receive updates containg the new data.
     */
    public void sync() {
	List<Item> dbItems = JPAUtil.getInstance().getEntityManager().createNamedQuery("Item.findAll", Item.class).getResultList();
	items.clear();
	items.addAll(dbItems);
    }

    public static ItemRepository getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new ItemRepository();
	}
	return INSTANCE;
    }

    public void add(Item item) {
	items.add(item);
    }

    public void remove(Item item) {
	items.remove(item);
    }

    public ObservableList<Item> getItems() {
	return items;
    }

    public Item getItemByName(String name) {
	try {
	    return items.stream().filter(i -> i.getName().equalsIgnoreCase(name)).findFirst().get();
	} catch (NoSuchElementException nsex) {
	    return null;
	}
    }

    public ObservableList<Item> getItemsByPartialName(String partialName) {
	return FXCollections.observableArrayList(items.stream().filter(i -> i.getName().toLowerCase().contains(partialName.toLowerCase())).collect(Collectors.toList()));
    }

    public ObservableList<Item> getItemsByCategory(String category) {
	return FXCollections.observableArrayList(items.stream().filter(i -> i.getAgeCategory().equalsIgnoreCase(category)).collect(Collectors.toList()));
    }

    public ObservableList<Item> getItemsByTheme(String theme) {
	return FXCollections.observableArrayList(items.stream().filter(i -> i.getTheme().equalsIgnoreCase(theme)).collect(Collectors.toList()));
    }

    /**
     * Saves every item in the internal list to the database.
     */
    public void saveChanges() {
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();
	items.forEach(manager::persist);
	manager.getTransaction().commit();
    }

    /**
     * Clear the internal list.
     */
    public void clearItems() {
	items.clear();
    }
}
