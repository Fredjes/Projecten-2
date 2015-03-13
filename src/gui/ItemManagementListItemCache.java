package gui;

/**
 *
 * @author Frederik
 */
public class ItemManagementListItemCache {

    private static ItemManagementListItemCache INSTANCE;

    public static ItemManagementListItemCache getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new ItemManagementListItemCache();
	}

	return INSTANCE;
    }

    public ItemManagementListItem getItemManagementListItem() {
	return new ItemManagementListItem();
    }
}
