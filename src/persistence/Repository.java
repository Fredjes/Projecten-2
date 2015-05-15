package persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Frederik De Smedt
 */
public abstract class Repository<E> {

    private List<Runnable> listeners = new ArrayList<>();
    private static List<Repository> repositories = new ArrayList<>();

    public Repository() {
	submitRepository(this);
    }
    
    public static void submitRepository(Repository repository){
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

    public void addAll(List<E> e) {
	e.forEach(this::add);
    }

    public void addAll(E... data) {
	Arrays.stream(data).forEach(this::add);
    }
    
    public void removeAll(List<E> e) {
	e.forEach(this::remove);
    }

    public void removeAll(E... data) {
	Arrays.stream(data).forEach(this::remove);
    }

    public static void saveAllChanges(){
	repositories.forEach(Repository::saveChanges);
    }
    
    public abstract void add(E e);

    public abstract void remove(E e);

    public abstract void saveChanges();
}
