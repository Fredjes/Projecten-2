package domain;

import java.util.ArrayList;
import java.util.List;
import persistence.ItemRepository;

/**
 * A Util-class that holds all drag functionality and logic.
 *
 * @author Frederik
 */
public class DragUtil {

    public static final String ITEM = "ITEM ";

    public static final String ITEM_REMOVE = "REMOVE ITEM ";

    public static Item getItem(DragCommand itemCommand) {
	String item = itemCommand.getCommand();
	if (item == null) {
	    return null;
	}

	item = item.substring(item.indexOf(" ") + 1);
	int hashCode = Integer.parseInt(item.substring(0, item.indexOf(" ")));
	String name = item.substring(item.indexOf(" ") + 1);
	List<Item> items = new ArrayList<>();
	ItemRepository.getInstance().getItems().stream().filter(i -> System.identityHashCode(i) == hashCode && i.getName().equals(name)).forEach(items::add); // avoiding findAny bug
	return items.get(0);
    }

    /**
     * Does not guarantee 100% uniqueness.
     *
     * @param item
     * @return
     */
    public static String createItemString(Item item) {
	return createItemString(item, ITEM);
    }

    /**
     * Does not guarantee 100% uniqueness.
     *
     * @param item
     * @return
     */
    public static String createItemString(Item item, String header) {
	return header + String.valueOf(System.identityHashCode(item) + " " + item.getName());
    }
}
