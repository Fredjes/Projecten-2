package persistence;

import domain.Book;
import domain.Damage;
import domain.Item;
import domain.ItemCopy;
import domain.PdfExporter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * The repository used to handle the storing of items, can synchronize save
 * changes with the database (using JPA).
 *
 * @author Brent
 */
public class ItemRepository extends Repository<Item> {

    private static ItemRepository INSTANCE;

    private ObservableList<Item> items = FXCollections.observableArrayList();
    private ObservableList<ItemCopy> itemCopies = FXCollections.observableArrayList();

    private List<Item> deletedElements = new ArrayList();

    private ItemRepository() {
	super();
	ListChangeListener changeListener = c -> {
	    while (c.next()) {
		c.getAddedSubList().forEach(i -> JPAUtil.getInstance().getEntityManager().persist(i));
	    }
	};

	items.addListener(changeListener);
	itemCopies.addListener(changeListener);
    }
    
    /**
     * Will get all items from the database and add them in the internal list.
     * Observers of the ObservableList will receive updates containg the new
     * data.
     */
    public void sync() {
	Thread t = new Thread(() -> {
	    synchronized (this) {
		loaded.set(false);
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		long count = (long) manager.createQuery("SELECT COUNT(i) FROM Item i").getSingleResult();
		items.clear();
		for (int i = 0; i < count; i += Repository.SYNC_BULK_STEP_COUNT) {
		    int size = (int) Math.min(Repository.SYNC_BULK_STEP_COUNT, count - i);
		    TypedQuery<Item> query = manager.createNamedQuery("Item.findAll", Item.class);
		    query.setMaxResults(size);
		    query.setFirstResult(i);
		    items.addAll(query.getResultList());
		}
//		items.setAll(JPAUtil.getInstance().getEntityManager().createNamedQuery("Item.findAll", Item.class).getResultList());
//		itemCopies.setAll(JPAUtil.getInstance().getEntityManager().createNamedQuery("ItemCopy.findAll", ItemCopy.class).getResultList());
		Logger.getLogger("Notification").log(Level.INFO, "Synchronized item repository with database");
		super.triggerListeners();
		try {
		    PdfExporter.saveItems();
		} catch (IOException ex) {
		}
		loaded.set(true);
	    }
	});

	t.setName("Item repository sync thread");
	t.start();
    }

    public static ItemRepository getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new ItemRepository();
	}
	return INSTANCE;
    }

    @Override
    public void add(Item item) {
	if (!items.contains(item)) {
	    items.add(item);
	}
    }

    @Override
    public void remove(Item item) {
	item.setVisible(false);
    }

    public void add(ItemCopy itemCopy) {
	itemCopies.add(itemCopy);
    }

    public void remove(ItemCopy itemCopy) {
	remove(itemCopy, true);
    }

    public void remove(ItemCopy itemCopy, boolean removeList) {
	itemCopies.remove(itemCopy);
	items.stream().filter(i -> i.getItemCopies().contains(itemCopy)).forEach((Item i) -> i.getItemCopies().remove(itemCopy));
    }

    public ObservableList<? extends Item> getItems() {
	return items;
    }

    public ObservableList<ItemCopy> getItemCopies() {
	return itemCopies;
    }

    public ObservableList<? extends Item> getItemsByClass(Class<? extends Item> clazz) {
	return getItemsByPredicate(i -> {
	    try {
		clazz.cast(i);
		return true;
	    } catch (ClassCastException ex) {
		return false;
	    }
	});
    }

    public ObservableList<ItemCopy> getItemCopiesByPredicate(Predicate<ItemCopy> predicate) {
	ObservableList<ItemCopy> filteredList = itemCopies.filtered(predicate);
	return filteredList;
    }

    public ObservableList<? extends Item> getItemsByPredicate(Predicate<Item> predicate) {
	ObservableList<? extends Item> filteredList = items.filtered(predicate);
	return filteredList;
    }

    /**
     * Only save the items that satisfy the predicate.
     *
     * @param itemPredicate The predicate which will be used to perform the
     * checks
     */
    public void saveChangesWithPredicate(Predicate<Item> itemPredicate) {
	Thread t = new Thread(() -> {
	    synchronized (this) {
		loaded.set(false);
		EntityManager manager = JPAUtil.getInstance().getEntityManager();
		manager.getTransaction().begin();

		getItemsByPredicate(itemPredicate).forEach(item -> {
		    if ((item.getName() == null || item.getName().isEmpty()) && (item.getDescription() == null || item.getDescription().isEmpty()) && item.getThemes().isEmpty() && (item.getAgeCategory() == null || item.getAgeCategory().isEmpty())) {
			return;
		    }

		    if (item.getId() == 0) {
			manager.persist(item);
		    } else {
			manager.merge(item);
		    }
		});

		deletedElements.stream().distinct().forEach((el) -> {
		    if (!manager.contains(el)) {
			if (manager.find(el.getClass(), el.getId()) != null) {
			    if (manager.contains(el)) {
				manager.remove(el);
			    } else {
				try {
				    manager.remove(manager.merge(el));
				} catch (Exception ex) {
				    ex.printStackTrace();
				}
			    }
			}
		    }
		});

		manager.getTransaction().commit();
		ItemRepository.getInstance().sync();
	    }
	});

	t.setName("DB save thread");
	t.start();
    }

    /**
     * Saves every item in the internal list to the database.
     */
    public void saveChanges() {
	saveChangesWithPredicate(i -> true);
    }

    public void saveItem(Item item) {
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();
	if (item.getId() != 0 && getItems().stream().anyMatch(i -> i.getId() == item.getId())) {
	    manager.merge(item);
	} else {
	    manager.persist(item);
	}
	manager.getTransaction().commit();
	add(item);
	super.triggerListeners();
    }

    public void saveItemCopy(ItemCopy copy) {
	EntityManager manager = JPAUtil.getInstance().getEntityManager();
	manager.getTransaction().begin();
	if (copy.getId() != 0) {
	    manager.merge(copy);
	} else {
	    manager.persist(copy);
	}
	manager.getTransaction().commit();
	add(copy);
	super.triggerListeners();
    }

    /**
     * Clear the internal list.
     */
    public void clearItems() {
	items.clear();
    }

    public ItemCopy createItemCopyFor(Item item) {
	List<ItemCopy> list = new ArrayList<>();
	ItemRepository.getInstance().getItemCopiesByPredicate(i -> i.getItem() != null && item.getClass().equals(i.getItem().getClass())).forEach(list::add);
	getItems().stream().filter(i -> !i.getItemCopies().isEmpty() && i.getClass().equals(item.getClass())).map(Item::getItemCopies).flatMap(List::stream).forEach(list::add);
	Optional<ItemCopy> last = list.stream().max((ic1, ic2) -> {
	    return Integer.parseInt(ic1.getCopyNumber().substring(1)) - Integer.parseInt(ic2.getCopyNumber().substring(1));
	});

	StringProperty lastNum = new SimpleStringProperty(Item.getCodePrefixFor(item) + "0");
	last.ifPresent(i -> lastNum.set(i.getCopyNumber()));
	lastNum.set(lastNum.get().substring(0, 1) + String.valueOf(Integer.parseInt(lastNum.get().substring(1)) + 1));
	ItemCopy ic = new ItemCopy(lastNum.get(), "", item, Damage.NO_DAMAGE);
	add(ic);
	return ic;
    }

    // Add mock data
    public static void main(String[] args) {
	//Book b = new Book("Romantiek", "8+", "Romeo en Julia", "Het beroemde verhaal van Shakespeare", "Shakespeare", "Shakespeare");
//	Book b2 = new Book("Actie", "12+", "Stormbreaker", "Een 15-jarige jongen treedt in dienst bij de geheime diensten.", "Anthony Horowitz", "Facet");
//	Game g = new Game("Financieel", "12+", "Monopolie", "Het beroemde Monopolie-spel", "Parkser Brothers");
	ItemRepository.getInstance().sync();
	Book b = (Book) ItemRepository.getInstance().getItemsByClass(Book.class).filtered(bo -> bo.getName().equals("Romeo en Julia")).stream().findFirst().get();
//	ItemCopy copy = new ItemCopy(6, "Achterste rij, links", b, Damage.MODERATE_DAMAGE);
//	ItemCopy copy2 = new ItemCopy(7, "Achterste rij, links", b, Damage.MODERATE_DAMAGE);
//	ItemRepository.getInstance().add(b);
//	ItemRepository.getInstance().add(b2);
//	ItemRepository.getInstance().add(g);
//	ItemRepository.getInstance().add(copy);
//	ItemRepository.getInstance().add(copy2);
//	StoryBag sb = new StoryBag();
//	sb.setName("De coole verteltas");
//	sb.setDescription("Een verteltas met nieuwe voorwerpen");
//	sb.setAgeCategory("10-12");
//	sb.setThemes("School");
	//ItemRepository.getInstance().add(sb);
	ItemRepository.getInstance().saveChanges();
	ItemRepository.getInstance().sync();
    }
    
    private BooleanProperty loaded = new SimpleBooleanProperty(false);
    
    @Override
    public BooleanProperty isLoaded() {
	return loaded;
    }
}
