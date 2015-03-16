package gui;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Frederik
 */
public class AsyncLoader<E> extends Service<E> {

    public static <E> void loadAsync(Supplier<E> supplier, Consumer<E> consumer) {
	AsyncLoader loader = new AsyncLoader();
	loader.setNodeSupplier(supplier);
	loader.setNodeConsumer(consumer);
	loader.start();
    }

    private Supplier<E> nodeSupplier;
    private Consumer<E> nodeConsumer;

    public Supplier<E> getNodeSupplier() {
	return nodeSupplier;
    }

    public void setNodeSupplier(Supplier<E> nodeSupplier) {
	this.nodeSupplier = nodeSupplier;
    }

    public Consumer<E> getNodeConsumer() {
	return nodeConsumer;
    }

    public void setNodeConsumer(Consumer<E> nodeConsumer) {
	this.nodeConsumer = nodeConsumer;
    }
    
    @Override
    protected Task<E> createTask() {
	Task<E> t = new Task<E>() {

	    @Override
	    protected E call() throws Exception {
		E e = nodeSupplier.get();
		nodeConsumer.accept(e);
		return e;
	    }
	};

	return t;
    }
}
