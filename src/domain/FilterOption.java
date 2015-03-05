package domain;

/**
 *
 * @author Frederik
 */
public enum FilterOption {

    BOOK(Book.class), CD(Cd.class), DVD(Dvd.class), GAME(Game.class), STORYBAG(StoryBag.class), USER(User.class), ITEMCOPY(ItemCopy.class);

    private final Class<?> clazz;

    private FilterOption(Class<?> clazz) {
	this.clazz = clazz;
    }

    public Class<?> getFilterClass() {
	return this.clazz;
    }
}
