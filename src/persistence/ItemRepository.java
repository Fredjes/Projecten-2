package persistence;

import domain.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;

/**
 *
 * @author Brent
 */
public class ItemRepository {

    private static ItemRepository INSTANCE;

    private final ObservableList<Item> items = FXCollections.observableArrayList();

    private ItemRepository() {

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

    public Item getItemByName(String name) {
	return null; //items.stream().filter(i -> i.getName().equals(name)).findFirst().get();
    }

    public void saveChanges() {
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();
	items.forEach(manager::persist);
	manager.getTransaction().commit();
    }
}
