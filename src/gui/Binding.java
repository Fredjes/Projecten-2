package gui;

/**
 * An abstract interface that allows the binding of some type T to something
 * (for example DetailViews).
 *
 * @author PjGeeroms
 */
public interface Binding<T> {

    void bind(T t);
}
