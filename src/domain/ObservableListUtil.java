package domain;

import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * A util-class that allows for some operations that are not supported in the
 * native JavaFX API.
 *
 * @author Frederik De Smedt
 */
public class ObservableListUtil {

    /**
     * This method will return a (new) ObservableList based on the
     * ObservableList that is given and a converter. The new ObservableList will
     * then hold the converted elements of the base ObservableList and will be
     * automatically updated.
     *
     * @param <E> Type of the data that the base ObservableList holds
     * @param <T> Type of the data that the derived ObservableList should hold
     * @param baseList The baselist which will be used to retrieve data
     * @param converter The converter that will convert the data of the base
     * ObservableList
     * @return A new ObservableList derived from the base ObservableList
     */
    public static <E, T> ObservableList<T> convertObservableList(ObservableList<? extends E> baseList, Function<E, T> converter) {
	ObservableList<T> converted = FXCollections.observableArrayList();
	baseList.forEach(i -> converted.add(converter.apply(i)));
	baseList.addListener((ListChangeListener.Change<? extends E> c) -> {
	    while (c.next()) {
		if (c.wasAdded()) {
		    c.getAddedSubList().forEach(i -> converted.add(converter.apply(i)));
		}

		if (c.wasRemoved()) {
		    c.getRemoved().forEach(i -> converted.remove(converter.apply(i)));
		}
	    }
	});

	return converted;
    }
}
