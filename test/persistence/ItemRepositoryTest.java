package persistence;

import domain.Book;
import java.util.Arrays;
import org.junit.Before;

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
	repo.add(new Book(Arrays.asList("voeding"), "3+", "Piet in de keuken", "Leer koken!", "Brent", "Frederik"));
	repo.add(new Book(Arrays.asList("kleding"), "7+", "Piet in de kledingwinkel", "Kies kleding!", "Brent", "Frederik"));
	repo.add(new Book(Arrays.asList("school"), "3+", "Piet in de schoolwinkel", "Koop schoolgerief!", "Brent", "Frederik"));
	repo.add(new Book(Arrays.asList("kleding"), "3+", "Piet in de kledingwinkel", "geen description", "Brent", "Frederik"));
	repo.add(new Book(Arrays.asList("jobs"), "3+", "Piet bij de brandweer", "geen description", "Brent", "Frederik"));
    }
}
