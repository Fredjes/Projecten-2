package gui;

/**
 *
 * @author Frederik
 */
public class ItemClass {

    private Class<?> itemClass;
    private String itemName;

    public ItemClass(String itemName, Class<?> itemClass) {
	this.itemClass = itemClass;
	this.itemName = itemName;
    }

    @Override
    public String toString() {
	return itemName;
    }

    public Class<?> getItemClass() {
	return itemClass;
    }
}
