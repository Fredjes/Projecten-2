package domain;

import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author Frederik
 */
public class SearchPredicate implements Predicate<Searchable> {

    private Class<?> selectedClass = Object.class;
    private final StringProperty searchQuery = new SimpleStringProperty("");

    public SearchPredicate() {
    }

    public SearchPredicate(Class<?> selectedClass, String searchQuery) {
	this.selectedClass = selectedClass;
	this.searchQuery.set(searchQuery);
    }

    @Override
    public boolean test(Searchable s) {
	String query = this.searchQuery.get();

	if (selectedClass == null) {
	    return s.test(query);
	} else {
	    return selectedClass.isAssignableFrom(s.getClass()) && s.test(query);
	}
    }

    public Class<?> getSelectedClass() {
	return selectedClass;
    }

    public void setSelectedClass(Class<?> selectedClass) {
	this.selectedClass = selectedClass;
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
	filteredList.setPredicate(predicate);
	return filteredList;
    }

    public static boolean containsIgnoreCase(String base, String contains) {
	return base.toLowerCase().contains(contains.toLowerCase());
    }
}
