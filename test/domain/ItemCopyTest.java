package domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import static junit.framework.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.JPAUtil;

/**
 *
 * @author Frederik
 */
public class ItemCopyTest {

    private EntityManager manager = JPAUtil.getInstance().getEntityManager();
    private Book item = new Book("Het thema", "8+", "De titel", "De uitleg", "De autheur", "De uitgeverij");
    private ItemCopy copy;

    @Before
    public void init() {
	manager.getTransaction().begin();
	copy = new ItemCopy(25, "Gang 15", item, Damage.MODERATE_DAMAGE);
    }

    @After
    public void finish() {
	if (manager.getTransaction().isActive()) {
	    manager.getTransaction().commit();
	}
    }
    
    @Test
    public void testJPAStoresItemCopyCorrectly(){
	manager.persist(copy);
	manager.getTransaction().commit();
	List<ItemCopy> itemCopies = manager.createNamedQuery("ItemCopy.findAll", ItemCopy.class).getResultList();
	assertEquals(1, itemCopies.stream().filter(i -> i.equals(copy)).count());
	
	ItemCopy itemCopy = itemCopies.stream().filter(i -> i.equals(copy)).collect(Collectors.toList()).get(0);
	assertEquals(itemCopy.getDamage(), copy.getDamage());
	assertEquals(itemCopy.getLocation(), copy.getLocation());
	
	Book dbItem = (Book)itemCopy.getItem();
	assertEquals(item.getAgeCategory(), dbItem.getAgeCategory());
	assertEquals(item.getDescription(), dbItem.getDescription());
	assertEquals(item.getAuthor(), dbItem.getAuthor());
	assertEquals(item.getFXImage(), dbItem.getFXImage());
	assertEquals(item.getName(), dbItem.getName());
	assertEquals(item.getPublisher(), dbItem.getPublisher());
	assertEquals(item.getTheme(), dbItem.getTheme());
	
	manager.getTransaction().begin();
	manager.remove(itemCopy);
	manager.remove(dbItem);
    }
}
