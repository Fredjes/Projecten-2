package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author Frederik
 */
public class SearchPredicate implements Predicate<Searchable> {

    private final ObjectProperty<Class<?>> selectedClass = new SimpleObjectProperty<>(Object.class);
    private final StringProperty searchQuery = new SimpleStringProperty("");

    public SearchPredicate() {
    }

    public SearchPredicate(Class<?> selectedClass, String searchQuery) {
	this.selectedClass.set(selectedClass);
	this.searchQuery.set(searchQuery);
    }

    @Override
    public boolean test(Searchable s) {
	String query = this.searchQuery.get();

	if (selectedClass == null) {
	    return s.test(query);
	} else {
	    return selectedClass.get().isAssignableFrom(s.getClass()) && s.test(query);
	}
    }

    public Class<?> getSelectedClass() {
	return selectedClass.get();
    }

    public void setSelectedClass(Class<?> selectedClass) {
	this.selectedClass.set(selectedClass);
    }

    public ObjectProperty<Class<?>> selectedClassProperty() {
	return this.selectedClass;
    }

    public String getSearchQuery() {
	return searchQuery.get();
    }

    public void setSearchQuery(String searchQuery) {
	this.searchQuery.set(searchQuery == null ? "" : searchQuery);
    }

    public StringProperty searchQueryProperty() {
	return searchQuery;
    }

    public static <E extends Searchable> ObservableList<E> filteredListFor(ObservableList<E> list, SearchPredicate predicate) {
	FilteredList<E> filteredList = new FilteredList(list);
	InvalidationListener l = e -> {
	    Searchable[] tempArr = new Searchable[filteredList.size()];
	    filteredList.toArray(tempArr);
	    List<Searchable> temp = new ArrayList<>(Arrays.asList(tempArr));
	    filteredList.forEach(filteredList::remove);
	    temp.forEach(s -> filteredList.add((E) s));
	};

//	predicate.searchQueryProperty().addListener(l);
//	predicate.selectedClassProperty().addListener(l);
	filteredList.setPredicate(predicate);
	return filteredList;
    }

    public static boolean containsIgnoreCase(String base, String contains) {
	if (base == null) {
	    return false;
	}

	if (contains == null) {
	    return true;
	}

	return base.toLowerCase().contains(contains.toLowerCase());
    }
}
