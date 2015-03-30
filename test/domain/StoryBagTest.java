package domain;

import java.util.ArrayList;
import java.util.Arrays;
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
 * @author Frederik De Smedt
 */
public class StoryBagTest {

    private EntityManager manager = JPAUtil.getInstance().getEntityManager();
    private List<Item> items = new ArrayList<>(Arrays.asList(new Item[]{
	new Book(Arrays.asList("Het thema"), "8+", "De titel", "De uitleg", "De autheur", "De uitgeverij"),
	new Game(Arrays.asList("Actie"), "6-8", "Pacman", "Het beroemde Pacman-spel", "Atari")
    }));

    private StoryBag bag;

    @Before
    public void init() {
	bag = new StoryBag();
	bag.setItems(items);

	manager.getTransaction().begin();
    }

    @After
    public void finish() {
	if (manager.getTransaction().isActive()) {
	    manager.getTransaction().commit();
	}
    }

    @Test
    public void testJPAStoresItemCopyCorrectly() {
	manager.persist(bag);
	manager.getTransaction().commit();
	List<StoryBag> storyBags = manager.createNamedQuery("StoryBag.findAll", StoryBag.class).getResultList();
	assertEquals(1, storyBags.stream().filter(i -> i.equals(bag)).count());

	StoryBag dbBag = storyBags.stream().filter(i -> i.equals(bag)).collect(Collectors.toList()).get(0);

	assertEquals(bag.getAgeCategory(), dbBag.getAgeCategory());
	assertEquals(bag.getDescription(), dbBag.getDescription());
	assertEquals(bag.getFXImage(), dbBag.getFXImage());
	assertEquals(bag.getName(), dbBag.getName());
	assertEquals(bag.getThemes(), dbBag.getThemes());
	assertEquals(2, dbBag.getItems().size());

	manager.getTransaction().begin();
	manager.remove(dbBag);
    }
}
