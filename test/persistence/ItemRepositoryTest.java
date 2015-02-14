package persistence;

import domain.Book;
import domain.Item;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 *
 * @author Brent
 */
public class ItemRepositoryTest {

    private final ItemRepository repo = ItemRepository.getInstance();

    public ItemRepositoryTest() {
    }

    @Before
    public void init() {
	//set up dummy data
	repo.clearItems();
	repo.add(new Book("voeding", "3+", "Piet in de keuken", "Leer koken!", "Brent", "Frederik"));
	repo.add(new Book("kleding", "7+", "Piet in de kledingwinkel", "Kies kleding!", "Brent", "Frederik"));
	repo.add(new Book("school", "3+", "Piet in de schoolwinkel", "Koop schoolgerief!", "Brent", "Frederik"));
	repo.add(new Book("kleding", "3+", "Piet in de kledingwinkel", "geen description", "Brent", "Frederik"));
	repo.add(new Book("jobs", "3+", "Piet bij de brandweer", "geen description", "Brent", "Frederik"));
    }

    @Test
    public void testGetItemByName() {
	Item i = repo.getItemByName("Piet in de keuken");
	assertNotNull(i);
	assertEquals("Piet in de keuken", i.getName());
    }

    @Test
    public void testRemove() {
	repo.remove(repo.getItemByName("Piet in de keuken"));
	assertNull(repo.getItemByName("Piet in de keuken"));
    }

    @Test
    public void testGetItemsByPartialName() {
	ObservableList<Item> items = repo.getItemsByPartialName("piet");
	assertEquals(5, items.size());
	items = repo.getItemsByPartialName("in de");
	assertEquals(4, items.size());
    }

    @Test
    public void testGetItemsByCategory() {
	ObservableList<Item> items = repo.getItemsByCategory("3+");
	assertEquals(4, items.size());
    }

    @Test
    public void testGetItemsByTheme() {
	ObservableList<Item> items = repo.getItemsByTheme("kleding");
	assertEquals(2, items.size());
    }

}
