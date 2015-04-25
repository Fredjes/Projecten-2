
import domain.Book;
import domain.Change;
import domain.ChangeBuilder;
import domain.ChangeConfig;
import domain.Damage;
import domain.Item;
import domain.ItemCopy;
import domain.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Frederik
 */
public class ChangeTest {

    private Item item;
    private Item item2;
    private User user;
    private ItemCopy copy;
    private ChangeBuilder builder;

    @Before
    public void initialize() {
	item = new Book(new ArrayList<>(Arrays.asList("test", "test2")), "16+", "Titel", "De omschrijving", "De autheur", "De uitgeverij");
	item2 = new Book(new ArrayList<>(Arrays.asList("test3", "test4")), "32+", "Titel2", "De omschrijving2", "De autheur2", "De uitgeverij2");
	user = new User("Voornaam Achternaam", "2a", "email@domain.com", User.UserType.TEACHER, "thepasswordhash");
	copy = new ItemCopy("T1", "De locatie", item, Damage.NO_DAMAGE);

	item.setId(1);
	user.setId(2);
	copy.setId(3);
	item2.setId(4);

	builder = new ChangeBuilder();
    }

    @Test
    public void builderBuildsChangeCorrectly() {
	builder.addChange(item, item2, user);
	builder.addDeletion(copy);
	Change change = builder.build();

	List<Integer> itemAdditions = change.getChangedEntities(ChangeConfig.BOOK_VERSION_ID);
	List<Integer> userAdditions = change.getChangedEntities(ChangeConfig.USER_VERSION_ID);
	List<Integer> itemCopyAdditions = change.getChangedEntities(ChangeConfig.ITEM_COPY_VERSION_ID);
	List<Integer> itemDeletions = change.getRemovedEntities(ChangeConfig.BOOK_VERSION_ID);
	List<Integer> userDeletions = change.getRemovedEntities(ChangeConfig.USER_VERSION_ID);
	List<Integer> itemCopyDeletions = change.getRemovedEntities(ChangeConfig.ITEM_COPY_VERSION_ID);
	
	Assert.assertEquals(2, itemAdditions.size());
	Assert.assertEquals(1, userAdditions.size());
	Assert.assertEquals(0, itemCopyAdditions.size());
	Assert.assertEquals(0, itemDeletions.size());
	Assert.assertEquals(0, userDeletions.size());
	Assert.assertEquals(1, itemCopyDeletions.size());
	Assert.assertTrue(itemAdditions.contains(item.getID()));
	Assert.assertTrue(itemAdditions.contains(item2.getID()));
	Assert.assertTrue(userAdditions.contains(user.getID()));
	Assert.assertTrue(itemCopyDeletions.contains(copy.getID()));
    }
}
