package domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import static junit.framework.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.JPAUtil;

/**
 *
 * @author Frederik De Smedt
 */
public class ItemTest {

    private EntityManager manager = JPAUtil.getInstance().getEntityManager();
    private Book item = new Book(Arrays.asList("Het thema"), "8+", "De titel", "De uitleg", "De autheur", "De uitgeverij");

    @Before
    public void init() {
	manager.getTransaction().begin();
    }

    @After
    public void finish() {
	if (manager.getTransaction().isActive()) {
	    manager.getTransaction().commit();
	}
    }

    @Test
    public void testJPAStoresItemCorrectly() {
	manager.persist(item);
	manager.getTransaction().commit();
	List<Item> items = manager.createNamedQuery("Item.findAll", Item.class).getResultList();
	assertEquals(1, items.stream().filter(i -> i.equals(item)).count());
	
	Book dbItem = (Book)items.stream().filter(i -> i.equals(item)).collect(Collectors.toList()).get(0);
	
	// Cannot use assertEquals on the objects themselves:
	// Item-instances use the id to create a hashCode and compare 2 items (equals),
	// yet the original item being persisted does not contain an id given by JPA.
	
	assertEquals(dbItem.getAgeCategory(), item.getAgeCategory());
	assertEquals(dbItem.getDescription(), item.getDescription());
	assertEquals(dbItem.getAuthor(), item.getAuthor());
	assertEquals(dbItem.getFXImage(), item.getFXImage());
	assertEquals(dbItem.getName(), item.getName());
	assertEquals(dbItem.getPublisher(), item.getPublisher());
	assertEquals(dbItem.getThemes(), item.getThemes());
	
	manager.getTransaction().begin();
	manager.remove(item);
    }
}
