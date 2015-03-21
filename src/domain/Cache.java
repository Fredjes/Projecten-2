package domain;

import gui.ItemManagementListItem;
import gui.LoanManagementListItem;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 *
 * @author Frederik
 */
public class Cache<K, E> {

    private static Cache<Item, ItemManagementListItem> listItemInstance;
    private static Cache<Loan, LoanManagementListItem> loanInstance;

    private WeakHashMap<K, E> cache = new WeakHashMap<>();
    private Function<K, E> supplier;

    private Cache(Function<K, E> supplier) {
	this.supplier = supplier;
    }

    private E createVal(K key) {
	return supplier.apply(key);
    }

    public static Cache<Item, ItemManagementListItem> getItemCache() {
	if (listItemInstance == null) {
	    listItemInstance = new Cache<>(ItemManagementListItem::new);
	}

	return listItemInstance;
    }

    public static Cache<Loan, LoanManagementListItem> getLoanCache() {
	if (loanInstance == null) {
	    loanInstance = new Cache<>(LoanManagementListItem::new);
	}
	
	return loanInstance;
    }

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
