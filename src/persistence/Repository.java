package persistence;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;

/**
 * An abstract Repository with some basic implementations and a minimum amount
 * of methods that must be supported by every Repository.
 *
 * Note: Abstraction (of the abstract methods) is necessary to enforce every
 * Repository to have a certain amount of methods, since some systems such as
 * the excel import system might not know which repository is being used.
 *
 * @author Frederik De Smedt
 */
public abstract class Repository<E> {

    private List<Runnable> listeners = new ArrayList<>();
    private static List<Repository> repositories = new ArrayList<>();
    
    public static int SYNC_BULK_STEP_COUNT = 5;

    public Repository() {
	submitRepository(this);
    }

    public static void submitRepository(Repository repository) {
	repositories.add(repository);
    }

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

    public static void saveAllChanges() {
	repositories.forEach(Repository::saveChanges);
    }

    public abstract void add(E e);

    public abstract void remove(E e);

    public abstract void saveChanges();
    
    public abstract BooleanProperty isLoaded();
}
