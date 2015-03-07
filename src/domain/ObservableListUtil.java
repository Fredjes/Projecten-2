package domain;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 *
 * @author Frederik
 */
public class ObservableListUtil {

    public static <E, T> ObservableList<T> convertObservableList(ObservableList<? extends E> baseList, ListConverter<E, T> converter) {
	ObservableList<T> converted = FXCollections.observableArrayList();
	baseList.forEach(i -> converted.add(converter.convert(i)));
	baseList.addListener((ListChangeListener.Change<? extends E> c) -> {
	    while (c.next()) {
		if (c.wasAdded()) {
		    c.getAddedSubList().forEach(i -> converted.add(converter.convert(i)));
		}

		if (c.wasRemoved()) {
		    c.getRemoved().forEach(i -> converted.remove(converter.convert(i)));
		}

		if (c.wasPermutated()) {
		    int oldIndex = c.getFrom();
		    int newIndex = c.getPermutation(oldIndex);
		    
		    T temp = converted.get(oldIndex);
		    converted.set(oldIndex, converted.get(newIndex));
		    converted.set(newIndex, temp);
		}
	    }
	});

	return converted;
    }

    public static interface ListConverter<E, T> {

	T convert(E e);
    }
}
