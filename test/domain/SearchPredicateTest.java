package domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Frederik
 */
public class SearchPredicateTest {

    private SearchPredicate predicate;

    @Before
    public void initialize() {
	predicate = new SearchPredicate();
    }

    @Test
    public void testPredicateUsesSearchable() {
	Assert.assertTrue(predicate.test(s -> true));
	Assert.assertFalse(predicate.test(s -> false));
    }

    @Test
    public void testPredicateUsesSearchQuery() {
	predicate.setSearchQuery("test");
	Assert.assertTrue(predicate.test(s -> s.equals("test")));
	Assert.assertFalse(predicate.test(s -> s.equals("test2")));
    }

    @Test
    public void testPredicateUsesSelectedClass() {
	predicate.setSelectedClass(Object.class);
	Assert.assertTrue(predicate.test(s -> true));

	predicate.setSelectedClass(SearchPredicate.class);
	Assert.assertFalse(predicate.test(s -> true));
    }

    @Test
    public void testFilteredListFactoryUsesParameters() {
	ObservableList<Searchable> baseList = FXCollections.observableArrayList();
	ObservableList<Searchable> filteredList = SearchPredicate.filteredListFor(baseList, predicate);
	predicate.setSearchQuery("test");
	for (int i = 0; i < 10; i++) {
	    baseList.add(Mockito.mock(Searchable.class));
	}

	filteredList.forEach(i -> {
	});
	
	baseList.forEach(i -> Mockito.verify(i).test("test"));
    }

    @Test
    public void testFilteredListOnlyReturnsPassedItems() {
	ObservableList<Searchable> baseList = FXCollections.observableArrayList();
	ObservableList<Searchable> filteredList = SearchPredicate.filteredListFor(baseList, predicate);
	predicate.setSearchQuery("test");

	Searchable s1 = s -> s.equals("test");
	Searchable s2 = s -> s.equals("notatest");
	Searchable s3 = s -> s.length() == 4;

	baseList.addAll(s1, s2, s3);

	Assert.assertEquals(2, filteredList.size());
	Assert.assertEquals(s1, filteredList.get(0));
	Assert.assertEquals(s3, filteredList.get(1));
    }
}