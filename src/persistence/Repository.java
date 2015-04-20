package persistence;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Frederik De Smedt
 */
public abstract class Repository<E> {

    private List<Runnable> listeners = new ArrayList<>();

    public void addSyncListener(Runnable r) {
	listeners.add(r);
    }

    public void removeSyncListener(Runnable r) {
	listeners.remove(r);
    }

    protected void triggerListeners() {
	listeners.forEach(Runnable::run);
	listeners.clear();
    }
    
    public abstract void add(E e);
    
    public abstract void remove(E e);
    
    public abstract void saveChanges();
}
