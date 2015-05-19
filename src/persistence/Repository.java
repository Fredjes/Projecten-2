package persistence;

import domain.Changeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author Frederik De Smedt
 */
public abstract class Repository<E extends Changeable> {

    private List<Runnable> listeners = new ArrayList<>();
    private static Map<Integer, Repository> repositories = new HashMap<>();

    public static void submitRepository(Repository repository, List<Integer> supportedVersions) {
	supportedVersions.forEach(v -> repositories.put(v, repository));
    }

    public static Repository getRepositorySupportingVersion(int versionId) {
	return repositories.getOrDefault(versionId, null);
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

    public static void saveAllChanges() {
	repositories.values().stream().distinct().forEach(Repository::saveChanges);
    }

    public abstract void add(E e);
    
    public void remove(E value) {
	remove(val -> val.equals(value));
    }
    
    public abstract void update(int pk);

    public abstract void remove(Predicate<E> predicate);

    public abstract void saveChanges();
}
