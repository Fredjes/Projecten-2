package domain;

import gui.BookLookupListItem;
import gui.ItemManagementListItem;
import gui.LoanManagementListItem;
import gui.UserManagementListItem;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * A cache that holds GUI-components for specific entities. This cache will hold
 * entity GUI-components so that the expensive FXML-file loading doesn't need to
 * occur when it is not required.
 *
 * @author Frederik De Smedt
 * @param <K> The entity backing the component
 * @param <E> The GUI component backed by the entity
 */
public class Cache<K, E> {

    private static Cache<Item, ItemManagementListItem> listItemInstance;
    private static Cache<Loan, LoanManagementListItem> loanInstance;
    private static Cache<User, UserManagementListItem> userInstance;
    private static Cache<Book, BookLookupListItem> bookInstance;

    private WeakHashMap<K, E> cache = new WeakHashMap<>();
    private Function<K, E> supplier;

    /**
     * Constructor that will register a supplier. The supplier receives the
     * backing entity and should return a GUI-component based on the backing
     * entity.
     *
     * @param supplier
     */
    public Cache(Function<K, E> supplier) {
        this.supplier = supplier;
    }

    /**
     * Create a new GUI-component based on the given supplier.
     *
     * @param key The entity on which the component should be based on.
     * @return A GUI-component based on the entity
     */
    private E createVal(K key) {
        return supplier.apply(key);
    }

    /**
     * Get a specific Cache implementation that holds
     * {@link ItemManagementListItem}s for each registered {@link Item}.
     *
     * @return The cache implementation
     */
    public static Cache<Item, ItemManagementListItem> getItemCache() {
        if (listItemInstance == null) {
            listItemInstance = new Cache<>(ItemManagementListItem::new);
        }

        return listItemInstance;
    }

    public static Cache<User, UserManagementListItem> getUserCache() {
        if (userInstance == null) {
            userInstance = new Cache<>(UserManagementListItem::new);
        }

        return userInstance;
    }

    /**
     * Get a specific Cache implementation that holds
     * {@link LoanManagementListItem}s for each registered {@link Loan}.
     *
     * @return The cache implementation
     */
    public static Cache<Loan, LoanManagementListItem> getLoanCache() {
        if (loanInstance == null) {
            loanInstance = new Cache<>(LoanManagementListItem::new);
        }

        return loanInstance;
    }

    /**
     * Get a specific Cache implementation that holds
     * {@link BookLookupListItem}s for each registered {@link Book}.
     *
     * @return the cache implementation
     */
    public static Cache<Book, BookLookupListItem> getBookCache() {
        if (bookInstance == null) {
            bookInstance = new Cache<>(BookLookupListItem::new);
        }
        return bookInstance;
    }

    /**
     * Will return a GUI-component for the specified entity. If it is the first
     * time that the entity is used, and thus there is no registered
     * GUI-component, it will automatically create a new GUI-component based on
     * the entity and the give supplier (constructor).
     *
     * @param key The entity that backs the returned component
     * @return The GUI-component backed by the entity
     */
    public E get(K key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        } else {
            E val = createVal(key);
            cache.put(key, val);
            return val;
        }
    }
}
