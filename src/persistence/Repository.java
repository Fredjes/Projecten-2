package persistence;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Frederik
 */
public class Repository {

    private List<Runnable> listeners = new ArrayList<>();

    public void addSyncListener(Runnable r) {
	listeners.add(r);
    }

    public void removeSyncListener(Runnable r) {
	listeners.remove(r);
    }

    protected void triggerListeners() {
	listeners.forEach(Runnable::run);
    }
}
