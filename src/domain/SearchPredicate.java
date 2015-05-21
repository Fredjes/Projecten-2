package domain;

import java.util.Arrays;
import java.util.function.Predicate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * The class that is used by all Management GUI's, formally it is a stateful
 * {@link java.util.function.Predicate}.
 *
 * Since every management system allows for a String lookup (search bar), a
 * query-property can be bound so that the predicate will automatically be
 * updated to the newest query.
 *
 * @author Frederik De Smedt
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

    /**
     * Create a FilteredList that uses the base ObservableList and the
     * SearchPredicate that are given.
     *
     * @param <E> The type of the derived ObservableList
     * @param list The base ObservableList
     * @param predicate The predicate that will be used to filter data
     * @return The derived ObservableList
     */
    public static <E extends Searchable> ObservableList<E> filteredListFor(ObservableList<E> list, SearchPredicate predicate) {
	FilteredList<E> filteredList = new FilteredList(list);
	filteredList.setPredicate(predicate);
	return filteredList;
    }

    /**
     * Convenience method to check whether a String contains another String
     * while ignoring case. Widely used throughout the application.
     *
     * @param base The base String that will be used to check whether it
     * contains the other String
     * @param contains The String that will be used to check whether it is
     * contained in the base String
     * @return True if the base String contains the second parameter, false
     * otherwise
     */
    public static boolean containsIgnoreCase(String base, String contains) {
	if (base == null) {
	    return false;
	}

	if (contains == null) {
	    return true;
	}

	return base.toLowerCase().contains(contains.toLowerCase());
    }

    /**
     * Convenience method that checks whether the base String contains any of
     * the given parameters. Used widely throughout the application.
     *
     * @param base The base String which will be used to check if it contains
     * one of the parameters
     * @param contains The "list" of String that will be checked whether it is
     * contained within the base String
     * @return True if the base String contains any of the parameters, false
     * otherwise
     */
    public static boolean containsAnyIgnoreCase(String base, String... contains) {
	for (int i = 0; i < contains.length; i++) {
	    if (containsIgnoreCase(base, contains[i])) {
		return true;
	    }
	}

	return false;

	// Another possible approach could be by using streams:
	// return Arrays.stream(contains).anyMatch(c -> containsIgnoreCase(base, c));
	// Yet this cannot be used since the convenience method is widely used
	// in all entity classes, yet due to a JPA bug the stream approach
	// will break due to the use of lambda's.
    }
}
